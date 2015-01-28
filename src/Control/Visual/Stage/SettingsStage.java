package Control.Visual.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.Slider;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Action;
import Control.Visual.Menu.Assets.Core.Component;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class SettingsStage extends Stage implements Action{
	
	private ArrayList<Component> comp;
	private int currentButton = 0;
	private String[] names = {
			"Low Settings",
			"LERP"
	};
	private HashMap<String, String> command = new HashMap<String, String>();
	private Slider volume;
	
	public SettingsStage(){
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Settings", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		comp = new ArrayList<Component>();

		command.put(names[0], "s_low_settings");
		command.put(names[1], "s_lerp");
		
		int i = 0;
		volume = new Slider(new Vector3f(-1.6f, 0.6f-0.4f*i, -2.1f), new Vector3f(2.4f, 0.3f, 0.1f));
		volume.setValue(Settings.floats.get("s_volume"));
		this.add(volume);
		comp.add(volume);

		Button b1 = new Button(new Vector3f(-1.6f, 0.6f-0.4f*i, -2.5f), new Vector3f(1.2f, 0.3f, 0.5f), "Volume");
		b1.setTextSize(0.2f);
		b1.setColour(new float[]{1,1,1,0});
		this.add(b1);
		i++;
		
		for(String s: names){
			Button b= new Button(new Vector3f(-1.2f, 0.6f-0.4f*i, -2.5f), new Vector3f(1.2f, 0.3f, 0.5f), s);
			b.setTextSize(0.2f);
			b.setAction(this);
			this.add(b);
			comp.add(b);
			i++;
		}
		
		if(new File("Res/s1.pref").exists()){
			Scanner prefScan;
			try {
				prefScan = new Scanner(new File("Res/s1.pref"));
				String data = "";
				
				while(prefScan.hasNext()) data+= prefScan.next();
				prefScan.close();
				
				String[] val = data.split(",");
				Settings.issueCommand("set s_volume " + val[0]);
				volume.setValue(Float.parseFloat(val[0]));
				
				for(int n = 1; n<comp.size(); n++){
					Button b = (Button) comp.get(n);
					if(Settings.toggles.get(command.get(b.getMessage())) != Boolean.parseBoolean(val[n])){
						Settings.issueCommand("toggle " + command.get(b.getMessage()));
					}
				}
				
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(Exception e){
				
			}
			
		}
	} 
	
	protected void updateInfo() {
		if(this.hasFocus()){
			
			//Back to menu
			if(Input.isKeyPressed(Input.KEY_BACK)){
				Stage.setStage(Stage.getStage("menu"));
				Input.recieved();
				
				String data = "" + volume.getValue();
				for(int i = 1; i<comp.size(); i++){
					Button b = (Button) comp.get(i);
					boolean val = Settings.toggles.get(command.get(b.getMessage()));	
					data += "," + val;
				}
				
				try{
					Formatter pref = new Formatter("Res/s1.pref");
					pref.format("%s", data);
					pref.close();
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
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
				currentButton = comp.size()-1;
			}
			if(currentButton > comp.size()-1){
				currentButton = 0;
			}			
			
			if(currentButton == 0){

				float value = 0;
				if(Input.isKeyPressed(Input.KEY_LEFT)){
					value = -0.05f;
					Input.recieved();
				}
				if(Input.isKeyPressed(Input.KEY_RIGHT)){
					value = 0.05f;
					Input.recieved();
				}
				volume.setValue(Math.max(0, Math.min(volume.getValue()+value, 1)));
				Settings.issueCommand("set s_volume " + volume.getValue());
			}
			
			if(Input.isKeyPressed(Input.KEY_FORWARD)){
				comp.get(currentButton).focus();
				Input.recieved();
			}
		}
		
		if(comp.get(0).hasFocus()){
			comp.get(0).unfocus();
		}
	}

	protected void updateUI(){
		for(int i = 0; i<comp.size(); i++){
			float alpha = 0.5f;
			if(i == currentButton){
				alpha = 1;
			}
			boolean val = false;
			float[] RGBA = {1,0,0,alpha};
			switch(i){
			case 1:
				Button b = (Button) comp.get(i);
				val = Settings.toggles.get(command.get(b.getMessage()));				
				break;
			case 2:
				Button b1 = (Button) comp.get(i);
				val = Settings.toggles.get(command.get(b1.getMessage()));
				break;
			case 0:
				RGBA[1] = 1;
				RGBA[2] = 1;
				break;
			}
			
			
			if(val){
				RGBA[0] = 0;
				RGBA[1] = 1;
			}
			comp.get(i).setColour(new float[]{1,1,1,alpha});
			
			if(i > 0){
				float[] rgba = RGBA.clone();
				rgba[3] =1;
				Button col = new Button(new Vector3f(-1.6f, 0.6f-0.4f*i, -2.5f), new Vector3f(0.3f, 0.3f, 0.5f), "");
				col.setColour(rgba);
				col.draw();
			}
		}
	}

	public void run(int ID) {
		for(int i = 1; i<3; i++){
			if(ID == comp.get(i).getID() && comp.get(i).hasFocus()){
				Button b = (Button) comp.get(i);
				Settings.issueCommand("toggle " + command.get(b.getMessage()));
				comp.get(i).unfocus();
			}
		}
		
	}
	
}
