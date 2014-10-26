package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;
import Collision.SquareHitbox;
import Control.Visual.DisplayControl;
import Entities.Player;

public class Settings implements Runnable{
	//Holds global key values
	public static final String Version = "1.0.9";
	public static ArrayList<Player> User = new ArrayList<Player>();
	public static List<SquareHitbox> hb;
	public static Cubef boundary = new Cubef(new Vector3f(-10,-10,0), new Vector3f(10,10,1f));

	public static List<String> toggleNames = new ArrayList<String>();
	public static HashMap<String,Boolean> toggles = new HashMap<String,Boolean>();

	public static List<String> floatNames = new ArrayList<String>();
	public static HashMap<String,Float> floats = new HashMap<String,Float>();
	

	public static List<Vector3f> playerColourProfiles = new ArrayList<Vector3f>();
	
	public static void setup(){
		playerColourProfiles.add(new Vector3f(1,0,0));
		playerColourProfiles.add(new Vector3f(0,1,0));
		playerColourProfiles.add(new Vector3f(0,0,1));
		playerColourProfiles.add(new Vector3f(1f,0.9f,0.2f));
		
		playerColourProfiles.add(new Vector3f(0.2f,1f,1));
		playerColourProfiles.add(new Vector3f(1f,0f,0.5f));
		playerColourProfiles.add(new Vector3f(0f,0.4f,0.2f));
		playerColourProfiles.add(new Vector3f(0.8f,0.6f,1f));
		
		//d_? = draw ?
		//s_? = setting ?

		//Toggles
		toggleNames.add("d_hitbox");
		toggleNames.add("d_wireframe");
		toggleNames.add("s_noclip");
		
		for(String s: toggleNames){
			toggles.put(s, false);
		}
		
		//Floats
		floatNames.add("s_light_deviation");
		
		for(String s: floatNames){
			floats.put(s, 0f);
		}
		
		floats.put("s_light_deviation", 0.25f);
		
		new Thread(new Settings()).start();
	}
	
	
	public void run(){
		Scanner console = new Scanner(System.in);
		
		while(!MainControl.CloseRequest){
			String input = console.nextLine();
			String[] raw = input.split(" ");
			
			switch(raw[0]){
			//Lists the commands
			case "help":
				System.out.println("toggle <variable>");
				System.out.println("list <list>");
				System.out.println("set <variable> <value>");
				System.out.println("stop");
				System.out.println("");
				break;
			
			//List toggle-able variables
			case "toggle":
					if(raw.length == 2){
						if(toggles.containsKey(raw[1])){
							toggles.put(raw[1], !toggles.get(raw[1]));
						}else{
							System.out.println("Variable " + raw[1] + " does not exist.");
						}
					}else{
						System.out.println("Usage: toggle <variable>");
					}
				break;
			
			//List variable which can be accessed
			case "list":
				if(raw.length == 2){
					switch(raw[1]){
					case "toggles":
						for(String s: toggleNames){
							System.out.println(" " + s);
						}
						break;
					case "floats":
						for(String s: floatNames){
							System.out.println(" " + s);
						}
						break;
					default:
						System.out.println("Invalid list name.");
						break;
					}
				}else{
					System.out.println("Usage: list <list>");
				}
				break;
				
			//Sets a certain value
			case	"set":
				if(raw.length == 3){
					if(floats.containsKey(raw[1])){
						try{
							float val = Float.valueOf(raw[2]);
							floats.put(raw[1], val);
						}catch(NumberFormatException e){
							System.out.println(raw[2] + " is not a float.");
						}
					}else{
						System.out.println("Variable " + raw[1] + " does not exist.");
					}
				}else{
					System.out.println("Usage: set <variable> <value>");
				}
				break;
				
			case "reset_stage":
				if(raw.length == 1){
					randomHitboxGen();
					DisplayControl.stage[DisplayControl.STAGE_OVERWORLD].prepare();
				}else{
					System.out.println("Usage: reset_stage");
				}
				break;
				
			//Force stops the JVM
			case "stop":
				System.exit(0);
				break;
				
			default:
				System.out.println("Command " + raw[0] + " is unknown.");
				System.out.println("Type help for a list of commands.");
				break;
			}
		}
		
		console.close();
	}
	
	public static void randomHitboxGen(){
		int scale = 8;
		Settings.hb = SquareHitbox.RandomGeneration(10, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
	}
}
