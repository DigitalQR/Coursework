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
	private Button2f Header;
	private Button2f[] Controller;
	private Button2f[][] Arrow;
	private Button2f[][] ControllerTask;
	private Font font;

	private int currentButton = 0;
	private int currentTask = 0;
	private int assign = -1;
	
	private final int TASK_UNASSIGNED = 0, TASK_DELETE = 1, TASK_ASSIGN = 2;
	
	public void prepare(Font font){
		Gamepad[] pad = Gamepad.getGamepads();
		Controller = new Button2f[pad.length];
		ControllerTask = new Button2f[3][pad.length];
		Arrow = new Button2f[pad.length][2];
		
		this.font = font;
		
		for(int i = 0; i<pad.length; i++){
			Controller[i] = new Button2f(new Vector2f(55, 90-15*i), new Vector2f(60, 10), pad[i].getName());
			
			ControllerTask[TASK_UNASSIGNED][i] = new Button2f(new Vector2f(120, 90-15*i), new Vector2f(30, 10), "Create \nProfile");

			Arrow[i][0] = new Button2f(new Vector2f(120, 90-15*i), new Vector2f(4, 10), "<");
			Arrow[i][1] = new Button2f(new Vector2f(148, 90-15*i), new Vector2f(4, 10), ">");
			ControllerTask[TASK_DELETE][i] = new Button2f(new Vector2f(125, 90-15*i), new Vector2f(20, 10), "Delete \nProfile");
			ControllerTask[TASK_ASSIGN][i] = new Button2f(new Vector2f(125, 90-15*i), new Vector2f(20, 10), "Assign \nProfile");
		}
		
		Header = new Button2f(new Vector2f(55,105), new Vector2f(95, 10), "Controller Setup");
	}

	public void update(){
		MenuStage.locked = true;
		
		if(Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_BACK) && assign == -1){
			MenuStage.locked = false;
		}
		
		if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_SELECT)){
			if(assign != -2 && (!Gamepad.Pad[currentButton].getProfileStatus())){				
				assign = currentButton;
				MenuStage.input();
				Gamepad.Pad[assign].startBinding();
				
			}else if(currentTask == TASK_ASSIGN){
				assign = -2;
				currentTask = 0;
				MenuStage.input();
			}else if(currentTask == TASK_DELETE){
				Gamepad.Pad[currentButton].deleteProfileStatus();
				MenuStage.input();
			}
		}
		
		if(assign == -1){
			
			//Main Text
			Model model = Header.getModel();
			model.setTexture(MenuStage.GreyButton);
			Renderer.render(model);
			font.drawText(Header.getMessage(), Header.getTextLocation(), 0.07f, 8f);
				
			//Button movement
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_UP)){
				currentButton--;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_DOWN)){
				currentButton++;
				MenuStage.input();
			}
			if(currentButton < 0){
				currentButton = Controller.length-1;
			}
			if(currentButton > Controller.length-1){
				currentButton = 0;
			}
			
			//Task button movement
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_RIGHT)){
				currentTask++;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_LEFT)){
				currentTask--;
				MenuStage.input();
			}
			if(currentTask < 1){
				currentTask = 2;
			}
			if(currentTask > 2){
				currentTask = 1;
			}
			
			//Draw buttons
			for(int i = 0; i<Controller.length; i++){
				//Controller names
				Model control = Controller[i].getModel();
				control.setTexture(MenuStage.GreyButton);
				Renderer.render(control);
				
				//Text
				String Player = "Unassigned";
				if(Gamepad.Pad[i].assignedPlayer != -1){
					Player = "" + Gamepad.Pad[i].assignedPlayer;
				}
				Vector3f loc = Controller[i].getTextLocation();
				loc.y+=0.1f;
				font.drawText(Player + ":\n" +Controller[i].getMessage(), loc, 0.03f, 7f);

				//Task
				int current = TASK_UNASSIGNED;
				if(Gamepad.Pad[i].getProfileStatus()){
					current = currentTask;
				}
				
				Model task = ControllerTask[current][i].getModel();
				if(currentButton == i){
					if(Gamepad.Pad[i].getProfileStatus()){						
						if(current ==  TASK_DELETE){
							task.setTexture(MenuStage.RedButtonSelected);
						}else{
							task.setTexture(MenuStage.GreyButtonSelected);
						}
					}else{
						task.setTexture(MenuStage.GreenButtonSelected);
					}
				}else{
					if(Gamepad.Pad[i].getProfileStatus()){
						if(current ==  TASK_DELETE){
							task.setTexture(MenuStage.RedButton);
						}else{
							task.setTexture(MenuStage.GreyButton);
						}
					}else{
						task.setTexture(MenuStage.GreenButton);
					}
				}
				Renderer.render(task);
				
				//Arrows
				if(Gamepad.Pad[i].getProfileStatus()){

					Model leftArrow = Arrow[i][0].getModel();
					leftArrow.setTexture(MenuStage.GreyButton);
					
					Vector3f Lloc = Arrow[i][0].getTextLocation();
					Lloc.z-=0.1f;
					font.drawText(Arrow[i][0].getMessage(), Lloc, 0.05f, 7);
					Renderer.render(leftArrow);

					Model rightArrow = Arrow[i][1].getModel();
					rightArrow.setTexture(MenuStage.GreyButton);

					Vector3f Rloc = Arrow[i][1].getTextLocation();
					Rloc.z-=0.1f;
					font.drawText(Arrow[i][1].getMessage(), Rloc, 0.05f, 7);
					Renderer.render(rightArrow);
				}
				
				//Text
				font.drawText(ControllerTask[current][i].getMessage(), ControllerTask[current][i].getTextLocation(), 0.03f, 7f);
			}
			
		//Assign controller to player
		}else if(assign == -2){
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_LEFT)){
				currentTask--;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_RIGHT)){
				currentTask++;
				MenuStage.input();
			}
			
			if(currentTask < 0){
				currentTask = Settings.User.length;
			}
			if(currentTask >= Settings.User.length){
				currentTask = 0;
			}
			
			font.drawText("Assign To Player:\n" + currentTask, new Vector3f(5.5f, 9, 0), 0.04f, 8f);
			
			if(MenuStage.timePassed() && Settings.User[0].isKeyPressed(Settings.User[0].KEY_MENU_SELECT)){
				Settings.User[currentTask].setControlScheme(Gamepad.Pad[currentButton].getGPID());
				Gamepad.Pad[currentButton].assignedPlayer = currentTask;
				MenuStage.input();
				
				currentTask = 0;
				currentButton = 0;
				
				assign = -1;
			}
			
		}else{
			drawAssignment();
		}
		
	}
	
	public void prepare(){
	}
	
	private void drawAssignment(){
		//Name Text
		Model model = Header.getModel();
		model.setTexture(MenuStage.GreyButton);
		Renderer.render(model);
		font.drawText(Controller[assign].getMessage(), Header.getTextLocation(), 0.07f, 8f);
		
		String current = Gamepad.Pad[assign].getCurrentBinding();
		
		if(!current.equals("DONE")){
			font.drawText("Press:\n" + current, new Vector3f(5.5f, 9, 0), 0.07f, 8f);
		}else{
			assign = -1;
		}
		
	}

}
