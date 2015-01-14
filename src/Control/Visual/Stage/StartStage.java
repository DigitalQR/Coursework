package Control.Visual.Stage;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Settings;
import Control.Input.Gamepad;
import Control.Server.Connection;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.Slider;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Component;
import Control.Visual.Stage.Core.Stage;
import Entities.Player;
import Entities.Tools.ControlScheme;

public class StartStage extends Stage{
	
	private PlayerSelect[] player;
	private ArrayList<Integer> GPIDs;
	private ArrayList<ControlScheme> controls;
	
	public StartStage(){
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Game setup  ", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		
		reset();
	}
	
	public void reset(){

		if(player != null){
			for(int i = 0; i<player.length; i++){
				this.remove(player[i]);
			}
		}
		
		player = new PlayerSelect[8];
		GPIDs = new ArrayList<Integer>();
		controls = new ArrayList<ControlScheme>();
		countDown = 0;
		PlayerSelect.IDTrack = 1;
		
		for(int i = 0; i<4; i++){
			player[i]= new PlayerSelect(new Vector3f(-1.6f+0.83f*i, -0.1f, -2.5f));
			this.add(player[i]);
		}
		
		for(int i = 0; i<4; i++){
			player[4+i] = new PlayerSelect(new Vector3f(-1.6f+0.83f*i, -1.2f, -2.5f));
			this.add(player[4+i]);
		}
	}
	
	private float countDown = 0;
	
	
	protected void updateInfo(){
		
		if(!Settings.isClientActive()){
			for(Gamepad gp: Gamepad.getGamepads()){
				
				if(!GPIDs.contains(gp.getGPID())){
					buttonSearch: for(int i = 0; i<Gamepad.BUTTON_LENGTH; i++){
						if(gp.isButtonPressed(i)){
							
							for(PlayerSelect p: player){
								if(p.getGPID() == -1 && p.getControlScheme() == null){
									p.setGPID(gp.getGPID());
									GPIDs.add(gp.getGPID());
									break buttonSearch;
								}
							}
							
						}
					}
				}
			}
			if(Settings.isHostActive()){
				for(Connection c: Settings.host.getConnections()){
					if(!controls.contains(c.controlScheme) && c.controlScheme.isAnyKeyPressed()){
						
						pl : for(PlayerSelect p: player){
							if(p.getGPID() == -1 && p.getControlScheme() == null){
								p.setControlScheme(c.controlScheme);
								controls.add(c.controlScheme);
								break pl;
							}
						}
						
					}
				}
			}
		}else{
			
		}
		
		
		boolean ready = true;
		int pCount = 0;
		for(PlayerSelect p: player){
			if(p.getGPID() != -1 || p.getControlScheme() != null || p.multiFlag){
				pCount++;
				if(!p.isReady()) ready = false;
			}
		}
		
		if(ready && pCount > 0){
			if(countDown == 0){
				countDown = System.nanoTime()/1000000000;
			}else if((System.nanoTime()/1000000000) - countDown >= 2){
				startGame(pCount);
			}
		}else{
			countDown = 0;
		}
		
		if(Settings.isHostActive()){
			String message = encode();
			Settings.host.addCommand(message);
		}
	}
	
	public String encode(){
		String command = "PS";
		
		for(PlayerSelect p: player){
			boolean flag = (p.getGPID() != -1 || p.getControlScheme() != null);
			command += flag + "," +  p.currentButton +"," + p.r + "," + p.g + "," + p.b + "," + p.isReady() + "]";
		}
		
		return command + ";";
	}
	
	public void decode(String message){
		String[] pl = message.split("]");
		
		for(int i = 0; i<pl.length; i++){
			String[] part = pl[i].split(",");
			
			if(player[i] != null){
				player[i].multiFlag = Boolean.parseBoolean(part[0]);
				player[i].currentButton = (int)Float.parseFloat(part[1]);
				player[i].r = Float.parseFloat(part[2]);
				player[i].g = Float.parseFloat(part[3]);
				player[i].b = Float.parseFloat(part[4]);
				player[i].ready = Boolean.parseBoolean(part[5]);
			}
		}
	}
	
