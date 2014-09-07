package Control.Visual.Menu;

public class Character {
	
	private boolean[][] Char;
	
	public Character(int size){
		Char = new boolean[size][size];
	}
	
	public void setChar(int x, int y, boolean val){
		Char[x][Char.length-y-1] = val;
	} 
	
	public boolean getValue(int x, int y){
		return Char[x][y];
	}
	
	
}
