package Control;

import org.lwjgl.input.Keyboard;

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
	
	public static Vector3f getRotation(){
		Rotation.x = (float) Math.toDegrees(Location.z);
		return Rotation;
	}
	
	public static void process(){
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			Location_AIM.z+=0.1;
		}if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			Location_AIM.z-=0.1;
		}
		
		float x = 0, y = 0;
		for(int i = 0; i<Settings.User.length; i++){
			x-=Settings.User[i].getLocation().x;
			y-=Settings.User[i].getLocation().y;
		}
		Location_AIM.x = x/Settings.User.length;
		Location_AIM.y = y/Settings.User.length;
		
		
		
		float Delta = 0;
		for(int i = 0; i<Settings.User.length; i++){
			for(int n = 0; n<Settings.User.length; n++){
				if(n!=i){
					float dx = (float)-Toolkit.Modulus(Toolkit.Differencef(Settings.User[i].getLocation().x, Settings.User[n].getLocation().x));
					float dy = (float)-Toolkit.Modulus(Toolkit.Differencef(Settings.User[i].getLocation().y, Settings.User[n].getLocation().y));
					if(dx < Delta){
						Delta = dx;
					}if(dy < Delta){
						Delta = dy;
					}
				}
			}
		}
		Location_AIM.z = Delta;
		if(Location_AIM.z > -10){
			Location_AIM.z = -10;
		}
	}
}
