package Control.Visual.Menu.Assets;

import org.lwjgl.input.Keyboard;

import Control.Visual.Menu.Assets.Core.Input;
import Tools.Maths.Vector3f;

public class TextField extends Button{
	
	private int messageMaxLength = 100000;

	public TextField(Vector3f location, Vector3f size, String message){
		super(location, size, message);
	}
	
	protected void process(){
		if(this.hasFocus()){

			for(char c = 'a'; c<='z'; c++){
				keyProcess(c);
			}
			for(char c = '0'; c<='9'; c++){
				keyProcess(c);
			}
			keyProcess((char)8);//Backspace
			keyProcess('.');
		}
	}
	
	private void keyProcess(char c){
		int key = getKey(c);
		try{
			if(Keyboard.isKeyDown(key) && Input.hasTimePassed()){
				if(c == 8 && message.length() > 0){
					message = message.substring(0, message.length()-1);
				}else if(message.length() < messageMaxLength){
					message+= c;
				}
				Input.recieved();
			}
		}catch(IllegalStateException e){}
	}

	public int getMessageMaxLength() {
		return messageMaxLength;
	}

	public void setMessageMaxLength(int messageMaxLength) {
		this.messageMaxLength = messageMaxLength;
	}
	
	public int getKey(char c){
		switch(c){
		case 'a': return Keyboard.KEY_A;
		case 'b': return Keyboard.KEY_B;
		case 'c': return Keyboard.KEY_C;
		case 'd': return Keyboard.KEY_D;
		case 'e': return Keyboard.KEY_E;
		case 'f': return Keyboard.KEY_F;
		case 'g': return Keyboard.KEY_G;
		case 'h': return Keyboard.KEY_H;
		case 'i': return Keyboard.KEY_I;
		case 'j': return Keyboard.KEY_J;
		case 'k': return Keyboard.KEY_K;
		case 'l': return Keyboard.KEY_L;
		case 'm': return Keyboard.KEY_M;
		case 'n': return Keyboard.KEY_N;
		case 'o': return Keyboard.KEY_O;
		case 'p': return Keyboard.KEY_P;
		case 'q': return Keyboard.KEY_Q;
		case 'r': return Keyboard.KEY_R;
		case 's': return Keyboard.KEY_S;
		case 't': return Keyboard.KEY_T;
		case 'u': return Keyboard.KEY_U;
		case 'v': return Keyboard.KEY_V;
		case 'w': return Keyboard.KEY_W;
		case 'x': return Keyboard.KEY_X;
		case 'y': return Keyboard.KEY_Y;
		case 'z': return Keyboard.KEY_Z;
		case 8: return Keyboard.KEY_BACK;
		case '.': return Keyboard.KEY_PERIOD;
		case '0': return Keyboard.KEY_0;
		case '1': return Keyboard.KEY_1;
		case '2': return Keyboard.KEY_2;
		case '3': return Keyboard.KEY_3;
		case '4': return Keyboard.KEY_4;
		case '5': return Keyboard.KEY_5;
		case '6': return Keyboard.KEY_6;
		case '7': return Keyboard.KEY_7;
		case '8': return Keyboard.KEY_8;
		case '9': return Keyboard.KEY_9;
		default: return -1;
		}
	}
	
}
