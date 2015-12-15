import model.IndexFiles;
import view.SearchGUI;
import view.SearchWindow;

public class Main {

	
	public static void main(String[] args)  {
//		IndexFiles index = new IndexFiles();
//		SearchWindow gui1 = new SearchWindow();
//		gui1.run();

		new IndexFiles();
		SearchGUI gui = new SearchGUI();

		gui.window.setVisible(true);
	}
	

}
