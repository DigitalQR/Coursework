package Control.Visual.Stage;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.Core.Action;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class SettingsStage extends Stage implements Action{
	
	private Button[] toggles;
	private int currentButton = 0;
	
	public SettingsStage(){
		toggles = new Button[Settings.toggleNames.size()];
		
		int i = 0;
		for(String s: Settings.toggleNames){
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
			if(Settings.toggles.get(toggles[i].getMessage())){
				RGBA[0] = 0;
				RGBA[1] = 1;
			}
			toggles[i].setColour(RGBA);
		}
	}

	public void run(int ID) {
		for(Button b: toggles){
			if(ID == b.getID() && b.hasFocus()){
				Settings.issueCommand("toggle " + b.getMessage());
				b.unfocus();
			}
		}		
	}
	
}
