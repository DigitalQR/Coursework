package Control.Visual.Stage;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.Settings;
import Control.Visual.Menu.Button2f;
import Control.Visual.Menu.Font;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public class MenuStage extends Stage{

	private long lastInput;
	private int selectedButton = 0;
	
	private Font font;
	private Texture buttonTexture, buttonSelectedTexture;
	private Button2f[] button;
	
	public void prepare(){	
		lastInput = System.nanoTime()/1000000;
		buttonTexture = Loader.loadTexture("Button");
		buttonSelectedTexture = Loader.loadTexture("Button_selected");
		font = new Font("Font/Default",8);
		
		button = new Button2f[9];
		for(int i = 0; i<button.length; i++){
			button[i] = new Button2f(new Vector2f(0,15*i), new Vector2f(80,10), "");
		}
		
		
		button[8] = new Button2f(new Vector2f(0,125), new Vector2f(150,25), "Untitled Game");
		button[7].setMessage("Start");
		button[6].setMessage("setup controllers");
		
		for(Button2f b:button){
			b.generateText(font, 7.5f, 20);
		}
		
		button[8].generateText(font, 10f, 10);
	}

	private boolean timePassed(){
		long current = System.nanoTime()/1000000;
		if((current-lastInput) > 100){
			return true;
		}
		return false;
	}
	
	public void update(){
		GL11.glTranslatef(-7.7f, -7.5f, -10);
		
		if(timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_UP)){
			selectedButton++;
			lastInput = System.nanoTime()/1000000;
		}
		
		if(timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_DOWN)){
			selectedButton--;
			lastInput = System.nanoTime()/1000000;
		}
		
		
		
		
		if(selectedButton < 0){
			selectedButton = button.length-2;
		}		
		if(selectedButton > button.length-2){
			selectedButton = 0;
		}
		
		
		for(int i = 0; i<button.length; i++){
			Model m = button[i].getModel();
			
			if(i == selectedButton){
				m.setTexture(buttonSelectedTexture);
			}else{
				m.setTexture(buttonTexture);
			}
			Renderer.render(m);
			
			Model[] text = button[i].getText();
			for(Model t:text){
				Renderer.render(t);
			}
		}
	}

}
