package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class SearchWindow {
	public void run() {
		JFrame frameMain = new JFrame("Java E-Book Search");
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setPreferredSize(new Dimension(800, 500));
			
		/****** Different Panels ******/
		JPanel panelMain = new JPanel();
		JPanel panelTab = new JPanel();
		JPanel panelcreateSearch = new JPanel();
		JPanel panelTable = new JPanel();
		JPanel panelBottomButtons = new JPanel();
					
		panelMain.setLayout(new FlowLayout());
			
		// tabbed contents
		panelcreateSearch.setLayout(new FlowLayout());
		panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));
		panelTab.setLayout(new BoxLayout(panelTab, BoxLayout.Y_AXIS));
			
		panelBottomButtons.setLayout(new BoxLayout(panelBottomButtons, BoxLayout.X_AXIS));
			
		//Initlizing menu system
		JMenuBar menuBar = new JMenuBar();
		JMenu menuSearch = new JMenu("Search");
		// create them
		JMenuItem menuCreate = new JMenuItem("Create...");
		JMenuItem menuExit = new JMenuItem("Exit");
		// add them
		frameMain.setJMenuBar(menuBar);
		menuBar.add(menuSearch);
		menuSearch.add(menuCreate);
		menuSearch.add(menuExit);
			
		//TODO Menu Actions
		menuCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Create Tab Searches?
			}
				
		});
			
		
			
			
		menuExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO save before it exits?
				System.exit(0);
			}
				
		});
			
			
		//Return Search List
		JTable searchList = new JTable();
		JScrollPane scrollPane = new JScrollPane(searchList);
		scrollPane.setPreferredSize(new Dimension(800, 300));
			
			
		/****** Text Fields ******/
		JTextField searchQuery = new JTextField(15);
		
			
		/***** Radio Button *****/
		JRadioButton radButton1 = new JRadioButton("SimpleSearch",true);
		JRadioButton radButton2 = new JRadioButton("Complex Search", false);
		JRadioButton radButton3 = new JRadioButton("Related Search Items",false);

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(radButton1);
		group.add(radButton2);
		group.add(radButton3);



		/****** Buttons ******/
		JButton commenceSearch = new JButton("Search");
		JButton btnCloseProgram = new JButton("Close");
			
		//TODO 
		commenceSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String holder = searchQuery.getText();
				 
				 
//				lucene.SearchFiles();
					
			}
		});

		
			
		btnCloseProgram.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int replyInt = JOptionPane.showConfirmDialog(
							   frameMain,
							   "Close?",
							   "Close Program",
							   JOptionPane.YES_NO_OPTION);
				if (replyInt == 0) {
					System.out.println("User did quit.");
					//save?
					System.exit(0);
				} else {
					System.out.println("User did not quit.");
				}
			}
				
		});
			
			
		// Add Panel
		panelcreateSearch.add(new JLabel("Enter Query"));
		panelcreateSearch.add(searchQuery);
		panelcreateSearch.add(radButton1);
		panelcreateSearch.add(radButton2);
		panelcreateSearch.add(radButton3);
		panelcreateSearch.add(commenceSearch);
			
		//Adding return list 
		panelTable.add(scrollPane);
			
		//Adding individual tab menu options
		panelTab.add(panelcreateSearch);
		panelTab.add(panelTable);
			
		//Allows several searches at once
		JTabbedPane tabbedPane = new JTabbedPane();		
		tabbedPane.addTab("Java Search", panelTab);
			
		//Add Close Button
		panelBottomButtons.add(btnCloseProgram);
			
		//Add to main Window
		panelMain.add(tabbedPane);
		// all bar bottom buttons should be within a tab
		panelMain.add(panelBottomButtons);
		frameMain.add(panelMain);
		frameMain.setResizable(false);
		frameMain.pack();
		frameMain.setVisible(true);
	}

}


