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
	
	public GamepadSetupStage(Font font){
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
		
		if(Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_BACK) && assign == -1){
			MenuStage.locked = false;
		}
		
		if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_SELECT)){
			if(assign != -2 && (!Gamepad.Pad[currentButton].getProfileStatus())){				
				assign = currentButton;
				MenuStage.input();
				Gamepad.Pad[assign].startBinding();
				
			}else if(currentTask == TASK_ASSIGN){
				assign = -2;
				currentTask = 0;
				MenuStage.locked = false;
				MenuStage.input();
			}else if(currentTask == TASK_DELETE){
				Gamepad.Pad[currentButton].deleteProfileStatus();
				currentTask = 0;
				MenuStage.input();
			}
		}
		
		if(assign == -1){
			
			//Main Text
			Model model = Header.getModel();
			model.setTexture(MenuStage.Button);
			Renderer.render(model);
			font.drawText(Header.getMessage(), Header.getTextLocation(), 0.07f, 8f);
				
			//Button movement
			if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_UP)){
				currentButton--;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_DOWN)){
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
			if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_RIGHT)){
				currentTask++;
				MenuStage.input();
			}
			if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_LEFT)){
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
				control.setTexture(MenuStage.Button);
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
							task.setTexture(MenuStage.ButtonSelected);
							task.setRGBA(1f, 0.2f, 0.2f, 1f);
						}else{
							task.setTexture(MenuStage.ButtonSelected);
						}
					}else{
						task.setTexture(MenuStage.ButtonSelected);
						task.setRGBA(0.2f, 1f, 0.2f, 1f);
					}
				}else{
					if(Gamepad.Pad[i].getProfileStatus()){
						if(current ==  TASK_DELETE){
							task.setTexture(MenuStage.Button);
							task.setRGBA(1f, 0.2f, 0.2f, 1f);
						}else{
							task.setTexture(MenuStage.Button);
						}
					}else{
						task.setTexture(MenuStage.Button);
						task.setRGBA(0.2f, 1f, 0.2f, 1f);
					}
				}
				Renderer.render(task);
				
				//Arrows
				if(Gamepad.Pad[i].getProfileStatus()){

					Model leftArrow = Arrow[i][0].getModel();
					leftArrow.setTexture(MenuStage.Button);
					
					Vector3f Lloc = Arrow[i][0].getTextLocation();
					Lloc.z-=0.1f;
					font.drawText(Arrow[i][0].getMessage(), Lloc, 0.05f, 7);
					Renderer.render(leftArrow);

					Model rightArrow = Arrow[i][1].getModel();
					rightArrow.setTexture(MenuStage.Button);

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
			Assign();			
		}else{
			drawAssignment();
		}
		
	}
	
	public void prepare(){
	}
	
	private int currentPlayer = 0;
	private void Assign(){
		if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_LEFT)){
			currentPlayer--;
			MenuStage.input();
		}
		if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_RIGHT)){
			currentPlayer++;
			MenuStage.input();
		}
		
		if(currentPlayer < 0){
			currentPlayer = Settings.User.size();
		}
		if(currentPlayer >= Settings.User.size()){
			currentPlayer = 0;
		}
		
		font.drawText("Assign To Player:\n" + currentPlayer, new Vector3f(5.5f, 9, 0), 0.04f, 8f);
		
		if(MenuStage.timePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_SELECT)){
			Settings.User.get(currentPlayer).setControlScheme(Gamepad.Pad[currentButton].getGPID());
			Gamepad.Pad[currentButton].assignToPlayer(currentPlayer);
			MenuStage.input();
			
			currentPlayer = 0;
			currentTask = 0;
			currentButton = 0;
			
			assign = -1;
		}
	}
	
	private void drawAssignment(){
		//Name Text
		Model model = Header.getModel();
		model.setTexture(MenuStage.Button);
		Renderer.render(model);
		font.drawText(Controller[assign].getMessage(), Header.getTextLocation(), 0.07f, 8f);
		
		String current = Gamepad.Pad[assign].getCurrentBinding();
		
		if(!current.equals("DONE")){
			font.drawText("Press:\n" + current, new Vector3f(5.5f, 9, 0), 0.07f, 8f);
		}else{
			assign = -1;
		}
		
	}

	public void switchToUpdate(){
	}

	public void switchFromUpdate(){
	}

}
