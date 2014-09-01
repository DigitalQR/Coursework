package Control;

import java.util.concurrent.TimeUnit;

import Tools.Maths.Toolkit;
import Collision.StaticHitbox2f;
import Control.input.Gamepad;
import Entity.Player;


public class MainControl{
	public static boolean CloseRequest = false;
	public static boolean Paused = true;
	
	public static void main(String[] args){
		//Gamepad.setup();
		setup();
		new Thread(new DisplayControl()).start();

		while(!CloseRequest){
			long StartTime = System.nanoTime();
			if(!Paused){
				for(int i = 0; i<Settings.User.length; i++){
					Settings.User[i].process();
				}
				
			}

			while(Toolkit.Differencef(StartTime, System.nanoTime())< (1000/60)*1000000){
				try{
					TimeUnit.NANOSECONDS.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	private static void setup(){
		Settings.hb = StaticHitbox2f.RandomGeneration(1000, -1000, -1000, 1000, 1000, 100);
		for(int i = 0; i<Settings.User.length; i++){
			System.out.println(i + " start.");
			Settings.User[i] = new Player(0,0);
			System.out.println(i + " end.\n");
			
		}
		Settings.User[0].setControlScheme(1);
	}

}
