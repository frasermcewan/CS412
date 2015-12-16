package view;

import model.IndexFiles;
import model.SearchFiles;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Grant on 02/12/2015.
 */
public class SearchGUI extends Observable implements HyperlinkListener {

	// Frame Objects
	public JFrame window = new JFrame();

	// Panel Objects
	private JPanel topPanel;
	private JPanel panelcreateSearchAdvanced1 = new JPanel();
	private JPanel panelcreateSearchAdvanced2 = new JPanel();
	private JPanel panelcreateSearchAdvanced3 = new JPanel();
	private JPanel panelcreateSearchAdvanced4 = new JPanel();
	private JPanel bottomPanel;

	// Pane Objects
	private JEditorPane displayEditorPane;
	private JEditorPane displayEditorPane2;

	// Menu Objects
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuFileNew;
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileHelp;
	private JMenuItem menuFileInfo;
	private JMenuItem menuFileExit;
	private String Holder;
	private String AdvancedHolder;

	private JButton contentSearch = new JButton("Content Search");
	private JButton titleSearch = new JButton("Title Search");

	private JButton RelatedSearchButton1 = new JButton("");
	private JButton RelatedSearchButton2 = new JButton("");
	private JButton RelatedSearchButton3 = new JButton("");


	private JTextField searchQueryStandard = new JTextField(25);
	private JTextField searchQueryIncludes = new JTextField(25);
	private JTextField searchQueryExcludes = new JTextField(25);
	private JTextField searchQueryExactQuote = new JTextField(25);

	private Map<String, String> results;

	private JList<String> listScrollPane = new JList<>(new DefaultListModel<>());
	private JList<String> listScrollPane2 = new JList<>(new DefaultListModel<>());

