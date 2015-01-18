package Control.Setup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import Control.MainControl;
import Control.Settings;
import Control.Audio.Sound;
import Control.Visual.DisplayControl;
import Control.Visual.DisplayManager;
import Debug.ErrorPopup;

public class Launch {

	protected static BufferedImage header;
	public static BufferedImage icon;
	
	private static void getResources(){
		try{
			header = ImageIO.read(new File("Res/Button/Header.png"));
			icon = ImageIO.read(new File("Res/Button/Icon64.png"));
		}catch(IOException e){
			ErrorPopup.createMessage(e, true);
		}
	}
	
	public static void main(String[] args){
		getResources();
		
		JFrame window  = new JFrame(" S Q U A R E   O F F ");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);
		window.setBackground(new Color(0.5f, 0.5f, 0.5f, 0));
		window.setIconImage(icon);
		window.setOpacity(1f);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		window.setSize(400,600);
		window.setLocation(screen.width/2-200, screen.height/2-300);
		
		Content content;
		try{
			content = new Content(window);
			content.setOpaque(true);
			window.add(content);
			
			window.setVisible(true);
		}catch(LWJGLException e){
			ErrorPopup.createMessage(e, true);
		}
	}
}

class Content extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private static BufferedImage header = Launch.header;
	
	public Content(final JFrame parent) throws LWJGLException{
		this.setLayout(null);
		
		//Resolution		
		final DisplayMode[] displays = Display.getAvailableDisplayModes();
		final ArrayList<Integer> displayPointer = new ArrayList<Integer>();
		ArrayList<String> content = new ArrayList<String>();
		
		int i = 0;
		for(DisplayMode dm: displays){
			if(dm.isFullscreenCapable() && (dm.getWidth()+dm.getHeight())%(16+9) == 0 ){
				
				if(!content.contains(dm.getWidth() + ":" + dm.getHeight())){
					content.add(dm.getWidth() + ":" + dm.getHeight());
					displayPointer.add(i);
				}
			}
			i++;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final
		JComboBox dropDown = new JComboBox(content.toArray());
		dropDown.setLocation(30, 160);
		dropDown.setSize(100, 20);
		dropDown.setBackground(new Color(0.9f,0.9f,0.9f));
		dropDown.setForeground(new Color(0f,0f,0f));
		this.add(dropDown);
		
		//Full-screen
		final JButton fullscreen = new JButton("T R U E");
		fullscreen.setBackground(new Color(0.9f,0.9f,0.9f));
		fullscreen.setForeground(new Color(0f,0f,0f));
		fullscreen.setLocation(30, 210);
		fullscreen.setSize(100, 20);
		fullscreen.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				if(fullscreen.getText().equals("T R U E")){
					fullscreen.setText("F A L S E");
				}else{
					fullscreen.setText("T R U E");
				}
			}
			
		});
		this.add(fullscreen);
		
		//Sound
		final JButton sound = new JButton("T R U E");
		sound.setBackground(new Color(0.9f,0.9f,0.9f));
		sound.setForeground(new Color(0f,0f,0f));
		sound.setLocation(30, 260);
		sound.setSize(100, 20);
		sound.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				if(sound.getText().equals("T R U E")){
					sound.setText("F A L S E");
				}else{
					sound.setText("T R U E");
				}
			}
			
		});
		this.add(sound);
		
		/*
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 30, 0);
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(10);
		//slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setLocation(130,  260);
		slider.setSize(200, 40);
		slider.setBackground(new Color(0.9f,0.9f,0.9f, 0f));
		slider.setForeground(new Color(0.9f,0.9f,0.9f));
		slider.setOpaque(false);
		slider.setPaintTrack(true);
		this.add(slider);
		*/
		
		//Username
		final JTextField name = new JTextField(System.getProperty("user.name"));
		name.setBackground(new Color(0.9f,0.9f,0.9f));
		name.setForeground(new Color(0f,0f,0f));
		name.setLocation(150, 160);
		name.setSize(100, 20);
		this.add(name);
		
		
		
		//Start/Exit
		JButton exit = new JButton("E X I T");
		exit.setBackground(new Color(1f,0f,0f));
		exit.setForeground(new Color(1f,1f,1f));
		exit.setLocation(20, 540);
		exit.setSize(100, 30);
		exit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				parent.setVisible(false);
				parent.dispose();
				System.exit(0);
			}
			
		});
		this.add(exit);

		JButton start = new JButton("S T A R T");
		start.setBackground(new Color(0f,1f,0f));
		start.setForeground(new Color(1f,1f,1f));
		start.setLocation(280, 540);
		start.setSize(100, 30);
		start.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				parent.setVisible(false);
				parent.dispose();
				String data = "";
				
				DisplayMode dm = displays[displayPointer.get(dropDown.getSelectedIndex())];
				DisplayControl.display = dm;
				data += dropDown.getSelectedIndex() + ";";
				
				if(fullscreen.getText().equals("T R U E")){
					DisplayManager.fullscreen = true;
					data += "t;";
				}else{
					DisplayManager.fullscreen = false;
					data += "f;";
				}

				if(sound.getText().equals("T R U E")){
					Sound.alive = true;
					data += "t;";
				}else{
					Sound.alive = false;
					data += "f;";
				}
				
				Settings.USERNAME = System.getProperty("user.name");
				if(!name.getText().equals("")){
					Settings.USERNAME = name.getText();
				}
				data += Settings.USERNAME + ";";
				
				
				try{
					Formatter pref = new Formatter("Res/startup.pref");
					pref.format("%s", data);
					pref.close();
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
				
				MainControl.launch();
			}
			
		});
		this.add(start);
		
		File pref = new File("Res/startup.pref");
		if(pref.exists()){
			try{
				Scanner prefScan = new Scanner(pref);
				String data = "";
				while(prefScan.hasNext()) data+= prefScan.next();
				prefScan.close();
				
				String[] parts = data.split(";");
				int index = Integer.parseInt(parts[0]);
				dropDown.setSelectedIndex(index);
					
				if(parts[1].equals("f")){
					fullscreen.setText("F A L S E");
				}
				if(parts[2].equals("f")){
					sound.setText("F A L S E");
				}
				if(!parts[3].equals("")){
					name.setText(parts[3]);
				}
				
			}catch(FileNotFoundException | IllegalArgumentException | ArrayIndexOutOfBoundsException e){}
		}
	}
	
	private float r = (float)Math.random(), g = (float)Math.random(), b = (float)Math.random();
	
	public void paintComponent(Graphics g){		
		g.setColor(new Color(this.r,this.g,this.b, 0.05f));
		for(int i = 0; i<100; i++){
			g.fillRect(i,i,399-i*2,599-i*2);
		}
		
		g.drawImage(header, 10, -10, 390, 135, 0, 0, header.getWidth(), header.getHeight(), null);

		drawText(g, 15, 25, 15, "Version " +Settings.Version);
		drawText(g, 20, 130, 15, "Launch Options:");
		drawText(g, 20, 150, 13, "Resolution");
		drawText(g, 150, 150, 13, "Online Username");
		drawText(g, 20, 200, 13, "Fullscreen");
		drawText(g, 20, 250, 13, "Sound");
	}
	
	public static void drawText(Graphics g, int x, int y, int size, String message){
		final Font font = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, size);
		g.setFont(font);

		g.setColor(new Color(0f,0f,0f));
		g.drawString(message, x-1, y+1);
		g.setColor(new Color(1f,1f,1f));
		g.drawString(message, x, y);
	}
	
}
