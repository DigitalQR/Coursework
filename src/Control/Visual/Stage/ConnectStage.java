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
	private String addition = "";
	public boolean disconnect = false;
	
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
				disconnect = false;
			}
		}catch(IllegalStateException e){}
		
		if(!Settings.isClientActive()){
			if(Input.isKeyPressed(Input.KEY_BACK) || Input.hasTimePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_START) || Input.isKeyPressed(Input.KEY_PAUSE) ){
				Stage.setStage(Stage.getStage("menu"));
				Input.recieved();
			}
		}
		
		if(Input.usingDefaultInput()){
			addition = "\n\nWarning: No profile is assigned to player 1.\n         You will be able to spectate, but\n         you will not be able to play.";
		}else{
			addition = "";
		}
		if(disconnect){
			addition += "\n\n\n  You have been disconnected!";
		}
	}

	protected void updateUI(){
		final Button IPText = new Button(new Vector3f(-1.6f, 0.6f,-2.5f), new Vector3f(0.2f, 0.3f, 0.5f), "IP: ");
		IPText.setTextSize(0.25f);
		IPText.draw();
		
		String data = "";
		if(!attempt.equals("") && !Settings.isClientActive()){
			data = " Error connecting to " + attempt;
		}
		if(Settings.isClientActive()){
			data = " Connected!\n Waiting for host to start.. \n Ping: " + Settings.client.getPing();
		}
		data += addition;
		
		final Button info = new Button(new Vector3f(-1.6f, 0.25f,-2.5f), new Vector3f(0.2f, 0.3f, 0.5f), data);
		info.setColour(new float[]{0,0,0,0});
		info.setTextSize(0.25f);
		info.draw();
	}

}
