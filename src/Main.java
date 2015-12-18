import model.IndexFiles;
import view.SearchGUI;


public class Main {

	
	public static void main(String[] args)  {
		new IndexFiles();
		SearchGUI gui = new SearchGUI();

		gui.window.setVisible(true);
	}
	

}
