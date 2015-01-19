package Control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
	
	
	public static void launch(){
		Settings.setup();
		Gamepad.setup();
		Sound.setup();
		
		try{
			Settings.User.add(new Player(0,0));
			
			File GPIDfile = new File("Res/p1.pref");
			if(GPIDfile.exists()){
				Scanner prefScan;
				try {
					prefScan = new Scanner(GPIDfile);
					String data = "";
					while(prefScan.hasNext()) data+= prefScan.next();
					prefScan.close();
					
					for(Gamepad gp: Gamepad.getGamepads()){
						if(gp.getName().equals(data) && gp.getProfileStatus()){
							Settings.User.get(0).setControlScheme(gp.getGPID());
							break;
						}					
					}
				}catch(FileNotFoundException e){
					
				}
			}else{
				Settings.User.get(0).setControlScheme(Gamepad.keyboard.getGPID());
			}
			
			
		}catch(NullPointerException e){
			
		}
		
		new Thread(new DisplayControl()).start();
		
		long overTime = 0;
		
		while(!CloseRequest){
			long FinishTime = System.nanoTime()+overTime+UPS;
			overTime = 0;
			Gamepad.pollPads();
			
			Settings.update();
			
			Camera.incrementRandomLighting();
			
			Stage.updateCurrentStage();
			while(true){
				overTime = FinishTime-System.nanoTime();
				if(overTime <= 0){
					break;
				}
				try{
					TimeUnit.NANOSECONDS.sleep(1000);
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

}
