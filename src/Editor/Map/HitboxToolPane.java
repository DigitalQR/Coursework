package Editor.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

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
				if(parent.getCurrentID() > parent.world.getHitboxList().size()-1){
					parent.setCurrentID(parent.world.getHitboxList().size()-1);
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
				if(parent.getCurrentID() > parent.world.getHitboxList().size()-1){
					parent.setCurrentID(parent.world.getHitboxList().size()-1);
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
				for(Hitbox hb: parent.world.getHitboxList()){
					if(i == parent.getCurrentID()){
						parent.world.removeHitbox(hb);
						parent.setCurrentID(-9897);
						break;
					}
					i++;
				}
				
			}
			
		});
		parent.add(delete);
	}
	
	public void paint(Graphics g){
		int y = 0;
		drawText(g, 10, y+=30, 30, "Hitbox Tools");
		drawText(g, 10, y+=20, 15, "Hitbox Picker:");
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
	
	public static void drawText(Graphics g, int x, int y, int size, String message){
		final Font font = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, size);
		g.setFont(font);

		g.setColor(new Color(0f,0f,0f));
		final int spacing = 1;
		g.drawString(message, x-spacing, y+spacing);
		g.drawString(message, x+spacing, y-spacing);
		g.drawString(message, x-spacing, y-spacing);
		g.drawString(message, x+spacing, y+spacing);
		g.setColor(new Color(1f,1f,1f));
		g.drawString(message, x, y);
	}
}
