package gravityballs;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXApplication extends Application {

	private final PhysicsEngine physics = new PhysicsEngine();

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("GravityBalls");
		final Pane root = new Pane();

		Canvas canvas = new Canvas(1000, 800);

		root.getChildren().addAll(canvas);

		Scene theScene = new Scene(root);
		// physics.spawnMovingObject(new Ball(200, 200, 50, 10, -600,600));
		// for (int i=0; i<5000; i++)
		// {
		// physics.spawnMovingObject(new Ball(0, 0, 10*i%90, 10, 5*i%100+new
		// Random().nextInt(500),5*i%100+new Random().nextInt(500)));
		//
		// }

		primaryStage.setScene(theScene);

		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreen(true);
		primaryStage.show();
		canvas.setHeight(primaryStage.getHeight());
		canvas.setWidth(primaryStage.getWidth());

		physics.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		physics.spawnMovingObject(new Ball(500, 500, 50, 50, new Random().nextInt(1000) - 500, 100, Color.BLUE));
		physics.spawnMovingObject(new Ball(800, 100, 50, 50, new Random().nextInt(1000) - 500, -60, Color.RED));
		physics.spawnMovingObject(new Ball(200, 500, 50, 50, new Random().nextInt(1000) - 500, -10, Color.GREEN));
		for (int i = 0; i < 10; i++) {
			physics.spawnMovingObject(new Ball(300 + 110 * i, 500, 50, 100, new Random().nextInt(1000) - 500,
					new Random().nextInt(1000) - 500, Color.YELLOWGREEN));
		}
		
		for (int i = 0; i < 10; i++) {
			physics.spawnMovingObject(new Ball(300 + 110 * i, 100, 50, 100, new Random().nextInt(1000) - 500,
					new Random().nextInt(1000) - 500, Color.BLANCHEDALMOND));
		}

		startAnimation(canvas);

	}

	long frames = 0;

	private void startAnimation(final Canvas gameCanvas) {
		final LongProperty lastUpdateTime = new SimpleLongProperty(0);
		final LongProperty averageElapsedTime = new SimpleLongProperty(0);
		final AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() > 0) {
					long elapsedTime = timestamp - lastUpdateTime.get();
					averageElapsedTime.set((averageElapsedTime.get() * frames + elapsedTime) / (frames + 1));
					frames++;
//					if (elapsedTime - averageElapsedTime.get() > 5000000) {
//						System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//
//					}
					// System.out.println(elapsedTime +" : " + averageElapsedTime.get());
					updateWorld(timestamp, elapsedTime, gameCanvas);
				}
				lastUpdateTime.set(timestamp);
			}

		};
		timer.start();

	}

	private void updateWorld(long timeStamp, long elapsedTime, Canvas canvas) {
		double elapsedSeconds = elapsedTime / 1_000_000_000.0;
		//System.out.print(".");
		
		physics.updateObjects();
		physics.applyGravity(elapsedSeconds);
		physics.updatePositions(elapsedSeconds);
		physics.detectCollisions();
		physics.reactToCollisions();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		physics.render(gc);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
