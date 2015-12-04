package view;

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

    // Pane Objects
    private JEditorPane displayEditorPane;
    private JTabbedPane tabbedPane;

    // Menu Objects
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuFileNew;
    private JMenuItem menuFileOpen;
    private JMenuItem menuFileSave;
    private JMenuItem menuFileSaveAs;
    private JMenuItem menuFileExit;

    private Map<String, String> results;

    private JList<String> listScrollPane = new JList<>(new DefaultListModel<>());

    public SearchGUI () {
        window.setTitle("Java E-Book Search");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(900, 700);
        window.setResizable(false);
        tabbedPane = new JTabbedPane();

        JPanel panelAll = new JPanel();
        panelAll.setLayout( new BorderLayout() );
        panelAll.add(makeSearchPanel(), BorderLayout.NORTH);
        makeMenu();
        panelAll.add(makeSplitPane(), BorderLayout.CENTER);
//        panelAll.add(tabbedPane, BorderLayout.WEST);
//        panelAll.add(makeDisplayPanel(), BorderLayout.EAST);
        window.getContentPane().add(panelAll);


        listScrollPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listScrollPane.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if(results != null) {
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

    }

    public void makeMenu() {
        topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );

        window.getContentPane().add( topPanel );

        // Create the menu bar
        menuBar = new JMenuBar();

        // Set this instance as the application's menu bar
        window.setJMenuBar( menuBar );

        // Create the file menu
        menuFile = new JMenu( "File" );
        menuBar.add( menuFile );

        // Create the file menu
        // Build a file menu items
        menuFileNew = new JMenuItem();
        menuFileNew.setText("New Search");
        menuFile.add(menuFileNew);

		menuFileOpen = new JMenuItem();
		menuFileOpen.setText("Re-Index");
		menuFile.add(menuFileOpen);
//
//		menuFileSave = new JMenuItem();
//		menuFileSave.setText("Save folio");
//		menuFile.add(menuFileSave);
//
//		menuFileSaveAs = new JMenuItem();
//		menuFileSaveAs.setText("Save folio As");
//		menuFile.add(menuFileSaveAs);

        menuFileExit = new JMenuItem();
        menuFileExit.setText("Close program");
        menuFile.add(menuFileExit);
    }

    private JSplitPane makeSplitPane() {

        JScrollPane scrollListPane = new JScrollPane(listScrollPane);
        JScrollPane scrollDisplayPane = new JScrollPane(makeDisplayPanel());

        final JSplitPane panelSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollListPane, scrollDisplayPane);

        panelSplitPane.setResizeWeight(0.5);
        panelSplitPane.setOneTouchExpandable(true);
        panelSplitPane.setContinuousLayout(true);


        return panelSplitPane;
    }

    private JPanel makeSearchPanel() {

        final JPanel panelCreateSearch = new JPanel();

        /****** Text Fields ******/
        JTextField searchQuery = new JTextField(15);

        /***** Radio Button *****/
//        JRadioButton radButton1 = new JRadioButton("SimpleSearch",true);
//        JRadioButton radButton2 = new JRadioButton("Complex Search", false);
//        JRadioButton radButton3 = new JRadioButton("Related Search Items",false);
//
//        //Group the radio buttons.
//        ButtonGroup group = new ButtonGroup();
//        group.add(radButton1);
//        group.add(radButton2);
//        group.add(radButton3);

        /****** Buttons ******/
        JButton commenceSearch = new JButton("Search");

        // Add Panel
        panelCreateSearch.add(new JLabel("Enter Query"));
        panelCreateSearch.add(searchQuery);
//        panelCreateSearch.add(radButton1);
//        panelCreateSearch.add(radButton2);
//        panelCreateSearch.add(radButton3);
        panelCreateSearch.add(commenceSearch);

        commenceSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	panelCreateSearch.setFocusable(true);
                String holder = searchQuery.getText();
                SearchFiles searcher = new SearchFiles();

                ((DefaultListModel)listScrollPane.getModel()).clear();

                try {
                    results = searcher.newsearch(holder);

                    for (String key : results.keySet()) {
                        ((DefaultListModel)listScrollPane.getModel()).addElement( key );
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


    @Override
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
                    System.out.println(event.getURL());
                    displayEditorPane.setPage(event.getURL());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
