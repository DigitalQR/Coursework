package RenderEngine.Shaders;

public class StaticShader extends ShaderProgram{

	private static final String
	VERTEX_FILE = "src/RenderEngine/Shaders/Shader.vs",
	FRAGMENT_FILE = "src/RenderEngine/Shaders/Shader.fs";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	protected void bindAttributes(){
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

}