	private void startGame(int playerCount){
		Settings.issueCommand("set player_count " + playerCount);
		
		int track = 0;
		for(PlayerSelect p: player){
			if(p.getGPID() != -1){
				Player player = Settings.User.get(track);
				player.setControlScheme(p.getGPID());
				player.setRGBA(p.getRGBA());
				
				track++;
			}
			if(p.getControlScheme() != null){
				Player player = Settings.User.get(track);
				player.setControlScheme(p.getControlScheme());
				player.setRGBA(p.getRGBA());
				
				for(Connection c: Settings.host.getConnections()){
					if(c.controlScheme.equals(p.getControlScheme())){
						c.setPlayer(player);
					}
				}
				
				track++;
			}
		}
		
		
		OverworldStage overworld = (OverworldStage) Stage.getStage("overworld");
		Stage.setStage(overworld);
		reset();
	}
	
	protected void updateUI(){
		
	}
	
}

class PlayerSelect extends Component{
	
	protected static int IDTrack = 1;
	private final int ID = IDTrack++;
		
	private Slider red = new Slider(new Vector3f(location.x+0.28f, location.y+0.6f, location.z+0.17f), new Vector3f(0.4f,0.5f,0.1f));
	private Slider green = new Slider(new Vector3f(location.x+0.28f, location.y+0.45f, location.z+0.17f), new Vector3f(0.4f,0.5f,0.1f));
	private Slider blue = new Slider(new Vector3f(location.x+0.28f, location.y+0.3f, location.z+0.17f), new Vector3f(0.4f,0.5f,0.1f));
	
	public float r = (float)(Math.random()), g = (float)(Math.random()), b = (float)(Math.random());
	
	private Animation playerModel = new Animation("Cube/Spin", 500);
	private int GPID  = -1;
	private ControlScheme control = null;
	boolean ready = false;
	boolean multiFlag = false;
	
	int currentButton = 0;
	
	public PlayerSelect(Vector3f location){
		super(location, new Vector3f(0.7f, 0.9f, 0.25f));
	}
	
	private long lastInput = 0;
	private int inputDelay = 100;
	
	public boolean hasTimePassed(){
		long current = System.nanoTime()/1000000;
		if((current-lastInput) > inputDelay){
			return true;
		}
		return false;
	}

	public void recieved(){
		lastInput = System.nanoTime()/1000000;	
	}
	
	public void reset(){
		GPID  = -1;
		control = null;
		ready = false;
		multiFlag = false;
	}
	
	public float[] getRGBA(){
		return new float[]{r,g,b,1};
	}
	
	public int getGPID() {
		return GPID;
	}

	public boolean isReady() {
		return ready;
	}

	public void setGPID(int gPID) {
		GPID = gPID;
		control = null;
	}
	
	public void setControlScheme(ControlScheme s){
		control = s;
		GPID = -1;
	}
	
	public ControlScheme getControlScheme(){
		return control;
	}
	
	private boolean isButtonPressed(int ID){
		if(GPID == -1){
			return control.isKeyPressed(ID);
		}else{
			return Gamepad.getGamepad(GPID).isButtonPressed(ID);
		}
	}

