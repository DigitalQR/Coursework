package Control.Visual.Stage;

import java.io.File;
import java.util.ArrayList;

import Level.BlankWorld;
import Level.LoadedWorld;
import Level.RandomWorld;
import Level.World;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Settings;
import Control.Visual.Menu.Assets.Button;
import Control.Visual.Menu.Assets.DropMenu;
import Control.Visual.Menu.Assets.TextBox;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;
import Entities.Player;
import Entities.Tools.Health;

public class GamemodeStage extends Stage{
	
	private int yi = 0, xi = 0;

	private ArrayList<World> queue = new ArrayList<World>();
	private World randomGen = new RandomWorld();
	private World random = new BlankWorld();
	private ArrayList<World> maps = new ArrayList<World>();
		
	private static final String[] modes = {"Deathmatch:Kills","Deathmatch:Stock","Deathmatch:Time"};
	
	private DropMenu mapList, modeList;
	private Button add, clear, start, plus, modeValue, minus;
	private TextBox queueInfo;
	
	public GamemodeStage(){
		File worlds = new File("Res/World");
		ArrayList<String> raw = new ArrayList<String>();
		maps.add(random);
		raw.add("Random Map");
		maps.add(randomGen);
		raw.add("Random Generation");
		
		for(String s: worlds.list()){
			if(s.endsWith(".pw")){
				maps.add(new LoadedWorld(s.substring(0, s.length()-3)));
				raw.add(s.substring(0, s.length()-3));
			}
		}
		
		String[] name = new String[raw.size()];
		for(int i = 0; i<name.length; i++){
			name[i] = raw.get(i);
		}
		
		TextBox header = new TextBox(new Vector3f(-1.6f,-0.5f,-2.5f),new Vector3f(3.2f,1.8f,0.5f), "Game setup  ", null);
		header.setHeaderTextSize(0.10f);
		this.add(header);
		
		mapList = new DropMenu(new Vector3f(-1.6f, 0.7f,-2.5f), new Vector3f(1.5f, 0.25f, 0.5f), "map", name);
		mapList.setTextSize(0.25f);
		mapList.setMaxEntries(7);
		this.add(mapList);
		
		modeList = new DropMenu(new Vector3f(-1.6f, 0.4f,-2.5f), new Vector3f(1.5f, 0.25f, 0.5f), "Mode", modes);
		modeList.setTextSize(0.25f);
		modeList.setMaxEntries(6);
		this.add(modeList);

		minus = new Button(new Vector3f(0f, 0.4f,-2.5f), new Vector3f(0.25f, 0.25f, 0.5f), " <");
		minus.setTextSize(0.3f);
		this.add(minus);

		modeValue = new Button(new Vector3f(0.25f, 0.4f,-2.5f), new Vector3f(0.25f, 0.25f, 0.5f), " 3");
		modeValue.setTextSize(0.3f);
		this.add(modeValue);
		
		plus = new Button(new Vector3f(0.5f, 0.4f,-2.5f), new Vector3f(0.25f, 0.25f, 0.5f), " >");
		plus.setTextSize(0.3f);
		this.add(plus);

		add = new Button(new Vector3f(0f, 0.7f,-2.5f), new Vector3f(0.5f, 0.25f, 0.5f), "add");
		add.setTextSize(0.3f);
		this.add(add);
		
		clear = new Button(new Vector3f(0.6f, 0.7f,-2.5f), new Vector3f(0.5f, 0.25f, 0.5f), "clear");
		clear.setTextSize(0.3f);
		this.add(clear);
		
		start = new Button(new Vector3f(1.1f, -1.4f,-2.5f), new Vector3f(0.5f, 0.25f, 0.5f), "start");
		start.setTextSize(0.3f);
		this.add(start);
		
		queueInfo = new TextBox(new Vector3f(-1.6f, -1f,-2.5f), new Vector3f(1.55f, 1.3f, 0.5f), null, "\nMap Queue:");
		queueInfo.setContentTextSize(0.03f);
		queueInfo.setHeaderHeight(-0.1f);
		queueInfo.setContentColour(new float[]{1,1,1,0});
		this.add(queueInfo);
		
		reset();
	}
	
