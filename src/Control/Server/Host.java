package Control.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import Control.MainControl;
import Control.Settings;
import Control.Server.Assets.Player;
import Debug.ErrorPopup;
import Entities.Powerup;
import Entities.Assets.Damage;
import Entities.Assets.Shield;

public class Host{

	public static final int UPS = MainControl.UPS;
	private static final int MAX_CONNECTIONS = 7;
	
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	private ServerSocket server;
	private boolean active = false;
	private boolean accepting = false;
	public final static int DEFAULT_PORT = 9798;

	
	public Host(int port){
		try {
			server = new ServerSocket(port);
			accepting = true;
			System.out.println("Server Socket initialised: " + server.getInetAddress() + " " + port + ".");
			connections.add(new Connection(server));
		} catch (IOException e) {
		}
		
		active = true;
	}	
	
	public void destroy(){
		for(Connection c: connections){
			c.destroy();
		}
		try{
			server.close();
			server = null;
		}catch(IOException e){
			ErrorPopup.createMessage(e, true);
		}catch(NullPointerException e){}
		
		System.out.println("ServerSocket closed.");
	}
	
	public boolean isActive(){
		return server != null;
	}
	
	
	public void addCommand(String comm){
		@SuppressWarnings("unchecked")
		ArrayList<Connection> conn = (ArrayList<Connection>) connections.clone();
		
		for(Connection c: conn){
			c.additions += comm;
		}
	}
	
	public void forcePlayerUpdate(){
		for(Connection c: connections){
			for(Player p: c.players){
				p.resetFlag = true;
			}
		}
	}
	
