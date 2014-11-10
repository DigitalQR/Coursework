package Collision;

import Tools.Maths.Vector2f;

public class MovingHitbox extends Hitbox{

	private Vector2f track = new Vector2f(0,0);
	private Vector2f factor = new Vector2f(0.1f,0);
	private Vector2f speed = new Vector2f(0.1f,0);
	public Vector2f velocity = new Vector2f(0,0); 
	public Vector2f lastVelocity = new Vector2f(0,0); 
	
	public MovingHitbox(Vector2f location, Vector2f size) {
		super(new Vector2f(location.x, location.y), size, Hitbox.TYPE_DYNAMIC);
		factor = new Vector2f((float)Math.random()/4, 0);
		speed = new Vector2f((float)Math.random()/2, 0);
	}
	
	public void update(){
		lastVelocity = new Vector2f(velocity.x, velocity.y);
		velocity.x =(float) (Math.cos(track.x+=speed.x)*factor.x);
		velocity.y =(float) (Math.cos(track.y+=speed.y)*factor.y);

		this.location.x+=velocity.x;
		this.location.y+=velocity.y;
	}
	
}
