package Control.Visual.Menu.Assets.Core;

import org.lwjgl.input.Keyboard;

import Control.Settings;
import Entities.Player;

public class Input {
	
	private static long lastInput = 0;
	private static int inputDelay = 140;
	
	public static boolean hasTimePassed(){
		long current = System.nanoTime()/1000000;
		if((current-lastInput) > inputDelay){
			return true;
		}
		return false;
	}

	public static void recieved(){
		lastInput = System.nanoTime()/1000000;	
	}
	
	public static final int 
	KEY_UP = 0,
	KEY_DOWN = 1,
	KEY_LEFT = 2,
	KEY_RIGHT = 3,
	KEY_FORWARD = 4,
	KEY_BACK = 5,
	KEY_PAUSE = 6;
	
	public static boolean isKeyPressed(int key){
		Player p = Settings.User.get(0);
		if(p.getControlScheme().getGPID() != -1){
			if(hasTimePassed()){
				switch(key){
				case KEY_UP:
					return p.isKeyPressed(p.getControlScheme().KEY_UP);
				case KEY_DOWN:
					return p.isKeyPressed(p.getControlScheme().KEY_DOWN);
				case KEY_LEFT:
					return p.isKeyPressed(p.getControlScheme().KEY_LEFT);
				case KEY_RIGHT:
					return p.isKeyPressed(p.getControlScheme().KEY_RIGHT);
				case KEY_FORWARD:
					return p.isKeyPressed(p.getControlScheme().KEY_SELECT);
				case KEY_BACK:
					return p.isKeyPressed(p.getControlScheme().KEY_BACK);
				case KEY_PAUSE:
					return p.isKeyPressed(p.getControlScheme().KEY_START);
				default:
					return false;
				}
			}else{
				return false;
			}
			
		}else{
			if(hasTimePassed()){
				switch(key){
				case KEY_UP:
					return Keyboard.isKeyDown(Keyboard.KEY_W);
				case KEY_DOWN:
					return Keyboard.isKeyDown(Keyboard.KEY_S);
				case KEY_LEFT:
					return Keyboard.isKeyDown(Keyboard.KEY_A);
				case KEY_RIGHT:
					return Keyboard.isKeyDown(Keyboard.KEY_D);
				case KEY_FORWARD:
					return Keyboard.isKeyDown(Keyboard.KEY_F);
				case KEY_BACK:
					return Keyboard.isKeyDown(Keyboard.KEY_H);
				case KEY_PAUSE:
					return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
				default:
					return false;
				}
			}else{
				return false;
			}
		}
	}
	
}