	protected void process(){
		if(!Settings.isClientActive()){
			if( (GPID != -1 || control != null) && !ready){
				int UP, DOWN, LEFT, RIGHT, FORWARD;
				UP = Gamepad.BUTTON_UP;
				DOWN = Gamepad.BUTTON_DOWN;
				LEFT = Gamepad.BUTTON_LEFT;
				RIGHT = Gamepad.BUTTON_RIGHT;
				FORWARD = Gamepad.BUTTON_MENU_FORWARD;
				
				if(GPID == -1){
					UP = control.KEY_UP;
					DOWN = control.KEY_DOWN;
					LEFT = control.KEY_LEFT;
					RIGHT = control.KEY_RIGHT;
					FORWARD = control.KEY_SELECT;
				}
				
				if(isButtonPressed(UP) && hasTimePassed()){
					currentButton--;
					recieved();
				}if(isButtonPressed(DOWN) && hasTimePassed()){
					currentButton++;
					recieved();
				}
				
				float val = 0;
				if(isButtonPressed(LEFT) && hasTimePassed()){
					val = -0.1f;
					recieved();
				}if(isButtonPressed(RIGHT) && hasTimePassed()){
					val = 0.1f;
					recieved();
				}
				
				
				if(currentButton < 0){
					currentButton = 0;
				}
				if(currentButton > 3){
					currentButton = 3;
				}
	
				if(currentButton == 3 && isButtonPressed(FORWARD) && hasTimePassed()){
					ready = true;
					recieved();
				}
		
				switch(currentButton){
				case 0://Red
					r+=val;
					r = Math.max(0, Math.min(r, 1));
					break;
				case 1://Green
					g+=val;
					g = Math.max(0, Math.min(g, 1));
					break;
				case 2://Blue
					b+=val;
					b = Math.max(0, Math.min(b, 1));
					break;
				}			
				
				
				red.setValue(r);
				green.setValue(g);
				blue.setValue(b);
				
			}else if(ready){
				int BACK = Gamepad.BUTTON_MENU_BACK;
				
				if(GPID == -1){
					BACK = control.KEY_BACK;
				}
	
				if(currentButton == 3 && isButtonPressed(BACK) && hasTimePassed()){
					ready = false;
					recieved();
				}
			}
		}else{
			
		}
	}

	protected void updateUI(){
		red.setValue(r);
		green.setValue(g);
		blue.setValue(b);
		
		if((GPID != -1 || control != null || multiFlag) && !ready){
			TextBox area = new TextBox(this.location, this.size, "Player " + ID, null);
			area.setHeaderHeight(0.1f);
			area.setContentColour(new float[]{1,1,1,0.5f});
			area.setHeaderTextSize(0.06f);
			area.draw();
			
			Button ready = new Button(new Vector3f(location.x+0.28f, location.y+0.03f, location.z+size.z), new Vector3f(0.4f, 0.2f, 0.05f), "Ready up");
			ready.setTextSize(0.23f);
			
			switch(currentButton){
			case 0: //Red
				red.setRGBA(new float[]{1,1,1,1});
				green.setRGBA(new float[]{0,1,0,1});
				blue.setRGBA(new float[]{0,0,1,1});
				ready.setColour(new float[]{1,1,1,0.5f});
				break;
				
			case 1: //Green
				red.setRGBA(new float[]{1,0,0,1});
				green.setRGBA(new float[]{1,1,1,1});
				blue.setRGBA(new float[]{0,0,1,1});
				ready.setColour(new float[]{1,1,1,0.5f});
				break;
				
			case 2: //Blue
				red.setRGBA(new float[]{1,0,0,1});
				green.setRGBA(new float[]{0,1,0,1});
				blue.setRGBA(new float[]{1,1,1,1});
				ready.setColour(new float[]{1,1,1,0.5f});
				break;
			case 3://Ready Button
				red.setRGBA(new float[]{1,0,0,1});
				green.setRGBA(new float[]{0,1,0,1});
				blue.setRGBA(new float[]{0,0,1,1});
				ready.setColour(new float[]{1,1,1,1});
				break;
			}
			red.draw();
			green.draw();
			blue.draw();
			ready.draw();
			
			drawPlayerAt(new Vector3f(location.x+0.15f, location.y+0.25f, location.z+0.3f), new float[]{r,g,b,1}, true, 0.6f);
			
		}else if(!ready){

			TextBox area = new TextBox(this.location, this.size, "", "\n\n\n Press any\n button to join");
			area.setHeaderHeight(-0.2f);
			area.setHeaderColour(new float[]{1,1,1,0});
			area.setContentColour(new float[]{1,1,1,0.5f});
			area.setHeaderTextSize(0.06f);
			area.setContentTextSize(0.05f);
			area.draw();
			
		}else{

			TextBox area = new TextBox(this.location, this.size, "Player " + ID, "\n\n\n Ready");
			area.setHeaderHeight(0.1f);
			area.setContentColour(new float[]{1,1,1,0.5f});
			area.setHeaderTextSize(0.06f);
			area.setContentTextSize(0.1f);
			area.draw();
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
}

