package gravityballs;

import javafx.scene.paint.Color;

public class Ball extends PhysicsObject {
	
	public Ball(double centerX, double centerY, double radius, double mass)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.mass = mass;
	}
	
	public Ball(double centerX, double centerY, double radius, double mass, double velX, double velY)
	{
		this(centerX, centerY, radius, mass);
		this.velX = velX;
		this.velY = velY;
	}
	
	public Ball(double centerX, double centerY, double radius, double mass, double velX, double velY, Color c)
	{
		this(centerX, centerY, radius, mass, velX, velY);
		setColor(c);
	}

}
