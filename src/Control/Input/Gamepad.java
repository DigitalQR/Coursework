package Control.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import Tools.String.Parametres;
import Control.Settings;
import Control.Visual.Menu.Assets.Core.Input;
import Debug.ErrorPopup;
import Entities.Player;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad{
	
	public static Gamepad[] Pad = new Gamepad[0];
	public static Gamepad keyboard;
	private static int GPID_TRACK = 0;
	
	private boolean doesProfileExist = false;
	
	public static Gamepad[] getGamepads(){
		return Pad;
	}
	
	public static void pollPads(){
		for(Gamepad p: Pad){
			p.poll();
		}
	}
	
	public static void setup(){
		ControllerEnvironment CE = ControllerEnvironment.getDefaultEnvironment();
		Controller[] Controllers = CE.getControllers();
		
		for(Controller pad:Controllers){
			if(pad.getType() == Controller.Type.STICK || pad.getType() == Controller.Type.GAMEPAD ){
				add(new Gamepad(pad));
			}
			if(pad.getType() == Controller.Type.KEYBOARD && keyboard == null){
				
				
				keyboard = new Gamepad(pad);
				keyboard.GPID = 100;
				if(!keyboard.doesProfileExist){
					keyboard.Key[0] = new Button(59,1.0f);
					keyboard.Key[1] = new Button(43,1.0f);
					keyboard.Key[2] = new Button(56,1.0f);
					keyboard.Key[3] = new Button(43,1.0f);
					keyboard.Key[4] = new Button(59,1.0f);
					keyboard.Key[5] = new Button(55,1.0f);
					keyboard.Key[6] = new Button(37,1.0f);
					keyboard.Key[7] = new Button(40,1.0f);
					keyboard.Key[8] = new Button(42,1.0f);
					keyboard.Key[9] = new Button(44,1.0f);
					keyboard.Key[10] = new Button(13,1.0f);
					keyboard.Key[11] = new Button(2,1.0f);
					keyboard.Key[12] = new Button(0,1.0f);
					keyboard.doesProfileExist = true;
				}
				add(keyboard);
			}
		}
	}
	
	public static Gamepad getGamepad(int GPID){
		for(Gamepad pad:Pad){
			if(pad.getGPID() == GPID){
				return pad;
			}
		}
		return null;
	}
	
	private static void add(Gamepad g){
		Gamepad[] Temp = Pad.clone();
		Pad = new Gamepad[Temp.length+1];
		for(int i = 0; i<Temp.length; i++){
			Pad[i] = Temp[i];
		}
		
		Pad[Temp.length] = g;
	}
	
	public static int BUTTON_LENGTH = 0;
	public static final int 
	BUTTON_JUMP = BUTTON_LENGTH++,
	BUTTON_DUCK = BUTTON_LENGTH++,
	BUTTON_BLOCK = BUTTON_LENGTH++,
	BUTTON_GRAB = BUTTON_LENGTH++,
	
	BUTTON_UP = BUTTON_LENGTH++,
	BUTTON_DOWN = BUTTON_LENGTH++,
	BUTTON_LEFT = BUTTON_LENGTH++,
	BUTTON_RIGHT = BUTTON_LENGTH++,
	
	BUTTON_PRIMARY = BUTTON_LENGTH++,
	BUTTON_SECONDARY = BUTTON_LENGTH++,

	BUTTON_PAUSE = BUTTON_LENGTH++,
	BUTTON_MENU_FORWARD = BUTTON_LENGTH++,
	BUTTON_MENU_BACK = BUTTON_LENGTH++;
		
	private static String[] KeyName = 
		{"Jump", "Duck", "Block", "Grab", "Up", "Down", "Left", "Right", "Primary\nattack", "Secondary\nattack", "Pause", "Menu\nForward", "Menu\nBack"};
	
	private Button[] Key = new Button[KeyName.length];
	private float[] Raw;
	private Controller Control;
	private int GPID;
	
	public Gamepad(Controller c){
		Control = c;
		Raw = new float[c.getComponents().length];
		GPID = GPID_TRACK++;
		loadProfile();
	}
	
	public int getGPID(){
		return GPID;
	}
	
	public String getName(){
		return Control.getName().trim().replace("/", "");
	}

	List<String> BoundButtons;
	private int bindingTrack;
	
	public void startBinding(){
		BoundButtons = new ArrayList<String>();
		bindingTrack = 0;
	}
	
	public String getCurrentBinding(){
		poll();
		if(Input.hasTimePassed()){
			for(int i = 0; i <Raw.length; i++){
				if(Raw[i] != 0){
					try{
						BoundButtons.add(i + " " + Raw[i]);
						Key[bindingTrack] = new Button(i, Raw[i]);
						bindingTrack++;
						Input.recieved();
					}catch(IndexOutOfBoundsException e){
						System.err.println("Minor @ Gamepad/getCurrentBinding(): " + e.getMessage());
					}
				}
			}
		}
		if(bindingTrack >= KeyName.length){
			if(!doesProfileExist){
				doesProfileExist = true;
				saveProfile();
			}
			return "DONE";
		}else{
			return KeyName[bindingTrack];
		}
		
	}
	
	public boolean getProfileStatus(){
		return doesProfileExist;
	}
	
	public void poll(){
		if(Control.poll()){
			Component[] Keys = Control.getComponents();
	
			Control.poll();
			for(int i = 0; i<Keys.length; i++){
				if(Keys[i].isAnalog()){
					Raw[i] = Math.round(Keys[i].getPollData());
				}else{
					Raw[i] = (Keys[i].getPollData());
				}
			}
		}else{
			ErrorPopup.createMessage("Controller: [" + GPID + "]" + getName() + " has been disconnect.", true);
		}
	}
	
	public boolean isButtonPressed(int ID){
		try{
			if(Key[ID].getState() == Raw[Key[ID].getID()]){
				return true;
			}
		}catch(NullPointerException e){}
		return false;
	}
	
	
	public void saveProfile(){
		String content = "";
		
		for(int i = 0; i<Key.length; i++){
			content+=Key[i].getID() + ":" + Key[i].getState() + ";";
		}
		try{
			new File("Res/GPProfile").mkdir();
			Formatter scribe = new Formatter("Res/GPProfile/" + getName() + ".GPP");
			scribe.format("%s", content);
			scribe.close();
		}catch(FileNotFoundException e){
			ErrorPopup.createMessage(e, true);
		}
	}
	
	public void loadProfile(){
		try{
			Scanner scan = new Scanner(new File("Res/GPProfile/" + getName() + ".GPP"));
			String rawContent = "";
			
			while(scan.hasNext()){
				rawContent+=scan.next();
			}
			scan.close();
			
			String[] temp = Parametres.getParameters(rawContent, ';');
			for(int i = 0; i<temp.length; i++){
				String[] t = Parametres.getParameters(temp[i], ':');
				
				int ID = Integer.valueOf(t[0]);
				float Value = Float.valueOf(t[1]);
				Key[i] = new Button(ID, Value);
			}
			doesProfileExist = true;
			
		}catch(FileNotFoundException e){
			System.out.println(getName() + " GPP doesn't exist..");
		}catch(ArrayIndexOutOfBoundsException e){
			deleteProfileStatus();
		}
	}

	public void deleteProfileStatus(){
		this.doesProfileExist = false;
		new File("Res/GPProfile/" + getName() + ".GPP").delete();
		
		for(Player p: Settings.User){
			if(p.getControlScheme().getGPID() == this.GPID){
				p.getControlScheme().setDefaultControls();
			}
		}
	}
	
	
	
}


