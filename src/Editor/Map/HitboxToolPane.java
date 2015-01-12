package Editor.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Collision.Hitbox;

public class HitboxToolPane {
	
	public static boolean change = false;
	
	private WorldCanvas parent;
	private JButton delete;
	
	public HitboxToolPane(final WorldCanvas parent){
		this.parent = parent;
		
		final JButton IDMinus = new JButton("-");
		IDMinus.setBackground(new Color(0.6f,0.6f,0.6f));
		IDMinus.setForeground(new Color(1f,1f,1f));
		IDMinus.setLocation(15,70);
		IDMinus.setSize(50,20);
		IDMinus.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				parent.setCurrentID(parent.getCurrentID() - 1);
				if(parent.getCurrentID() < 0){
					parent.setCurrentID(0);
				}
				if(parent.getCurrentID() > parent.layer.get(parent.currentLayer).size()-1){
					parent.setCurrentID(parent.layer.get(parent.currentLayer).size()-1);
				}
			}
			
		});
		parent.add(IDMinus);
		
		final JButton IDPlus = new JButton("+");
		IDPlus.setBackground(new Color(0.6f,0.6f,0.6f));
		IDPlus.setForeground(new Color(1f,1f,1f));
		IDPlus.setLocation(70,70);
		IDPlus.setSize(50,20);
		IDPlus.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				parent.setCurrentID(parent.getCurrentID() + 1);
				if(parent.getCurrentID() < 0){
					parent.setCurrentID(0);
				}
				if(parent.getCurrentID() > parent.layer.get(parent.currentLayer).size()-1){
					parent.setCurrentID(parent.layer.get(parent.currentLayer).size()-1);
				}
			}
			
		});
		parent.add(IDPlus);
		
		delete = new JButton("D e l e t e");
		delete.setBackground(new Color(1f,0f,0f));
		delete.setForeground(new Color(1f,1f,1f));
		delete.setLocation(130,70);
		delete.setSize(100,20);
		delete.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				int i = 0;
				for(Hitbox hb: parent.layer.get(parent.currentLayer)){
					if(i == parent.getCurrentID()){
						parent.layer.get(parent.currentLayer).remove(hb);
						parent.setCurrentID(-9897);
						break;
					}
					i++;
				}
				
			}
			
		});
		
		delete = new JButton("D e l e t e");
		delete.setBackground(new Color(1f,0f,0f));
		delete.setForeground(new Color(1f,1f,1f));
		delete.setLocation(130,70);
		delete.setSize(100,20);
		delete.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				int i = 0;
				for(Hitbox hb: parent.layer.get(parent.currentLayer)){
					if(i == parent.getCurrentID()){
						parent.layer.get(parent.currentLayer).remove(hb);
						parent.setCurrentID(-9897);
						break;
					}
					i++;
				}
				
			}
			
		});
		parent.add(delete);

		//RGBA
		final JTextField[] RGBA = new JTextField[4];
		
		for(int i = 0; i<4; i++){
			RGBA[i] = new JTextField("1.0");
			RGBA[i].setBackground(new Color(0.8f,0.8f,0.8f));
			RGBA[i].setForeground(new Color(0f,0f,0f));
			RGBA[i].setLocation(260,55+20*i);
			RGBA[i].setSize(60,20);
			parent.add(RGBA[i]);
		}
		
		JButton setColour = new JButton("S e t");
		setColour.setBackground(new Color(0.6f,0.6f,0.6f));
		setColour.setForeground(new Color(1f,1f,1f));
		setColour.setLocation(240,140);
		setColour.setSize(60,20);
		setColour.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				try{
					float[] colour = new float[4];
					for(int i = 0; i<4; i++){
						colour[i] = Float.parseFloat(RGBA[i].getText());
					}
					
					parent.setBrushColour(colour);
				}catch(NumberFormatException e){
					
				}
				
			}
			
		});
		parent.add(setColour);
		
	}
	
	public void paint(Graphics g){
		int y = 0;
		drawText(g, 10, y+=30, 30, "Hitbox Tools");
		drawText(g, 10, y+=20, 15, "Hitbox Picker:");

		HitboxToolPane.r = parent.getBrushColour()[0];
		HitboxToolPane.g = parent.getBrushColour()[1];
		HitboxToolPane.b = parent.getBrushColour()[2];
		HitboxToolPane.r1 = 0;
		HitboxToolPane.g1 = 0;
		HitboxToolPane.b1 = 0;
		drawText(g, 240, y, 15, "Brush Colour:");
		HitboxToolPane.r = 1;
		HitboxToolPane.g = 1;
		HitboxToolPane.b = 1;
		HitboxToolPane.r1 = 0;
		HitboxToolPane.g1 = 0;
		HitboxToolPane.b1 = 0;
		drawText(g, 240, y+20, 15, "R:");
		drawText(g, 240, y+40, 15, "G:");
		drawText(g, 240, y+60, 15, "B:");
		drawText(g, 240, y+80, 15, "A:");
		
		String ID = "ID: ";
		if(parent.getCurrentID() >= 0){
			ID+= parent.getCurrentID();
		}else if(parent.getCurrentID() == -3){
			ID += " >Picking";
		}
		drawText(g, 10, y+=15, 15, ID);
		drawText(g, 10, y+=45, 15, "Location: ");
		
		if(parent.getCurrentID() < 0){
			delete.setVisible(false);
		}else{
			delete.setVisible(true);
		}
	}

	private static float r = 0, g = 0, b = 0;
	private static float r1 = 1, g1 = 1, b1 = 1;
	
	public static void drawText(Graphics g, int x, int y, int size, String message){
		final Font font = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, size);
		g.setFont(font);

		g.setColor(new Color(r1,g1,b1));
		final int spacing = 1;
		g.drawString(message, x-spacing, y+spacing);
		g.drawString(message, x+spacing, y-spacing);
		g.drawString(message, x-spacing, y-spacing);
		g.drawString(message, x+spacing, y+spacing);
		g.setColor(new Color(r1,g1,b1));
		g.setColor(new Color(HitboxToolPane.r,HitboxToolPane.g,HitboxToolPane.b));
		g.drawString(message, x, y);
	}
}
