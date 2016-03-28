import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class FileSearch extends JFrame implements MouseListener{

	JLabel lbName;
	JLabel lbDir;

	JTextField jtfName;
	JTextField jtfDir;

	JButton btnSearch;
	JButton btnExit;

	public static ViewList resultList;
	HashMap<String, String> hashM = new HashMap<String, String>();;
	Scanner sc;
	boolean check = false;

	FileSearch(){

		lbName = new JLabel("File Name (NO Extension!):");
		lbName.setFont(new Font("helvetica",Font.ROMAN_BASELINE,18));
		lbName.setBounds(10,10,300,30);

		jtfName = new JTextField();
		jtfName.setFont(new Font("helvetica",Font.ROMAN_BASELINE,18));
		jtfName.setBounds(10,40,300,30);

		lbDir = new JLabel("Directory:(With Colon, e.g: D:) ");
		lbDir.setFont(new Font("helvetica",Font.ROMAN_BASELINE,18));
		lbDir.setBounds(10,80,300,30);

		jtfDir = new JTextField();
		jtfDir.setFont(new Font("helvetica",Font.ROMAN_BASELINE,18));
		jtfDir.setBounds(10,120,300,30);

		btnSearch = new JButton("Search");
		btnSearch.setFocusable(false);
		btnSearch.addMouseListener(this);;
		btnSearch.setBounds(30,170,150,30);

		btnExit = new JButton("Exit");
		btnExit.setFocusable(false);
		btnExit.addMouseListener(this);;
		btnExit.setBounds(210,170,150,30);

		resultList = new ViewList();
		resultList.setBounds(5, 220, 380, 150);

		getContentPane().add(resultList);
		getContentPane().add(btnExit);
		getContentPane().add(btnSearch);
		getContentPane().add(jtfDir);
		getContentPane().add(lbDir);
		getContentPane().add(jtfName);
		getContentPane().add(lbName);

		setLayout(null);
		setBounds(500,120,400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setVisible(true);


	}

	private String fileNameToSearch;
	private List<String> result = new ArrayList<String>();
	private List<String> result2 = new ArrayList<String>();
	String ch [];

	public String getFileNameToSearch() {
		return fileNameToSearch;
	}

	public void setFileNameToSearch(String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	}

	public List<String> getResult() {
		return result;
	}

	public static void main(String[] args) {

		new FileSearch();
		//try different directory and filename :)
		//fileSearch.searchDirectory(new File("D:"), "AC");


	}

	public boolean searchDirectory(File directory, String fileNameToSearch) {

		setFileNameToSearch(fileNameToSearch);

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
					else {

						if ( temp.getName().endsWith(".txt")){


							try{
								BufferedReader br = new BufferedReader(new FileReader(temp));

								String line = null;

								while ((line = br.readLine()) != null) {
									if(line.contains(getFileNameToSearch())){
										//System.out.println("The string exists in file: "+" "+val);
										result2.add("ContentThread :" + jtfName.getText() + " :Found in: "+ temp.getAbsolutePath());
										check = true;
										
										for ( int i = 0;i<result2.size();i++){
											for ( int  j=i+1;j<result2.size();j++){
												if ( result2.get(i).matches(result2.get(j)))
													result2.remove(i);
											}
										}
										
										//System.out.println("File " + temp.getName() + " contains searchString " + jtfName.getText() + "!");
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//System.out.println(temp.getName());
								//e.printStackTrace();
							}
						}



						try{
							ch  = temp.getName().split("\\.(?=[^\\.]+$)");
							//System.out.println("Name: "+ch[0]);
							//System.out.println(temp.getName());
						}
						catch ( Exception e){
							//System.out.println(e);
						}
						if (getFileNameToSearch().toLowerCase().equals(temp.getName().toLowerCase()) ||
								ch[0].toLowerCase().startsWith(getFileNameToSearch().toLowerCase())
								) {			

							//result.add(temp.getAbsoluteFile().toString());
							check = true;
						}

					}

				} 
			}
			catch (Exception e){}
		}
		return check;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		Object obj = me.getSource();
		if ( obj == btnExit){
			this.dispose();
		}

		if ( obj == btnSearch){
			
			
			resultList.md.removeAllElements();
			String name = jtfName.getText().trim();
			String dir = jtfDir.getText().trim();

			Thread mt1 = new Thread(new Crawler(name,dir));
			mt1.setName("Crawler1");
			mt1.setDaemon(false);
			mt1.start();
			
			searchDirectory(new File(dir), name);
			for (int i=0;i<result2.size();i++){
				resultList.md.addElement(result2.get(i));
			}
			
			

			int count = getResult().size();

			if(count ==0){
				//System.out.println("\nNo result found!");
				//resultList.md.addElement("No Result Found!");
			}
			else{
				System.out.println("\nFound " + count + " result!\n");
				for (String matched : getResult()){
					System.out.println("Found : " + matched);
					//resultList.md.addElement("Found: " + matched);
				}
			}

		}

	}

	class ViewList extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5516877123660980496L;
		DefaultListModel<String> md = new DefaultListModel<String>();
		JList<String> jl;
		JScrollPane jsp ;

		ViewList(){

			setResizable(false);
			jl = new JList<String>(md);
			jl.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			jl.setBackground(Color.WHITE);
			jl.setForeground(Color.BLACK);
			jl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			jl.setVisibleRowCount(5000);
			jl.setFont(new Font("helvetica",Font.PLAIN, 14));

			jsp = new JScrollPane(jl);
			jsp.setPreferredSize(new Dimension(380,150));
			jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			add(jsp,BorderLayout.CENTER);



		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}