package Level;

import java.util.ArrayList;

import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;
import Collision.Hitbox;
import Control.Settings;

public class RandomWorld extends World{
	
	public RandomWorld(){
		randomHitboxGen();

		generateMaingroundBasedModels();
		generateBackgroundModels();
		generateImmediateBackgroundModels();
		generateImmediateForegroundModels();
		generateForegroundModels();
	}

	private void randomHitboxGen(){
		int scale = 8;
		ArrayList<Hitbox> hb = Hitbox.RandomGeneration(10, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
		for(Hitbox h: hb){
			this.addHitbox(h);
		}
	}

	public void generateMaingroundBasedModels(){
		
		for(Hitbox h: this.getHitboxList()){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
				
				Model m = new Model(temp);
				m.setRGBA((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);
				this.addToBackground(m);
			}
		}
	}
	
	public void generateImmediateBackgroundModels(){
		int scale = 8;
		ArrayList<Hitbox> hb = Hitbox.RandomGeneration(3, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
		
		for(Hitbox h: hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 0.1f));
				
				Model m = new Model(temp);
				m.setRGBA((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f);
				this.addToBackground(m);
			}
		}
	}
	
	public void generateImmediateForegroundModels(){
		int scale = 8;
		ArrayList<Hitbox> hb = Hitbox.RandomGeneration(3, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
		
		for(Hitbox h: hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0.9f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
				
				Model m = new Model(temp);
				m.setRGBA((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.2f);
				this.addToForeground(m);
			}
		}
	}
	
	public void generateForegroundModels(){
		int scale = 8;
		ArrayList<Hitbox> hb = Hitbox.RandomGeneration(3, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
		
		for(Hitbox h: hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 1f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 2f));
				
				Model m = new Model(temp);
				m.setRGBA((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.2f);
				this.addToForeground(m);
			}
		}
	}

	public void generateBackgroundModels(){
		int scale = 8;
		ArrayList<Hitbox> hb = Hitbox.RandomGeneration(3, (int)Settings.boundary.getLocation().x*scale, (int)Settings.boundary.getLocation().y*scale, (int)Settings.boundary.getSize().x*scale, (int)Settings.boundary.getSize().y*scale, 10, 50);
		
		for(Hitbox h: hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, -1f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 0f));
				
				Model m = new Model(temp);
				m.setRGBA((float)Math.random(), (float)Math.random(), (float)Math.random(), 0.5f);
				this.addToBackground(m);
			}
		}
	}
}
