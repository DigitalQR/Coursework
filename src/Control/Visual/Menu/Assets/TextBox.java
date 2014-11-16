package Control.Visual.Menu.Assets;

import org.newdawn.slick.opengl.Texture;

import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;
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
	private float headerTextSize = 0.1f;
	private float contentTextSize = 0.03f;
	
	
	public TextBox(Vector3f location, Vector3f size, String header, String content){
		super(location, size);
		this.header = header;
		this.content = content;
		this.headerHeight = size.y*0.15f;
		
		if(header == null){
			disableHeader();
		}if(content == null){
			disableContent();
		}
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
		this.headerHeight = size.y*headerHeight;
	}
	
	public void setHeaderTextSize(float size){
		this.headerTextSize = size;
	}
	
	public void setContentTextSize(float size){
		this.contentTextSize = size;
	}

	protected void process(){
		
	}


	private Cubef getHeaderCube(){
		Vector3f location = this.getLERPHUDLocation();
		Cubef cube = new Cubef(new Vector3f(location.x, location.y+size.y-headerHeight, location.z), new Vector3f(location.x+size.x, location.y+size.y+headerHeight, location.z+size.z));
		return cube;
	}

	private Cubef getContentCube(){
		Vector3f location = this.getLERPHUDLocation();
		Cubef cube = new Cubef(new Vector3f(location.x, location.y, location.z), new Vector3f(location.x+size.x, location.y+size.y-headerHeight-0.1f, location.z+size.z));
		return cube;
	}

	public void disableHeader(){
		headerHeight = 0;
		headerEnabled = false;
	}
	
	public void enableHeader(){
		headerHeight = size.y*0.15f;
		headerEnabled = true;
	}

	public void disableContent(){
		contentEnabled = false;
	}
	
	public void enableContent(){
		contentEnabled = true;
	}
	
	public void updateUI(){
		if(headerEnabled){
			Model h = new Model(getHeaderCube());
			h.setRGBA(headerColour[0], headerColour[1], headerColour[2], headerColour[3]);
			
			if(headerTexture != null){
				h.setTexture(headerTexture);
			}
			Renderer.render(h);
			
			Vector3f location = getHeaderCube().getLocation();
			text.setRGBA(0, 0, 0, 1);
			text.drawText(header, new Vector3f(location.x, location.y+size.y/8, location.z+size.z+0.001f), size.y*(headerTextSize/7), 7f);
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
			text.drawText(content, new Vector3f(location.x, location.y+getContentCube().getSize().y-size.y*(0.03f/7)*10, location.z+size.z+0.001f), size.y*(contentTextSize/7), 7f);
		}
	}

}
