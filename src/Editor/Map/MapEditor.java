package Editor.Map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import Collision.ColourHitbox;
import Collision.Hitbox;
import Debug.ErrorPopup;
import Level.BlankWorld;
import Level.World;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

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
		
		window.setSize(1100,760);
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
				w.resetLayers();
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
					World world = new BlankWorld();
				
					ArrayList<Hitbox> hb = new ArrayList<Hitbox>();
					for(ColourHitbox h: w.layer.get(2)){
						hb.add(h);
					}
					world.setHitboxes(hb);
										
					world.generateBackgroundModels(w.layer.get(0));
					world.generateImmediateBackgroundModels(w.layer.get(1));
					world.generateMaingroundModels(w.layer.get(2));
					world.generateImmediateForegroundModels(w.layer.get(3));
					world.generateForegroundModels(w.layer.get(4));
					
					scribe.format("%s", world.encode());
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
					
					World world = World.decode(data.substring(3, data.length()-1));
					
					w.resetLayers();

					for(Model m: world.getBackRenderList()){
						int layer = 1;
						if(m.getVertices()[2] == -1f){
							layer = 0;
						}else if(m.getVertices()[14] != 0.1f){
							layer = 2;
						}
						
						float x = m.getVertices()[0];
						float y = m.getVertices()[1];
						float xi = m.getVertices()[3];
						float yi = m.getVertices()[7];

						if(xi < x){
							float temp = xi;
							xi = x;
							x = temp;
						}
						if(yi < y){
							float temp = y;
							yi = y;
							y = temp;
						}
						
						ColourHitbox hb = new ColourHitbox(new Vector2f(x,y), new Vector2f(xi-x, yi-y));
						hb.setRGBA(m.getRGBA());
						w.layer.get(layer).add(hb);
					}
					
					for(Model m: world.getFrontRenderList()){
						int layer = 3;
						if(m.getVertices()[2] == 1f){
							layer = 4;
						}
						
						float x = m.getVertices()[0];
						float y = m.getVertices()[1];
						float xi = m.getVertices()[3];
						float yi = m.getVertices()[7];

						if(xi < x){
							float temp = xi;
							xi = x;
							x = temp;
						}
						if(yi < y){
							float temp = y;
							yi = y;
							y = temp;
						}

						ColourHitbox hb = new ColourHitbox(new Vector2f(x,y), new Vector2f(xi-x, yi-y));
						hb.setRGBA(m.getRGBA());
						w.layer.get(layer).add(hb);
					}

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
