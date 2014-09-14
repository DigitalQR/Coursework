package Control.Visual.Stage;

import Control.Settings;
import Control.Input.Gamepad;
import Control.Visual.Font;
import Control.Visual.Menu.Button2f;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class GamepadSetupStage extends Stage{
	private Button2f[] ControllerButton;
	private Button2f[] Controller;
	private Button2f[] ControllerTask;
	private Font font;
	
	private int currentTask = 0;
	private int assign = -1;
	
	public void prepare(Font font){
		Gamepad[] pad = Gamepad.getGamepads();
		Controller = new Button2f[pad.length];
		ControllerTask = new Button2f[pad.length];
		
		this.font = font;
		
		for(int i = 0; i<pad.length; i++){
			Controller[i] = new Button2f(new Vector2f(55, 90-15*i), new Vector2f(60, 10), pad[i].getName());

			ControllerTask[i] = new Button2f(new Vector2f(120, 90-15*i), new Vector2f(30, 10), "Create \nProfile");
		}
		
		ControllerButton = new Button2f[1];
		
		ControllerButton[0] = new Button2f(new Vector2f(55,105), new Vector2f(95, 10), "Controller Setup");
	}

	public void update(){
		MenuStage.locked = true;
		
		if(Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_BACK) && assign == -1){
			MenuStage.locked = false;
		}
		
		if(assign == -1){
			//Main Text
			Model model = ControllerButton[0].getModel();
			model.setTexture(MenuStage.GreyButton);
			Renderer.render(model);
			font.drawText(ControllerButton[0].getMessage(), ControllerButton[0].getTextLocation(), 0.07f, 8f);
			
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_SELECT)){
				assign = currentTask;
				MenuStage.input();
				Gamepad.Pad[assign].startBinding();
			}
				
			//Button movement
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_UP)){
				currentTask--;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_DOWN)){
				currentTask++;
				MenuStage.input();
			}
			if(currentTask < 0){
				currentTask = Controller.length-1;
			}
			if(currentTask > Controller.length-1){
				currentTask = 0;
			}
			
			//Draw buttons
			for(int i = 0; i<Controller.length; i++){
				//Controller names
				Model control = Controller[i].getModel();
				control.setTexture(MenuStage.GreyButton);
				Renderer.render(control);
				
				//Text
				font.drawText(Controller[i].getMessage(), Controller[i].getTextLocation(), 0.04f, 7f);

				//Task
				Model task = ControllerTask[i].getModel();
				if(currentTask == i){
					if(Gamepad.Pad[i].doesProfileExist){
						task.setTexture(MenuStage.RedButtonSelected);
					}else{
						task.setTexture(MenuStage.GreenButtonSelected);
					}
				}else{
					if(Gamepad.Pad[i].doesProfileExist){
						task.setTexture(MenuStage.RedButton);
					}else{
						task.setTexture(MenuStage.GreenButton);
					}
				}
				Renderer.render(task);
				
				//Text
				font.drawText(ControllerTask[i].getMessage(), ControllerTask[i].getTextLocation(), 0.03f, 7f);
			}
		}else{
			drawAssignment();
		}
		
	}
	
	public void prepare(){
	}
	
	private void drawAssignment(){
		//Name Text
		Model model = ControllerButton[0].getModel();
		model.setTexture(MenuStage.GreyButton);
		Renderer.render(model);
		font.drawText(Controller[assign].getMessage(), ControllerButton[0].getTextLocation(), 0.07f, 8f);
		
		String current = Gamepad.Pad[assign].getCurrentBinding();
		
		if(!current.equals("DONE")){
			font.drawText("Press\n" + current, new Vector3f(5.5f, 9, 0), 0.07f, 8f);
		}else{
			Settings.User[0].setControlScheme(Gamepad.Pad[assign].getGPID());
			Gamepad.Pad[assign].doesProfileExist = true;
			ControllerTask[assign].setMessage("Edit\nProfile");
			assign = -1;
		}
		
	}

}
