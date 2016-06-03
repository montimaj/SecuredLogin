import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.security.SecureRandom;

class TestSignatures {
	private ArrayList<String> sigList=new ArrayList<>();
	private static final String sigFileNames[]={"sig1.txt","sig2.txt","sig3.txt"};	
	private static int sigCounter=0;	
	private void generateRandomSigFile(String fileName) throws IOException {
		int len=sigList.size();
		File file=new File("Signatures",fileName);
		FileWriter fileWriter=new FileWriter(file);
		boolean flag[]=new boolean[len];
		for(int i=0;i<len;++i) {
			int r=new SecureRandom().nextInt(len);
			if(!flag[r]) {
				fileWriter.write(sigList.get(r)+"\n");
				flag[r]=true;			
			}
			else
				--i;
		}
		fileWriter.close();			
	}
	private static void testSignatures() throws IOException, InterruptedException {
		SecureRandom secureRandom=new SecureRandom();
		int fileNumber=secureRandom.nextInt(sigFileNames.length);
		TestSignatures ts=new TestSignatures();
		ts.readSigFile(sigFileNames[fileNumber]);
		int len=ts.sigList.size();	
		int sigNumber=secureRandom.nextInt(len);
		String userName=ts.sigList.get(sigNumber);
		String passwd;
		switch(userName) {
			case "admin": passwd="1234";
				      break;
			case "foo":   passwd="5678";
				      break;
			case "bar":   passwd="3421";
				      break;
			default:      passwd=ts.sigList.get(secureRandom.nextInt(len));				      
		}
		System.out.println("Username= "+userName+"\nPassword= "+passwd);
		String x[]={"python","login.py",userName,passwd};
		new ProcessBuilder(x).start().waitFor();
	}
	private void readSigFile(String fileName) throws IOException {
		File file=new File("Signatures",fileName);
		BufferedReader br=new BufferedReader(new FileReader(file));
		String signatures;
		while((signatures=br.readLine())!=null) {
			sigList.add(signatures);			
		}
		br.close();
	}
	public static void main(String args[]) {
		try {			
			TestSignatures ts=new TestSignatures();
			ts.readSigFile(sigFileNames[0]);
			ts.generateRandomSigFile(sigFileNames[1]);
			ts.generateRandomSigFile(sigFileNames[2]);
			int len=ts.sigList.size()*3;
			while(sigCounter++<=len)			
				testSignatures();			
			
		}catch(IOException|InterruptedException e) {
			e.printStackTrace();		
		}	
	}
}
