package Editor.Map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Debug.ErrorPopup;
import Level.BlankWorld;
import Level.World;

public class MapEditor {
	
	public static BufferedImage icon;
	
	private static void getResources(){
		try{
			icon = ImageIO.read(new File("Res/Button/Icon64.png"));
		}catch(IOException e){
			ErrorPopup.createMessage(e, true);
		}
	}
	
	public static void main(String[] args){
		getResources();
		
		final JFrame window  = new JFrame(" S Q U A R E   O F F   [   M a p   E d i t o r   ] - '<Untitled>'");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setIconImage(icon);
		
		window.setSize(1100,735);
		window.setResizable(false);

		final WorldCanvas w = new WorldCanvas();
		window.add(w);
		
		JMenuBar menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);

		JMenuItem New = new JMenuItem("New");
		New.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				w.world = new BlankWorld();
				window.setTitle(" S Q U A R E   O F F   [   M a p   E d i t o r   ] - '<Untitled>'");
			}
		});
		file.add(New);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				 String name = JOptionPane.showInputDialog(window, "File name:",  "Save", 1);
				 File file = new File("Res/World");
				 file.mkdir();
				 
				try{
					Formatter scribe = new Formatter("Res/World/" + name + ".pw");
					scribe.format("%s", w.world.encode());
					scribe.close();
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
				window.setTitle(" S Q U A R E   O F F   [   M a p   E d i t o r   ] - '" + name +"' ");
			}
		});
		file.add(save);
		
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				String name = JOptionPane.showInputDialog(window, "File name",  "Load", 1);

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
					
					w.world = World.decode(data.substring(3, data.length()-1));

					window.setTitle(" S Q U A R E   O F F   [   M a p   E d i t o r   ] - '" + name +"' ");
				}else{
					JOptionPane.showMessageDialog(window, "Could not load " + name + "!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		file.add(load);
		
		window.setVisible(true);
		
		while(window.isVisible()){
			w.updateUI();
			
			try {
				TimeUnit.MILLISECONDS.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
