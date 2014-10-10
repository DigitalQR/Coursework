package Control;

import Entities.Player;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;



public class Camera {

	private static Vector3f Location = new Vector3f(0,0,0);
	private static Vector3f Location_AIM = new Vector3f(0,0,0);
	private static Vector3f Rotation = new Vector3f(0,0,0);
	

	public static void setLocation(int x, int y, int z){
		Location.x = x;
		Location.y = y;
		Location.z = z;
	}
	
	public static Vector3f getLocation(){
		return Location_AIM;
	}
	
	//static float track = 0;
	public static Vector3f getRotation(){
		//Rotation.y = (float) Math.toDegrees(track+=0.01f);
		return Rotation;
	}
	
	public static void process(Player[] User){		
		float x = 0, y = 0;
		for(int i = 0; i<User.length; i++){
			x-=User[i].getLERPLocation().x;
			y-=User[i].getLERPLocation().y;
		}
		Location_AIM.x = x/User.length;
		Location_AIM.y = y/User.length;
		
		
		
		float Delta = 0;
		for(int i = 0; i<User.length; i++){
			for(int n = 0; n<User.length; n++){
				if(n!=i){
					float dx = (float)-Toolkit.Modulus(Toolkit.Differencef(User[i].getLERPLocation().x, User[n].getLERPLocation().x));
					float dy = (float)-Toolkit.Modulus(Toolkit.Differencef(User[i].getLERPLocation().y, User[n].getLERPLocation().y));
					if(dx < Delta){
						Delta = dx;
					}if(dy < Delta){
						Delta = dy;
					}
				}
			}
		}
		Location_AIM.z = Delta;
		if(Location_AIM.z > -5f){
			Location_AIM.z = -5f;
		}
	}
}
