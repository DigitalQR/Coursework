package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Control.Audio.Sound;
import Control.Input.Gamepad;
import Control.Visual.DisplayControl;
import Control.Visual.Stage.Core.Stage;
import Debug.ErrorPopup;
import Entities.Player;


public class MainControl{
	public static boolean CloseRequest = false;
	public static boolean Paused = false;
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
			
			try{
				Camera.incrementRandomLighting();
			}catch(Exception e){}
			
			Stage.updateCurrentStage();

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

