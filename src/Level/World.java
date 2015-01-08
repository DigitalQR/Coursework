package Level;

import java.util.ArrayList;

import Collision.Hitbox;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public abstract class World {
	
	private ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	
	private ArrayList<Model> foreground = new ArrayList<Model>();
	private ArrayList<Model> background = new ArrayList<Model>();

	public ArrayList<Hitbox> getHitboxList(){
		@SuppressWarnings("unchecked")
		ArrayList<Hitbox> hb = (ArrayList<Hitbox>) hitboxes.clone();
		return hb;
	}
	
	public ArrayList<Model> getBackRenderList(){
		ArrayList<Model> render = new ArrayList<Model>();
		render.addAll(background);
		return render;
	}

	public ArrayList<Model> getFrontRenderList(){
		ArrayList<Model> render = new ArrayList<Model>();
		render.addAll(foreground);
		return render;
	}
	
	public boolean equals(World w){
		return hitboxes.equals(w.hitboxes) && foreground.equals(w.foreground) && background.equals(w.background);
	}
	
	public String encode(){
		String command = "wdh";

		int track = 0;
		command += "~:";
		for(Hitbox hb: hitboxes){
			command += hb.getLocation().x + "," + hb.getLocation().y + "," + hb.getSize().x + "," + hb.getSize().y ;
			if(track != hitboxes.size()-1){
				command +=":";
			}
			track++;
		}
		command +="]";
		command += "~#";
		
		track = 0;
		for(Model m: background){
			
			for(int i = 0; i<m.getVertices().length; i++){
				command += m.getVertices()[i];
				if(i != m.getVertices().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}
			
			for(int i = 0; i<m.getTextureCoords().length; i++){
				command += m.getTextureCoords()[i];
				if(i != m.getTextureCoords().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getIndices().length; i++){
				command += m.getIndices()[i];
				if(i != m.getIndices().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getNormals().length; i++){
				command += m.getNormals()[i];
				if(i != m.getNormals().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getRGBA().length; i++){
				command += m.getRGBA()[i];
				if(i != m.getRGBA().length-1){
					command +=",";
				}
			}
			
			if(track != background.size()-1){
				command +="#";
			}
			track++;
		}
		command +="]";
		command += "~#";
		
		track = 0;
		for(Model m: foreground){
			
			for(int i = 0; i<m.getVertices().length; i++){
				command += m.getVertices()[i];
				if(i != m.getVertices().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}
			
			for(int i = 0; i<m.getTextureCoords().length; i++){
				command += m.getTextureCoords()[i];
				if(i != m.getTextureCoords().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getIndices().length; i++){
				command += m.getIndices()[i];
				if(i != m.getIndices().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getNormals().length; i++){
				command += m.getNormals()[i];
				if(i != m.getNormals().length-1){
					command +=",";
				}else{
					command +=":";
				}
			}

			for(int i = 0; i<m.getRGBA().length; i++){
				command += m.getRGBA()[i];
				if(i != m.getRGBA().length-1){
					command +=",";
				}
			}
			
			if(track != foreground.size()-1){
				command +="#";
			}
			track++;
		}
		
		return command + ";";
	}
	
	public static World decode(String message){
		return new ParsedWorld(message);
	}
	
	protected void generateMaingroundBasedModels(){
		background = new ArrayList<Model>();
		for(Hitbox h: hitboxes){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
				
				Model m = new Model(temp);
				background.add(m);
			}
		}
	}
	
	public void addHitbox(Hitbox h){
		hitboxes.add(h);
	}
	
	public void removeHitbox(Hitbox h){
		hitboxes.remove(h);
	}
	
	public void addToForeground(Model m){
		foreground.add(m);
	}
	
	public void removeFromForeground(Model m){
		foreground.remove(m);
	}

	public void addToBackground(Model m){
		background.add(m);
	}
	
	public void removeFromBackground(Model m){
		background.remove(m);
	}
}
