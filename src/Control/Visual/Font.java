package Control.Visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Font{
	
	private Character[] Letter;
	
	public Font(String file){
		Letter = new Character[42];
		
		try{
			BufferedImage fontsheet = ImageIO.read(new File("res/" + file + ".png"));
			int size = fontsheet.getHeight();
			
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
				Letter[i].generateModel();
			}
			
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private Cubef[] getSentence(String Message, Vector3f translation, float scale, float LetterSize){
		String mes = correctString(Message);
		Cubef[] temp = new Cubef[getCubeCount(mes)];
		float x = translation.x;
		int track = 0;
		LetterSize*=scale;
		
		for(char s:mes.toCharArray()){
			int Char = getLetterID(s);
			if(Char >= 0){
				Cubef[] t = Letter[Char].getCubeModel(translation, scale);
				for(int i = 0; i < t.length; i++){
					temp[track++] = t[i];
				}
				
			}else{
				if(-Char == '\n'){
					translation.y-=LetterSize;
					translation.x = x-LetterSize;
				}
			}
			
			translation.x+=LetterSize;
		}
																																																																																																																																																																																								
		return temp;
	}
	

	
	public void drawText(String Message, Vector3f translation, float scale, float LetterSize){
		Cubef[] mes = getSentence(Message, translation, scale, LetterSize);

		for(Cubef c:mes){
			Model m = new Model(c);
			m.setRGBA(0f, 0f, 0f, 1);
			Renderer.render(m);
		}
	}
	
	private int getCubeCount(String mes){
		int count = 0;
		
		for(char s:mes.toCharArray()){
			if(getLetterID(s) >= 0){
				count+=Letter[getLetterID(s)].getCubeCount();
			}
		}
		
		return count;
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
			if(c == '\n'){
				Out+='\n';
			}
			if(c  >= '0' && c <= '?'){
				Out+=c;
			}
		}
		return Out;
	}
	
	private int getLetterID(int Char){
		if(Char <= 'z' && Char >= 'a'){
			return Char-97;
		}else if(Char >= '0' && Char <= '?'){
			return Char-22;
		}else{
			return -Char;
		}
	}
		
	
}
