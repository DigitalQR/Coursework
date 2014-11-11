package Control.Visual.Stage;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Control.Visual.Font;
import Control.Visual.Menu.Button2f;
import Control.Visual.Menu.Assets.TextBox;
import Entities.Player;
import RenderEngine.textureLoader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class MenuStage extends Stage{

	public static boolean locked = false;
	private static long lastInput = 0;
	private int selectedButton = 0;
	private int activeButton = 1;
	
	public Font font;
	public static Texture Button, ButtonSelected;
	
	private Button2f[] MainButton;
	
	GamepadSetupStage GamepadSetup;
	SettingStage Setting;
	
	public void prepare(){
		font = new Font("Font/Default");
		
		Button = textureLoader.loadTexture("Button/Grey");
		ButtonSelected = textureLoader.loadTexture("Button/Grey_selected");
		
		MainPrepare();
		
		GamepadSetup = new GamepadSetupStage(font);
		Setting = new SettingStage(font);
		
		player = Settings.User.get(0);

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
	
	private static Player player;
	
	public void update(){
        
		Camera.setLocation(-7.7f, -7.5f, -10);
		Camera.focus();
		
		Model m = player.getModel();
		m.scaleBy(20);
		m.setLocation(new Vector3f(4, -7, -15));
		m.setTexture(Button);
        Renderer.render(m);

		MainDraw();
		
		switch(activeButton){
			//Start
			case 1:
				break;
			
			//Setup Controllers
			case 2:
				GamepadSetup.update();
				break;
			
			//Settings
			case 3:
				Setting.update();
				break;
			
			//Exit
			case 4:
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
		
		//Version number
		font.drawText("Version: " + Settings.Version, new Vector3f(-0.5f,-0.3f,0), 0.04f, 7f);
		
		//Button logic
		if(activeButton == 1){
			if(timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_UP)){
				selectedButton--;
				input();
			}
			if(timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_DOWN)){
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
				m.setTexture(ButtonSelected);
			}else{
				m.setTexture(Button);
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
		if(Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_SELECT) && (activeButton == 1 || activeButton == 4)){
			activeButton = selectedButton;
			input();
			switch(selectedButton){
			
			//Start
			case 1:
					DisplayControl.setStage(DisplayControl.STAGE_OVERWORLD);
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
		}else if(Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_BACK) && !locked){
			activeButton = 1;
		}
	}

	public void switchToUpdate(){
	}
	
	public void switchFromUpdate(){
	}
	
}
