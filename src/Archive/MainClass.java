package Archive;
import java.util.concurrent.TimeUnit;


public class MainClass {
	static GameWindow Window;
	
	
	public static void main(String[] args){		
	
		Gamepad.setup();
		
		Var.HitBoxes = HitBox.RandomGeneration(1000, -5000, -6000, 5000, 6000, 500);
		Var.Player = new Player[2];
		Var.Player[0] = new Player(0, 0, Gamepad.lookup("AIRFLO"));
		Var.Player[1] = new Player(0, 0);
		//Var.Player[2] = new Player(10,10, 73, 75, 74, 76, 59, 222);
		//Var.Player[3] = new Player(0, 0, 38, 12, 37, 39, 107, 109);
				
		Window = new GameWindow(false);
		MainControl();
		
	}
	
	//Controls the main updates for the game
		private static void MainControl(){
			//120 UPS
			long Dif = 1;
			long BeforeTime = System.currentTimeMillis();
			long AfterTime = System.currentTimeMillis();
			
			new Thread(new Track()).start();
			while(true){

				
				Var.ProcessInput();
				Gamepad.ProcessInput();
				
				if(GraphicContent.CurrentScreen == GraphicContent.OverWorld){
					for(int i = 0; i< Var.Player.length; i++){
						Var.Player[i].update();	
					}
					
					for(int i = 0; i<Var.Particles.length; i++){
						Var.Particles[i].update();
						if(Var.Particles[i].Life < 0){
							Var.Particles = Particle.remove(Var.Particles, Var.Particles[i].ID);
							i--;
						}
					}
					
				}
				Track.UPS++;
				


				try{
					TimeUnit.MILLISECONDS.sleep(1000/(120+Dif) );
					
				}catch(InterruptedException e){
					DebugMenu.Log("[ERROR]MainClass/MechanicControl/run(): " + e.getMessage());
				}
				AfterTime = System.currentTimeMillis();
				Dif = (AfterTime-BeforeTime);
				
				BeforeTime = System.currentTimeMillis();
			}
			
		}
	
}



class Track implements Runnable{
	static int UPS = 0;
	static int FPS = 0;
	
	
	public void run(){
		while(true){
			
			
			
			try{
				TimeUnit.MILLISECONDS.sleep(1000);
				
			}catch(InterruptedException e){
				DebugMenu.Log("[ERROR]MainClass/MechanicControl/run(): " + e.getMessage());
			}
			System.out.println("[INFO]UPS: " + UPS + " FPS: " + FPS );
			UPS = 0;
			FPS = 0;
		}
	}
}
