import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class DebugMenu extends JFrame implements Runnable{
	private static final long serialVersionUID = -50418515800051093L;

	public DebugMenu(){
		this.setTitle("Debug Menu");
		this.setSize(600,400);
		
		this.add(ContentPane());
		
		this.setVisible(true);
		new Thread(this).start();
	}
	static String Log = "", VarLog = "";
	static JPanel Content;
	static JTextArea LogArea;
	static JLabel[] Variables = new JLabel[10];
	
	
	private JPanel ContentPane(){
		Content = new JPanel();
		Content.setLayout(null);
		
		LogArea = new JTextArea(Log);
		LogArea.setLocation(0,0);
		LogArea.setEditable(false);
		LogArea.setSize(200, 250);
		
		JScrollPane LogScroll = new JScrollPane(LogArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		LogScroll.setLocation(5, 5);
		LogScroll.setSize(350, 350);
		
		Content.add(LogScroll);
		
		int Track = -50;
		for(int i = 0; i<Variables.length; i++){
			Variables[i] = new JLabel();
			Variables[i].setSize(1000,100);
			Variables[i].setLocation(360, Track+=15);
			Content.add(Variables[i]);
		}
		setLabels();
		
		Content.setOpaque(true);
		return Content;
	}
	
	
	static int Track = 0;
	public static void setLabels(){
		Variables[0].setText("FPS: " + GraphicContent.Sleep);
		Variables[1].setText("Scale: [Actual]" + Maths.Round(Var.ActualScale, 2) + " [Aim]" + Maths.Round(Var.ScaleAim,2));
		Variables[2].setText("Offset: (" + Var.XOffset + ", " + Var.YOffset + ")");
		
		Track++;
		int Time = 100;
		if(Track > (Var.Player.length*Time)-1){
			Track = 0;
		}
		Variables[3].setText("Player[" +  (int)(Track/Time) + "]: (" + Var.Player[(int)(Track/Time)].x + ", " + Var.Player[(int)(Track/Time)].y + ")" );
	}
	
	
	public static void Log(String Message){
		Log = Maths.getTime() + ": " + Message + "\n" + Log;
		try{
			LogArea.setText(Log);
			Content.updateUI();
		}catch(NullPointerException e){};
	}	
	
	
	public static void VarLog(String Message){
		VarLog = Maths.getTime() + Message + "\n" + Log;
		try{
			LogArea.setText(Log);
			Content.updateUI();
		}catch(NullPointerException e){};
	}

	
	//Handles how often it is updated
	public void run() {
		while(this.isVisible()){
			setLabels();
			try{
				Content.updateUI();
			}catch(NullPointerException e){};
			
			try{
				TimeUnit.MILLISECONDS.sleep(10);
			}catch(InterruptedException e){}
		}
		
	}
}
