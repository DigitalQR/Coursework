package Editor.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

import org.lwjgl.opengl.GL11;

import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Vector3f;
import Tools.String.Parametres;

public class Triangle{
	public static Triangle[] triangle = new Triangle[0];
	
	public static void addTriangle(Triangle t){
		Triangle[] temp = new Triangle[triangle.length+1];
		
		for(int i = 0; i<triangle.length; i++){
			temp[i] = triangle[i];
		}
		
		temp[triangle.length] = t;
		triangle = temp;
	}
	
	private Vector3f ind;
	
	public Triangle(int a, int b, int c){
		this.ind = new Vector3f(a,b,c);
	}

	public Vector3f getInd(){
		return ind;
	}

	public void setInd(Vector3f ind){
		this.ind = ind;
	}
	
	public void draw(){
		int[] index = {0,1,2};
		float[] tex = {0,0, 0,1, 1,1};
		
		Vector3f loc0 = Node.getNode((int)ind.x).getLocation();
		Vector3f loc1 = Node.getNode((int)ind.y).getLocation();
		Vector3f loc2 = Node.getNode((int)ind.z).getLocation();
		
		float[] vert = {
				loc0.x, loc0.y, loc0.z,
				loc1.x, loc1.y, loc1.z,
				loc2.x, loc2.y, loc2.z
		};
		Model m = new Model(vert, tex, index);
		m.setRGBA(1, 0, 0, 0.7f);
		
		Renderer.render(m);
		
		m.setRGBA(0, 0, 0, 1);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		Renderer.render(m);

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public static void saveData(String fileName){
		fileName = fileName.toUpperCase();
		File path = new File("Res/Model/" + fileName);
		path.mkdirs();

		//Node
		String node = "";
		for(Node n: Node.node){
			node += n.getID() + ":" + n.getParentID() + ":" + n.getName() + ":" + n.getType() + ":";
			if(n.getParentID() == -1){
				node += n.getLocation().x + ":" + n.getLocation().y + ":" + n.getLocation().z + ":";
			}else{
				node += n.getR() + ":" + n.getTheta() + ":" + n.getPsi() + ":";
			}
			node += ";";
		}
		try{
			Formatter nodeScribe = new Formatter(path + "/node.me");//model editor
			nodeScribe.format("%s", node);
			nodeScribe.close();
	
			//Triangles
			String tri = "";
			for(Triangle t: Triangle.triangle){
				tri += t.getInd().x + ":" + t.getInd().y + ":" + t.getInd().z + ":;";
			}
			
			Formatter triScribe = new Formatter(path + "/tri.me");//model editor
			triScribe.format("%s", tri);
			triScribe.close();

		}catch(FileNotFoundException e){}
	}

	public static void loadData(String fileName){
		fileName = fileName.toUpperCase();
		File path = new File("Res/Model/" + fileName);

		try{
			if(path.exists() && new File(path + "/node.me").exists() && new File(path + "/tri.me").exists()){
				//Node
				Scanner nodeScan = new Scanner(new File(path + "/node.me"));
				String rawNode = "";
				
				while(nodeScan.hasNext()){
					rawNode+=nodeScan.next();
				}
				nodeScan.close();
				
				String[] nodeInfo = Parametres.getParameters(rawNode, ';');
				Node[] node = new Node[nodeInfo.length];
				for(int i = 0; i<nodeInfo.length; i++){
					String[] para = Parametres.getParameters(nodeInfo[i], ':');
					
					int ID = Integer.valueOf(para[0]);
					int parentID = Integer.valueOf(para[1]);
					String name = para[2];
					int type = Integer.valueOf(para[3]);
					float x = Float.valueOf(para[4]);
					float y = Float.valueOf(para[5]);
					float z = Float.valueOf(para[6]);
					
					node[i] = new Node(name, type, ID);
					node[i].setParentID(parentID);
					if(parentID != -1){
						node[i].setR(x);
						node[i].setTheta(y);
						node[i].setPsi(z);
					}else{
						node[i].setLocation(new Vector3f(x,y,z));
					}
					
					
				}
				
				//Tri
				Scanner triScan = new Scanner(new File(path + "/tri.me"));
				String rawTri = "";
				
				while(triScan.hasNext()){
					rawTri+=triScan.next();
				}
				triScan.close();
				
				String[] triInfo = Parametres.getParameters(rawTri, ';');
				Triangle[] tri = new Triangle[triInfo.length];
				for(int i = 0; i<triInfo.length; i++){
					String[] para = Parametres.getParameters(triInfo[i], ':');
					
					int a = Math.round(Float.valueOf(para[0]));
					int b = Math.round(Float.valueOf(para[1]));
					int c = Math.round(Float.valueOf(para[2]));
					tri[i] = new Triangle(a,b,c);
					
				}
				
				Node.node = node;
				triangle = tri;
				Option.updateNodeList();
				Option.updateVertexList();
			}
		}catch(FileNotFoundException | NumberFormatException e){
			e.printStackTrace();
			System.exit(0);
		}
		
	}
}
