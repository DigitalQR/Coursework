package Control.Visual.Stage;

import Control.Settings;
import Control.Input.Gamepad;
import Control.Visual.Menu.Button2f;
import Control.Visual.Menu.Font;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class GamepadSetupStage extends Stage{
	private Button2f[] ControllerButton;
	private Button2f[] Controller;
	private Button2f[] ControllerTask;
	
	private int currentTask = 0;
	
	public void prepare(Font font){
		Gamepad[] pad = Gamepad.getGamepads();
		Controller = new Button2f[pad.length];
		ControllerTask = new Button2f[pad.length];
		
		for(int i = 0; i<pad.length; i++){
			Controller[i] = new Button2f(new Vector2f(55, 90-15*i), new Vector2f(60, 10), pad[i].getName());
			Controller[i].generateText(font, 8f, 30);

			ControllerTask[i] = new Button2f(new Vector2f(120, 90-15*i), new Vector2f(30, 10), "Create Profile");
			ControllerTask[i].generateText(font, 8f, 45);
		}
		
		ControllerButton = new Button2f[1];
		
		ControllerButton[0] = new Button2f(new Vector2f(55,105), new Vector2f(95, 10), "Controller Setup");
		ControllerButton[0].generateText(font, 10f, 30);
	}

	public void update(){
		Model model = ControllerButton[0].getModel();
		model.setTexture(MenuStage.GreyButton);
		Renderer.render(model);
		
		for(Model m:ControllerButton[0].getText()){
			Renderer.render(m);
		}
		
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
			Model control = Controller[i].getModel();
			control.setTexture(MenuStage.GreyButton);
			Renderer.render(control);
			
			Model[] controlText = Controller[i].getText();
			for(Model m:controlText){
				Renderer.render(m);
			}

			Model task = ControllerTask[i].getModel();
			if(currentTask == i){
				task.setTexture(MenuStage.GreenButtonSelected);
			}else{
				task.setTexture(MenuStage.GreenButton);
			}
			Renderer.render(task);
			
			Model[] taskText = ControllerTask[i].getText();
			for(Model m:taskText){
				Renderer.render(m);
			}
		}
	}

	public void prepare(){
	}

}
