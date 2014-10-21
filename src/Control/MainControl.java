package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Collision.SquareHitbox;
import Control.Input.Gamepad;
import Control.Visual.DisplayControl;
import Debug.ErrorPopup;
import Entities.Player;
import Entities.Assets.Damage;


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
			Damage.updateDamage();
			
			while(Toolkit.Differencef(StartTime, System.nanoTime()) < UPS){
				try{
					TimeUnit.NANOSECONDS.sleep(1);
				}catch(InterruptedException e){
					ErrorPopup.createMessage(e, true);
				}
			}
			
		}
		
		while(DisplayControl.exists){
			try{
				TimeUnit.NANOSECONDS.sleep(1000);
			}catch(InterruptedException e){
				ErrorPopup.createMessage(e, true);
			}
		}
		System.out.println("Closing down main thread.");
		System.exit(0);
	}
	
	private static void setup(){
		int scale = 8;
		Settings.hb = SquareHitbox.RandomGeneration(10, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);

		Settings.User.add(new Player(0,0));
		Settings.User.add(new Player(0,0));
		
	}

}

