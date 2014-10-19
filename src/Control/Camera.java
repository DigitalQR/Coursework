package Control;

import java.util.List;

import Entities.Player;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;

public class Camera {

	private static Vector3f Location = new Vector3f(0,0,0);
	
	public static void setLocation(int x, int y, int z){
		Location.x = x;
		Location.y = y;
		Location.z = z;
	}
	
	public static Vector3f getLocation(){
		return Location;
	}
	
	public static void process(List<Player> User){		
		float x = 0, y = 0;
		for(Player p: User){
			x-= p.getLERPLocation().x;
			y-= p.getLERPLocation().y;
		}
		Location.x = x/User.size();
		Location.y = y/User.size();
		
		
		
		float Delta = 0;
		for(Player a :User){
			for(Player b :User){
				if(!a.equals(b)){
					float dx = (float)-Toolkit.Modulus(Toolkit.Differencef(a.getLERPLocation().x, b.getLERPLocation().x));
					float dy = (float)-Toolkit.Modulus(Toolkit.Differencef(a.getLERPLocation().y, b.getLERPLocation().y));
					if(dx < Delta){
						Delta = dx;
					}if(dy < Delta){
						Delta = dy;
					}
				}
			}
		}
		Location.z = Delta;
		if(Location.z > -5f){
			Location.z = -5f;
		}
	}
}
