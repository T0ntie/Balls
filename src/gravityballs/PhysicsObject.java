package gravityballs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PhysicsObject {

	public static double EPSILON = 0.9;
	protected double centerX;
	protected double centerY;
	protected double radius;
	protected double mass;
	
	protected double velX;
	protected double velY;
	
	protected double oldVelX;
	protected double oldVelY;
	
	protected double corrX;
	protected double corrY;
	

	public double getCorrX() {
		return corrX;
	}

	public void setCorrX(double corrX) {
		this.corrX = corrX;
	}

	public double getCorrY() {
		return corrY;
	}

	public void setCorrY(double corrY) {
		this.corrY = corrY;
	}

	public double getOldVelX() {
		return oldVelX;
	}

	public void setOldVelX(double oldVelX) {
		this.oldVelX = oldVelX;
	}

	public double getOldVelY() {
		return oldVelY;
	}

	public void setOldVelY(double oldVelY) {
		this.oldVelY = oldVelY;
	}

	private Color color = Color.RED;

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		
		this.velX = (Math.abs(velX) > EPSILON) ? velX : 0;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = (Math.abs(velY) > EPSILON) ? velY : 0;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void render(GraphicsContext gc) {
		gc.setFill(color);
		gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
	}
	

}
