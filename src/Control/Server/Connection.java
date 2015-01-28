package Control.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Control.Settings;
import Control.Server.Assets.Player;
import Entities.Powerup;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import Entities.Tools.ServerControlScheme;
import Level.World;

public class Connection {

	private static int IDTrack = 1;
	
	private final int ID = IDTrack++;
	private Entities.Player player;
	private int stage = -1;
	public int ping = 0;
	private boolean connected = false;
	private boolean disconnected = false;
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	public ServerControlScheme controlScheme;
	
	protected ArrayList<Player> players = Player.getNullList();
	protected World world;
	protected ArrayList<Damage> damage = new ArrayList<Damage>();
	protected ArrayList<Shield> shields = new ArrayList<Shield>();
	protected ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	protected String additions = "";
	
	public String USERNAME = "?";
	
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
					
					while(!disconnected){
						processMessage();
					}
					
				}catch(IOException | NullPointerException e){
					destroy();
				}
			}
			
		}).start();
	}
	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getID(){
		return ID;
	}
	
	public InetAddress getIP(){
		return socket.getInetAddress();
	}
	
	public void setPlayer(Entities.Player player){
		this.player = player;
	}
	
	public Entities.Player getPlayer(){
		return player;
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
		if(connected){
			try{
				socket.close();
				output.flush();
				output.close();
				input.close();
				connected = false;
				disconnected = true;

				if(player != null){
					Settings.User.remove(player);
				}
				
				System.out.println("Connection " + getID() + " removed");
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
}
