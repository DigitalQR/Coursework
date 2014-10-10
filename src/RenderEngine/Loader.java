package RenderEngine;

import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Debug.ErrorPopup;

public class Loader {
	
	public static Texture loadTexture(String fileName){
		Texture texture = null;
		try{
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		}catch(IOException e){
			ErrorPopup.createMessage(e);
		}
		return texture;
	}
	
	
	
}
	
	