package Control.Visual.DepricatedStage;

import java.util.ArrayList;

public abstract class Stage {
	
	public abstract void prepare();
	
	public abstract void update();

	public abstract void switchToUpdate();
	
	public abstract void switchFromUpdate();
}
