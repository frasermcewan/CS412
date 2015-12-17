package view;

import model.IndexFiles;
import model.SearchFiles;
import model.RelatedSearches;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileHelp;
	private JMenu resultsFile;
	private JMenuItem resultsFile10;
	private JMenuItem resultsFile20;
	private JMenuItem resultsFile30;
	private JMenuItem resultsFile40;
	private JMenuItem resultsFile50;
	private JMenuItem menuFileCommands;
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

    private RelatedSearches rsearches = new RelatedSearches();
    private List<String> related;

	private JList<String> listScrollPane = new JList<>(new DefaultListModel<>());
	private JList<String> listScrollPane2 = new JList<>(new DefaultListModel<>());
	
	private int results_per_page = 20;

	public SearchGUI() {
		window.setTitle("Java E-Book Search");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setSize(1000, 700);
		window.setResizable(false);
		window.getContentPane().setBackground(Color.ORANGE);

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

                        if (!selectedValuesList2.get(0).equals(null)) {


                            String fileLocation = results.get(selectedValuesList2.get(0));
                            File file = new File(fileLocation);

                            System.out.println("HERE  " + results.get(selectedValuesList2.get(0)));


                            try {
                                System.out.println(file.toURI().toURL());
                                displayEditorPane2.setPage(file.toURI().toURL());
                            } catch (IOException event) {
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

		menuFileOpen = new JMenuItem();
		menuFileOpen.setText("Re-Index");
		menuFile.add(menuFileOpen);

		menuFileOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new IndexFiles();
			}

		});
		
		resultsFile = new JMenu("No. of Results");
		menuFile.add(resultsFile);
		
		resultsFile10 = new JMenuItem();
		resultsFile10.setText("Return 10 results");
		resultsFile.add(resultsFile10);

		resultsFile10.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					results_per_page = 10;
			}

		});
		
		resultsFile20 = new JMenuItem();
		resultsFile20.setText("Return 20 results");
		resultsFile.add(resultsFile20);

		resultsFile20.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				results_per_page = 20;
			}

		});
		
		resultsFile30 = new JMenuItem();
		resultsFile30.setText("Return 30 results");
		resultsFile.add(resultsFile30);

		resultsFile30.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				results_per_page = 30;
			}

		});
		
		resultsFile40 = new JMenuItem();
		resultsFile40.setText("Return 40 results");
		resultsFile.add(resultsFile40);

		resultsFile40.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				results_per_page = 40;
			}

		});
		
		resultsFile50 = new JMenuItem();
		resultsFile50.setText("Return 50 results");
		resultsFile.add(resultsFile50);

		resultsFile50.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				results_per_page = 50;
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
		
		menuFileCommands = new JMenuItem();
		menuFileCommands.setText("Commands");
		menuFile.add(menuFileCommands);

		menuFileCommands.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createCommandsDialogBox();
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

		panelCreateSearch.add(titleQuery);
		panelCreateSearch.add(titleSearch);
	
		
		contentQuery.addKeyListener(new KeyListener() {


            @Override
            public void keyPressed(KeyEvent k) {
                int i = 0;
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {


                    Holder = contentQuery.getText().toLowerCase();
                    System.out.println("Key: " + Holder);
                    SearchFiles searcher = new SearchFiles();
                    searcher.getHitsPerPage(results_per_page);

                    ((DefaultListModel) listScrollPane.getModel()).clear();

                    try {
                        results = searcher.directQuoteSearch(Holder, "content");

                        ((DefaultListModel) listScrollPane.getModel()).addElement("Search Query: " + Holder);
                        for (String key : results.keySet()) {
                            ((DefaultListModel) listScrollPane.getModel()).addElement(key);
                            System.out.println(key);
                            i++;
                        }
                        ((DefaultListModel) listScrollPane.getModel()).addElement("Number of results: " + i);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }


        });
		
		titleQuery.addKeyListener(new KeyListener() {


            @Override
            public void keyPressed(KeyEvent k) {
                int i = 0;
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {
                    Holder = titleQuery.getText().toLowerCase();
                    SearchFiles searcher = new SearchFiles();
                    searcher.getHitsPerPage(results_per_page);

                    ((DefaultListModel) listScrollPane.getModel()).clear();

                    try {
                        results = searcher.directQuoteSearch(Holder, "title");

                        ((DefaultListModel) listScrollPane.getModel()).addElement("Search Query: " + Holder);
                        for (String key : results.keySet()) {
                            ((DefaultListModel) listScrollPane.getModel()).addElement(key);
                            i++;
                        }
                        ((DefaultListModel) listScrollPane.getModel()).addElement("Number of results: " + i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }


        });
		
		
		contentSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int i = 0;
                Holder = contentQuery.getText().toLowerCase();
                System.out.println("hi: " + Holder);
                SearchFiles searcher = new SearchFiles();
                searcher.getHitsPerPage(results_per_page);
                
                ((DefaultListModel) listScrollPane.getModel()).clear();

                try {
                    results = searcher.directQuoteSearch(Holder, "content");
                    

                    ((DefaultListModel) listScrollPane.getModel()).addElement("Search Query: " + Holder);
                    for (String key : results.keySet()) {
                        ((DefaultListModel) listScrollPane.getModel()).addElement(key);
                        i++;
                    }
                    ((DefaultListModel) listScrollPane.getModel()).addElement("Number of results: " + i);
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
				int i = 0;
				panelCreateSearch.setFocusable(true);
				topPanel.setVisible(true);
				Holder = titleQuery.getText().toLowerCase();
				SearchFiles searcher = new SearchFiles();
				searcher.getHitsPerPage(results_per_page);

				((DefaultListModel) listScrollPane.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(Holder, "title");


                    ((DefaultListModel) listScrollPane.getModel()).addElement("Search Query: " + Holder);
					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane.getModel()).addElement(key);
						i++;
					}
					
					((DefaultListModel) listScrollPane.getModel()).addElement("Number of results: " + i);

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
		searchQueryExactQuote = new JTextField(25);

		JButton booleanSearch = new JButton("Boolean Search");
		contentSearch = new JButton("Exact Quote Search");

	;
		panelcreateSearchAdvanced1.add(searchQueryStandard);
		panelcreateSearchAdvanced1.add(booleanSearch);

		
		panelcreateSearchAdvanced4.add(searchQueryExactQuote);
		panelcreateSearchAdvanced4.add(contentSearch);

		panelComplexSearch.add(panelcreateSearchAdvanced1);
		panelComplexSearch.add(panelcreateSearchAdvanced4);
		panelComplexSearch.setVisible(true);
		
		
	
		searchQueryStandard.addKeyListener( new KeyListener() {

			@Override
			public void keyPressed(KeyEvent k) {
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {
                	int i = 0;
    				panelComplexSearch.setFocusable(true);
    				topPanel.setVisible(true);
    				AdvancedHolder = searchQueryStandard.getText();
    				SearchFiles searcher = new SearchFiles();
    				searcher.getHitsPerPage(results_per_page);
    				
    				

    				((DefaultListModel) listScrollPane2.getModel()).clear();

    				try {
    					results = searcher.directQuoteSearch(AdvancedHolder, "content");
    					related = rsearches.getRelated(AdvancedHolder);
                        System.out.println(related.toString());

                        ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
    					for (String key : results.keySet()) {
    						((DefaultListModel) listScrollPane2.getModel()).addElement(key);
    						i++;
    					}
                        ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
    					changeButtons(related);
                        window.repaint();

    				} catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
	

			@Override
			public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

			@Override
			public void keyReleased(KeyEvent K) {
				// TODO Auto-generated method stub
				
			}
			
		
		});

		
		searchQueryExactQuote.addKeyListener( new KeyListener() {

			@Override
			public void keyPressed(KeyEvent k) {
                int i = 0;
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {
    				panelComplexSearch.setFocusable(true);
    				topPanel.setVisible(true);
    				AdvancedHolder = searchQueryExactQuote.getText();
    				SearchFiles searcher = new SearchFiles();
    				searcher.getHitsPerPage(results_per_page);

    				((DefaultListModel) listScrollPane2.getModel()).clear();

    				try {
    					results = searcher.directQuoteSearch(AdvancedHolder, "content");
    					related = rsearches.getRelated(AdvancedHolder);
                        System.out.println(related.toString());

                        ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
    					for (String key : results.keySet()) {
    						((DefaultListModel) listScrollPane2.getModel()).addElement(key);
    						i++;
    					}
                        ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
    					changeButtons(related);
                        window.repaint();

    				} catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
	

			@Override
			public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

			@Override
			public void keyReleased(KeyEvent K) {
				// TODO Auto-generated method stub
				
			}
			
		
		});
		
		contentSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i = 0;
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryExactQuote.getText();
				SearchFiles searcher = new SearchFiles();
				searcher.getHitsPerPage(results_per_page);

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(AdvancedHolder, "content");
					related = rsearches.getRelated(AdvancedHolder);
                    System.out.println(related.toString());

                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane2.getModel()).addElement(key);
						i++;
					}
                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
					changeButtons(related);
                    window.repaint();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

			}

		});
		
		
			booleanSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i = 0;
				panelComplexSearch.setFocusable(true);
				topPanel.setVisible(true);
				AdvancedHolder = searchQueryStandard.getText();
				SearchFiles searcher = new SearchFiles();
				searcher.getHitsPerPage(results_per_page);

				((DefaultListModel) listScrollPane2.getModel()).clear();

				try {
					results = searcher.directQuoteSearch(AdvancedHolder, "content");
					related = rsearches.getRelated(AdvancedHolder);
                    System.out.println(related.toString());

                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
					for (String key : results.keySet()) {
						((DefaultListModel) listScrollPane2.getModel()).addElement(key);
						i++;
					}
                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
					changeButtons(related);
                    window.repaint();

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
		
		bottomPanel.setVisible(false);

		RelatedSearchButton1 = new JButton("Related1");
		bottomPanel.add(RelatedSearchButton1);
		
		RelatedSearchButton2 = new JButton("Related2");
		bottomPanel.add(RelatedSearchButton2);
		
		RelatedSearchButton3 = new JButton("Related3");
		bottomPanel.add(RelatedSearchButton3);

        RelatedSearchButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int i = 0;
                topPanel.setVisible(true);
                AdvancedHolder = RelatedSearchButton1.getText();
                SearchFiles searcher = new SearchFiles();
                searcher.getHitsPerPage(results_per_page);

                ((DefaultListModel) listScrollPane2.getModel()).clear();

                try {
                    results = searcher.directQuoteSearch(AdvancedHolder, "content");
                    related = rsearches.getRelated(AdvancedHolder);
                    System.out.println(related.toString());

                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
                    for (String key : results.keySet()) {
                        ((DefaultListModel) listScrollPane2.getModel()).addElement(key);
                        i++;
                    }
                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
                    changeButtons(related);
                    window.repaint();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        RelatedSearchButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int i = 0;
                topPanel.setVisible(true);
                AdvancedHolder = RelatedSearchButton2.getText();
                SearchFiles searcher = new SearchFiles();
                searcher.getHitsPerPage(results_per_page);

                ((DefaultListModel) listScrollPane2.getModel()).clear();

                try {
                    results = searcher.directQuoteSearch(AdvancedHolder, "content");
                    related = rsearches.getRelated(AdvancedHolder);
                    System.out.println(related.toString());

                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
                    for (String key : results.keySet()) {
                        ((DefaultListModel) listScrollPane2.getModel()).addElement(key);
                        i++;
                    }
                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
                    changeButtons(related);
                    window.repaint();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        RelatedSearchButton3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int i = 0;
                topPanel.setVisible(true);
                AdvancedHolder = RelatedSearchButton3.getText();
                SearchFiles searcher = new SearchFiles();
                searcher.getHitsPerPage(results_per_page);

                ((DefaultListModel) listScrollPane2.getModel()).clear();

                try {
                    results = searcher.directQuoteSearch(AdvancedHolder, "content");
                    related = rsearches.getRelated(AdvancedHolder);
                    System.out.println(related.toString());

                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Search Query: " + AdvancedHolder);
                    for (String key : results.keySet()) {
                        ((DefaultListModel) listScrollPane2.getModel()).addElement(key);
                        i++;
                    }
                    ((DefaultListModel) listScrollPane2.getModel()).addElement("Number of results: " + i);
                    changeButtons(related);
                    window.repaint();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

	}

	private void changeButtons(List<String> related){
		if(related.get(0).equals("")){
            RelatedSearchButton1.setVisible(false);
            RelatedSearchButton2.setVisible(false);
            RelatedSearchButton3.setVisible(false);
            bottomPanel.setVisible(false);
		}
		else{
            RelatedSearchButton1.setVisible(true);
            RelatedSearchButton2.setVisible(true);
            RelatedSearchButton3.setVisible(true);
            bottomPanel.setVisible(true);
		}

		RelatedSearchButton1.setText(related.get(0));
		RelatedSearchButton2.setText(related.get(1));
		RelatedSearchButton3.setText(related.get(2));

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
	
	private void createHelpDialogBox() {
		Object[] options = { "Close" };
		Component frame = null;
		int n = JOptionPane.showOptionDialog(frame, "STANDARD SEARCH \n \n"
				+ "Standard search has two search boxes. The box on the left allows you to search all \n"
				+ "of the documents in the indexed list. The search box to the right allows you to search \n "
				+ "through only the titles of the indexed documents.\n \n After you type in your search"
				+ "query, a pane on the left side of the screen will be updated \n with a list of relevant"
				+ " documents.\n \n Clicking on one of these will load it into the screen on the "
				+ "right and from there you can \n read the document. \n \n You can also use the inbuilt "
				+ "hyperlinks within the document to traverse the book. \n \n"
				+ "ADVANCED SEARCH \n \n"
				+ "Advanced Search allows the searching of queries in both the standard way \n"
				+ "and as whole quotes through the use of the quoted search. \n \n"
				+ "When you perform a search using the advanced search, you may recieve \n"
				+ "buttons appearing at the bottom of the screen. These are a list of \n"
				+ "words related to your search query and selecting one of these will \n"
				+ "search for that word."
				+ ""
				+ ""
				+ ""
				+ "", 
				"Help", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);

	}
	
	private void createCommandsDialogBox() {
		Object[] options = { "Close" };
		Component frame = null;
		int n = JOptionPane.showOptionDialog(frame, ""
				+ "Enter: Hitting enter on standard search will perform a search. \n \n"
				+ "AND: A boolean operator that allows for searching for multiple words \n"
				+ "within the same document(String AND 1234).\n \n"
				+ "OR: A boolean operator that allows for searching for multiple words \n"
				+ "within the entire index(String OR 1234).\n \n"
				+ "NOT: A boolean operator that allows for the removal of search results \n"
				+ "containing certain words(String NOT 1234). \n \n"
				+ "*/?: Wildcard operators that can be inserted into words if you do not \n"
				+ "know the exact spelling. * allows for one missing letter while ? \n"
				+ "allows for multiple missing letters(Str*ng). \n \n"
				+ "^: Boosted operator that allows you to increase the importance of a \n"
				+ "particular search term(String^20 OR 1234). \n \n"
				+ "",
				"Commands", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
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
				+ "<html><a href=\" " + "\">https://lucene.apache.org/</a></html> \n"
				+ "\n GUI designed using the Swing Java Toolkit. ",
				"Information", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
				options[0]);
	}

}
