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
	}
	
	public void paint(Graphics g){
		drawText(g, 385, 440, 20, "Map Tools");
		drawText(g, 385, 460, 15, "Snap to grid:            Grid spacing:");
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
