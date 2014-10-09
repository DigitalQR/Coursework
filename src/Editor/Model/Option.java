package Editor.Model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Tools.Maths.Vector3f;

public class Option{

	private static JFrame frame;
	private static int verticalTrack = -objectSize();
	public static boolean KappaAssist = false;
	public static boolean floorAssist = true;
	public static boolean drawNodes = true;
	public static boolean drawVertices= true;
	public static boolean drawPartOnly= false;
	public static float nodeScale = 0.2f;
	public static boolean drawCross = true; 
	
	public static void launch(){
		frame = new JFrame("Tools");
		frame.setSize(400, 700);
		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		
		frame.add(population());
		frame.setJMenuBar(menuPopulation());
		
		frame.setVisible(true);
	}
	
	private static JPanel population(){
		JPanel master = new JPanel();
		master.setSize(frame.getWidth(), frame.getHeight());
		master.setLayout(null);
		
		master = settingsSetup(master);
		master = nodeSetup(master);
		master =  vertexSetup(master);
		
		master.setOpaque(true);
		return master;
	}

	private static JPanel settingsSetup(JPanel master){
		master.add(createText("General Settings", frame.getWidth()/2-50, objectHeight()));
		objectHeight();
		
		final JButton reset = new JButton("Reset Camera");
			reset.setLocation(10, objectHeight());
			reset.setSize(160, objectSize());
			reset.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					ModelStage.setDefaultCamera();
				}
			});
		master.add(reset);
		
		int label0 = objectHeight();
		int button0 = objectHeight();
		
		master.add(createText("Kappa assist:", 0, label0));
		final JButton Kappa = new JButton("" + KappaAssist);
			Kappa.setLocation(10, button0);
			Kappa.setSize(80, objectSize());
			Kappa.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					if(KappaAssist){
						KappaAssist = false;
					}else{
						KappaAssist = true;
					}
					Kappa.setText("" + KappaAssist);
				}
			});
		master.add(Kappa);
		
		master.add(createText("Floor assist:", 100, label0));
		final JButton floor = new JButton("" + floorAssist);
			floor.setLocation(110, button0);
			floor.setSize(80, objectSize());
			floor.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					if(floorAssist){
						floorAssist = false;
					}else{
						floorAssist = true;
					}
					floor.setText("" + floorAssist);
				}
			});
		master.add(floor);
		
		int label1 = objectHeight();
		int button1 = objectHeight();
	
		master.add(createText("Draw nodes:", 0, label1));
		final JButton nodeButton = new JButton("" + drawNodes);
			nodeButton.setLocation(10, button1);
			nodeButton.setSize(80, objectSize());
			nodeButton.addActionListener(new ActionListener(){
	
				public void actionPerformed(ActionEvent arg0){
					if(drawNodes){
						drawNodes = false;
					}else{
						drawNodes = true;
					}
					nodeButton.setText("" + drawNodes);
				}
			});
		master.add(nodeButton);	
		
		master.add(createText("Draw vertices:", 100, label1));
		final JButton vertices = new JButton("" + drawVertices);
			vertices.setLocation(110, button1);
			vertices.setSize(80, objectSize());
			vertices.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					if(drawVertices){
						drawVertices = false;
					}else{
						drawVertices = true;
					}
					vertices.setText("" + drawVertices);
				}
			});
		master.add(vertices);
		
		int label2 = objectHeight();
		int button2 = objectHeight();
		
		master.add(createText("XYZ Cross:", 0, label2));
		final JButton XYZ = new JButton("" + drawCross);
			XYZ.setLocation(10, button2);
			XYZ.setSize(80, objectSize());
			XYZ.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					if(drawCross){
						drawCross = false;
					}else{
						drawCross = true;
					}
					XYZ.setText("" + drawCross);
				}
			});
		master.add(XYZ);
		
		master.add(createText("Draw part only:", 100, label2));
		final JButton part = new JButton("" + drawPartOnly);
			part.setLocation(110, button2);
			part.setSize(80, objectSize());
			part.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0){
					if(drawPartOnly){
						drawPartOnly = false;
					}else{
						drawPartOnly = true;
					}
					part.setText("" + drawPartOnly);
				}
			});
		master.add(part);
		
		JPanel line = new JPanel();
		line.setLocation(5, objectHeight()+5);
		line.setSize(frame.getWidth()-20 , 1);
		line.setBackground(Color.GRAY);
		master.add(line);
		
		return master;
	}
	
	private static JTextArea nodeText, vertexText;
	public static JTextField[] current = new JTextField[8];
	public static final int FIELD_ID = 0, FIELD_PARENT_ID = 1, FIELD_NAME = 2, FIELD_SET_ID = 3, FIELD_X = 4, FIELD_Y = 5, FIELD_Z = 6, FIELD_INTEREST_ID = 7;
	
	private static JPanel nodeSetup(JPanel master){
		master.add(createText("Node Settings", frame.getWidth()/2-50, objectHeight()));
		objectHeight();
		
		nodeText = new JTextArea();
		nodeText.setLineWrap(false);
		nodeText.setEditable(false);
		
        JScrollPane scroll = new JScrollPane(nodeText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setLocation(10, objectHeight());
		scroll.setSize(370, objectSize()*7);
		for(int i = 0; i<6; i++){
			objectHeight();
		}
		master.add(scroll);
		
		vertexText = new JTextArea();
		vertexText.setLineWrap(false);
		vertexText.setEditable(false);
		
        JScrollPane scrollV = new JScrollPane(vertexText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollV.setLocation(10, objectHeight());
		scrollV.setSize(370, objectSize()*7);
		for(int i = 0; i<6; i++){
			objectHeight();
		}
		master.add(scrollV);
		
		int addHeight = objectHeight();
		JButton add = new JButton("Add Node");
		add.setLocation(10, addHeight);
		add.setSize(170, objectSize());
		add.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				Node n = new Node("null", Node.TYPE_NODE);
				setCurrentNode(n.getID());
			}
		});
		master.add(add);

		JButton addV = new JButton("Add Vertex");
		addV.setLocation(180, addHeight);
		addV.setSize(170, objectSize());
		addV.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				Node n = new Node("null", Node.TYPE_VERTEX);
				setCurrentNode(n.getID());
			}
		});
		master.add(addV);

		//Assignment
		int labely = objectHeight();
		int fieldy = objectHeight();
		
		master.add(createText("Select Interest ID:", 10, labely));
		current[FIELD_INTEREST_ID] = new JTextField();
		current[FIELD_INTEREST_ID].setLocation(10, fieldy);
		current[FIELD_INTEREST_ID].setSize(50, objectSize());
		current[FIELD_INTEREST_ID].setEditable(true);
		
		JButton interestSet = new JButton("Set");
		interestSet.setLocation(60, fieldy);
		interestSet.setSize(80, objectSize()-1);
		interestSet.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				try{
					updateVertexList();
				}catch(NumberFormatException e){}
			}
		});
		master.add(interestSet);
		
		int label0y = objectHeight();
		int field0y = objectHeight();
		
		master.add(createText("Select ID:", 10, label0y));
		current[FIELD_SET_ID] = new JTextField();
		current[FIELD_SET_ID].setLocation(10, field0y);
		current[FIELD_SET_ID].setSize(50, objectSize());
		current[FIELD_SET_ID].setEditable(false);
		
		JButton select = new JButton("Select");
		select.setLocation(60, field0y);
		select.setSize(80, objectSize()-1);
		select.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				try{
					setCurrentNode(Integer.valueOf(current[FIELD_SET_ID].getText()));
				}catch(NumberFormatException e){}
			}
		});
		master.add(select);
		
		//ID Info
		int label1y = objectHeight();
		int field1y = objectHeight();
		
		master.add(createText("ID:", 10, label1y));
		current[FIELD_ID] = new JTextField();
		current[FIELD_ID].setLocation(10, field1y);
		current[FIELD_ID].setSize(50, objectSize());
		current[FIELD_ID].setEditable(false);

		master.add(createText("Parent:", 70, label1y));
		current[FIELD_PARENT_ID] = new JTextField();
		current[FIELD_PARENT_ID].setLocation(70, field1y);
		current[FIELD_PARENT_ID].setSize(50, objectSize());
		
		master.add(createText("Name:", 130, label1y));
		current[FIELD_NAME] = new JTextField();
		current[FIELD_NAME].setLocation(130, field1y);
		current[FIELD_NAME].setSize(50, objectSize());
		
		//x y z data
		int label3y = objectHeight();
		int field3y = objectHeight();

		master.add(createText("X/R:", 10, label3y));
		current[FIELD_X] = new JTextField();
		current[FIELD_X].setLocation(10, field3y);
		current[FIELD_X].setSize(50, objectSize());
		current[FIELD_X].setEditable(false);
		
		master.add(createText("Y/Theta:", 70, label3y));
		current[FIELD_Y] = new JTextField();
		current[FIELD_Y].setLocation(70, field3y);
		current[FIELD_Y].setSize(50, objectSize());
		current[FIELD_Y].setEditable(false);

		master.add(createText("Z/Psi:", 130, label3y));
		current[FIELD_Z] = new JTextField();
		current[FIELD_Z].setLocation(130, field3y);
		current[FIELD_Z].setSize(50, objectSize());
		current[FIELD_Z].setEditable(false);
		
		//Set/Reset
		int field2y = objectHeight()+1;
				
		JButton set = new JButton("Set");
		set.setLocation(10, field2y);
		set.setSize(80, objectSize()-1);
		set.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				updateNodeList();
				try{
					int ID = Integer.valueOf(current[FIELD_ID].getText());
					int parent = Integer.valueOf(current[FIELD_PARENT_ID].getText());
					
					if(ID != parent){
						float x = Float.valueOf(current[FIELD_X].getText());
						float y = Float.valueOf(current[FIELD_Y].getText());
						float z = Float.valueOf(current[FIELD_Z].getText());
						if(parent < -1 || parent >= Node.node.length){
							parent = -1;
						}
						
						Node.node[ID].setParentID(parent);
						Node.node[ID].setName(current[FIELD_NAME].getText());
						
						if(parent == -1){
							Node.node[ID].setLocation(new Vector3f(x,y,z));	
						}else{
							Node.node[ID].setR(x);
							Node.node[ID].setTheta(y);
							Node.node[ID].setPsi(z);
					}
					}
				}catch(NumberFormatException e){}
			}
		});
		master.add(set);
		
		JButton reset = new JButton("Back");
		reset.setLocation(100, field2y);
		reset.setSize(80, objectSize()-1);
		reset.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				setCurrentNode(-1);
			}
		});
		master.add(reset);

		for(JTextField f: current){
			master.add(f);
		}
		JPanel line = new JPanel();
		line.setLocation(5, objectHeight()+5);
		line.setSize(frame.getWidth()-20 , 1);
		line.setBackground(Color.GRAY);
		master.add(line);
		
		setCurrentNode(-1);
		return master;
	}
	
	public static int[] getSelectedIndices(){
		int a = -1;
		int b = -1;
		int c = -1;
		
		try{
			a = Integer.valueOf(index[0].getText());
		}catch(NumberFormatException e){}
		try{
			b = Integer.valueOf(index[1].getText());
		}catch(NumberFormatException e){}
		try{
			c = Integer.valueOf(index[2].getText());
		}catch(NumberFormatException e){}
		
		return new int[]{a,b,c};
	}
	
	private static JTextField[] index = new JTextField[3]; 

	private static JPanel vertexSetup(JPanel master){
		master.add(createText("Polygon Settings", frame.getWidth()/2-50, objectHeight()));
		objectHeight();

		//x y z data
		int label0y = objectHeight();
		int field0y = objectHeight();

		master.add(createText("1:", 10, label0y));
		index[0] = new JTextField();
		index[0].setLocation(10, field0y);
		index[0].setSize(50, objectSize());
		index[0].setEditable(true);
		master.add(index[0]);
		
		master.add(createText("2:", 70, label0y));
		index[1] = new JTextField();
		index[1].setLocation(70, field0y);
		index[1].setSize(50, objectSize());
		index[1].setEditable(true);
		master.add(index[1]);
		
		master.add(createText("3:", 130, label0y));
		index[2] = new JTextField();
		index[2].setLocation(130, field0y);
		index[2].setSize(50, objectSize());
		index[2].setEditable(true);
		master.add(index[2]);
		
		//Set/Reset
		int field1y = objectHeight()+1;
				
		JButton set = new JButton("Add");
		set.setLocation(10, field1y);
		set.setSize(80, objectSize()-1);
		set.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				try{
					int a = Integer.valueOf(index[0].getText());
					int b = Integer.valueOf(index[1].getText());
					int c = Integer.valueOf(index[2].getText());
					Triangle.addTriangle(new Triangle(a,b,c));
				}catch(NumberFormatException e){}
			}
		});
		master.add(set);

		JPanel line = new JPanel();
		line.setLocation(5, objectHeight()+5);
		line.setSize(frame.getWidth()-20 , 1);
		line.setBackground(Color.GRAY);
		master.add(line);
		
		setCurrentNode(-1);
		return master;
	}
	
	public static void setCurrentNode(int ID){
		if(ID >= 0 && ID < Node.node.length){
			current[FIELD_SET_ID].setText("");
			current[FIELD_PARENT_ID].setText("" + Node.node[ID].getParentID());
			current[FIELD_ID].setText("" + Node.node[ID].getID());
			current[FIELD_NAME].setText("" + Node.node[ID].getName());
			
			if(Node.node[ID].getParentID() == -1){
				current[FIELD_X].setText("" + Node.node[ID].getLocation().x);
				current[FIELD_Y].setText("" + Node.node[ID].getLocation().y);
				current[FIELD_Z].setText("" + Node.node[ID].getLocation().z);
			}else{
				current[FIELD_X].setText("" + Node.node[ID].getR());
				current[FIELD_Y].setText("" + Node.node[ID].getTheta());
				current[FIELD_Z].setText("" + Node.node[ID].getPsi());
			}
			
			current[FIELD_X].setEditable(true);
			current[FIELD_Y].setEditable(true);
			current[FIELD_Z].setEditable(true);
			current[FIELD_SET_ID].setEditable(true);
			current[FIELD_PARENT_ID].setEditable(true);
			current[FIELD_NAME].setEditable(true);
		}else{
					
			current[FIELD_X].setEditable(false);
			current[FIELD_Y].setEditable(false);
			current[FIELD_Z].setEditable(false);
			current[FIELD_SET_ID].setEditable(true);
			current[FIELD_PARENT_ID].setEditable(false);
			current[FIELD_NAME].setEditable(false);
			for(JTextField j: current){
				j.setText("");
			}
		}
		try{
			updateNodeList();
		}catch(NullPointerException e){}
	}
	
	public static int getCurrentNodeID(){
		try{
			return Integer.valueOf(Option.current[Option.FIELD_ID].getText());
		}catch(NullPointerException | NumberFormatException e){	
			return -1;
		}		
	}
	
	public static int getCurrentInterestNode(){
		try{
			return Integer.valueOf(Option.current[Option.FIELD_INTEREST_ID].getText());
		}catch(NullPointerException | NumberFormatException e){	
			return -1;
		}
	}
	
	
	
	public static void updateNodeList(){
		String list = "";
		
		for(Node n: Node.node){
			if(n.getType() == Node.TYPE_NODE){
				list += "[" + n.getID() + "](" + n.getName() + "):\n";
				if(n.getParentID() != -1){
					list += "   ParentID: " + n.getParentID() + "\n";
				}else{
					list += "   Location: (" + n.getLocation().x + ", " + n.getLocation().y + ", " + n.getLocation().z + ")\n";
				}
			}
		}
		
		nodeText.setText(list);
		updateVertexList();
	} 
	
	
	public static void updateVertexList(){
		String list = "";
		
		for(Node n: Node.node){
			if(n.getType() == Node.TYPE_VERTEX){
				if(getCurrentInterestNode() == -1 || getCurrentInterestNode() == n.getParentID()){
					if(n.getParentID() != -1){
						list += Node.getNode(n.getParentID()).getName(); 
					}
					list += "[" + n.getID() + "](" + n.getName() + "):\n";
					if(n.getParentID() != -1){
						list += "   ParentID: " + n.getParentID() + "\n";
					}else{
						list += "   Location: (" + n.getLocation().x + ", " + n.getLocation().y + ", " + n.getLocation().z + ")\n";
					}
				}
			}
		}
		
		vertexText.setText(list);
	}
	
	
	public static void setNew(){
		Triangle.triangle = new Triangle[0];
		Node.node = new Node[0];
		Node.reset();

		new Node("Offset", Node.TYPE_NODE);
		new Node("Rotation", Node.TYPE_NODE).setParentID(0);
		
		updateNodeList();
		updateVertexList();
	}
	
	private static JMenuBar menuPopulation(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		menuBar.add(file);

		JMenuItem New = new JMenuItem("New");
		New.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				setNew();
			}
		});
		file.add(New);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				 String fileName = JOptionPane.showInputDialog(null, "File name:",  "Save", 1);
				 Triangle.saveData(fileName);
			}
		});
		file.add(save);
		
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				Node.reset();
				String fileName = JOptionPane.showInputDialog(null, "File name",  "Load", 1);
				Triangle.loadData(fileName);
			}
		});
		file.add(load);
		
		JMenu panelSwitch = new JMenu("Panel");
		menuBar.add(panelSwitch);
		
		JMenuItem settings = new JMenuItem("Update nodes");
		settings.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				updateNodeList();
			}
		});
		panelSwitch.add(settings);
		
		return menuBar;
	}
	
	
	private static int objectHeight(){
		return verticalTrack+=objectSize();
	}
	
	
	private static int objectSize(){
		return 15;
	}
	
	
	public static JLabel createText(String s, int x, int y){
		JLabel text = new JLabel(s);
		text.setLocation(x, y);
		text.setSize((int) text.getPreferredSize().getWidth(), objectSize());
		return text;
	}
}