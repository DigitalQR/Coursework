package Control.Visual.Stage;

import Control.Settings;
import Control.Input.Gamepad;
import Control.Visual.Font;
import Control.Visual.Menu.Button2f;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class GamepadSetupStage extends Stage{
	private Button2f[] ControllerButton;
	private Button2f[] Controller;
	private Button2f[] ControllerTask;
	private Font font;
	
	private int currentTask = 0;
	
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
		Model model = ControllerButton[0].getModel();
		model.setTexture(MenuStage.GreyButton);
		Renderer.render(model);
		
		//Text
		font.drawText(ControllerButton[0].getMessage(), ControllerButton[0].getTextLocation(), 0.07f, 8f);
		
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
				task.setTexture(MenuStage.GreenButtonSelected);
			}else{
				task.setTexture(MenuStage.GreenButton);
			}
			Renderer.render(task);
			
			//Text
			font.drawText(ControllerTask[i].getMessage(), ControllerTask[i].getTextLocation(), 0.03f, 7f);
		}
	}

	public void prepare(){
	}

}
