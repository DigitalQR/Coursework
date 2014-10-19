package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Collision.SquareHitbox;
import Control.Input.Gamepad;
import Control.Visual.DisplayControl;
import Debug.ErrorPopup;
import Entities.Player;


public class MainControl{
	public static boolean CloseRequest = false;
	public static boolean Paused = true;
	public final static int UPS = (1000/30)*1000000;
	
	
	public static void main(String[] args){
		Settings.setup();
		Gamepad.setup();
		setup();
		new Thread(new DisplayControl()).start();
		
		while(!CloseRequest){
			long StartTime = System.nanoTime();
			if(!Paused){
				for(Player p: Settings.User){
					p.update();
				}
			}
			
			while(Toolkit.Differencef(StartTime, System.nanoTime()) < UPS){
				try{
					TimeUnit.NANOSECONDS.sleep(1);
				}catch(InterruptedException e){
					ErrorPopup.createMessage(e, true);
				}
			}
			
		}
		
		while(DisplayControl.exists){}
		System.out.println("Closing down main thread.");
		System.exit(0);
	}
	
	private static void setup(){
		Settings.hb = SquareHitbox.RandomGeneration(1000, -1000, -1000, 1000, 1000, 100);

		Settings.User.add(new Player(0,0));
		Settings.User.add(new Player(0,0));
		
	}

}

