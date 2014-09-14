package Control.Input;

import java.util.ArrayList;
import java.util.List;

import Control.Visual.Stage.MenuStage;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad{
	
	public static Gamepad[] Pad = new Gamepad[0];
	private static int GPID_TRACK = 0;
	
	public boolean doesProfileExist = false;
	
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
		

		//Pad[1].setBindings();
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
		 "DPAD_UP", "DPAD_DOWN", "DPAD_LEFT", "DPAD_RIGHT",
		 "LEFT_STICK_UP", "LEFT_STICK_DOWN", "LEFT_STICK_LEFT", "LEFT_STICK_RIGHT", 
		 "RIGHT_STICK_UP", "RIGHT_STICK_DOWN", "RIGHT_STICK_LEFT", "RIGHT_STICK_RIGHT", 
		 "START", "SELECT",
		 "LEFT_TRIGGRER", "RIGHT_TRIGGER", "LEFT_BUMPER", "RIGHT_BUMPER"};
	
	private Button[] Key = new Button[KeyName.length];
	private float[] Raw;
	private Controller Control;
	private int GPID;
	
	public Gamepad(Controller c){
		Control = c;
		Raw = new float[c.getComponents().length];
		GPID = GPID_TRACK++;
	}
	
	public int getGPID(){
		return GPID;
	}
	
	public String getName(){
		return Control.getName();
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
			return "DONE";
		}else{
			return KeyName[bindingTrack];
		}
		
	}
	
	
	public void poll(){
		Component[] Keys = Control.getComponents();

		Control.poll();
		for(int i = 0; i<Keys.length; i++){
			if(Keys[i].isAnalog()){
				Raw[i] = Math.round(Keys[i].getPollData());
			}else{
				Raw[i] = (Keys[i].getPollData());
			}
		}
	}
	
	public boolean isButtonPressed(int ID){
		poll();
		if(Key[ID].getState() == Raw[Key[ID].getID()]){
			return true;
		}
		return false;
	}
	
}