	public void addToQueueInfo(String name){
		if(!name.equals("R?!")){
			queueInfo.setContent(queueInfo.getContent()+"\n" +name);
		}else{
			reset();
		}
	}
	
	public void cycleWorldQueue(){
		if(queue.size() == 0){
			Stage.setStage(this);
			if(Settings.isHostActive()){
				Settings.host.addCommand("Sst" + Stage.getStageID("gamemode") + ";");
			}
			
		}else{
			World w = queue.get(0);
			if(w.equals(random)){
				w = maps.get(Toolkit.RandomInt(1, maps.size()-1));
			}
			if(w.equals(randomGen)){
				w = new RandomWorld();
			}
			
			Settings.setWorld(w);
			queue.remove(0);
		}
	}
	
	public void reset(){
		queueInfo.setContent("\nMap Queue:");

		if(Settings.isHostActive()){
			Settings.host.addCommand("SsgR?!;");
		}							
	}
	
	public void start(){
		int val = Integer.parseInt(modeValue.getMessage().trim());
		
		switch(modeList.getCurrentItem()){
		case "Deathmatch:Kills":
			Health.stockCap = -1;
			Health.killCap = val;
			Health.timeCap = -1;
			if(Settings.isHostActive()){
				Settings.host.addCommand("Csk" + val + ";");
			}
			break;
		case "Deathmatch:Stock":
			Health.stockCap = val;
			Health.killCap = -1;
			Health.timeCap = -1;
			if(Settings.isHostActive()){
				Settings.host.addCommand("Css" + val + ";");
			}
			Health.startTime = Camera.getLERPTime();
			
			break;
		case "Deathmatch:Time":
			Health.stockCap = -1;
			Health.killCap = -1;
			Health.timeCap = val;
			if(Settings.isHostActive()){
				Settings.host.addCommand("Cst" + val + ";");
			}
			break;
		}
		
	}
	
	private void addToMap(){
		queue.add(maps.get(mapList.getCurrentItemIndex()));
		queueInfo.setContent(queueInfo.getContent() + "\n" + mapList.getCurrentItem());
		if(Settings.isHostActive()){
			Settings.host.addCommand("Ssg" + mapList.getCurrentItem() + ";");
		}							
	}
	
	public static int isGameOver(){
		int i = 0;
		int stockID = -1;
		for(Player p: Settings.User){
			if(Health.killCap != -1 && p.killCount >= Health.killCap){
				return i;
			}
			if(Health.stockCap != -1 && p.getStock() > 0){
				if(stockID == -1){
					stockID = i;
				}else{
					stockID = -2; 
				}
			}
			i++;
		}

		i = 0;
		if(Health.timeCap != -1){
			int dif = Health.timeCap*60+7 - (int) Math.round((Camera.getLERPTime()-Health.startTime)/1000000000);
			if(dif <= 0){
				for(Player p: Settings.User){
					if(p.killCount > 0){
						return i;
					}
					i++;
				}
			}
		}
		
		if(stockID >= 0){
			return stockID;
		}
		
		
		return -1;
	}
	
