import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SearchWindow {

	public void run() {
		JFrame frame1 = new JFrame("Java e-book search");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(1200, 800));


		/****** Different Panels ******/
		JPanel panelMain = new JPanel();
		JPanel panelTab1 = new JPanel();
		JPanel panelTab2 = new JPanel();
		JPanel panelcreateSearch = new JPanel();
		JPanel panelcreateSearchAdvanced1 = new JPanel();
		JPanel panelcreateSearchAdvanced2 = new JPanel();
		JPanel panelcreateSearchAdvanced3 = new JPanel();
		JPanel panelcreateSearchAdvanced4 = new JPanel();
		JPanel panelTable1 = new JPanel();
		JPanel panelTable2 = new JPanel();
		JPanel panelBottomButtons = new JPanel();
		panelMain.setLayout(new FlowLayout());
		// tabbed contents
		panelcreateSearch.setLayout(new FlowLayout());
		panelTable1.setLayout(new BoxLayout(panelTable1, BoxLayout.Y_AXIS));
		panelTable2.setLayout(new BoxLayout(panelTable2, BoxLayout.Y_AXIS));
		panelTab1.setLayout(new BoxLayout(panelTab1, BoxLayout.Y_AXIS));
		panelTab2.setLayout(new BoxLayout(panelTab2, BoxLayout.Y_AXIS));
		

		panelBottomButtons.setLayout(new BoxLayout(panelBottomButtons,
				BoxLayout.X_AXIS));

		// Initlizing menu system
		JMenuBar menuBar = new JMenuBar();
		JMenu menuSearch = new JMenu("Search");
		// create them
		JMenuItem menuCreate = new JMenuItem("Create...");
		JMenuItem menuSwap = new JMenuItem("Swap search mode(Basic <--> Advanced)");
		JMenuItem menuExit = new JMenuItem("Exit");
		// add them
		frame1.setJMenuBar(menuBar);
		menuBar.add(menuSearch);
		menuSearch.add(menuCreate);
		menuSearch.add(menuSwap);
		menuSearch.add(menuExit);
		
		// TODO Menu Actions
		menuCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Create Tab Searches?
			}

		});
		
		menuSwap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Create Tab Searches?
			}

		});

		menuExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO save before it exits?
				System.exit(0);
			}

		});
		
	
		// Return Search List
		JTable searchList = new JTable();
		JScrollPane scrollPane = new JScrollPane(searchList);
		scrollPane.setPreferredSize(new Dimension(1200, 600));

		/****** Text Fields ******/
		JTextField searchQuery = new JTextField(25);
		JTextField searchQueryStandard = new JTextField(25);
		JTextField searchQueryIncludes = new JTextField(25);
		JTextField searchQueryExcludes = new JTextField(25);
		JTextField searchQueryExactQuote = new JTextField(25);

		/****** Buttons ******/
		JButton commenceSearch = new JButton("Search");
		JButton commenceSearchOR = new JButton("Search");
		JButton commenceSearchAND = new JButton("Search");
		JButton commenceSearchNOT = new JButton("Search");
		JButton commenceSearchQUOTED = new JButton("Search");
		JButton btnCloseProgram = new JButton("Close");

		// TODO
		commenceSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String holder = searchQuery.getText();

				// lucene.SearchFiles();

			}
		});

		btnCloseProgram.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				{
					int replyInt = JOptionPane.showConfirmDialog(frame1,
							"Close?", "Close Program",
							JOptionPane.YES_NO_OPTION);
					if (replyInt == 0) {
						System.out.println("User did quit.");
						// save?
						System.exit(0);
					} else {
						System.out.println("User did not quit.");
					}
				}
			}

		});

		// Add Panel
		panelcreateSearch.add(new JLabel("Enter Query"));
		panelcreateSearch.add(searchQuery);
		panelcreateSearch.add(commenceSearch);
		
		panelcreateSearchAdvanced1.add(new JLabel("Standard Search(OR)"));
		panelcreateSearchAdvanced1.add(searchQueryStandard);
		panelcreateSearchAdvanced1.add(commenceSearchOR);
		panelcreateSearchAdvanced2.add(new JLabel("Included search(AND)"));
		panelcreateSearchAdvanced2.add(searchQueryIncludes);
		panelcreateSearchAdvanced2.add(commenceSearchAND);
		panelcreateSearchAdvanced3.add(new JLabel("Excluded search(NOT)"));
		panelcreateSearchAdvanced3.add(searchQueryExcludes);
		panelcreateSearchAdvanced3.add(commenceSearchNOT);
		panelcreateSearchAdvanced4.add(new JLabel("Exact search(Quoted search)"));
		panelcreateSearchAdvanced4.add(searchQueryExactQuote);
		panelcreateSearchAdvanced4.add(commenceSearchQUOTED);
		
		// Adding return list
		panelTable1.add(scrollPane);
		panelTable2.add(scrollPane);
		
		// Adding individual tab menu options
		panelTab1.add(panelcreateSearch);
		panelTab1.add(panelTable1);
		panelTab1.setVisible(true);
		
	    panelTab2.add(panelcreateSearchAdvanced1);
	    panelTab2.add(panelcreateSearchAdvanced2);
	    panelTab2.add(panelcreateSearchAdvanced3);
	    panelTab2.add(panelcreateSearchAdvanced4);
		panelTab2.add(panelTable2);
		panelTab2.setVisible(true);
		

		// Allows several searches at once
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Basic Search", panelTab1);
		tabbedPane.addTab("Advanced Search", panelTab2);
		

		// Add Close Button
		panelBottomButtons.add(btnCloseProgram);

		// Add to main Window
		panelMain.add(tabbedPane);
		// all bar bottom buttons should be within a tab
		panelMain.add(panelBottomButtons);
		frame1.add(panelMain);
		frame1.setResizable(false);
		frame1.pack();
		frame1.setVisible(true);

	}

}
