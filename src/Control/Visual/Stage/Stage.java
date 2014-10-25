package Control.Visual.Stage;

public abstract class Stage {
	
	public abstract void prepare();
	
	public abstract void update();

	public abstract void switchToUpdate();
	
	public abstract void switchFromUpdate();
}
