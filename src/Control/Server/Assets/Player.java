package Control.Server.Assets;

import java.util.ArrayList;

import Control.Settings;
import Tools.Maths.Vector2f;

public class Player {
	
	private Vector2f location;
	
	public Player(Vector2f location){
		this.location = location;
	}

	public boolean equals(Player p){
		return p.getLocation().x == getLocation().x && p.getLocation().y == getLocation().y;
	}
	
	public Vector2f getLocation() {
		location.x = Math.round(location.x*100000);
		location.x/=100000;
		location.y = Math.round(location.y*100000);
		location.y/=100000;
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}
	
	public static ArrayList<Player> getList(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(Entities.Player p: Settings.User){
			Vector2f loc = new Vector2f(p.getLocation().x, p.getLocation().y);
			players.add(new Player(loc));
		}
		return players;
	}
	
	public static ArrayList<Player> getNullList(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(int i = 0; i<Settings.User.size(); i++){
			players.add(new Player(new Vector2f(98970,98970)));
		}
		
		return players;
	}
}
