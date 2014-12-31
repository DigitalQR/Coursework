package Control;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import Control.Visual.Stage.Core.Stage;
import Entities.Player;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Camera {

	private static Vector3f aimLocation = new Vector3f(0,0,-10f);
	private static Vector3f LastLocation = new Vector3f(0,0,-10f);
	private static Vector3f LERPLocation = new Vector3f(0,0,-10f);
	private static Vector3f Location = new Vector3f(0,0,-10f);
	private static float LERPTime = 0;
	
	private static float LastUpdate = 0;
	private static float cameraDepthLimit = 5f;
	
	private static float r = 0.5f, g = 1f, b = 0.5f;
	private static float[] RGBA = {0f,0f,0f,1f};
	
	public static float[] getRGBA(){
		return RGBA;
	}

	public static float[] getInverseRGBA(){
		float[] RGBA = getRGBA().clone();
		for(int i = 0; i<3; i++){
			RGBA[i] = 1f - RGBA[i];

		}
		return RGBA;
	}
	
	public static void incrementRandomLighting(){
		float speed = Settings.floats.get("s_light_deviation");
		r+=0.01f*speed;
		g+=0.015f*speed;
		b+=0.02f*speed;
		
		RGBA[0] = Toolkit.Modulus((float)Math.sin(r));
		RGBA[1] = Toolkit.Modulus((float)Math.sin(g));
		RGBA[2] = Toolkit.Modulus((float)Math.sin(b));
		
		for(int i = 0; i<3; i++){
			if(RGBA[i] > 0.7f){
				RGBA[i] = 0.7f;
			}
		}
	}
	
	public static void setRGB(float R, float G, float B){
		r = R;
		g = G;
		b = B;
	}
	
	public static void setLocation(float x, float y, float z){
		Location.x = x;
		Location.y = y;
		Location.z = z;
	}
	
	public static void setLERPTime(){
		LERPTime = System.nanoTime()-MainControl.UPS;
	}
	
	public static float getLERPTime(){
		return LERPTime;
	}

	public static void focus(){
		GL11.glTranslatef(LERPLocation.x, LERPLocation.y, LERPLocation.z);
	}
	
	public static Vector3f getLocation(){
		return Location;
	}
	
	public static Vector3f getLERPLocation(){
		return LERPLocation;
	}
	
	public static void processLERP(){
		setLERPTime();
		
		@SuppressWarnings("unchecked")
		List<Player> User = (List<Player>) Settings.User.clone();
		 
		for(Player p: User){
			p.processLERPLocation();
		}
		
		if(Settings.toggles.get("s_lerp")){
			float LookupTime = LERPTime;
			float CurrentTime = System.nanoTime();
			Vector3f location = new Vector3f(Location.x, Location.y, Location.z);
			
			float x = Toolkit.LERPValue(new Vector2f(LastUpdate, LastLocation.x), new Vector2f(CurrentTime, location.x), LookupTime);
			float y = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.y), new Vector2f(CurrentTime, location.y), LookupTime);
			LERPLocation = new Vector3f(x, y, location.z);
			
		}else{
			LERPLocation = new Vector3f(Location.x, Location.y, Location.z);
		}
		
		//Light position
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{-LERPLocation.x, -LERPLocation.y , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		float[] RGBA = getRGBA().clone();
		Ambient.put(new float[]{RGBA[0], RGBA[1], RGBA[2], RGBA[3]});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
	}
	
	public static void process(){
		LastUpdate = System.nanoTime()-MainControl.UPS;
		
		if(Stage.isCurrentStage(Stage.getStage("overworld"))){
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
							
							float dx = (float)-Toolkit.Modulus(a.getLERPLocation().x - b.getLERPLocation().x)-1f;
							float dy = (float)-Toolkit.Modulus(a.getLERPLocation().y - b.getLERPLocation().y)-1f;
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
				float move = CAMERA_SPEED*2;
				
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
		}else{
			Location = new Vector3f(0,0,-10f);
		}
	}
}
