package Control.Visual.Stage;

import org.lwjgl.opengl.GL11;

import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;
import Entities.Player;

public class ResultStage extends Stage{

	private Animation playerModel = new Animation("Cube/Spin", 500);
	private float offset = 0;
	private float  offsetSpeed = 0;
	
	public ResultStage(){
		TextBox header = new TextBox(new Vector3f(-0.6f,-0.7f,-2.5f),new Vector3f(1.9f,2f,0.5f), "Results", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);

		TextBox kills = new TextBox(new Vector3f(-0.6f,-0.9f,-2.5f),new Vector3f(1.9f,2f,0.5f), "Kills", null);
		kills.setHeaderTextSize(0.05f);
		kills.setHeaderColour(new float[]{1,1,1,0});
		this.add(kills);
		
		TextBox deaths = new TextBox(new Vector3f(0.4f,-0.9f,-2.5f),new Vector3f(1.9f,2f,0.5f), "Deaths", null);
		deaths.setHeaderTextSize(0.05f);
		deaths.setHeaderColour(new float[]{1,1,1,0});
		this.add(deaths);

		Button start = new Button(new Vector3f(0.9f, -1.4f,-2.5f), new Vector3f(0.7f, 0.25f, 0.5f), "Continue");
		start.setTextSize(0.3f);
		this.add(start);
	}
	
	protected void updateInfo(){
		if(Input.isKeyPressed(Input.KEY_UP)){
			offsetSpeed+=0.1f;
			Input.recieved();
		}
		if(Input.isKeyPressed(Input.KEY_DOWN)){
			offsetSpeed-=0.1f;
			Input.recieved();
		}
		offset+=offsetSpeed;
		offsetSpeed-=0.01f*Toolkit.Sign(offsetSpeed);
		if(Math.round(offsetSpeed*100) == 0){
			offsetSpeed = 0;
		}
		
		if(Toolkit.Modulus(offsetSpeed) > 0.3f){
			offsetSpeed = Toolkit.Sign(offsetSpeed)*0.3f;
		}
		
		if(Input.isKeyPressed(Input.KEY_FORWARD)){
			Stage.setStage(Stage.getStage("gamemode"));
			offset = 0;
			offsetSpeed = 0;
			Input.recieved();
		}
		
	}

	protected void updateUI(){
		int i = 0;
		for(Player p: Settings.User){
			drawPlayerAt(new Vector3f(-1.2f,-0.7f-0.5f*i+offset+0.3f,-2.5f+0.3f), p.getRGBA(), true, 0.9f);

			TextBox number = new TextBox(new Vector3f(-1.6f,-2f-0.5f*i+offset,-2.5f),new Vector3f(0.1f,2f,0.5f), "" + (i+1) + ":", null);
			number.setHeaderTextSize(0.05f);
			number.setHeaderColour(new float[]{1,1,1,0});
			number.draw();
			
			TextBox kills = new TextBox(new Vector3f(-0.6f,-2f-0.5f*i+offset,-2.5f),new Vector3f(1.9f,2f,0.5f), "" + p.getTotalKills(), null);
			kills.setHeaderTextSize(0.05f);
			kills.setHeaderColour(new float[]{1,1,1,0});
			kills.draw();
			
			TextBox deaths = new TextBox(new Vector3f(0.4f,-2f-0.5f*i+offset,-2.5f),new Vector3f(1.9f,2f,0.5f), "" + p.getTotalDeaths(), null);
			deaths.setHeaderTextSize(0.05f);
			deaths.setHeaderColour(new float[]{1,1,1,0});
			deaths.draw();
			i++;
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
