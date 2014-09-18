package Control.Visual.Stage;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Control.Visual.Font;
import Control.Visual.Menu.Button2f;
import Entities.Tools.ControlScheme;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class MenuStage extends Stage{

	public static boolean locked = false;
	private static long lastInput = 0;
	private int selectedButton = 0;
	private int activeButton = 1;
	
	public Font font;
	public static Texture 
	GreyButton, GreyButtonSelected,
	RedButton, RedButtonSelected,
	GreenButton, GreenButtonSelected;
	
	private Button2f[] MainButton;
	
	GamepadSetupStage GamepadSetup;
	
	public void prepare(){
		font = new Font("Font/Default");
		
		GreyButton = Loader.loadTexture("Button/Grey");
		GreyButtonSelected = Loader.loadTexture("Button/Grey_selected");
		
		RedButton = Loader.loadTexture("Button/Red");
		RedButtonSelected = Loader.loadTexture("Button/Red_selected");

		GreenButton = Loader.loadTexture("Button/Green");
		GreenButtonSelected = Loader.loadTexture("Button/Green_selected");
		
		MainPrepare();
		
		GamepadSetup = new GamepadSetupStage();
		GamepadSetup.prepare(font);
	}
	
	public static boolean timePassed(){
		long current = System.nanoTime()/1000000;
		if((current-lastInput) > 150){
			return true;
		}
		return false;
	}
	
	public static void input(){
		lastInput = System.nanoTime()/1000000;
	}
	
	public void update(){
		//Light position
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{0, 0 , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
		GL11.glTranslatef(-7.7f, -7.5f, -10);
		
		MainDraw();
		
		
		switch(activeButton){
			case 2:
				GamepadSetup.update();
			break;
		}
		
	}
	
	
	
	private void MainPrepare(){
		//Initialise MainButtons
		MainButton = new Button2f[5];
		
		//Set MainButton information
		MainButton[0] = new Button2f(new Vector2f(0,125), new Vector2f(150,25), "Untitled Game");
		MainButton[1] = new Button2f(new Vector2f(0,105), new Vector2f(50, 10), "Start");
		MainButton[2] = new Button2f(new Vector2f(0,90), new Vector2f(50, 10), "Setup Controllers");
		MainButton[3] = new Button2f(new Vector2f(0,75), new Vector2f(50, 10), "Settings");
		MainButton[4] = new Button2f(new Vector2f(0,60), new Vector2f(50, 10), "Exit");
	}
	
	private void MainDraw(){
		//Button logic
		if(activeButton == 1){
			if(timePassed() && Settings.User[0].isKeyPressed(ControlScheme.KEY_MENU_UP)){
				selectedButton--;
				input();
			}
			if(timePassed() && Settings.User[0].isKeyPressed(ControlScheme.KEY_MENU_DOWN)){
				selectedButton++;
				input();
			}
		}
		if(selectedButton < 1){
			selectedButton = MainButton.length-1;
		}		
		if(selectedButton > MainButton.length-1){
			selectedButton = 1;
		}
		
		//Draw buttons
		for(int i = 0; i<MainButton.length; i++){
			
			Model m = MainButton[i].getModel();
			if(i == selectedButton){
				m.setTexture(GreyButtonSelected);
			}else{
				m.setTexture(GreyButton);
			}
			Renderer.render(m);
			
			//Draw text
			if(i != 0){
				font.drawText(MainButton[i].getMessage(), MainButton[i].getTextLocation(), 0.04f, 7f);
			}else{
				font.drawText(MainButton[i].getMessage(), MainButton[i].getTextLocation(), 0.15f, 7.5f);
			}
		}
		
		if(!locked){
			MainProcessMainButton();
		}
	}
	
	private void MainProcessMainButton(){
		if(Settings.User[0].isKeyPressed(ControlScheme.KEY_MENU_SELECT)){
			activeButton = selectedButton;
			input();
			switch(selectedButton){
			
			//Start
			case 1:
					DisplayControl.setStage(DisplayControl.STAGE_TESTSTAGE);
					MainControl.Paused = false;
				break;
			
			//Setup Controllers
			case 2:
				break;
			
			//Settings
			case 3:
				break;
			
			//Exit
			case 4:
				MainControl.CloseRequest = true;
				break;	
			}
		}else if(Settings.User[0].isKeyPressed(ControlScheme.KEY_MENU_BACK) && !locked){
			activeButton = 1;
		}
	}
	
}
