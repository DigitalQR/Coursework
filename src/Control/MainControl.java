package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Collision.Hitbox;
import Control.Audio.Sound;
import Control.Input.Gamepad;
import Control.Visual.DisplayControl;
import Debug.ErrorPopup;
import Entities.Player;
import Entities.Assets.Damage;
import Entities.Assets.Shield;


public class MainControl{
	public static boolean CloseRequest = false;
	public static boolean Paused = true;
	public final static int UPS = (1000/30)*1000000;
	
	
	public static void main(String[] args){
		Settings.setup();
		Gamepad.setup();
		Sound.setup();
		setup();
		new Thread(new DisplayControl()).start();
		
		while(!CloseRequest){
			long StartTime = System.nanoTime();
			Gamepad.pollPads();
			
			if(!Paused){
				for(Hitbox hb: Settings.hb){
					hb.update();
				}
				Damage.updateDamage();
				Shield.updateShields();
				
				for(Player p: Settings.User){
					p.update();
				}
				Camera.process();
				
			}

			while(Toolkit.Differencef(StartTime, System.nanoTime()) < UPS){
				try{
					TimeUnit.NANOSECONDS.sleep(100000);
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
		Settings.randomHitboxGen();
		
		Settings.User.add(new Player(0,0));
		Settings.User.add(new Player(0,0));
		
		Gamepad.keyboard.assignToPlayer(0);
		
	}

}

