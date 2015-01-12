package Control.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Audio.Sound;
import Control.Visual.Stage.StartStage;
import Control.Visual.Stage.Core.Stage;
import Debug.ErrorPopup;
import Entities.Entity;
import Entities.Player;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import Level.World;

public class Client implements Runnable{
	
	private PrintWriter output;
	private BufferedReader input;
	private Socket socket;
	private Thread thread;
	
	public Client(String host, int port){
		try{
			socket = new Socket(host, port);
			socket.setTcpNoDelay(true);
			output = new PrintWriter(socket.getOutputStream(),true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to: " + socket.getInetAddress() + " " + socket.getPort());
			thread = new Thread(this);
			thread.start();
		}catch(IOException e){
			System.err.println("Error connecting to host:\n" + e.getLocalizedMessage());
			socket = null;
		}
	}

	private long time;
	private int ping = 0;
	private String name = "";
	
	public int getPing(){
		return ping;
	}
	
	public void serverUpdate(){
		String command = "";
		
		if(thread != null){
			command += processMovement();
			
			if(name.equals("")){
				command += "si" + Settings.USERNAME + ";";
			}
	
			if( ( System.nanoTime()/1000000 - time)/1000 >= 1){
				time = (long) (System.nanoTime()/1000000);
				command += "ping," + Stage.getCurrentStageID() + "," + ping + ";";
			}
			
			sendMessage(command);	
		}
	}
	
	public void run(){
		while(!isDestroyed()){
			try{
				String input = getRecievedMessage();
				decodeCommands(input);
			}catch(SocketException e){
				e.printStackTrace();
			}
		}
		System.out.println("Client message decoding thread destroyed.");
	
	}
	
	private boolean[] keys = new boolean[12];
	
	private String processMovement(){
		String command = "";
		Player player = Settings.User.get(0);
		
		final int[] buttons = {
				player.getControlScheme().KEY_JUMP,
				player.getControlScheme().KEY_DUCK,
				player.getControlScheme().KEY_LEFT,
				player.getControlScheme().KEY_RIGHT,
				player.getControlScheme().KEY_UP,
				player.getControlScheme().KEY_DOWN,
				player.getControlScheme().KEY_PRIMARY,
				player.getControlScheme().KEY_SECONDARY,
				player.getControlScheme().KEY_BLOCK,
				player.getControlScheme().KEY_SELECT,
				player.getControlScheme().KEY_START
		};
		
		final char[] prefix = {
				 'j','d','l','r','u','D','p','s','b','S','x'
				 
		};
		
		for(int i = 0; i <buttons.length; i++){
			if(player.isKeyPressed(buttons[i]) != keys[i]){
				keys[i] = player.isKeyPressed(buttons[i]);
				command += "c"+ prefix[i];
				
				if(keys[i]){
					command += "t;";
				}else{
					command += "f;";
				}
			}
		}
		return command;
	}
	
	public void decodeCommands(String input){
		if(input != null || input != ""){
			String[] commands = input.split(";");
			
			for(String s: commands){
				if(s.startsWith("PS")){
					StartStage st = (StartStage) Stage.getStage("start");
					st.decode(s.substring(2));
				}
				
				if(s.startsWith("ping")){
					ping = Math.round(System.nanoTime()/1000000-time);
				}
				
				if(s.startsWith("Sst")){
					int ID = (int)Float.parseFloat(s.substring(3));
					Stage st = Stage.getStage(ID);
					System.out.print(s + " : ");
					System.out.println(ID + "," + st.getID());
					Stage.setStage(st);
				}
				
				if(s.startsWith("ps")){
					int player = (int)Float.parseFloat(s.substring(2));
					Settings.issueCommand("set player_count " + player);
				}
				
				if(s.startsWith("pl")){
					
					String no = "";
					int playerID = -1;
					char action = ' ';
					for(char c: s.substring(2).toCharArray() ){

						if('0' <= c && c <= '9'){
							no+=c;
						}else{
							playerID = Integer.parseInt(no);
							action = c;
							break;
						}
					}
					String subCommand = s.substring( ("pl" + playerID + "" + action).length());

					if(playerID < Settings.User.size()-1){
						switch(action){
							//Location
							case 'l':
								
								String[] para = subCommand.split(",");
								float[] val = {
										Float.parseFloat(para[0]),
										Float.parseFloat(para[1])
								};
								
								Settings.User.get(playerID).setLocation(new Vector3f(val[0], val[1], 0));
								break;
							
							//Information
							case 'i':
								//Colour
								if(subCommand.startsWith("c")){
									String[] col = subCommand.substring(1).split(",");
									float r = Float.parseFloat(col[0]);
									float g = Float.parseFloat(col[1]);
									float b = Float.parseFloat(col[2]);
									
									Settings.User.get(playerID).setRGBA(new float[]{r,g,b,1});
								}
								
								//Health
								if(subCommand.startsWith("h")){
									String[] par = subCommand.substring(1).split(",");
									int killCount = (int)Float.parseFloat(par[0]);
									float factor = Float.parseFloat(par[1]);
	
									Settings.User.get(playerID).killCount = killCount;
									Settings.User.get(playerID).health.factor = factor;
								}
								
								break;
						}
					}
				}
				
				if(s.startsWith("wd")){
					//World
					if(s.charAt(2) == 'h'){
						World w = World.decode(s.substring(3));
						Settings.setWorld(w);
					}
					
					//Damage
					if(s.charAt(2) == 'd'){
						String[] para = s.substring(3).split(",");
						try{
							Vector2f location = new Vector2f(Float.parseFloat(para[0]), Float.parseFloat(para[1]));
							Vector2f size = new Vector2f(Float.parseFloat(para[2]), Float.parseFloat(para[3]));
							int life = (int)Float.parseFloat(para[4]);
							float damageValue = Float.parseFloat(para[5]);
							Entity parent = Settings.User.get((int)Float.parseFloat(para[6]));
							boolean stuck = false;
							if(para[7].equals("true")) stuck = true;
							Vector2f velocity = new Vector2f(Float.parseFloat(para[8]), Float.parseFloat(para[9]));
							Vector2f damageVelocity = new Vector2f(Float.parseFloat(para[10]), Float.parseFloat(para[11]));
							
							Damage d = new Damage(location, size, life, damageValue, parent, stuck, Damage.CUBE, new Sound("Effects/Attack"));
							d.setVelocity(velocity);
							d.setDamageVelocity(damageVelocity);
							Damage.add(d);
							
						}catch(NumberFormatException e){
							ErrorPopup.createMessage(e, false);
						}
						
					}
					
					//Shields
					if(s.charAt(2) == 's'){
						String[] para = s.substring(3).split(",");
						try{
							int life = (int)Float.parseFloat(para[0]);
							float r = Float.parseFloat(para[1]);
							float g = Float.parseFloat(para[2]);
							float b = Float.parseFloat(para[3]);
							float a = Float.parseFloat(para[4]);
							int parent = (int)Float.parseFloat(para[5]);
							
							Shield.add(new Shield(life, new float[]{r,g,b,a}, Settings.User.get(parent)));
							
						}catch(NumberFormatException e){
							ErrorPopup.createMessage(e, false);
						}
					}
				}
				
			}
		}
		
	}
	
	private void sendMessage(String message){
		output.println(message);
		output.flush();
	}
	
	public String getRecievedMessage() throws SocketException{
		try{
			return input.readLine();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
			return "";
		}
	}
	
	public void destroy(){
		try {
			socket.close();
			socket = null;
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		output.flush();
		output.close();
		
	}
	
	public boolean isDestroyed(){
		return socket == null;
	}
	
}
