package controller;

import lucene.IndexFiles;
import view.SearchWindow;

public class Main {

	
	public static void main(String[] args)  {
//		IndexFiles index = new IndexFiles();
		SearchWindow gui = new SearchWindow();
		gui.run();
	}
	

}
