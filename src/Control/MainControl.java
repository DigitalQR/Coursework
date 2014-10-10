package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Collision.StaticHitbox2f;
import Control.Input.Gamepad;
import Control.Visual.DisplayControl;
import Debug.ErrorPopup;
import Entities.Player;


public class MainControl{
	public static boolean CloseRequest = false;
	public static boolean Paused = true;
	public final static int UPS = (1000/30)*1000000;
	
	
	public static void main(String[] args){
		Gamepad.setup();
		setup();
		new Thread(new DisplayControl()).start();
		
		while(!CloseRequest){
			long StartTime = System.nanoTime();
			if(!Paused){
				for(int i = 0; i<Settings.User.length; i++){
					Settings.User[i].update();
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
		System.out.println("Closing down main thread..");
	}
	
	private static void setup(){
		Settings.hb = StaticHitbox2f.RandomGeneration(1000, -1000, -1000, 1000, 1000, 100);
		for(int i = 0; i<Settings.User.length; i++){
			Settings.User[i] = new Player(0,0);
		}
		
	}

}

