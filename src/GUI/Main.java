package GUI;

import javax.swing.SwingUtilities;

public class Main {

	
	public static void main(String[] args)  {
		SearchWindow gui = new SearchWindow();
		SwingUtilities.invokeLater((Runnable) gui);
	}
	

}
