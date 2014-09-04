package Control.Visual.Menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Font{
	
	private Character[] Letter;
	private int size;
	
	public Font(String file, int size){
		Letter = new Character[26];
		this.size = size;
		try{
			BufferedImage fontsheet = ImageIO.read(new File("res/" + file + ".png"));
			
			for(int i = 0; i < Letter.length; i++){
				BufferedImage temp = fontsheet.getSubimage(i*size, 0, size, size);
				Letter[i] = new Character(size);

				for(int y = 0; y<size; y++){
					for(int x = 0; x<size; x++){
						if(temp.getRGB(x, y) != -1){
							Letter[i].setChar(x, y, true);
						}else{
							Letter[i].setChar(x, y, false);
						}
					}
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Cubef[] generateString(Vector3f Location, String message, float size, float divisor){
		message = correctString(message);
		

		int track = 0;
		for(int i = 0; i<message.length(); i++){
			int c = getLetter(message.charAt(i));
			
			if(c >= 0){
				for(int y = 0; y<this.size; y++){
					for(int x = 0; x<this.size; x++){
						if(Letter[c].getValue(x, y)){
							track++;
						}
					}
				}
			}
		}
		
		Cubef[] Message = new Cubef[track];
		track = 0;
		for(int i = 0; i<message.length(); i++){
			int c = getLetter(message.charAt(i));
			
			if(c >= 0){
				for(int y = 0; y<this.size; y++){
					for(int x = 0; x<this.size; x++){
						if(Letter[c].getValue(x, y)){
							Message[track] = new Cubef(new Vector3f(Location.x+(x+size)/divisor, Location.y+(y+size)/divisor, Location.z), new Vector3f(Location.x+(x+size)/divisor+size/(divisor*this.size), Location.y+(y+size)/divisor+size/(divisor*this.size), Location.z+size/(divisor*this.size)));
							track++;
						}
					}
				}
			}

			Location.x+=size/divisor;
		}
		
		return Message;
	}
	
	private static String correctString(String message){
		String Out = "";
		for(char c:message.toLowerCase().toCharArray()){
			if(c > 96 && c < 123){
				Out+=c;
			}
			if(c == 32){
				Out+=' ';
			}
		}
		return Out;
	}
	
	public int getLetter(int Char){
		return Char-97;
	}
	
	
}
