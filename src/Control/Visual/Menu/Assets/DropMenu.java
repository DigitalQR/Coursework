package Control.Visual.Menu.Assets;

import java.util.ArrayList;

import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;
import Control.Visual.Menu.Assets.Core.Component;
import Control.Visual.Menu.Assets.Core.Input;

public class DropMenu extends Component{

	private int itemLocation = 0;
	private int maxEntries = 3;
	private float[] colour = {1,1,1,1};
	private ArrayList<String> items = new ArrayList<String>();
	private float textSize = 0.06f;
	private String name;
	
	
	public DropMenu(Vector3f location, Vector3f size, String name, String[] item){
		super(location, size);
		for(String s: item){
			addItem(s);
		}
		this.name = name;
	}
	
	public void setMaxEntries(int i){
		if(i >= 1){
			maxEntries = i;
		}
	}
	
	public void addItem(String item){
		items.add(item);
	}
	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	protected void process(){
		if(this.hasFocus()){
			if(Input.isKeyPressed(Input.KEY_UP)){
				itemLocation--;
				Input.recieved();
				
			}if(Input.isKeyPressed(Input.KEY_DOWN)){
				itemLocation++;
				Input.recieved();
				
			}
			
			if(itemLocation < 0){
				itemLocation = items.size()-1;
			}if(itemLocation > items.size()-1){
				itemLocation = 0;
			}
			
			if(Input.isKeyPressed(Input.KEY_FORWARD) || Input.isKeyPressed(Input.KEY_BACK)){
				this.unfocus();
				Input.recieved();
			}
			
		}
	}
	
	private Cubef getCube(){
		Vector3f location = this.getLERPHUDLocation();
		Cubef cube = new Cubef(new Vector3f(location.x, location.y, location.z), new Vector3f(location.x+size.x, location.y+size.y, location.z+size.z));
		return cube;
	}
	
	public float[] getColour() {
		return colour;
	}

	public void setColour(float[] colour) {
		this.colour = colour;
	}

	public void updateUI(){
		if(this.hasFocus()){
			float gap = 0.05f;
			Model m = new Model(getCube());
			Vector3f location = this.getLERPHUDLocation();
				
				int start = 0;
				int end = items.size();
				if(items.size() > maxEntries){
					start = itemLocation-(maxEntries)/2;
					if(start < 0){
						start = 0;
					}
					
					end = start+maxEntries;
					if(end > items.size()){
						end = items.size();
					}
				}
			
				for(int i = start; i<end; i++){
					if(i>=items.size()){
						break;
					}
					
					if(i == itemLocation){
						m.setRGBA(1, 1, 1, 1);
					}else{
						m.setRGBA(1, 1, 1, 0.5f);
					}
					
					Renderer.render(m);
					m.translate(new Vector3f(0, -size.y-gap, 0));
					
					text.setRGBA(0, 0, 0, 1);
					text.drawText(items.get(i), new Vector3f(location.x, location.y+size.y/2, location.z+size.z+0.001f), size.y*(textSize/7), 7f);
					location.y-=size.y+gap;
				
				}
			}else{
			Model m = new Model(getCube());
			m.setRGBA(colour[0], colour[1], colour[2], colour[3]);
			
			Renderer.render(m);
			
			Vector3f location = this.getLERPHUDLocation();
			text.setRGBA(0, 0, 0, 1);
			text.drawText(name + ": " + items.get(itemLocation), new Vector3f(location.x, location.y+size.y/2, location.z+size.z+0.001f), size.y*(textSize/7), 7f);
		}
	}
	
	public String getCurrentItem(){
		return items.get(itemLocation);
	}
	
	public int getCurrentItemIndex(){
		return itemLocation;
	}
}
