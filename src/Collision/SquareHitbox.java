package Collision;

import Tools.Maths.Vector2f;

public class SquareHitbox extends Hitbox{

	public SquareHitbox(Vector2f location, Vector2f size) {
		super(location, size, Hitbox.TYPE_STATIC);
	}

	public void update() {
	}
	
	
}
