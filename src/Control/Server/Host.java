package Control.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Collision.Hitbox;
import Control.MainControl;
import Control.Settings;
import Control.Server.Assets.Player;
import Entities.Tools.ServerControlScheme;

public class Host implements Runnable{

	public static final int UPS = MainControl.UPS;
	
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	private int port;
	private ServerSocket server;
	private Thread thread;
	
	public Host(int port){
		this.port = port;
		thread = new Thread(this);
	}
	
	public void start(){
		thread.start();
	}
	
	
	public void run(){
		
		try{
			server = new ServerSocket(port);
			System.out.println("Server Socket initialised.");
			connections.add(new Connection(server));
			
			while(true){
				connectionHandler(server);
				
				@SuppressWarnings("unchecked")
				ArrayList<Connection> conn = (ArrayList<Connection>) connections.clone();
				for(Connection c: conn){
					
					if(c.isConnected()){
						ArrayList<Player> player = Player.getList();
						String command = "";
						
						for(int i = 0; i<player.size(); i++){
							if(!player.get(i).locationEquals(c.players.get(i))){
								Player p = player.get(i);
								command += "pl" + i + "l" + p.getLocation().x + "," + p.getLocation().y + ";"; 
								//pl - player
								//wd - world
								
								//l - location
								//h - hitbox
								//i - Information
							}
							if(!player.get(i).colourEquals(c.players.get(i))){
								Player p = player.get(i);
								command += "pl" + i + "ic" + p.getColour().x + "," + p.getColour().y + "," + p.getColour().z + ";"; 
							}
						}
						c.players = player;
						
						if(!c.hitboxes.equals(Settings.hb)){
							command += "wdh";
							
							int track = 0;
							for(Hitbox hb: Settings.hb){
								command += hb.getLocation().x + "," + hb.getLocation().y + "," + hb.getSize().x + "," + hb.getSize().y ;
								if(track != Settings.hb.size()-1){
									command +=",";
								}else{
									command +=";";
								}
								track++;
							}
							c.hitboxes = Settings.hb;
						}

						String message = c.getRecievedMessage();
						if(message != ""){
							for(String mes: message.split(";")){
								if(mes.equals("ping")){
									command += "ping";
								}
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
									}

								}
							}
						}
						
						
						if(!command.equals("")){
							c.sendMessage(command);
						}
					}
					if(c.isDisconnected()){
						connections.remove(c);
						System.out.println("Connection " + c.getID() + " removed");
					}
				}
				
				try {
					TimeUnit.NANOSECONDS.sleep(UPS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void connectionHandler(ServerSocket server){
		if(connections.size() == 0){
			connections.add(new Connection(server));
		}
		if(connections.get(connections.size()-1).isConnected()){
			connections.add(new Connection(server));
		}
	}
	
}

class Connection{
	
	private static int IDTrack = 1;
	
	private final int ID = IDTrack++;
	private int playerID = ID+1;
	private boolean connected = false;
	private boolean disconnected = false;
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	public ServerControlScheme controlScheme;
	
	protected ArrayList<Player> players = Player.getNullList();
	protected ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	
	public Connection(final ServerSocket listener){
		new Thread(new Runnable(){
			
			public void run(){
				try{
					socket = listener.accept();
					socket.setTcpNoDelay(true);
					connected = true;
					output = new PrintWriter(socket.getOutputStream(),true);
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					controlScheme = new ServerControlScheme();
					Settings.User.get(ID).setControlScheme(controlScheme);
					
					while(!disconnected){
						processMessage();
					}
					
				}catch(IOException e){
					destroy();
				}
			}
			
		}).start();
	}
	
	public int getID(){
		return ID;
	}
	
	public void setPlayerID(int id){
		playerID = id;
	}
	
	public int getPlayerID(){
		return playerID;
	}
	
	public boolean isDisconnected(){
		return disconnected;
	}
	
	public boolean isConnected(){
		return connected && !disconnected;
	}
	
	public void sendMessage(String message){
		try{
			output.println(message);
		}catch(NullPointerException e){
			destroy();
		};
	}
	
	public String getRecievedMessage(){
		if(message == null){
			message = "";
		}
		String mes = message;
		message = "";
		if(mes == null){
			mes = "";
		}
		return mes;
	}
	
	private String message = "";
	
	private void processMessage() throws IOException{
		String mes = input.readLine();
		if(mes == null){
			destroy();
		}else{
			message+= mes + ";";
		}
	}
	
	public void destroy(){
		try{
			socket.close();
			output.flush();
			output.close();
			input.close();
			connected = false;
			disconnected = true;
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
