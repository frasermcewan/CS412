package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.apache.lucene.queryparser.classic.ParseException;

import lucene.SearchFiles;


public class SearchWindow implements HyperlinkListener {
	// Editor pane for displaying pages.
    private JEditorPane displayEditorPane;
    
	public void run() {
		JFrame frameMain = new JFrame("Java E-Book Search");
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.setPreferredSize(new Dimension(800, 800));

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

		// Options Menu
		JMenu menuOption = new JMenu("Options");
		JMenuItem menuhelp = new JMenuItem("Help");
		JMenuItem menuabout = new JMenuItem("About");
		menuBar.add(menuOption);
		menuOption.add(menuhelp);
		menuOption.add(menuabout);

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
				SearchFiles searcher = new SearchFiles();

				try {
					Map<String, String> results = searcher.newsearch(holder);
					
					File file = new File("data\\index.htm");
			        try {
			            displayEditorPane.setPage(file.toURI().toURL());
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

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
		
		// Set up page display.
        displayEditorPane = new JEditorPane();
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);
        displayEditorPane.addHyperlinkListener(this);

        File file = new File("data\\index.htm");
        try {
            displayEditorPane.setPage(file.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
		// all bar bottom buttons should be within a tab
		panelMain.add(panelBottomButtons);
		panelMain.add(displayEditorPane);
		panelMain.add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);
		frameMain.add(panelMain);
		frameMain.setResizable(false);
		frameMain.pack();
		frameMain.setVisible(true);
	}

	// Handle hyperlink's being clicked.
    public void hyperlinkUpdate(HyperlinkEvent event) {
        HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent =
                        (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document =
                        (HTMLDocument) displayEditorPane.getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
            	try {
					displayEditorPane.setPage(event.getURL());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }

}
