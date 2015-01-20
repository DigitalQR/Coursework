package Control.Server.Assets;

import java.util.ArrayList;

import Control.Settings;
import Entities.Powerup;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Player {
	
	private Vector2f location;
	private Vector3f colour = new Vector3f(-1,-1,-1);
	private int killCount = 0;
	private int stock = 0;
	private float factor = 0;
	public boolean resetFlag = false;
	public Powerup powerup;
	
	public Player(Vector2f location){
		this.location = location;
	}
	
	public Vector3f getColour(){
		return colour;
	}

	public boolean locationEquals(Player p){
		return (p.getLocation().x == getLocation().x && p.getLocation().y == getLocation().y) && !resetFlag;
	}
	
	public boolean powerupEquals(Player p){
		if(powerup == null && p.powerup == null){
			return true;
		}else if(powerup != null){
			return powerup.equals(p.powerup);
		}else{
			return p.powerup.equals(powerup);
		}
	}
	
	public boolean colourEquals(Player p){
		return (p.getColour().x == colour.x && p.getColour().y == colour.y && p.getColour().z == colour.z) && !resetFlag;
	}
	
	public boolean combatEquals(Player p){
		return (killCount == p.killCount && factor == p.factor && stock == p.stock) && !resetFlag;
	}
	
	public Vector2f getLocation() {
		location.x = Math.round(location.x*100000);
		location.x/=100000;
		location.y = Math.round(location.y*100000);
		location.y/=100000;
		return location;
	}

	public int getKillCount() {
		return killCount;
	}

	public float getFactor() {
		return factor;
	}

	public int getStock(){
		return stock;
	}
	
	public void setLocation(Vector2f location) {
		this.location = location;
	}
	
	public static ArrayList<Player> getList(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		for(Entities.Player p: Settings.User){
			Vector2f loc = new Vector2f(p.getLocation().x, p.getLocation().y);
			Player player = new Player(loc);
			player.colour = new Vector3f(p.getRGBA()[0], p.getRGBA()[1], p.getRGBA()[2]);
			player.killCount = p.killCount;
			player.factor = p.getFactor();
			player.stock = p.getStock();
			player.powerup = p.getPowerup();
			players.add(player);
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
