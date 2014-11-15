package Control.Visual.Stage;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Action;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class MenuStage extends Stage implements Action{
	
	private String[] buttonName = {"Start", "Gamepad setup", "Settings", "Exit"};
	private Button[] button = new Button[4];
	private TextBox[] text = new TextBox[4];
	private int currentButton = 0;
	
	public MenuStage(){
		super();
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Untitled Game", null);
		this.add(header);

		TextBox version = new TextBox(new Vector3f(-1.6f,0.6f,-2.5f),new Vector3f(1f,0.5f,0.5f), "Version: " + Settings.Version, null);
		version.setHeaderColour(new float[]{0,0,0,0});
		this.add(version);
		
		for(int i = 0; i<4; i++){
			button[i] = new Button(new Vector3f(-1.6f, 0.6f-0.4f*i, -2.5f), new Vector3f(1.2f, 0.3f, 0.5f), buttonName[i]);
			button[i].setTextSize(0.2f);
			button[i].setAction(this);
			this.add(button[i]);
			
			text[i] = new TextBox(new Vector3f(-0.3f, -1.3f, -2.5f), new Vector3f(1.9f, 2f, 0.5f), buttonName[i], "This is for testing...\nMhm");
			text[i].setHeaderHeight(0.1f);
			text[i].setHeaderTextSize(0.05f);
			text[i].setContentTextSize(0.02f);
			text[i].setContentColour(new float[]{1,1,1,0.5f});
			text[i].setDrawn(false);
			this.add(text[i]);
		}
	}
	
	protected void updateInfo(){
		if(this.hasFocus()){
			if(Input.isKeyPressed(Input.KEY_UP)){
				currentButton--;
				Input.recieved();
			}if(Input.isKeyPressed(Input.KEY_DOWN)){
				currentButton++;
				Input.recieved();
			}
			if(currentButton < 0){
				currentButton = 3;
			}
			if(currentButton > 3){
				currentButton = 0;
			}
			if(Input.isKeyPressed(Input.KEY_FORWARD)){
				button[currentButton].focus();
				Input.recieved();
			}
		}

		
		for(int i = 0; i<4; i++){
			if(i == currentButton){
				button[i].setColour(new float[]{1,1,1,1});
			}else{
				button[i].setColour(new float[]{1,1,1,0.5f});
			}
			
			if(button[i].hasFocus()){				
				text[i].setDrawn(true);
				
				if(Input.isKeyPressed(Input.KEY_BACK)){
					button[i].unfocus();
					Input.recieved();
				}
			}else{
				text[i].setDrawn(false);
			}
		}
	}

	protected void updateUI(){
	}

	public void run(int ID){
		
		//Start
		if(ID == button[0].getID() && button[0].hasFocus()){
			Stage.setStage(Stage.getStage("overworld"));
		}
	}

}
