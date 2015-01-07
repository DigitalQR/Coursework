package Control.Visual.Stage;

import java.util.ArrayList;

import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Server.Connection;
import Control.Server.Host;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;

public class ServerStage extends Stage{

	private TextBox info;
	
	public ServerStage(){
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Online  ", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		
		info = new TextBox(new Vector3f(0.05f, -0.4f,-2.5f), new Vector3f(1.55f, 1.3f, 0.5f), null, "\nInfo:");
		info.setContentTextSize(0.05f);
		info.setHeaderHeight(-0.1f);
		info.setContentColour(new float[]{1,1,1,0.5f});
		this.add(info);
	}
	
	protected void updateInfo(){
		if(Settings.isHostActive()){
			info.setContent("\nInfo:\n\nServer socket is up\nand waiting for players \nto connect.\n\n The default port is\n " + Host.DEFAULT_PORT);
		}else{
			info.setContent("\nInfo:\n\nServer isn't running.\nHave you port forwarded?\n\n The default port is\n " + Host.DEFAULT_PORT);
		}
		
		if(Settings.host.getConnections().size() > 0 && Input.isKeyPressed(Input.KEY_FORWARD)){
			Stage.setStage(Stage.getStage("start"));
			Settings.host.addCommand("Sst" + Stage.getStageID("start") + ";");
			Input.recieved();
		}
		
		if(Input.isKeyPressed(Input.KEY_BACK) || Input.hasTimePassed() && Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_START)){
			Stage.setStage(Stage.getStage("menu"));
			Settings.host.destroy();
			Input.recieved();
		}
	}

	protected void updateUI(){
		if(Settings.isHostActive()){
			
			ArrayList<TextBox> conn = new ArrayList<TextBox>();
			
			for(int i = 0; i<Settings.host.getConnections().size(); i++){
				Connection c = Settings.host.getConnections().get(i);
				
				TextBox tb = new TextBox(new Vector3f(-1.6f, 0.6f-0.3f*i,-2.5f), new Vector3f(1.55f, 0.15f, 0.5f), c.USERNAME + " " + c.ping, "IP:" + c.getIP());
				tb.setContentColour(new float[]{1,1,1,0});
				tb.setContentColour(new float[]{1,1,1,0});
				tb.setHeaderTextSize(0.5f);
				tb.setContentTextSize(0.2f);
				tb.setHeaderHeight(0.6f);
				conn.add(tb);
			}
			
			for(int i = conn.size()-1; i>=0; i--){
				conn.get(i).draw();
			}
			
			if(Settings.host.getConnections().size() > 0){
				Button b = new Button(new Vector3f(1.1f, -0.8f,-2.5f), new Vector3f(0.5f, 0.3f, 0.5f), "Start");
				b.setTextSize(0.3f);
				b.draw();
			}
		}
	}

}
