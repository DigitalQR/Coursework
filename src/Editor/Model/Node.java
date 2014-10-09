package Editor.Model;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;

public class Node {
	
	private static int trackID = 0;
	public static final int TYPE_NODE = 0, TYPE_VERTEX = 1;
	public static Node[] node = new Node[0];
	
	public static void addNode(Node no){
		Node[] n = new Node[node.length+1];
		
		for(int i = 0; i<node.length; i++){
			n[i] = node[i];
		}
		n[node.length] = no;
		node = n;
	}
	
	public static Node getNode(int i){
		for(Node n: node){
			if(n.getID() == i){
				return n;
			}
		}
		return null;
	}
	
	public static void reset(){
		trackID = 0;
	}
	
	private Vector3f location;
	private int parentID;
	private int ID;
	private float r, theta, psi;
	private String name;
	private int type;

	public Node(String name, int type){
		this.location = new Vector3f(0,0,0);
		this.parentID = -1;
		this.ID = trackID++;
		this.r = 0;
		this.theta = 0;
		this.psi = 0;
		this.name = name;
		this.type = type;
		addNode(this);
	}
	
	public Node(String name, int type, int ID){
		this.location = new Vector3f(0,0,0);
		this.parentID = -1;
		if(trackID <= ID){
			trackID = ID+1;
		}
		
		this.ID = ID;
		this.r = 0;
		this.theta = 0;
		this.psi = 0;
		this.name = name;
		this.type = type;
	}
	
	public Node(Vector3f location, String name){
		this.location = location;
		this.parentID = -1;
		this.ID = -1;
		this.r = 0;
		this.theta = 0;
		this.name = name;
		this.type = TYPE_NODE;
		this.psi = 0;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getR() {
		return r;
	}

	public void setR(float r){
		r = Math.round(r*100);
		r/=100;
		this.r = r;
	}

	public float getTrueTheta(){
		if(parentID != -1){
			return theta + Node.getNode(parentID).getTrueTheta();
		}else{
			return theta;	
		}
	}
	
	public float getTheta(){
		return theta;
	}

	public void setTheta(float theta){
		theta = Math.round(theta*100);
		theta/=100;
		this.theta = theta;
	}

	public float getTruePsi() {
		if(parentID != -1){
			return psi + Node.getNode(parentID).getTruePsi();
		}else{
			return psi;	
		}
	}
	
	public float getPsi(){
		return psi;
	}

	public void setPsi(float psi) {
		psi = Math.round(psi*100);
		psi/=100;
		this.psi = psi;
	}

	public int getID(){
		return ID;
	}
	
	public Vector3f getLocation(){
		Vector3f loc = new Vector3f(location.x, location.y, location.z);
		
		if(parentID != -1){
			loc = getParentLocation();
			Vector3f trans = Toolkit.toCartesian(new Vector3f(r, getTrueTheta(), getTruePsi()));
			loc.x += trans.x;
			loc.y += trans.y;
			loc.z += trans.z;
		}
		
		return loc;
	}
	
	public Vector3f getParentLocation(){
		Vector3f loc = new Vector3f(0,0,0);
		if(parentID != -1){
			try{
				loc = getNode(parentID).getLocation();
			}catch(NullPointerException e){}
		}
		return loc;
	}
	
	public void setLocation(Vector3f location){
		location.x = Math.round(location.x*100);
		location.x/=100;
		location.y = Math.round(location.y*100);
		location.y/=100;
		location.z = Math.round(location.z*100);
		location.z/=100;
		
		this.location = location;
	}
	
	public Vector3f getPolarInfo() {
		return Toolkit.toCartesian(location);
	}
	
	public int getParentID() {
		return parentID;
	}
	
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}
	
}
