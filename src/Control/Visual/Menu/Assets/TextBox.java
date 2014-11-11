package Control.Visual.Menu.Assets;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;
import Control.Camera;
import Control.Visual.Menu.Assets.Core.Component;

public class TextBox extends Component{
	
	private String header;
	private String content;
	private float headerHeight;
	private float[] headerColour = {1,1,1,1};
	private float[] contentColour = {1,1,1,1};
	private Texture headerTexture;
	private Texture contentTexture;
	private boolean headerEnabled = true;
	private boolean contentEnabled = true;
	
	
	public TextBox(Vector3f location, Vector3f size, String header, String content){
		super(location, size);
		this.header = header;
		this.content = content;
		this.headerHeight = size.y*0.15f;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float[] getHeaderColour() {
		return headerColour;
	}

	public void setHeaderColour(float[] headerColour) {
		this.headerColour = headerColour;
	}

	public float[] getContentColour() {
		return contentColour;
	}

	public void setContentColour(float[] contentColour) {
		this.contentColour = contentColour;
	}

	public Texture getHeaderTexture() {
		return headerTexture;
	}

	public void setHeaderTexture(Texture headerTexture) {
		this.headerTexture = headerTexture;
	}

	public Texture getContentTexture() {
		return contentTexture;
	}

	public void setContentTexture(Texture contentTexture) {
		this.contentTexture = contentTexture;
	}

	public float getHeaderHeight() {
		return headerHeight;
	}

	public void setHeaderHeight(float headerHeight) {
		this.headerHeight = headerHeight;
	}

	protected void update(){
		this.runAction();
	}


	private Cubef getHeaderCube(){
		Vector3f location = new Vector3f(this.location.x, this.location.y, this.location.z);
		location.x -= Camera.getLERPLocation().x;
		location.y -= Camera.getLERPLocation().y;
		location.z -= Camera.getLERPLocation().z;
		Cubef cube = new Cubef(new Vector3f(location.x, location.y+size.y-headerHeight, location.z), new Vector3f(location.x+size.x, location.y+size.y+headerHeight, location.z+size.z));
		return cube;
	}

	private Cubef getContentCube(){
		Vector3f location = new Vector3f(this.location.x, this.location.y, this.location.z);
		location.x -= Camera.getLERPLocation().x;
		location.y -= Camera.getLERPLocation().y;
		location.z -= Camera.getLERPLocation().z;
		Cubef cube = new Cubef(new Vector3f(location.x, location.y, location.z), new Vector3f(location.x+size.x, location.y+size.y-headerHeight-0.1f, location.z+size.z));
		return cube;
	}
	
	public void disableHeader(){
		headerHeight = 0;
		headerEnabled = false;
	}
	
	public void disableContent(){
		contentEnabled = false;
	}
	
	public void draw(){
		if(headerEnabled){
			Model h = new Model(getHeaderCube());
			h.setRGBA(headerColour[0], headerColour[1], headerColour[2], headerColour[3]);
			
			if(headerTexture != null){
				h.setTexture(headerTexture);
			}
			Renderer.render(h);
			
			Vector3f location = getHeaderCube().getLocation();
			text.setRGBA(0, 0, 0, 1);
			text.drawText(header, new Vector3f(location.x, location.y+size.y/8, location.z+size.z+0.001f), size.y*(0.1f/7), 7f);
		}
		
		if(contentEnabled){
			Model c = new Model(getContentCube());
			c.setRGBA(contentColour[0], contentColour[1], contentColour[2], contentColour[3]);
			
			if(contentTexture != null){
				c.setTexture(contentTexture);
			}
			Renderer.render(c);
			Vector3f location = getContentCube().getLocation();
			text.setRGBA(0, 0, 0, 1);
			text.drawText(content, new Vector3f(location.x, location.y+size.y-headerHeight-0.5f, location.z+size.z+0.001f), size.y*(0.03f/7), 7f);
		}
	}

}
