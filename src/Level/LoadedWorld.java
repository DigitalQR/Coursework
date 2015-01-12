package Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;

public class LoadedWorld extends ParsedWorld{

	public LoadedWorld(String name) {
		super(getData(name));
		this.generateMaingroundBasedModels();
	}
	
	private static String getData(String name){
		File file = new File("Res/World/" + name + ".pw");
		if(file.exists()){
			Scanner scan = null;
			try {
				scan = new Scanner(file);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			String data = "";
			
			while(scan.hasNext()) data+= scan.next();
			scan.close();
			
			return data.substring(3, data.length()-1);

		}else{
			JOptionPane.showMessageDialog(Display.getParent(), "Could not load map: " + name + "!", "Error", JOptionPane.ERROR_MESSAGE);
			return "Error";
		}
	}
	
}
