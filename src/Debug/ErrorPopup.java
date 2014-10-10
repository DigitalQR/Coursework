package Debug;

import javax.swing.JOptionPane;

public class ErrorPopup{

	public static void createMessage(Exception e, boolean close){
		System.err.println("-==--ERROR--==-");
		e.printStackTrace();
		System.err.println("-==---------==-");
		JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		if(close){
			System.exit(0);
		}
	}

	public static void createMessage(String s, boolean close){
		System.err.println("-==--ERROR--==-");
		System.err.println(s);
		System.err.println("-==--=====--==-");
		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
		if(close){
			System.exit(0);
		}
	}
}
