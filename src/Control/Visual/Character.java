package Control.Visual;

import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class Character {
	
	private Vector2f location;
	private int ID;
	private Model model;
	
	public Character(int ID, int totalWidth, int totalHeight){
		this.ID = ID;
		genModel();
	}
	
	public Vector2f getLocation() {
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}

	private void genModel(){
		float[] vert = {
				0,0,0,  0,1,0,  0.67f,0,0,  0.67f,1,0
		};

		float width = 1000000000/(Font.CHARACTER_TOTAL+21);
		float height = 1;
		width/=1000000000;
		
		float x =	ID*width;
		float y = 0;
		
		float[] text = {
				x,y,  x,y-height,  x+width,y,  x+width,y-height
		};

		int[] ind = {
				0,1,2,  1,3,2
		};

		float[] norm = {
				0,0,-1,  0,0,-1,  0,0,-1,  0,0,-1
		};
		
		model = new Model(vert, text, ind, norm);
	}
	
	public Model getModel(){
		return model.clone();
	}	
	
}
