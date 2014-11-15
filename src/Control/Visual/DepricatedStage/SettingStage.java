package Control.Visual.DepricatedStage;

import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Control.Settings;
import Control.Visual.Font;
import Control.Visual.Menu.Button2f;
import Entities.Player;

public class SettingStage extends Stage{

	private Font font;
	private Button2f header;
	private Button2f[] labels = new Button2f[Settings.toggleNames.size()];
	private int position = 0;
	
	
	public SettingStage(Font font){
		this.font = font;

		header = new Button2f(new Vector2f(55,105), new Vector2f(95,10), "Settings");
		int track = 0;
		for(String s: Settings.toggleNames){
			labels[track] = new Button2f(new Vector2f(55,92-10*track), new Vector2f(45,8), s);	
			track++;
		}
		
	}
	
	public void prepare(){
	}

	public void update(){
		Player main = Settings.User.get(0);
		if(MenuStage.timePassed()){
			if(main.isKeyPressed(main.getControlScheme().KEY_DOWN)){
				position++;
				MenuStage.input();
			}
			if(main.isKeyPressed(main.getControlScheme().KEY_UP)){
				position--;
				MenuStage.input();
			}
		}
		if(position < 0){
			position = labels.length-1;
		}
		if(position > labels.length-1){
			position = 0;
		}
		
		Model h = header.getModel();
		h.setTexture(MenuStage.Button);
		Renderer.render(h);

		font.drawText(header.getMessage(), header.getTextLocation(), 0.07f, 8f);
		
		int track = 0;
		for(Button2f button: labels){
			Model m = button.getModel();
			if(track == position){
				m.setTexture(MenuStage.ButtonSelected);
				if(MenuStage.timePassed() && main.isKeyPressed(main.getControlScheme().KEY_SELECT)){
					Settings.issueCommand("toggle " + button.getMessage());
					MenuStage.input();
				}
				
			}else{
				m.setTexture(MenuStage.Button);
			}
			if(Settings.toggles.get(button.getMessage())){
				m.setRGBA(0.2f, 1, 0.2f, 1);
			}else{
				m.setRGBA(1, 0.2f, 0.2f, 1);
			}
			
			Renderer.render(m);

			font.drawText(button.getMessage().substring(1), button.getTextLocation(), 0.05f, 8f);
			track++;
		}
	}

	public void switchToUpdate(){
	}

	public void switchFromUpdate(){
	}

}
