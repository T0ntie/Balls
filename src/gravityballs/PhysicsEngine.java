package gravityballs;

import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class PhysicsEngine {

	public static double BOUNCE_FRICTION = 0.7;

	private Rectangle2D bounds = null;
	private final ArrayList<PhysicsObject> movingObjects = new ArrayList<>();

	private final ArrayList<PhysicsObject> toBeReomvedMovingObjects = new ArrayList<>();
	private final ArrayList<PhysicsObject> toBeAddedMovingObjects = new ArrayList<>();
	private final ArrayList<PhysicsObject> collidingMovingObjects = new ArrayList<>();

	public PhysicsEngine() {

	}

	public void setBounds(double minX, double minY, double width, double height) {
		this.bounds = new Rectangle2D(minX, minY, width, height);
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}

	public void spawnMovingObject(PhysicsObject o) {
		toBeAddedMovingObjects.add(o);
	}

	public void despawnMovingObject(PhysicsObject o) {
		toBeReomvedMovingObjects.add(o);
	}

	public void updateObjects() {
		movingObjects.addAll(toBeAddedMovingObjects);
		movingObjects.removeAll(toBeReomvedMovingObjects);
		toBeAddedMovingObjects.clear();
		toBeReomvedMovingObjects.clear();
	}

	private void detectBoundCollision(PhysicsObject obj) {
		double maxX = obj.getCenterX() + obj.getRadius();
		double minX = obj.getCenterX() - obj.getRadius();
		double maxY = obj.getCenterY() + obj.getRadius();
		double minY = obj.getCenterY() - obj.getRadius();

		// detect bound collision

		// left bound
		if (minX < bounds.getMinX()) {
			obj.setCorrX(bounds.getMinX() - minX);

			// obj.setCenterX(obj.getCenterX() + corrX);
			if (obj.getVelX() < 0) {
				obj.setVelX(obj.getVelX() * -BOUNCE_FRICTION);
				obj.setOldVelX(obj.getVelX());
			}
		}
		// right bound
		if (maxX > bounds.getMaxX()) {
			obj.setCorrX(bounds.getMaxX() - maxX);
			if (obj.getVelX() > 0) {
				obj.setVelX(obj.getVelX() * -BOUNCE_FRICTION);
				obj.setOldVelX(obj.getVelX());
			}
		}
		// top bound
		if (minY < bounds.getMinY()) {
			obj.setCorrY(bounds.getMinY() - minY);
			if (obj.getVelY() < 0) {
				obj.setVelY(obj.getVelY() * -BOUNCE_FRICTION);
				obj.setOldVelY(obj.getVelY());
			}
		}
		// bottom bound
		if (maxY > bounds.getMaxY()) {
			obj.setCorrY(bounds.getMaxY() - maxY);
			if (obj.getVelY() > 0) {
				obj.setVelY(obj.getVelY() * -BOUNCE_FRICTION);
				obj.setOldVelY(obj.getVelY());
			}
		}
	}

	public boolean isColliding(final PhysicsObject o1, final PhysicsObject o2, final double deltaX,
			final double deltaY) {
		// square of distance between flying objects is s^2 = (x2-x1)^2 + (y2-y1)^2
		// flying objects are "overlapping" if s^2 < (r1 + r2)^2
		// We also check that distance is decreasing, i.e.
		// d/dt(s^2) < 0:
		// 2(x2-x1)(x2'-x1') + 2(y2-y1)(y2'-y1') < 0

		final double radiusSum = o1.getRadius() + o2.getRadius();
		if (deltaX * deltaX + deltaY * deltaY <= radiusSum * radiusSum) {

			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			double corr = distance - radiusSum;
			o1.setCorrX((deltaX/distance) * corr/2);
			o1.setCorrY((deltaY/distance) * corr/2);
			o2.setCorrX((deltaX/distance) * corr/-2);
			o2.setCorrY((deltaY/distance) * corr/-2);

			return true;
			
//			if (deltaX * (o2.getVelX() - o1.getVelX()) + deltaY * (o2.getVelY() - o1.getVelY()) < 0) {
//				return true;
//			}
		}
		return false;
	}
	
	public void bounce(final PhysicsObject o1, final PhysicsObject o2, final double deltaX, final double deltaY)
	{
		final double distance = sqrt(deltaX * deltaX + deltaY * deltaY);
		final double unitContactX = deltaX / distance;
		final double unitContactY = deltaY / distance;

		final double xVelocity1 = o1.getVelX();
		final double yVelocity1 = o1.getVelY();
		final double xVelocity2 = o2.getVelX();
		final double yVelocity2 = o2.getVelY();

		final double u1 = xVelocity1 * unitContactX + yVelocity1 * unitContactY; // velocity of ball 1 parallel to
																					// contact vector
		final double u2 = xVelocity2 * unitContactX + yVelocity2 * unitContactY; // same for ball 2

		final double massSum = o1.getMass() + o2.getMass();
		final double massDiff = o1.getMass() - o2.getMass();

		final double v1 = (2 * o2.getMass() * u2 + u1 * massDiff) / massSum; // These equations are derived for
																				// one-dimensional collision by
		final double v2 = (2 * o1.getMass() * u1 - u2 * massDiff) / massSum; // solving equations for conservation of
																				// momentum and conservation of energy

		final double u1PerpX = xVelocity1 - u1 * unitContactX; // Components of ball 1 velocity in direction
																// perpendicular
		final double u1PerpY = yVelocity1 - u1 * unitContactY; // to contact vector. This doesn't change with collision
		final double u2PerpX = xVelocity2 - u2 * unitContactX; // Same for ball 2....
		final double u2PerpY = yVelocity2 - u2 * unitContactY;

		o1.setVelX(v1 * unitContactX + u1PerpX);
		o1.setVelY(v1 * unitContactY + u1PerpY);
		o2.setVelX(v2 * unitContactX + u2PerpX);
		o2.setVelY(v2 * unitContactY + u2PerpY);

	}

	public void detectCollisions() {

		Collections.sort(movingObjects, new Comparator<PhysicsObject>() {
			@Override
			public int compare(PhysicsObject o1, PhysicsObject o2) {
				
				return new Double(o2.getCenterX()-o1.getCenterX()).intValue();
			}
		});
		
		ListIterator<PhysicsObject> slowIt = movingObjects.listIterator();
		while (slowIt.hasNext()) {
			PhysicsObject obj1 = slowIt.next();

			detectBoundCollision(obj1);

			ListIterator<PhysicsObject> fastIt = movingObjects.listIterator(slowIt.nextIndex());
			while (fastIt.hasNext()) {
				PhysicsObject obj2 = fastIt.next();
				final double deltaX = obj2.getCenterX() - obj1.getCenterX();
				final double deltaY = obj2.getCenterY() - obj1.getCenterY();
				if (isColliding(obj1, obj2, deltaX, deltaY))
				{
					bounce(obj1, obj2, deltaX, deltaY);
				}
			}
			//detectBoundCollision(obj1);


		}
	}

	public void reactToCollisions() {
		for (PhysicsObject obj : movingObjects) {
			obj.setCenterX(obj.getCenterX() + obj.getCorrX());
			obj.setCenterY(obj.getCenterY() + obj.getCorrY());

			obj.setCorrX(0);
			obj.setCorrY(0);
		}

	}

	public void applyGravity(double dt) {
		for (PhysicsObject obj : movingObjects) {

			double vx = (obj.getOldVelX() + obj.getVelX()) / 2;
			double vy = (obj.getOldVelY() + obj.getVelY()) / 2;
			double dy = vy / Math.sqrt(vy * vy + vx * vx);
			double dx = vx / Math.sqrt(vy * vy + vx * vx);
			double dragy = -dy * 100;
			double dragx = -dx * 100;
			double ay = (981 + dragy);
			double ax = dragx;
			vy += ay * dt;
			vx += ax * dt;
			obj.setVelY(vy);
			obj.setVelX(vx);
			// obj.setVelY((obj.getOldVelY()+obj.getVelY())/2 + 981 * dt);
		}

	}

	public void updatePositions(double dt) {
		for (PhysicsObject obj : movingObjects) {

			// obj.setCenterX(obj.getCenterX() + obj.getVelX()*dt);
			// obj.setCenterY(obj.getCenterY() + obj.getVelY()*dt);
			obj.setCenterX(obj.getCenterX() + (obj.getOldVelX() + obj.getVelX()) * 0.5 * dt);
			obj.setCenterY(obj.getCenterY() + (obj.getOldVelY() + obj.getVelY()) * 0.5 * dt);
			obj.setOldVelX(obj.getVelX());
			obj.setOldVelY(obj.getVelY());
		}

	}

	public void render(GraphicsContext gc) {
		for (PhysicsObject obj : movingObjects) {
			obj.render(gc);
		}
	}
}
