package GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class SearchWindow {
	

		
		boolean debug = true;

		public void run() {
			JFrame frameMain = new JFrame("Folio Tracker");
			frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameMain.setPreferredSize(new Dimension(800, 375));
			
			/****** Different Panels ******/
			JPanel panelMain = new JPanel();
			JPanel panelTab = new JPanel();
			JPanel panelAddStock = new JPanel();
			JPanel panelTable = new JPanel();
			JPanel panelBottomButtons = new JPanel();
			
			/*** Layouts for Panels ***/
			panelMain.setLayout(new FlowLayout());
			
			// tabbed contents
			panelAddStock.setLayout(new FlowLayout());
			panelTable.setLayout(new BoxLayout(panelTable, BoxLayout.Y_AXIS));
			panelTab.setLayout(new BoxLayout(panelTab, BoxLayout.Y_AXIS));
			
			panelBottomButtons.setLayout(new BoxLayout(panelBottomButtons, BoxLayout.X_AXIS));
			
			/****** Menu Bar ******/
			JMenuBar menuBar = new JMenuBar();
			JMenu menuFolio = new JMenu("Folio");
			// create them
			JMenuItem menuCreate = new JMenuItem("Create...");
			JMenuItem menuOpen = new JMenuItem("Open...");
			JMenuItem menuSave = new JMenuItem("Save...");
			JMenuItem menuExit = new JMenuItem("Exit");
			// add them
			frameMain.setJMenuBar(menuBar);
			menuBar.add(menuFolio);
			menuFolio.add(menuCreate);
			menuFolio.add(menuOpen);
			menuFolio.add(menuSave);
			menuFolio.add(menuExit);
			
			/****** Menu Actions ******/
			menuCreate.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Create Stock?
				}
				
			});
			
			menuOpen.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Open stocks
				}
				
			});
			
			menuSave.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Save stocks
				}
				
			});
			
			menuExit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO save before it exits?
					System.exit(0);
				}
				
			});
			
			
			/****** Stock Info Table ******/
			JTable tableStock = new JTable();
			JScrollPane scrollPane = new JScrollPane(tableStock);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			
			
			/****** Text Fields ******/
			JTextField textTickerSymbol = new JTextField(8);
			JTextField textNumberOfShares = new JTextField(8);
			
			/****** Buttons ******/
			JButton btnAddStock = new JButton("Add");
			JButton btnDeleteStock = new JButton("Delete");
			JButton btnCloseProgram = new JButton("Close");
			
			/****** Button Action Listeners ******/
			btnAddStock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// take input from textTickerSymbol, textNumberOfShares.
					// check if valid
					// create stock with these values.
					if (textTickerSymbol.getText().length() > 0) {
						String stockSymbol = textTickerSymbol.getText();
						int stockAmount = 0;
						try {
							stockAmount = Integer.parseInt(textNumberOfShares.getText());
						} catch (NumberFormatException e) {
							// do nothing
						}
						if (stockAmount > 0) {
							//TODO actually add the stock
							System.out.println(String.format(
									"Add stock '%s' (%s) (placeholder)", stockSymbol, stockAmount));
						} else {
							JOptionPane.showMessageDialog(frameMain, "Invalid Stock Number",
									"Error Creating Stock.", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(frameMain, "Invalid Stock Name", 
								"Error Creating Stock.", JOptionPane.ERROR_MESSAGE);
					}
				}

			});

			btnDeleteStock.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// delete stock, one that is selected in table?
					// or the one that symbol name is input?
					int replyInt = JOptionPane.showConfirmDialog(
						    frameMain,
						    "Are you sure you want to delete this Portfolio?",
						    "Portfolio Deletion",
						    JOptionPane.YES_NO_OPTION);
					String reply = ((replyInt == 0) ? "Yes" : "No");
				
					System.out.println(String.format("Users reply to deleting Portfolio: '%s'. (placeholder)", reply));
				}
				
			});
			
			btnCloseProgram.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (debug) {
						System.exit(0);
					} else {
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
				}
				
			});
			
			
			/****** Add Stock Panel ******/
			panelAddStock.add(new JLabel("Ticker Symbol"));
			panelAddStock.add(textTickerSymbol);
			panelAddStock.add(new JLabel("Number of Shares"));
			panelAddStock.add(textNumberOfShares);
			panelAddStock.add(btnAddStock);
			
			/****** Add Table Panel ******/
			panelTable.add(scrollPane);
			panelTable.add(new JLabel("Total Value of Stocks: "));
			
			/****** Add above to tab panel ******/
			panelTab.add(panelAddStock);
			panelTab.add(panelTable);
			
			/****** Create tabs ******/
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Folio 1", panelTab);		
			tabbedPane.addTab("Folio 2", panelTab);
			
			/****** Add Bottom Buttons Panel ******/
			panelBottomButtons.add(btnDeleteStock);
			panelBottomButtons.add(btnCloseProgram);
			
			/****** Add them all all to the window ******/
			panelMain.add(tabbedPane);
			// all bar bottom buttons should be within a tab
			panelMain.add(panelBottomButtons);
			frameMain.add(panelMain);
			frameMain.setResizable(false);
			frameMain.pack();
			frameMain.setVisible(true);
		}

	}


