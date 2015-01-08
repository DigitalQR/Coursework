package Control.Visual.Stage;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Settings;
import Control.Input.Gamepad;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Action;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class GamepadSetupStage extends Stage implements Action{

	private Vector3f currentFocus = new Vector3f(0,0,0);
	private Button[] button;
	private Button[] subButton = new Button[3];
	private TextBox header;
	private TextBox playerName;
	private Animation playerModel = new Animation("Cube/Spin", 500);
	private int currentPad = 0;
	private int currentButton = 0;
	
	public GamepadSetupStage(){
		super();

		header = new TextBox(new Vector3f(-0.3f,-0.7f,-2.5f),new Vector3f(1.9f,2f,0.5f), "Gamepad Setup", "Select a controller to \ncontinue.");
		header.setHeaderTextSize(0.07f);
		header.setContentColour(new float[]{1,1,1,0.5f});
		this.add(header);
		
		playerName = new TextBox(new Vector3f(0.9f,0.2f,-2.5f),new Vector3f(0.1f,0.1f,0.5f), "1", null);
		playerName.setHeaderHeight(1);
		playerName.setHeaderTextSize(0.7f);
		playerName.setHeaderColour(new float[]{1,1,1,0f});
		playerName.setDrawn(false);
		this.add(playerName);
		
		String[] names = {"Create profile", "Assign to player 1" , "Delete profile"};
		for(int i = 0; i<names.length; i++){
			subButton[i] = new Button(new Vector3f(-0.3f,0.5f-0.4f*i,-2.5f), new Vector3f(1.2f, 0.3f, 0.5f), names[i]);
			
			subButton[i].setTextSize(0.2f);
			subButton[i].setAction(this);
			this.add(subButton[i]);
		}
		
		
		button = new Button[Gamepad.getGamepads().length];
		int i = 0;
		for(Gamepad p: Gamepad.getGamepads()){
			button[i] = new Button(new Vector3f(0,0,0), new Vector3f(1.0f, 0.3f, 0.5f), p.getName());
			button[i].setTextSize(0.15f);
			this.add(button[i]);
			i++;
		}
		
	}
	
	public Vector3f getCameraFocus(){
		return currentFocus;
	}
	
	protected void updateInfo(){
		if(this.hasFocus()){
			header.enableContent();
			
			//Back to menu
			if(Input.isKeyPressed(Input.KEY_BACK)){
				Stage.setStage(Stage.getStage("menu"));
				Input.recieved();
			}
			
			//Button process
			if(Input.isKeyPressed(Input.KEY_UP)){
				currentPad--;
				Input.recieved();
			}if(Input.isKeyPressed(Input.KEY_DOWN)){
				currentPad++;
				Input.recieved();
			}
			if(currentPad < 0){
				currentPad = button.length-1;
			}
			if(currentPad > button.length-1){
				currentPad = 0;
			}
			if(Input.isKeyPressed(Input.KEY_FORWARD)){
				button[currentPad].focus();
				currentButton = 0;
				Input.recieved();
			}
		}else{
			header.disableContent();
		}

		
		for(int i = 0; i<button.length; i++){
			//Button colour
			if(this.hasFocus()){
				if(i == currentPad){
					button[i].setColour(new float[]{1,1,1,1});
				}else{
					button[i].setColour(new float[]{1,1,1,0.5f});
				}
				
				button[i].setLocation(new Vector3f(-1.6f, 0.2f-0.4f*(i-currentPad), -2.5f));
				
				for(Button b: subButton){
					b.setDrawn(false);
				}
			}else{
				if(button[i].hasFocus()){
					button[i].setLocation(new Vector3f(-1.6f, 0.2f-0.4f*(i-i), -2.5f));
				}
			}
			//Button submenu
			if(button[i].hasFocus()){
				Gamepad pad = Gamepad.getGamepads()[i];
				List<Integer> buttons = new ArrayList<Integer>();
				
				if(pad.getProfileStatus()){
					buttons.add(1);
					buttons.add(2);
				}else{
					buttons.add(0);
				}
				
				
				//Button process
				currentButton = buttons.indexOf(currentButton);
				if(Input.isKeyPressed(Input.KEY_UP)){
					currentButton--;
					Input.recieved();
				}if(Input.isKeyPressed(Input.KEY_DOWN)){
					currentButton++;
					Input.recieved();
				}
				if(currentButton < 0){
					currentButton = buttons.size()-1;
				}
				if(currentButton > buttons.size()-1){
					currentButton = 0;
				}
				currentButton = buttons.get(currentButton);
				if(Input.isKeyPressed(Input.KEY_FORWARD)){
					subButton[currentButton].focus();
					playerName.setDrawn(true);
					
					if(currentButton == 0){
						Gamepad.getGamepads()[currentPad].startBinding();
					}
					currentButton = 0;
					Input.recieved();
				}
				
				for(int n = 0; n<subButton.length; n++){
					subButton[n].setDrawn(true);
					if(n == currentButton){
						subButton[n].setColour(new float[]{1,1,1,1});
					}else{
						subButton[n].setColour(new float[]{1,1,1,0.5f});
					}
				}
				
				if(!pad.getProfileStatus()){
					float[] RGBA = subButton[1].getColour();
					RGBA[3] = 0.2f;
					subButton[1].setColour(RGBA);
					
					RGBA = subButton[2].getColour();
					RGBA[3] = 0.2f;
					subButton[2].setColour(RGBA);
				}else{
					float[] RGBA = subButton[0].getColour();
					RGBA[3] = 0.2f;
					subButton[0].setColour(RGBA);
				}
				
				if(Input.isKeyPressed(Input.KEY_BACK)){
					button[i].unfocus();
					currentButton = i;
					Input.recieved();
				}
			}
		}
		
		for(int i = 1; i<subButton.length; i++){
			if(subButton[i].hasFocus() && Input.isKeyPressed(Input.KEY_BACK)){
				subButton[i].unfocus();
				currentButton = 0;
				playerName.setDrawn(false);
				Input.recieved();
			}
		}
		
		//Create
		if(subButton[0].hasFocus()){
			String text = Gamepad.getGamepads()[currentPad].getCurrentBinding();
			
			if(!text.equals("DONE")){
				playerName.setHeader("Press:\n" + text);
			}else{
				subButton[0].unfocus();
				currentButton = 0;
				playerName.setDrawn(false);
				Input.recieved();
			}
			
		}
		
		//Assign
		if(subButton[1].hasFocus()){
			Settings.User.get(0).setControlScheme(Gamepad.getGamepads()[currentPad].getGPID());
			subButton[1].unfocus();
			playerName.setDrawn(false);
		}
		
		//Delete
		if(subButton[2].hasFocus()){
			Gamepad.getGamepads()[currentPad].deleteProfileStatus();
			subButton[2].unfocus();
			currentButton = 0;
			playerName.setDrawn(false);
			
		}
	}

	protected void updateUI(){
		for(int i = 0; i<button.length; i++){
			//Draw players
			Gamepad pad = Gamepad.getGamepads()[i];

			float[] RGBA = {0.5f, 0.5f, 0.5f, 1f};
			
			try{
				if(Gamepad.getGamepads()[i].getGPID() == Settings.User.get(0).getControlScheme().getGPID()){
					RGBA = Camera.getInverseRGBA();
				}
			}catch(IndexOutOfBoundsException e){
				
			}
			
			drawPlayerAt(new Vector3f(-0.5f, button[i].getLocation().y-0.05f,-2.3f), RGBA, pad.getProfileStatus(), 0.5f);
			
		}
		
	}

	private void drawPlayerAt(Vector3f location, float[] RGBA, boolean fill, float scale){
		location.x -= Camera.getLERPLocation().x;
		location.y -= Camera.getLERPLocation().y;
		location.z -= Camera.getLERPLocation().z;
		
		Stencil.enable();
		
		//Outline
		GL11.glDisable(GL11.GL_LIGHTING);

		Model m = playerModel.getCurrentFrame();
		m.setLocation(location.clone());
		m.setRGBA(0, 0, 0, 1);
		m.getLocation().y-=0.24f*scale;
		m.scaleBy(1.6f*scale*6);
		Renderer.render(m);		
		
		Model m1 = playerModel.getCurrentFrame();
		m1.setLocation(location.clone());
		m1.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
		m1.getLocation().y-=0.2f*scale;
		m1.scaleBy(1.5f*scale*6);
		Renderer.render(m1);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		Stencil.cylce();

		if(fill){
			Model m2 = playerModel.getCurrentFrame();
			m2.setLocation(location.clone());
			m2.scaleBy(scale*6);
			Renderer.render(m2);
		}
		
		Stencil.disable();
	}
	
	public void run(int ID){
	}

}
