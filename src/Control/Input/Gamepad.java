package Control.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;

import Tools.String.Parametres;
import Control.Settings;
import Control.Visual.Stage.MenuStage;
import Debug.ErrorPopup;
import Entities.Player;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad{
	
	public static Gamepad[] Pad = new Gamepad[0];
	private static int GPID_TRACK = 0;
	
	private boolean doesProfileExist = false;
	
	public static Gamepad[] getGamepads(){
		return Pad;
	}
	
	public static void setup(){
		ControllerEnvironment CE = ControllerEnvironment.getDefaultEnvironment();
		Controller[] Controllers = CE.getControllers();
		
		for(Controller pad:Controllers){
			if((pad.getType() == Controller.Type.STICK || pad.getType() == Controller.Type.GAMEPAD) && pad.getComponents().length >= 18){
				add(new Gamepad(pad));
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
	private static int T = 0;
	public static final int 
		A = T++, B = T++, X = T++ ,Y = T++, 
		DPAD_UP = T++, DPAD_DOWN = T++, DPAD_LEFT = T++, DPAD_RIGHT = T++, 
		LEFT_STICK_UP = T++, LEFT_STICK_DOWN = T++, LEFT_STICK_LEFT = T++, LEFT_STICK_RIGHT = T++, 
		RIGHT_STICK_UP = T++, RIGHT_STICK_DOWN = T++, RIGHT_STICK_LEFT = T++, RIGHT_STICK_RIGHT = T++, 
		START = T++, SELECT = T++,
		LEFT_TRIGGER = T++, RIGHT_TRIGGER = T++, LEFT_BUMPER = T++, RIGHT_BUMPER = T++;
		
	private static String[] KeyName = 
		{"A", "B", "X", "Y",
		 "DPAD> UP", "DPAD> DOWN", "DPAD> LEFT", "DPAD> RIGHT",
		 "LEFT STICK> UP", "LEFT STICK> DOWN", "LEFT STICK> LEFT", "LEFT STICK> RIGHT", 
		 "RIGHT STICK> UP", "RIGHT STICK> DOWN", "RIGHT STICK> LEFT", "RIGHT STICK> RIGHT", 
		 "START", "SELECT",
		 "LEFT TRIGGRER", "RIGHT TRIGGER", "LEFT BUMPER", "RIGHT BUMPER"};
	
	private Button[] Key = new Button[KeyName.length];
	private float[] Raw;
	private Controller Control;
	private int GPID;
	public int assignedPlayer = -1;
	
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
		return Control.getName().trim();
	}

	List<String> BoundButtons;
	private int bindingTrack;
	
	public void startBinding(){
		BoundButtons = new ArrayList<String>();
		bindingTrack = 0;
	}
	
	public String getCurrentBinding(){
		poll();
		if(MenuStage.timePassed()){
			for(int i = 0; i <Raw.length; i++){
				if(Raw[i] != 0 && !BoundButtons.contains(i + " " + Raw[i])){
					BoundButtons.add(i + " " + Raw[i]);
					Key[bindingTrack] = new Button(i, Raw[i]);
					bindingTrack++;
					MenuStage.input();
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
		poll();
		if(Key[ID].getState() == Raw[Key[ID].getID()]){
			return true;
		}
		return false;
	}
	
	
	public void saveProfile(){
		String content = "";
		
		for(int i = 0; i<Key.length; i++){
			content+=Key[i].getID() + ":" + Key[i].getState() + ";";
		}
		try{
			new File("Res/GPProfile").mkdirs();
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
		}
	}
	
	public void assignToPlayer(int i){
		for(Player p: Settings.User){
			if(p.getControlScheme().GPID == this.GPID){
				p.setControlScheme(Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_G, Keyboard.KEY_H, Keyboard.KEY_ESCAPE, Keyboard.KEY_L);
			}
		}
		assignedPlayer = i;
		Settings.User.get(i).setControlScheme(this.GPID);
	}

	public void deleteProfileStatus(){
		this.doesProfileExist = false;
		this.assignedPlayer = -1;
		new File("Res/GPProfile/" + getName() + ".GPP").delete();
		
		for(Player p: Settings.User){
			if(p.getControlScheme().GPID == this.GPID){
				p.setControlScheme(Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_G, Keyboard.KEY_H, Keyboard.KEY_ESCAPE, Keyboard.KEY_L);
			}
		}
	}
	
	
	
}


