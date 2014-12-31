package Control.Visual.Stage.Core;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Visual.Menu.Assets.Core.Component;
import Control.Visual.Menu.Assets.Core.FocusableItem;
import Control.Visual.Stage.ConnectStage;
import Control.Visual.Stage.GamepadSetupStage;
import Control.Visual.Stage.MenuStage;
import Control.Visual.Stage.OverworldStage;
import Control.Visual.Stage.ServerStage;
import Control.Visual.Stage.SettingsStage;
import Control.Visual.Stage.StartStage;

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
		SettingsStage settings = new SettingsStage();
		StartStage start = new StartStage();
		ConnectStage connect = new ConnectStage();
		ServerStage server = new ServerStage();

		stageID.put("menu", menu.ID);
		stageID.put("overworld", over.ID);
		stageID.put("gamepadsetup", gamepad.ID);
		stageID.put("settings", settings.ID);
		stageID.put("start", start.ID);
		stageID.put("connect", connect.ID);
		stageID.put("server", server.ID);
		
		for(int i = 0; i<stageID.size(); i++){
			System.out.println(stageID.keySet().toArray()[i] + " " + stageID.get(stageID.keySet().toArray()[i]));
		}
		
		menu.setAsCurrentStage();
	}
	
	public static void updateCurrentStage(){
		@SuppressWarnings("unchecked")
		ArrayList<Stage> st = (ArrayList<Stage>) stage.clone();
		for(Stage s: st){
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
	
	public static int getCurrentStageID(){
		return currentStage;
	}
	
	public static Stage getStage(String name){
		return getStage(stageID.get(name));
	}
	
	public static int getStageID(String name){
		return stageID.get(name);
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
		try{
			for(Component c: comp){
				c.draw();
			}
		}catch(ConcurrentModificationException e){}
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
