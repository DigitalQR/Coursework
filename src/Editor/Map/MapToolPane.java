package Editor.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class MapToolPane{

	public MapToolPane(final WorldCanvas parent){
		final JButton gridSnap = new JButton("O F F");
		gridSnap.setBackground(new Color(1f,0f,0f));
		gridSnap.setForeground(new Color(1f,1f,1f));
		gridSnap.setLocation(390,465);
		gridSnap.setSize(70,20);
		gridSnap.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				parent.gridSnapping=!parent.gridSnapping;
				
				if(parent.gridSnapping){
					gridSnap.setText("O N");
					gridSnap.setBackground(new Color(0f,1f,0f));
				}else{
					gridSnap.setText("O F F");
					gridSnap.setBackground(new Color(1f,0f,0f));
				}

			}
			
		});
		parent.add(gridSnap);

		final JButton gridMinus = new JButton("-");
		gridMinus.setBackground(new Color(0.6f,0.6f,0.6f));
		gridMinus.setForeground(new Color(1f,1f,1f));
		gridMinus.setLocation(515,465);
		gridMinus.setSize(50,20);
		gridMinus.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				parent.alterGridSpacing(2);
			}
			
		});
		parent.add(gridMinus);
		
		final JButton gridPlus = new JButton("+");
		gridPlus.setBackground(new Color(0.6f,0.6f,0.6f));
		gridPlus.setForeground(new Color(1f,1f,1f));
		gridPlus.setLocation(570,465);
		gridPlus.setSize(50,20);
		gridPlus.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent a){
				parent.alterGridSpacing(-2);
			}
			
		});
		parent.add(gridPlus);

		final JButton[] currentLayer = new JButton[5];
		final JButton[] viewLayer = new JButton[5];
		
		for(int i = 0; i<5; i++){
			currentLayer[i] = new JButton("Edit");
			currentLayer[i].setBackground(new Color(0.6f,0.6f,0.6f));
			if(i == parent.currentLayer){
				currentLayer[i].setBackground(new Color(0f, 1f, 0f));
			}
			currentLayer[i].setForeground(new Color(1f,1f,1f));
			currentLayer[i].setLocation(410, 505+25*i);
			currentLayer[i].setSize(55,20);
			
			final int val = i;
			currentLayer[i].addActionListener(new ActionListener(){
				
				
				public void actionPerformed(ActionEvent a){
					for(int n = 0; n<5; n++){
						if(n == val){
							currentLayer[n].setBackground(new Color(0f, 1f, 0f));
							viewLayer[n].setBackground(new Color(0f, 1f, 0f));
							parent.drawLayer[n] = true;
						}else{
							currentLayer[n].setBackground(new Color(0.6f,0.6f,0.6f));
						}
					}
					parent.currentLayer = val;
				}
				
			});
			parent.add(currentLayer[i]);
			

			viewLayer[i] = new JButton("");
			if(parent.drawLayer[i]){
				viewLayer[i].setBackground(new Color(0f, 1f, 0f));
			}else{
				viewLayer[i].setBackground(new Color(1f, 0f, 0f));
			}
			viewLayer[i].setForeground(new Color(1f,1f,1f));
			viewLayer[i].setLocation(470, 505+25*i);
			viewLayer[i].setSize(20,20);
			
			final int val1 = i;
			viewLayer[i].addActionListener(new ActionListener(){
				
				
				public void actionPerformed(ActionEvent a){
					if(val1 != parent.currentLayer){
						parent.drawLayer[val1] = !parent.drawLayer[val1];
						if(parent.drawLayer[val1]){
							viewLayer[val1].setBackground(new Color(0f, 1f, 0f));
						}else{
							viewLayer[val1].setBackground(new Color(1f, 0f, 0f));
						}
					}
				}
				
			});
			parent.add(viewLayer[i]);
		}
	}
	
	public void paint(Graphics g){
		drawText(g, 385, 440, 20, "Map Tools");
		drawText(g, 385, 460, 15, "Snap to grid:            Grid spacing:");
		drawText(g, 385, 500, 15, "Layers:");
		
		for(int i = 0; i<5; i++){
			if(i == 2){
				r = 1;
			}else{
				r = 0;
			}
			drawText(g, 385, 520+25*i, 15, "[" + i + "]:");
		}
	}
	
	private static float r = 0;
	
	public static void drawText(Graphics g, int x, int y, int size, String message){
		final Font font = new Font(new JLabel().getFont().getFontName(), Font.PLAIN, size);
		g.setFont(font);

		g.setColor(new Color(r,0f,0f));
		final int spacing = 1;
		g.drawString(message, x-spacing, y+spacing);
		g.drawString(message, x+spacing, y-spacing);
		g.drawString(message, x-spacing, y-spacing);
		g.drawString(message, x+spacing, y+spacing);
		g.setColor(new Color(1f,1f,1f));
		g.drawString(message, x, y);
	}
}