	public SearchGUI() {
		window.setTitle("Java E-Book Search");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setSize(1000, 800);
		window.setResizable(false);

		makeMenu();
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(makeSearchPanel(), BorderLayout.NORTH);
		panel1.add(makeSplitPane(), BorderLayout.CENTER);
		
		
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(makeComplexSearchPanel(), BorderLayout.NORTH);
		panel2.add(makeSplitPane2(), BorderLayout.CENTER);

	
	
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Basic Search", panel1);
		tabbedPane.addTab("Advanced Search", panel2);
		window.getContentPane().add(tabbedPane);
		
		makeBottom();

		listScrollPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScrollPane.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					if (results != null) {
						// parameter this

						final List<String> selectedValuesList = listScrollPane.getSelectedValuesList();
						String fileLocation = results.get(selectedValuesList.get(0));
						File file = new File(fileLocation);

						System.out.println("HERE  " + results.get(selectedValuesList.get(0)));

						try {
							System.out.println(file.toURI().toURL());
							displayEditorPane.setPage(file.toURI().toURL());
						} catch (IOException event) {
							event.printStackTrace();
						}
						
					}
			
				}
			}
		});

		
		listScrollPane2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listScrollPane2.addListSelectionListener(new ListSelectionListener() {
		
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				if (results != null) {
					// parameter this

					final List<String> selectedValuesList2 = listScrollPane2.getSelectedValuesList();
					
					if(!selectedValuesList2.get(0).equals(null)) {
						
					
					String fileLocation = results.get(selectedValuesList2.get(0));
					File file = new File(fileLocation);

					System.out.println("HERE  " + results.get(selectedValuesList2.get(0)));
					
					

					try {
						System.out.println(file.toURI().toURL());
						displayEditorPane2.setPage(file.toURI().toURL());
					}
					catch (IOException event) {
						event.printStackTrace();
					}
				}
				}
			}
		}
	});

		
		
	}

	public void makeMenu() {
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		window.getContentPane().add(topPanel);

		// Create the menu bar
		menuBar = new JMenuBar();

	
		window.setJMenuBar(menuBar);

		
		menuFile = new JMenu("File");
		menuBar.add(menuFile);


		menuFileNew = new JMenuItem();
		menuFileNew.setText("New Search");
		menuFile.add(menuFileNew);

		menuFileOpen = new JMenuItem();
		menuFileOpen.setText("Re-Index");
		menuFile.add(menuFileOpen);

		menuFileOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new IndexFiles();
			}

		});
		
		menuFileHelp = new JMenuItem();
		menuFileHelp.setText("Help");
		menuFile.add(menuFileHelp);

		menuFileHelp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createHelpDialogBox();
			}

		});
		
		menuFileInfo = new JMenuItem();
		menuFileInfo.setText("Info");
		menuFile.add(menuFileInfo);

		menuFileInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createInfoDialogBox();
			}

		});

		menuFileExit = new JMenuItem();
		menuFileExit.setText("Close program");
		menuFile.add(menuFileExit);

		menuFileExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int replyInt = JOptionPane.showConfirmDialog(window, "Close?", "Close Program",
						JOptionPane.YES_NO_OPTION);
				if (replyInt == 0) {
					System.out.println("User did quit.");
					// save?
					System.exit(0);
				} else {
					System.out.println("User did not quit.");
				}
			}

		});
	}

	private JSplitPane makeSplitPane() {

		JScrollPane scrollListPane = new JScrollPane(listScrollPane);
		JScrollPane scrollDisplayPane = new JScrollPane(makeDisplayPanel());

		final JSplitPane panelSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollListPane,
				scrollDisplayPane);

		panelSplitPane.setResizeWeight(0.3);
		panelSplitPane.setOneTouchExpandable(true);
		panelSplitPane.setContinuousLayout(true);

		return panelSplitPane;
	}
	
	private JSplitPane makeSplitPane2() {
		JScrollPane scrollListPane = new JScrollPane(listScrollPane2);
		JScrollPane scrollDisplayPane = new JScrollPane(makeDisplayPanel2());

		final JSplitPane panelSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollListPane,
				scrollDisplayPane);

		panelSplitPane.setResizeWeight(0.3);
		panelSplitPane.setOneTouchExpandable(true);
		panelSplitPane.setContinuousLayout(true);

		return panelSplitPane;
		
	}

	private JPanel makeSearchPanel() {

		final JPanel panelCreateSearch = new JPanel();		
		panelCreateSearch.setLayout(new BoxLayout(panelCreateSearch, BoxLayout.X_AXIS));


		/****** Text Fields ******/
		JTextField contentQuery = new JTextField(25);
		JTextField titleQuery = new JTextField(25);

		/****** Buttons ******/
		JButton contentSearch = new JButton("Content Search");
		JButton titleSearch = new JButton("Title Search");

		// Add Panel
		panelCreateSearch.add(new JLabel("Enter Query"));
		panelCreateSearch.add(contentQuery);
		panelCreateSearch.add(contentSearch);

		panelCreateSearch.add(titleSearch);
		panelCreateSearch.add(titleQuery);
		
		contentSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelCreateSearch.setFocusable(true);
				topPanel.setVisible(true);
				Holder = contentQuery.getText().toLowerCase();
				System.out.println("hi: " + Holder);
				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(Holder, "content");

					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane.getModel()).addElement(key);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}

		});
		
		titleSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelCreateSearch.setFocusable(true);
				topPanel.setVisible(true);
				Holder = titleQuery.getText().toLowerCase();
				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(Holder, "title");

					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane.getModel()).addElement(key);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}

		});

		return panelCreateSearch;
	}

	private JPanel makeComplexSearchPanel() {
		final JPanel panelComplexSearch = new JPanel();

		panelComplexSearch.setLayout(new BoxLayout(panelComplexSearch, BoxLayout.Y_AXIS));

		// panelTab2.setLayout(new BoxLayout(panelTab2, BoxLayout.Y_AXIS));

		searchQueryStandard = new JTextField(25);
		searchQueryIncludes = new JTextField(25);
		searchQueryExcludes = new JTextField(25);
		searchQueryExactQuote = new JTextField(25);

		contentSearch = new JButton("Content Search");

		panelcreateSearchAdvanced1.add(new JLabel("Standard Search(OR)"));
		panelcreateSearchAdvanced1.add(searchQueryStandard);

		panelcreateSearchAdvanced2.add(new JLabel("Included search(AND)"));
		panelcreateSearchAdvanced2.add(searchQueryIncludes);

		panelcreateSearchAdvanced3.add(new JLabel("Excluded search(NOT)"));
		panelcreateSearchAdvanced3.add(searchQueryExcludes);

		panelcreateSearchAdvanced4.add(new JLabel("Exact search(Quoted search)"));
		panelcreateSearchAdvanced4.add(searchQueryExactQuote);
		panelcreateSearchAdvanced4.add(contentSearch);

		panelComplexSearch.add(panelcreateSearchAdvanced1);
		panelComplexSearch.add(panelcreateSearchAdvanced2);
		panelComplexSearch.add(panelcreateSearchAdvanced3);
		panelComplexSearch.add(panelcreateSearchAdvanced4);
		panelComplexSearch.setVisible(true);

		contentSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryStandard.getText();
				searchQueryIncludes.setText(null);
				searchQueryExcludes.setText(null);
				searchQueryExactQuote.setText(null);
				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(AdvancedHolder, "content");

					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane2.getModel()).addElement(key);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}

		});

		return panelComplexSearch;

	}

	private JEditorPane makeDisplayPanel() {
		displayEditorPane = new JEditorPane();

		displayEditorPane.setContentType("text/html");
		displayEditorPane.setEditable(false);
		displayEditorPane.addHyperlinkListener(this);

		File file = new File("data/index.htm");

		try {
			displayEditorPane.setPage(file.toURI().toURL());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return displayEditorPane;
	}
	
	private JEditorPane makeDisplayPanel2() {
		displayEditorPane2 = new JEditorPane();

		displayEditorPane2.setContentType("text/html");
		displayEditorPane2.setEditable(false);
		displayEditorPane2.addHyperlinkListener(this);

		File file = new File("data/index.htm");

		try {
			displayEditorPane2.setPage(file.toURI().toURL());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return displayEditorPane2;
	}
	
	private void makeBottom() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0,3));
		window.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		bottomPanel.setVisible(true);
		
		RelatedSearchButton1 = new JButton("Related1");
		bottomPanel.add(RelatedSearchButton1);
		
		RelatedSearchButton2 = new JButton("Related2");
		bottomPanel.add(RelatedSearchButton2);
		
		RelatedSearchButton3 = new JButton("Related3");
		bottomPanel.add(RelatedSearchButton3);
	}
	

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		HyperlinkEvent.EventType eventType = event.getEventType();
		if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
			if (event instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
				HTMLDocument document = (HTMLDocument) displayEditorPane.getDocument();
				document.processHTMLFrameHyperlinkEvent(linkEvent);
			} else {
				try {
					System.out.println(event.getURL());
					displayEditorPane.setPage(event.getURL());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void changeButtons() {

	}
	
	private void createHelpDialogBox() {
		Object[] options = { "Close" };
		Component frame = null;
		int n = JOptionPane.showOptionDialog(frame, "STANDARD SEARCH \n" + "\n To use standard search, "
				+ "simply select the standard search tab and then input your \n search words into the "
				+ "search box. \n \n Once you click search, select one of the search results shown below "
				+ "the search \n on the left to view the page. \n \n ADVANCED SEARCH \n \n"
				+ "OR: The OR search is identical to the standard searches search.\n\n"
				+ "AND: The AND search allows you to search for multiple words in \n"
				+ "the same document. \n \n"
				+ "NOT: NOT allows you to add related words which you wish to ignore. \n\n"
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
				+ "for \n CS412 at Strathclyde University in 2015. Authors: \n\n"
				+ "Fraser McEwan \n"
				+ "Graeme Sutters \n"
				+ "Jhordan Tease \n"
				+ "David Thomson \n"
				+ "Grant Toghill \n"
				+ "\n Framework for Search Engine provided by Apache Lucene. \n \n"
				+ "https://lucene.apache.org/ \n"
				+ "\n GUI designed using the Swing Java Toolkit. ",
				"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
				options[0]);
	}

}

