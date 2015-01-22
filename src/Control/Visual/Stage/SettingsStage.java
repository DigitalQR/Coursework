package Control.Visual.Stage;

import java.util.HashMap;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Action;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class SettingsStage extends Stage implements Action{
	
	private Button[] toggles;
	private int currentButton = 0;
	private String[] names = {
			"Low Settings"
	};
	private HashMap<String, String> command = new HashMap<String, String>();
	
	public SettingsStage(){
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Settings", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		toggles = new Button[names.length];
		
		command.put("Low Settings", "s_low_settings");
		
		int i = 0;
		for(String s: names){
			toggles[i] = new Button(new Vector3f(-1.6f, 1.3f-0.4f*i, -2.5f), new Vector3f(1.2f, 0.3f, 0.5f), s);
			toggles[i].setTextSize(0.2f);
			toggles[i].setAction(this);
			this.add(toggles[i]);
			i++;
		}
	} 
	
	protected void updateInfo() {
		if(this.hasFocus()){
			
			//Back to menu
			if(Input.isKeyPressed(Input.KEY_BACK)){
				Stage.setStage(Stage.getStage("menu"));
				Input.recieved();
			}
			
			//Button process
			if(Input.isKeyPressed(Input.KEY_UP)){
				currentButton--;
				Input.recieved();
			}if(Input.isKeyPressed(Input.KEY_DOWN)){
				currentButton++;
				Input.recieved();
			}
			if(currentButton < 0){
				currentButton = toggles.length-1;
			}
			if(currentButton > toggles.length-1){
				currentButton = 0;
			}
			if(Input.isKeyPressed(Input.KEY_FORWARD)){
				toggles[currentButton].focus();
				Input.recieved();
			}
			
		}
	}

	protected void updateUI(){
		for(int i = 0; i<toggles.length; i++){
			float alpha = 0.5f;
			if(i == currentButton){
				alpha = 1;
			}
			
			float[] RGBA = {1,0,0,alpha};
			if(Settings.toggles.get(command.get(toggles[i].getMessage()))){
				RGBA[0] = 0;
				RGBA[1] = 1;
			}
			toggles[i].setColour(RGBA);
		}
	}

	public void run(int ID) {
		for(Button b: toggles){
			if(ID == b.getID() && b.hasFocus()){
				Settings.issueCommand("toggle " + command.get(b.getMessage()));
				b.unfocus();
			}
		}		
	}
	
}
