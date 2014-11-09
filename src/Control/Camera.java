package Control;


import java.util.List;

import Control.Visual.Stage.OverworldStage;
import Entities.Player;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Camera {

	private static Vector3f aimLocation = new Vector3f(0,0,-10f);
	private static Vector3f LastLocation = new Vector3f(0,0,-10f);
	private static Vector3f LERPLocation = new Vector3f(0,0,-10f);
	private static Vector3f Location = new Vector3f(0,0,-10f);
	
	private static float LastUpdate = 0;
	private static float cameraDepthLimit = 5f;
	
	public static void setLocation(int x, int y, int z){
		Location.x = x;
		Location.y = y;
		Location.z = z;
	}
	
	public static Vector3f getLocation(){
		return Location;
	}
	
	public static Vector3f getLERPLocation(){
		return LERPLocation;
	}
	
	public static void processLERP(){
		if(Settings.toggles.get("s_lerp")){
			float LookupTime = OverworldStage.getLERPTime();
			float CurrentTime = System.nanoTime();
			Vector3f location = new Vector3f(Location.x, Location.y, Location.z);
			
			float x = Toolkit.LERPValue(new Vector2f(LastUpdate, LastLocation.x), new Vector2f(CurrentTime, location.x), LookupTime);
			float y = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.y), new Vector2f(CurrentTime, location.y), LookupTime);
			LERPLocation = new Vector3f(x, y, location.z);
			
		}else{
			LERPLocation = new Vector3f(Location.x, Location.y, Location.z);
		}
	}
	
	public static void process(){		
		LastUpdate = System.nanoTime()-MainControl.UPS;
		
		@SuppressWarnings("unchecked")
		List<Player> User = (List<Player>) Settings.User.clone();
		LastLocation = new Vector3f(Location.x, Location.y, Location.z);
		Vector3f location = new Vector3f(Location.x, Location.y, Location.z);
		
		float x = 0, y = 0;
		int playerCount = 0;
		for(Player p: User){
			if(!p.isDead()){
				x-= p.getLERPLocation().x;
				y-= p.getLERPLocation().y;
				playerCount++;
			}
		}
		if(playerCount != 0){
			aimLocation.x = x/playerCount;
			aimLocation.y = y/playerCount;
		}
		
		
		float Delta = 0;
		for(Player a :User){
			if(!a.isDead()){
				
				for(Player b :User){
					if(!a.equals(b) && !b.isDead()){
						
						float dx = (float)-Toolkit.Modulus(Toolkit.Differencef(a.getLERPLocation().x, b.getLERPLocation().x))-1f;
						float dy = (float)-Toolkit.Modulus(Toolkit.Differencef(a.getLERPLocation().y, b.getLERPLocation().y))-1f;
						if(dx < Delta){
							Delta = dx;
						}if(dy < Delta){
							Delta = dy;
						}
					}
					
				}
				
			}
		}
		aimLocation.z = Delta;
		if(aimLocation.z > -cameraDepthLimit){
			aimLocation.z = -cameraDepthLimit;
		}
		
		float dif = (float) Math.round(Math.sqrt((location.x-aimLocation.x)*(location.x-aimLocation.x)+(location.y-aimLocation.y)*(location.y-aimLocation.y))*100);
		dif/=100;
		
		float CAMERA_SPEED = dif/20;

		if(Math.round(location.x*100) != Math.round(aimLocation.x*100)){
			float move = CAMERA_SPEED;
			
			if(Toolkit.Modulus(location.x-aimLocation.x) < CAMERA_SPEED){
				move = Toolkit.Modulus(location.x-aimLocation.x);
			}
			
			if(location.x > aimLocation.x){
				location.x-=move;
			}
			if(location.x < aimLocation.x){
				location.x+=move;
			}
		}


		if(Math.round(location.y*100) != Math.round(aimLocation.y*100)){
			float move = CAMERA_SPEED;
			
			if(Toolkit.Modulus(location.y-aimLocation.y) < CAMERA_SPEED){
				move = Toolkit.Modulus(location.y-aimLocation.y);
			}
			
			if(location.y > aimLocation.y){
				location.y-=move;
			}
			if(location.y < aimLocation.y){
				location.y+=move;
			}
		}


		if(Math.round(location.z*100) != Math.round(aimLocation.z*100)){
			float move = CAMERA_SPEED;
			
			if(Toolkit.Modulus(location.z-aimLocation.z) < CAMERA_SPEED){
				move = Toolkit.Modulus(location.z-aimLocation.z);
			}
			
			if(location.z > aimLocation.z){
				location.z-=move;
			}
			if(location.z < aimLocation.z){
				location.z+=move;
			}
		}

		
		Location = location;
	}
}
