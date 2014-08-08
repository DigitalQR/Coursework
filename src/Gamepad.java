import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class Gamepad{
	
	static Gamepad[] Pad = new Gamepad[0];
	
	
	float[] Input = new float[0];
	String Name;
	Controller Control;
	
	public Gamepad(Controller c){
		Name = c.toString();
		Control = c;
		Input = new float[c.getComponents().length];
	}	

	public void process(){
		Component[] Keys = Control.getComponents();

		Control.poll();
		for(int i = 0; i<Keys.length; i++){
			Input[i] = Math.round(Keys[i].getPollData());
			
			//if(Input[i] != 0){
				//System.out.println(i + ": "+ Input[i]);
			//}
		}
	}
	
	
	public static void ProcessInput(){
		for(int i = 0; i<Pad.length; i++){
			Pad[i].process();
		}
	}
	
	//Lookups a controller
	public static int lookup(String s){
		for(int i = 0; i<Pad.length; i++){
			if(Pad[i].Control.toString().trim().toUpperCase().equals(s.trim().toUpperCase())){
				return i;
			}
		}
		return -1;
	}
	
	
	//Adds a gamepad to the pad array
	public static void add(Gamepad g){
		Gamepad[] Temp = Pad;
		Pad = new Gamepad[Temp.length+1];
		
		for(int i = 0; i<Temp.length; i++){
			Pad[i] = Temp[i];
		}
		
		Pad[Temp.length] = g;
	}
	
	
	//Setups the listener for gamepads
	public static void setup(){
		ControllerEnvironment CE = ControllerEnvironment.getDefaultEnvironment();
		Controller[] Pads = CE.getControllers();
		
		for(int i = 0; i <Pads.length; i++){
			
			//Makes sure the input is correct
			if(Pads[i].getType() == Controller.Type.STICK || Pads[i].getType() == Controller.Type.GAMEPAD){
				add(new Gamepad(Pads[i]));
				DebugMenu.Log("[INFO]Conrtoller " + Pads[i].toString().trim() + " added.");
			}
			
		}
		
	}	
		
		
}
