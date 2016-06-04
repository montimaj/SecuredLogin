package securedlogin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttackDetector {
	private String text, regexFilePath;
	private int regexFileLines=0;
	private ArrayList<String> regex=new ArrayList<>();
	public AttackDetector(String text, String regexFilePath) throws IOException, ClassNotFoundException, SQLException {
			this.text=text;
			this.regexFilePath=regexFilePath;
	}
	private void readPatterns() throws IOException {
		BufferedReader br= new BufferedReader(new FileReader(regexFilePath));
		String r;
		while((r=br.readLine())!=null) {
			++regexFileLines;
			regex.add(r);
		}
		br.close();
	}
	private String detectAttackClass(int regexIndex) {
		if(regexIndex==regexFileLines-2 || regexIndex==regexFileLines-1)
			return "XSS";
		return "SQLi";
	}	
	private String detectAttackType(int regexIndex) {
		text=text.toLowerCase();
		if(regexIndex==regexFileLines-2 && !text.contains("src"))
			return "Reflective";
		else if(regexIndex==regexFileLines-1 || text.contains("src"))
			return "Stored";
		else {			
			if(regexIndex==0) {
				if(isAlternateEncoding())
					return "Alternate encoding";
				else if(isStoredProcedure())
					return "Stored procedures";
				else if(isTautology(regexIndex)) {
					if(text.contains("if"))
						return "Inference blind";
					return "tautology";
				}
				else if(isUnionQuery(regexIndex))
					return "Union Queries";
				return "Inference blind";
			}
			if(regexIndex==3) {
				if(isInferenceBlind(regexIndex))
					return "Inference blind";
				else if(isStoredProcedure())
					return "Stored procedures";
				else if(isAlternateEncoding())
					return "Alternate encoding";
				return "Piggy-backed";
			}
			if(isTautology(regexIndex)) {
				if(text.contains("if"))
					return "Inference blind";
				return "tautology";
			}
			if(regexIndex==1 || regexIndex==2 || isStoredProcedure())
				return "Stored procedures";
			if(isUnionQuery(regexIndex))
				return "Union queries";
			if(regexIndex==1 || regexIndex==2 || isAlternateEncoding())
				return "Alternate encoding";
		}
		return "";		
	}
	private boolean isTautology(int regexIndex) {
		return regexIndex==1 || (text.contains("like") && !text.contains("and")) || text.contains("=");
	}
	private boolean isInferenceBlind(int regexIndex) {
		return text.contains("like") || text.contains("if") || regexIndex==2 || text.contains("else") || text.contains("waitfor");
	}
	private boolean isAlternateEncoding() {
		return text.contains("exec(") || text.contains("char(") || text.contains("hex(") || text.contains("bin(") || text.contains("unhex(") || text.contains("base64(")||text.contains("dec(") || text.contains("rot13(") || text.contains("ascii(");
	}
	private boolean isStoredProcedure() {
		return text.contains("exec") || text.contains("shutdown") || text.contains("xp_cmdshell()") || text.contains("sp_execwebtask()");
	}
	private boolean isUnionQuery(int regexIndex) {
		return regexIndex==1 || regexIndex==2 || regexIndex==4 || text.contains("union");
	}
	public boolean detectAttacks() throws IOException, ClassNotFoundException, SQLException {
		readPatterns();
		int len=regex.size();
		boolean isAnAttack=false;
		LogDatabase logDataBase=new LogDatabase();
		String text=this.text;
		for(int regexIndex=0;regexIndex<len;++regexIndex){
			Pattern pattern=Pattern.compile(regex.get(regexIndex));
			Matcher matcher=pattern.matcher(text);
			if(matcher.find()) {
				String attackClass=detectAttackClass(regexIndex);
				String attackType=detectAttackType(regexIndex);
				isAnAttack=true;				
				if(attackType.equals(""))
					isAnAttack=false;
				else {
					isAnAttack=true;
					logDataBase.generateLogDB(attackClass, attackType,text);
				}
			}			
		}
		return isAnAttack;
	}
}
