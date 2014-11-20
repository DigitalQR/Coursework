package Control.Visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Debug.ErrorPopup;
import RenderEngine.Renderer;
import RenderEngine.textureLoader;
import RenderEngine.Model.Model;
import Tools.Maths.Vector3f;

public class Font{
	
	private Character[] Letter;
	public static final int CHARACTER_TOTAL = 44;
	private Texture texture;
	private float[] RGBA = {0f,0f,0f,1f};
	
	public void setRGBA(float r, float g, float b, float a){
		RGBA[0] = r;
		RGBA[1] = g;
		RGBA[2] = b;
		RGBA[3] = a;
	}
	
	public Font(String file){
		Letter = new Character[CHARACTER_TOTAL];
		texture = textureLoader.loadTexture("Font/Default");
		
		try{
			BufferedImage fontsheet = ImageIO.read(new File("res/" + file + ".png"));
			
			for(int i = 0; i < CHARACTER_TOTAL; i++){
				Letter[i] = new Character(i, fontsheet.getWidth(), fontsheet.getHeight());
			}
			
		}catch(IOException e){
			System.err.println("For " + "res/" + file + ".png");
			ErrorPopup.createMessage(e, true);
		}
	}

	public void drawText(String Message, Vector3f translation, float scale, float LetterSize){
		boolean light = GL11.glGetBoolean(GL11.GL_LIGHTING);
		scale*=8;
		LetterSize/=8;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		if(light){
			GL11.glDisable(GL11.GL_LIGHTING);	
		}
		
		float originalX = translation.x;
		for(char s:correctString(Message).toCharArray()){
			int c = getLetterID(s);
			if(c != -' '){
				if(-c == '\n'){
					translation.x = originalX - LetterSize*scale;
					translation.y -= LetterSize*scale;
				}else if(c >= 0){
					Model m = Letter[c].getModel();
					m.scaleBy(scale);
					m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
					m.setLocation(translation);
					m.setTexture(texture);
					Renderer.render(m);
				}
			}
			translation.x+= LetterSize*scale;
		}
		
		if(light){
			GL11.glEnable(GL11.GL_LIGHTING);	
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
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
			if(c == '.'){
				Out+=c;
			}
			if(c  >= '0' && c <= '?'){
				Out+=c;
			}
			if(c == '%'){
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
		}else if(Char == '.'){
			return 42;
		}else if(Char == '%'){
			return 43;
		}else{
			return -Char;
		}
	}
		
	
}
