package Control;

import Control.input.Gamepad;


public class MainControl{
	
	
	public static void main(String[] args){
		Gamepad.setup();
		new Thread(new DisplayControl()).start();
	}

}
