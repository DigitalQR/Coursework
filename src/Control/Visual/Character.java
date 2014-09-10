package Control.Visual;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Character {
	
	private boolean[][] Char;
	private Cubef[] model;
	
	public Character(int size){
		Char = new boolean[size][size];
		model = new Cubef[0];
	}
	
	public void setChar(int x, int y, boolean val){
		Char[Char.length-y-1][x] = val;
	} 
	
	public boolean getValue(int x, int y){
		return Char[y][x];
	}
	
	public void flip(){
		boolean[][] temp = new boolean[Char.length][Char.length];
		
		for(int i = 0; i<Char.length; i++){
			temp[i] = Char[Char.length-1-i];
		}
		
		Char = temp;
	}
	
	public void generateModel(){
		for(int y = 0; y<Char.length; y++){
			for(int x = 0; x<Char.length; x++){
				if(getValue(x,y)){
					addToModel(new Cubef(new Vector3f(x,y,0), new Vector3f(x+1, y+1, 0.01f)));
				}
			}
		}
	}
	
	public int getCubeCount(){
		return model.length;
	}
	
	private void addToModel(Cubef c){
		Cubef[] temp = new Cubef[model.length+1];
		
		for(int i = 0; i<model.length; i++){
			temp[i] = model[i];
		}
		
		temp[temp.length-1] = c;
		model = temp;
	}
	
	public Cubef[] getModel(Vector3f translation, float scale){
		Cubef[] c = new Cubef[model.length];
		
		for(int i = 0; i<model.length; i++){
			c[i] = new Cubef(model[i].StartLocation, model[i].EndLocation);
			c[i].enlarge(scale);
			c[i].translate(translation);
		}
		return c;
	}
	
	
	
}
