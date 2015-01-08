package Level;

import Collision.SquareHitbox;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;

public class ParsedWorld extends World{
	
	public ParsedWorld(String data){
		String[] part = data.split("]");
		
		//Hitbox
		String[] hb = part[0].split(":");
		
		for(String h: hb){
			if(!h.equals("~")){
				String[] para = h.split(",");
				float x = Float.parseFloat(para[0]);
				float y = Float.parseFloat(para[1]);
				float width = Float.parseFloat(para[2]);
				float height = Float.parseFloat(para[3]);
			
				this.addHitbox(new SquareHitbox(new Vector2f(x,y), new Vector2f(width, height)));
			}
		}
		System.out.println(data);
		//Background
		String[] back = part[1].split("#");
		for(String b: back){
			if(!b.equals("~")){
				String[] list = b.split(":");
				
				//Vertices
				String[] v = list[0].split(",");
				float[] vert = new float[v.length];
				for(int i = 0; i<v.length; i++){
					vert[i] = Float.parseFloat(v[i]);
				}
				//Texture
				String[] t = list[1].split(",");
				float[] tex = new float[t.length];
				for(int i = 0; i<t.length; i++){
					tex[i] = Float.parseFloat(t[i]);
				}
				//Indices
				String[] in = list[2].split(",");
				int[] ind = new int[in.length];
				for(int i = 0; i<in.length; i++){
					ind[i] = (int)Float.parseFloat(in[i]);
				}
				//Normals
				String[] n = list[3].split(",");
				float[] norm = new float[v.length];
				for(int i = 0; i<n.length; i++){
					norm[i] = Float.parseFloat(n[i]);
				}
				//RGBA
				String[] c = list[4].split(",");
				float[] RGBA = new float[v.length];
				for(int i = 0; i<c.length; i++){
					RGBA[i] = Float.parseFloat(c[i]);
				}
				
				Model m = new Model(vert, tex, ind, norm);
				m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
				
				this.addToBackground(m);
			}
		}
		
		//Foreground
		String[] fore = part[2].split("#");
		for(String f: fore){
			if(!f.equals("~")){
				String[] list = f.split(":");
				
				//Vertices
				String[] v = list[0].split(",");
				float[] vert = new float[v.length];
				for(int i = 0; i<v.length; i++){
					vert[i] = Float.parseFloat(v[i]);
				}
				//Texture
				String[] t = list[1].split(",");
				float[] tex = new float[t.length];
				for(int i = 0; i<t.length; i++){
					tex[i] = Float.parseFloat(t[i]);
				}
				//Indices
				String[] in = list[2].split(",");
				int[] ind = new int[in.length];
				for(int i = 0; i<in.length; i++){
					ind[i] = (int)Float.parseFloat(in[i]);
				}
				//Normals
				String[] n = list[3].split(",");
				float[] norm = new float[v.length];
				for(int i = 0; i<n.length; i++){
					norm[i] = Float.parseFloat(n[i]);
				}
				//RGBA
				String[] c = list[4].split(",");
				float[] RGBA = new float[v.length];
				for(int i = 0; i<c.length; i++){
					RGBA[i] = Float.parseFloat(c[i]);
				}
				
				Model m = new Model(vert, tex, ind, norm);
				m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
				
				this.addToForeground(m);
			}
		}
	}
	
}
