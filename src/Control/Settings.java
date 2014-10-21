package Control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import Collision.SquareHitbox;
import Entities.Player;

public class Settings implements Runnable{
	//Holds global key values
	public static final String Version = "0.16.2";
	public static ArrayList<Player> User = new ArrayList<Player>();
	public static List<SquareHitbox> hb;
	
	public static List<String> toggleNames = new ArrayList<String>();
	public static HashMap<String,Boolean> toggles = new HashMap<String,Boolean>();
	
	public static void setup(){
		//d_? = draw ?
		//s_? = setting ?

		toggleNames.add("d_hitbox");
		toggleNames.add("d_wireframe");
		toggleNames.add("d_damage");
		toggleNames.add("s_noclip");
		
		for(String s: toggleNames){
			toggles.put(s, false);
		}
		toggles.put("d_damage", true);
		toggles.put("d_hitbox", true);
		
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
					default:
						System.out.println("Invalid list name.");
						break;
					}
				}else{
					System.out.println("Usage: list <list>");
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
}
