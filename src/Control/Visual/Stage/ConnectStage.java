package Control.Visual.Stage;

import org.lwjgl.input.Keyboard;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Server.Host;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.TextField;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class ConnectStage extends Stage{
	
	private TextField ip;
	private String attempt = "";
	
	public ConnectStage(){
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Online  ", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		
		ip = new TextField(new Vector3f(-1.3f, 0.6f,-2.5f), new Vector3f(2f, 0.3f, 0.5f), "");
		ip.setTextSize(0.3f);
		ip.setMessageMaxLength(22);
		this.add(ip);
		
	}
	
	protected void updateInfo(){
		if(!Settings.isClientActive()){
			ip.focus();
		}else{
			this.focus();
		}
		
		if(!ip.getMessage().equals(attempt)){
			attempt = "";
		}
		
		try{
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !ip.getMessage().equals(attempt)){
				Settings.issueCommand("connect " + ip.getMessage() + " " + Host.DEFAULT_PORT);
				attempt = ip.getMessage();
			}
		}catch(IllegalStateException e){}
		
		if(!Settings.isClientActive()){
			if(Input.isKeyPressed(Input.KEY_BACK) || Input.hasTimePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_START)){
				Stage.setStage(Stage.getStage("menu"));
				Input.recieved();
			}
		}
	}

	protected void updateUI(){
		final Button IPText = new Button(new Vector3f(-1.6f, 0.6f,-2.5f), new Vector3f(0.2f, 0.3f, 0.5f), "IP: ");
		IPText.setTextSize(0.25f);
		IPText.draw();
		
		if(!attempt.equals("") && !Settings.isClientActive()){
			final Button error = new Button(new Vector3f(-1.6f, 0.25f,-2.5f), new Vector3f(0.2f, 0.3f, 0.5f), " Error connecting to " + attempt);
			error.setColour(new float[]{0,0,0,0});
			error.setTextSize(0.25f);
			error.draw();
		}
		
		if(Settings.isClientActive()){
			final Button info = new Button(new Vector3f(-1.6f, 0.25f,-2.5f), new Vector3f(0.2f, 0.3f, 0.5f), " Connected!\n Waiting for host to start.. \n Ping: " + Settings.client.getPing());
			info.setColour(new float[]{0,0,0,0});
			info.setTextSize(0.25f);
			info.draw();
		}
	}

}
