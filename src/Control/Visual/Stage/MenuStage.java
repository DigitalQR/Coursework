package Control.Visual.Stage;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Control.Visual.Menu.Button2f;
import Control.Visual.Menu.Font;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class MenuStage extends Stage{

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
		GreyButton = Loader.loadTexture("Button/Grey");
		GreyButtonSelected = Loader.loadTexture("Button/Grey_selected");
		
		RedButton = Loader.loadTexture("Button/Red");
		RedButtonSelected = Loader.loadTexture("Button/Red_selected");

		GreenButton = Loader.loadTexture("Button/Green");
		GreenButtonSelected = Loader.loadTexture("Button/Green_selected");
		font = new Font("Font/Default",8);
		
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
		
		//Finalise MainButtons
		for(Button2f b:MainButton){
			b.generateText(font, 7.5f, 30);
		}
		
		MainButton[0].generateText(font, 10f, 10);
	}
	
	private void MainDraw(){
		if(activeButton == 1){
			if(timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_UP)){
				selectedButton--;
				input();
			}
			if(timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_DOWN)){
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
		
		for(int i = 0; i<MainButton.length; i++){
			Model m = MainButton[i].getModel();
			if(i == selectedButton){
				m.setTexture(GreyButtonSelected);
			}else{
				m.setTexture(GreyButton);
			}
			Renderer.render(m);
			
			Model[] text = MainButton[i].getText();
			for(Model t:text){
				Renderer.render(t);
			}
		}
		
		MainProcessMainButton();
	}
	
	private void MainProcessMainButton(){
		if(Settings.User[0].isKeyPressed(Settings.User[0].KEY_UP)){
			activeButton = selectedButton;
			input();
			switch(selectedButton){
			
			//START
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
		}else if(Settings.User[0].isKeyPressed(Settings.User[0].KEY_DOWN)){
			activeButton = 1;
		}
	}
	
}
