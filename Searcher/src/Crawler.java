import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Crawler implements Runnable {

	String toSearch = "";
	String dir = ""; 
	String ch [];
	List<String> result = new ArrayList<String>();

	boolean check = false;
	Crawler (String sString ,String dirSearch){

		toSearch  = sString;
		dir = dirSearch;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if("Crawler1".equals(Thread.currentThread().getName())){
			System.out.println("IM Crawler");
		}


		searchDirectory(new File(dir), toSearch);

		for (int i=0; i< result.size();i++){
			//System.out.println("Found : " + matched);
			FileSearch.resultList.md.addElement("Thread1 Found: " + result.get(i));
		}

	}

	public boolean searchDirectory(File directory, String fileNameToSearch) {

		if (directory.isDirectory()) {
			check = search(directory);
		} else {
			System.out.println(directory.getAbsoluteFile() + " is not a directory!");
		}

		return check;
	}

	public boolean search(File file) {


		if (file.isDirectory()) {

			//System.out.println("Searching directory ... " + file.getAbsoluteFile());
			try{
				//do you have permission to read this directory?	
				for (File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						search(temp);
					} 


					try{
						ch  = temp.getName().split("\\.(?=[^\\.]+$)");
						//System.out.println("Name: "+ch[0]);
						//System.out.println(temp.getName());
					}
					catch ( Exception e){
						//System.out.println(e);
					}
					if (toSearch.toLowerCase().equals(temp.getName().toLowerCase()) ||
							ch[0].toLowerCase().startsWith(toSearch.toLowerCase())
							) {			

						result.add(temp.getAbsoluteFile().toString());
						check = true;
					}

				}

			} 
		
		catch (Exception e){}
	}
	return check;
}

}
