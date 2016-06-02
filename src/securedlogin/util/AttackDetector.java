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
	private ArrayList<String> regex=new ArrayList<>();
	public AttackDetector(String text, String regexFilePath) throws IOException, ClassNotFoundException, SQLException {
			this.text=text;
			this.regexFilePath=regexFilePath;
	}
	private void readPatterns() throws IOException {
		BufferedReader br= new BufferedReader(new FileReader(regexFilePath));
		String r;
		while((r=br.readLine())!=null) {
			regex.add(r);
		}
		br.close();
	}
	private String detectAttackClass(int regexIndex) {
		if(regexIndex>=0 && regexIndex<=3)
			return "SQLi";
		return "XSS";
	}
	private String detectAttackType(int regexIndex) {
		if(regexIndex==4)
			return "Reflective";
		else if(regexIndex==5)
			return "Stored";
		else {
			String text=this.text.toLowerCase();
			if(regexIndex==3) {
				if(text.contains("if") || regexIndex==2 || text.contains("else") || text.contains("waitfor"))
					return "Inference";
				return "Piggy-backed";
			}
			else if(regexIndex==1 || text.contains("like") || text.contains("=")) {
				return "tautology";
			}
			else if(text.contains("exec") || text.contains("shutdown") || text.contains("xp_cmdshell()") || text.contains("sp_execwebtask()"))
				return "Stored procedures";
			else if(text.contains("union"))
				return "Union queries";
			else if(text.contains("exec()") || text.contains("char()") || text.contains("hex()") || text.contains("bin()") || text.contains("unhex()") || text.contains("base64()") ||text.contains("dec()") || text.contains("rot13()"))
				return "Alternate encoding";
		}
		return "";		
	}	
	public boolean detectAttacks() throws IOException, ClassNotFoundException, SQLException {
		readPatterns();
		int len=regex.size();
		boolean isAnAttack=false;
		LogDatabase logDataBase=new LogDatabase();
		for(int regexIndex=0;regexIndex<len;++regexIndex){
			Pattern pattern=Pattern.compile(regex.get(regexIndex));
			Matcher matcher=pattern.matcher(this.text);
			if(matcher.find()) {
				String attackClass=detectAttackClass(regexIndex);
				String attackType=detectAttackType(regexIndex);
				isAnAttack=true;				
				if(attackType.equals(""))
					isAnAttack=false;
				else {
					isAnAttack=true;
					logDataBase.generateLogDB(attackClass, attackType);
				}
			}			
		}
		return isAnAttack;
	}
}