	public void serverUpdate(){
		if(active){
				
			connectionHandler(server);
			
			@SuppressWarnings("unchecked")
			ArrayList<Connection> conn = (ArrayList<Connection>) connections.clone();
			for(Connection c: conn){
				
				if(c.isConnected()){
					ArrayList<Player> player = Player.getList();
					String command = c.additions;
					c.additions = "";
					
					//Amount of players
					if(player.size() != c.players.size()){
						command += "ps" + player.size() + ";";
					}
					
					//Player information
					for(int i = 0; i<player.size(); i++){
						try{
							Player p = player.get(i);
							
							//Location
							if(!p.locationEquals(c.players.get(i)) ){
								command += "pl" + i + "l" + p.getLocation().x + "," + p.getLocation().y + ";"; 
							}
							
							//Colour
							if(!p.locationEquals(c.players.get(i)) ){
								command += "pl" + i + "ic" + p.getColour().x + "," + p.getColour().y + "," + p.getColour().z + ";"; 
							}
							
							//Health
							if(!p.combatEquals(c.players.get(i)) ){
								command += "pl" + i + "ih" + p.getKillCount() + "," + p.getFactor() + "," + p.getStock() + ";";
							}
							
							//Powerup
							if(!p.powerupEquals(c.players.get(i))){
								String pow = "null";
								if(p.powerup != null){
									pow = "" + p.powerup.getID();
								}
								command += "pl" + i + "p" + pow + ";";
							}
						}catch(IndexOutOfBoundsException e){
							
						}
					}
					
					c.players = player;
					
					//World Information
					boolean worldEqual = false;
					if(c.world != null){
						worldEqual = c.world.equals(Settings.getWorld());
					}
					
					//World
					if(!worldEqual){
						c.world = Settings.getWorld();
						String message = c.world.encode();
						command += message;
					}
					
					//Damage information
					for(Damage d: Damage.getDamageInfo()){
						if(!c.damage.contains(d)){
							int parent = -1;
							for(int i = 0; i<Settings.User.size(); i++){
								if(d.getParent().equals( Settings.User.get(i) ) ){
									parent = i;
									break;
								}
							}
							
							command += "wdd" + 
									d.getLocation().x + "," + d.getLocation().y + "," + 
									d.getSize().x + "," + d.getSize().y + "," + 
									d.getLife() + "," + 
									d.getDamageValue() + "," +
									parent + "," + 
									d.isStuckToParent() + "," + 
									d.getVelocity().x + "," + d.getVelocity().y + "," + 
									d.getDamageVelocity().x + "," + d.getDamageVelocity().y + ";";
						}
					}
					c.damage = Damage.getDamageInfo();

					//Sheild information
					for(Shield s: Shield.getShieldInfo()){
						if(!c.shields.contains(s)){
							int parent = -1;
							for(int i = 0; i<Settings.User.size(); i++){
								if(s.getParent().equals( Settings.User.get(i) ) ){
									parent = i;
									break;
								}
							}
							command += "wds" + s.getLife() + "," + s.getRGBA()[0] + "," + s.getRGBA()[1] + "," + s.getRGBA()[2] + "," + s.getRGBA()[3] + "," + parent + ";";
						}
					}
					c.shields = Shield.getShieldInfo();
					
					//Powerup
					for(Powerup p: Powerup.getPowerUps()){
						if(!c.powerups.contains(p)){
							command += "wdp" +p.encode() + ";";
						}
					}
					c.powerups = Powerup.getPowerUps();
					
					String message = c.getRecievedMessage();
					if(message != ""){
						for(String mes: message.split(";")){
							//Ping calc
							if(mes.startsWith("ping")){
								String[] subMes = mes.split(",");
								c.setStage((int)Float.parseFloat(subMes[1]));
								c.ping = (int)Float.parseFloat(subMes[2]);
								
								command += "ping";
							}
							
							
							//Control
							if(mes.startsWith("c")){
								boolean val = false;
								if(mes.charAt(2) == 't'){
									val = true;
								}
								switch(mes.charAt(1)){
								case 'j':
									c.controlScheme.setKeyState(c.controlScheme.KEY_JUMP, val);
									break;
								case 'l':
									c.controlScheme.setKeyState(c.controlScheme.KEY_LEFT, val);
									break;
								case 'r':
									c.controlScheme.setKeyState(c.controlScheme.KEY_RIGHT, val);
									break;
								case 'd':
									c.controlScheme.setKeyState(c.controlScheme.KEY_DUCK, val);
									break;
								case 'p':
									c.controlScheme.setKeyState(c.controlScheme.KEY_PRIMARY, val);
									break;
								case 's':
									c.controlScheme.setKeyState(c.controlScheme.KEY_SECONDARY, val);
									break;
								case 'u':
									c.controlScheme.setKeyState(c.controlScheme.KEY_UP, val);
									break;
								case 'D':
									c.controlScheme.setKeyState(c.controlScheme.KEY_DOWN, val);
									break;
								case 'b':
									c.controlScheme.setKeyState(c.controlScheme.KEY_BLOCK, val);
									break;
								case 'S':
									c.controlScheme.setKeyState(c.controlScheme.KEY_SELECT, val);
									break;
								case 'x':
									c.controlScheme.setKeyState(c.controlScheme.KEY_START, val);
								case 'g':
									c.controlScheme.setKeyState(c.controlScheme.KEY_GRAB, val);
									break;
								}

							}
							
							//Username
							if(mes.startsWith("si")){
								c.USERNAME = mes.substring(2);
							}
							
						}
					}
					
					
					if(!command.equals("")){
						c.sendMessage(command);
					}
				}
				if(c.isDisconnected()){					
					connections.remove(c);
					c.destroy();
				}
			}
		}
	}
	
	public void connectionHandler(ServerSocket server){
		if(accepting){
			if(connections.size() == 0){
				connections.add(new Connection(server));
			}
			if(connections.get(connections.size()-1).isConnected() && connections.size() < MAX_CONNECTIONS){
				connections.add(new Connection(server));
			}
		}
	}
	
	public ArrayList<Connection> getConnections(){
		@SuppressWarnings("unchecked")
		ArrayList<Connection> conn = (ArrayList<Connection>) connections.clone();

		if(conn.size() > 0){
			if(!conn.get(conn.size()-1).isConnected()){
				conn.remove(conn.size()-1);
			}
		}
		
		return conn;		
	}
	
}
