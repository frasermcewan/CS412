import model.IndexFiles;
import view.SearchGUI;
import view.SearchWindow;

public class Main {

	
	public static void main(String[] args)  {
//		IndexFiles index = new IndexFiles();
//		SearchWindow gui = new SearchWindow();
//		gui.run();

		SearchGUI gui = new SearchGUI();
		IndexFiles index = new IndexFiles();

		gui.window.setVisible(true);
	}
	

}
