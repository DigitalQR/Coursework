package Archive;

//Holds key values for manipulation
public class Var{
	static HitBox[] HitBoxes = new HitBox[0];
	static Particle[] Particles = new Particle[0];

	static int XOffset = 0, YOffset = 0;
	static double ActualScale = 1, ScaleAim = 1;
	
	static DebugMenu Console;
	
	
	static boolean[] KeyBoard = new boolean[300];
	static Player[] Player;
	
	//Key bindings
	static int Fullscreen = 122;
	
	//Processes important inputs
	public static void ProcessInput(){
		//Toggle fullscreen
		if(KeyBoard[Fullscreen]){
			KeyBoard[Fullscreen] = false;
			MainClass.Window.remove(GameWindow.GC);
			if(MainClass.Window.Fullscreen){
				MainClass.Window.dispose();
				MainClass.Window = new GameWindow(false);
			}else{
				MainClass.Window.dispose();
				MainClass.Window = new GameWindow(true);
			}
		}
		
		if(KeyBoard[27]){//ESC
			GraphicContent.CurrentScreen = GraphicContent.Menu;
		}
		
		if(KeyBoard['-']){
			try{
				Console.dispose();
			}catch(NullPointerException e){};
			Console = new DebugMenu();
			KeyBoard['-'] = false;
		}
		
		if(KeyBoard[89]){
			ActualScale+=0.01;
		}
		if(KeyBoard[72]){
			ActualScale-=0.01;
		}
	}
	
	
}
