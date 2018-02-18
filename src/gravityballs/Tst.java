package gravityballs;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Tst extends Application {


	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Tst");
		final BorderPane root = new BorderPane();

		Scene theScene = new Scene(root, 800, 600);
		primaryStage.setScene(theScene);

		Canvas canvas = new Canvas(500, 500);

		final Label stats = new Label();
		stats.setTextFill(Color.RED);

		final Pane canvasPane = new Pane();
		canvasPane.getChildren().add(canvas);

		root.setCenter(canvasPane);
		root.setBottom(stats);


	
		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreen(true);
		primaryStage.show();

		canvas.setHeight(canvasPane.getHeight());
		canvas.setWidth(canvasPane.getWidth());

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
					averageElapsedTime.set((averageElapsedTime.get()*frames + elapsedTime)/(frames+1));
					frames++;
					if (elapsedTime - averageElapsedTime.get() > 5000000)
					{
						System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
						
					}
					
					updateWorld(timestamp, elapsedTime, gameCanvas);
				}
				lastUpdateTime.set(timestamp);
			}

		};
		timer.start();
	}
	
	private void updateWorld(long timeStamp, long elapsedTime, Canvas canvas)
	{
		double elapsedSeconds = elapsedTime / 1_000_000_000.0;
		GraphicsContext gx = canvas.getGraphicsContext2D();
		
		gx.setFill(Color.BLACK);
		gx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		System.out.print(".");
//		
//		physics.updateObjects();
//		physics.applyGravity(elapsedSeconds);
//		physics.updatePositions(elapsedSeconds);
//		physics.detectCollisions();
//		physics.reactToCollisions();
//		physics.render(gc);
	}

	
	public static void main(String[] args) {
		launch(args);
	}

}
