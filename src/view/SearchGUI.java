package view;

import model.IndexFiles;
import model.SearchFiles;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
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

	// Pane Objects
	private JEditorPane displayEditorPane;
	private JEditorPane displayEditorPane2;

	// Menu Objects
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuFileNew;
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileExit;
	private String Holder;
	private String AdvancedHolder;

	private JButton commenceSearchOR = new JButton("Search");
	private JButton commenceSearchAND = new JButton("Search");
	private JButton commenceSearchNOT = new JButton("Search");
	private JButton commenceSearchQUOTED = new JButton("Search");

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
		window.setSize(1000, 700);
		window.setResizable(false);

		
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.add(makeSearchPanel(), BorderLayout.NORTH);
		makeMenu();
		panel1.add(makeSplitPane(), BorderLayout.CENTER);
		
		
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(makeComplexSearchPanel(), BorderLayout.NORTH);
		makeMenu();
		panel2.add(makeSplitPane2(), BorderLayout.CENTER);

	
	
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Basic Search", panel1);
		tabbedPane.addTab("Advanced Search", panel2);
		window.getContentPane().add(tabbedPane);

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

		/****** Text Fields ******/
		JTextField searchQuery = new JTextField(15);

		/****** Buttons ******/
		JButton commenceSearch = new JButton("Search");

		// Add Panel
		panelCreateSearch.add(new JLabel("Enter Query"));
		panelCreateSearch.add(searchQuery);
		panelCreateSearch.add(commenceSearch);

		commenceSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelCreateSearch.setFocusable(true);
				topPanel.setVisible(true);
				Holder = searchQuery.getText();
				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane.getModel()).clear();

				try {
					results = searcher.newsearch(Holder);

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

		commenceSearchOR = new JButton("Search");
		commenceSearchAND = new JButton("Search");
		commenceSearchNOT = new JButton("Search");
		commenceSearchQUOTED = new JButton("Search");

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

		panelComplexSearch.add(panelcreateSearchAdvanced1);
		panelComplexSearch.add(panelcreateSearchAdvanced2);
		panelComplexSearch.add(panelcreateSearchAdvanced3);
		panelComplexSearch.add(panelcreateSearchAdvanced4);
		panelComplexSearch.setVisible(true);

		commenceSearchOR.addActionListener(new ActionListener() {

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
					results = searcher.newsearch(AdvancedHolder);

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

		commenceSearchAND.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryIncludes.getText();
				searchQueryStandard.setText(null);
				searchQueryExcludes.setText(null);
				searchQueryExactQuote.setText(null);

				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.newsearch(AdvancedHolder);

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

		commenceSearchNOT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryExcludes.getText();
				searchQueryIncludes.setText(null);
				searchQueryStandard.setText(null);
				searchQueryExactQuote.setText(null);

				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.newsearch(AdvancedHolder);

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

		commenceSearchQUOTED.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryExactQuote.getText();
				searchQueryIncludes.setText(null);
				searchQueryExcludes.setText(null);
				searchQueryStandard.setText(null);
				SearchFiles searcher = new SearchFiles();

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.newsearch(AdvancedHolder);

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

}