	protected void updateInfo(){	
		if(this.hasFocus()){
			if(Input.isKeyPressed(Input.KEY_UP)){
				yi--;
				Input.recieved();
			}if(Input.isKeyPressed(Input.KEY_DOWN)){
				yi++;
				Input.recieved();
			}
			int bound = 1;
			if(queue.size() > 0){
				bound = 2;
			}
			if(yi > bound){
				yi = bound;
			}if(yi < 0){
				yi = 0;
			}
			
			if(Input.isKeyPressed(Input.KEY_LEFT)){
				xi--;
				Input.recieved();
			}if(Input.isKeyPressed(Input.KEY_RIGHT)){
				xi++;
				Input.recieved();
			}
			bound = 0;
			switch(yi){
			case 0:
				bound = 2;
				break;
			case 1:
				bound = 2;
				break;
			}
			
			if(xi > bound){
				xi = bound;
			}if(xi < 0){
				xi = 0;
			}
			
			if(!Settings.isHostActive() && Input.isKeyPressed(Input.KEY_BACK)){
				Stage.setStage(Stage.getStage("menu"));
				
				Input.recieved();
			}
			
			
			if(Input.isKeyPressed(Input.KEY_FORWARD) && !Settings.isClientActive()){
				switch(yi){
					case 0:
						switch(xi){
						case 0:
							mapList.focus();
							break;
						case 1:
							addToMap();
							break;
						case 2:
							reset();
							queue = new ArrayList<World>();
							break;
						}
						break;
						
					case 1:
						switch(xi){
						case 0:
							modeList.focus();
							break;
						case 1:
							int val = Integer.parseInt(modeValue.getMessage().trim());
							val--;
							if(val < 1){
								val = 1;
							}
							modeValue.setMessage(" " + val);
							break;
						case 2:
							int val1 = Integer.parseInt(modeValue.getMessage().trim());
							val1++;
							if(val1 > 10){
								val1 = 10;
							}
							modeValue.setMessage(" " + val1);
							break;
						}
						break;
						
					case 2:
						Stage.setStage(Stage.getStage("start"));
						OverworldStage overworld = (OverworldStage) Stage.getStage("overworld");
						overworld.reset();
						
						if(Settings.isHostActive()){
							Settings.host.addCommand("Sst" + Stage.getStageID("start") + ";");
						}
						
						reset();
						break;
				}
				
				Input.recieved();
			}
		}
	}

	protected void updateUI(){
		
		if(!Settings.isClientActive()){
			mapList.setDrawn(true);
			add.setDrawn(true);
			clear.setDrawn(true);
			start.setDrawn(true);
			modeValue.setDrawn(true);
			plus.setDrawn(true);
			minus.setDrawn(true);
			
			queueInfo.setLocation(new Vector3f(-1.6f, -1f,-2.5f)); 
			queueInfo.setContentTextSize(0.03f);
			if(mapList.hasFocus()){
				queueInfo.setDrawn(false);
				modeList.setDrawn(false);
			}else{
				queueInfo.setDrawn(true);
				modeList.setDrawn(true);
			}
			if(modeList.hasFocus()){
				queueInfo.setDrawn(false);
			}

			mapList.setColour(new float[]{1,1,1,0.5f});
			modeList.setColour(new float[]{1,1,1,0.5f});
			add.setColour(new float[]{1,1,1,0.5f});
			start.setColour(new float[]{1,1,1,0.5f});
			clear.setColour(new float[]{1,1,1,0.5f});
			plus.setColour(new float[]{1,1,1,0.5f});
			minus.setColour(new float[]{1,1,1,0.5f});
			
			switch(yi){
				case 0:
					switch(xi){
						case 0:
							mapList.setColour(new float[]{1,1,1,1});
							break;
						case 1:
							add.setColour(new float[]{1,1,1,1});
							break;
						case 2:
							clear.setColour(new float[]{1,1,1,1});
							break;
					}
					break;
				case 1:
					switch(xi){
					case 0:
						modeList.setColour(new float[]{1,1,1,1});
						break;
					case 1:
						minus.setColour(new float[]{1,1,1,1});
						break;
					case 2:
						plus.setColour(new float[]{1,1,1,1});
						break;
				}
				break;
					
				case 2:
					start.setColour(new float[]{1,1,1,1});
			}
			
		}else{
			mapList.setDrawn(false);
			modeList.setDrawn(false);
			add.setDrawn(false);
			start.setDrawn(false);
			clear.setDrawn(false);
			modeValue.setDrawn(false);
			plus.setDrawn(false);
			minus.setDrawn(false);
			
			queueInfo.setLocation(new Vector3f(-1.6f, -0.3f,-2.5f)); 
			queueInfo.setContentTextSize(0.07f);
			queueInfo.setDrawn(true);
			
		}
	}

}
