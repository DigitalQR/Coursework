import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


public class GameWindow extends JFrame{
	private static final long serialVersionUID = -2411774477093183174L;
	boolean Fullscreen;
	static GraphicContent GC = new GraphicContent();
	
	public GameWindow(boolean FullScreen){
		Fullscreen = FullScreen;
		setSize(1000, 600);
		setTitle("RPSG");
		
		setDefaultLookAndFeelDecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Adds button presses to Var KeyBoard
		addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent k){
				//System.out.println(k.getKeyCode());
				Var.KeyBoard[k.getKeyCode()] = true;
			}

			public void keyReleased(KeyEvent k){
				Var.KeyBoard[k.getKeyCode()] = false;
			}

			public void keyTyped(KeyEvent k){
			}
			
		});
		
		if(FullScreen){
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setUndecorated(true);
		}
		
		
		setIgnoreRepaint(true);
		//Add the panel which paints all graphics
		add(GC);
		setVisible(true);
	}
	
	
	//Toggles fullscreen
	public static GameWindow ToggleFullscreen(GameWindow G){
		G.setVisible(false);
		G = new GameWindow(true);
		return G;
	}
	
}
