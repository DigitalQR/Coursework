package Control.Visual.Menu.Assets.Core;

import Control.Visual.Font;

public class FocusableItem {
	
	private static int IDTrack = 0;
	private static int focusedID = -1;
	public static Font text = new Font("Font/Default");
	
	private int ID = IDTrack++;
	private int parentID = -1;
	
	public final int getID(){
		return ID;
	}
	
	public void setParent(FocusableItem i){
		this.parentID = i.ID;
	}
	
	public void focus(){
		focusedID = ID;
	}
	
	public boolean hasFocus(){
		return focusedID == ID;
	}
	
	public void unfocus(){
		if(parentID != -1){
			focusedID = parentID;
		}
	}
	
}
