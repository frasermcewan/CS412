import model.IndexFiles;
import view.SearchGUI;


public class Main {

	
	public static void main(String[] args)  {
//		IndexFiles index = new IndexFiles();
//		SearchWindow gui = new SearchWindow();
//		gui.run();

		new IndexFiles();
		SearchGUI gui = new SearchGUI();

		gui.window.setVisible(true);
	}
	

}
