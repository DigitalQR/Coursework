package Control.Visual.Stage.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Visual.Menu.Assets.Core.Component;
import Control.Visual.Menu.Assets.Core.FocusableItem;
import Control.Visual.Stage.GamepadSetupStage;
import Control.Visual.Stage.MenuStage;
import Control.Visual.Stage.OverworldStage;

public abstract class Stage extends FocusableItem{
	
	private static ArrayList<Stage> stage = new ArrayList<Stage>();
	public static HashMap<String, Integer> stageID = new HashMap<String, Integer>();
	private static int IDTrack = 0;
	private static int currentStage = -1;
	
	public static void setStage(Stage s){
		currentStage = s.ID;
		getStage(s.ID).focus();
	}
	
	public static void setupStages(){
		MenuStage menu = new MenuStage();
		OverworldStage over = new OverworldStage();
		GamepadSetupStage gamepad = new GamepadSetupStage();

		stageID.put("menu", menu.ID);
		stageID.put("overworld", over.ID);
		stageID.put("gamepadsetup", gamepad.ID);
		
		menu.setAsCurrentStage();
	}
	
	public static void updateCurrentStage(){
		for(Stage s: stage){
			if(s.ID == currentStage){
				s.update();
			}
		}
	}
	
	public static boolean isCurrentStage(Stage s){
		return currentStage == s.ID;
	}
	
	public static void drawCurrentStage(){
		for(Stage s: stage){
			if(s.ID == currentStage){
				s.draw();
			}
		}
	}
	
	public static Stage getStage(int ID){
		for(Stage s: stage){
			if(s.ID == ID){
				return s;
			}
		}
		return null;
	}
	
	public static Stage getCurrentStage(){
		for(Stage s: stage){
			if(s.ID == currentStage){
				return s;
			}
		}
		return null;
	}
	
	public static Stage getStage(String name){
		return getStage(stageID.get(name));
	}
	
	protected final int ID = IDTrack++;
	
	public Stage(){
		stage.add(this);
	}
	
	public void setAsCurrentStage(){
		currentStage = ID;
		focus();
	}
	
	private List<Component> comp = new ArrayList<Component>();

	public void add(Component c){
		c.setParent(this);
		comp.add(c);
	}

	public void remove(Component c){
		comp.remove(c);
	}
	
	private void updateItems(){
		for(Component c: comp){
			c.update();
		}
	}
	
	private void drawItems(){
		for(Component c: comp){
			c.draw();
		}
	}
	
	public void update(){
		Camera.process();
		updateItems();
		updateInfo();
	}
	
	public void draw(){
		Camera.processLERP();
		Camera.focus();
		drawItems();
		updateUI();
	}
	
	public boolean isCurrentStage(){
		return currentStage == ID;
	}
	

	public Vector3f getCameraFocus(){
		return new Vector3f(0,0,0);
	}

	
	protected abstract void updateInfo();
	protected abstract void updateUI();
	
}
