import java.awt.Component;
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

		panelBottomButtons.setLayout(new BoxLayout(panelBottomButtons, BoxLayout.X_AXIS));

		// Initlizing menu system
		JMenuBar menuBar = new JMenuBar();
		JMenu menuSearch = new JMenu("File");
		// create them
		JMenuItem menuHelp = new JMenuItem("Help");
		JMenuItem menuInfo = new JMenuItem("Info");
		JMenuItem menuExit = new JMenuItem("Exit");
		// add them
		frame1.setJMenuBar(menuBar);
		menuBar.add(menuSearch);
		menuSearch.add(menuHelp);
		menuSearch.add(menuInfo);
		menuSearch.add(menuExit);

		// TODO Menu Actions
		menuHelp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createHelpDialogBox();
			}

		});

		menuInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createInfoDialogBox();
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
		JScrollPane scrollPane1 = new JScrollPane(searchList);
		scrollPane1.setPreferredSize(new Dimension(1200, 600));
		JScrollPane scrollPane2 = new JScrollPane(searchList);
		scrollPane2.setPreferredSize(new Dimension(1200, 600));

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
					int replyInt = JOptionPane.showConfirmDialog(frame1, "Close?", "Close Program",
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
		panelTable1.add(scrollPane1);
		panelTable2.add(scrollPane2);

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

	private void createHelpDialogBox() {
		Object[] options = { "Close" };
		Component frame = null;
		int n = JOptionPane.showOptionDialog(frame, "STANDARD SEARCH \n" + "\n To use standard search, "
				+ "simply select the standard search tab and then input your \n search words into the "
				+ "search box. \n \n Once you click search, select one of the search results shown below "
				+ "the search \n on the left to view the page. \n \n ADVANCED SEARCH \n \n"
				+ "OR: The OR search is identical to the standard searches search.\n"
				+ "AND: The AND search allows you to search for multiple words in \n"
				+ "the same document. \n"
				+ "NOT: NOT allows you to add related words which you wish to ignore. \n"
				+ "Quoted Search: This allows you to search for a full quote "
				+ "in a document \n(Watch your punctuation when using this search)\n"
				+ "", 
				"Help", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);

	}

	private void createInfoDialogBox() {
		Object[] options = { "Close" };
		Component frame = null;
		int n = JOptionPane.showOptionDialog(frame, "INFORMATION \n \n This search engine was designed "
				+ "for \n CS412 at Strathclyde University in 2015. Authors: \n"
				+ "Fraser McEwan \n"
				+ "Graeme Sutters \n"
				+ "Jhordan Tease \n"
				+ "David Thomson \n"
				+ "Grant Toghill \n"
				+ "\n Framework for Search Engine provided by Apache Lucene. \n \n"
				+ "https://lucene.apache.org/ \n", 
				"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
				options[0]);
	}

}
