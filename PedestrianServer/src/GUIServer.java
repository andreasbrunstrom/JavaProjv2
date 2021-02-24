
// GUIServer extends application
//The Main Application thread and running the GUI
import javafx.geometry.Insets;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.shape.Circle;
import javafx.animation.*;
public class GUIServer extends Application{

//The two other threads, the network controller and the simulation controller
private static Thread theServer;
private static Thread theSimulation;

public static void initialize() {
	//Creating the threads
	 theServer = new Thread(new ServerHandler());
	 theSimulation = new Thread(new Simulation());
}

void setStarted()
{
	// Sets the static boolean variable in the application to start the simulation
	Simulation.setStarted();
}

	@Override
	public void start(Stage primarystage) throws Exception {
		//Setting up the GUI Stage and Scene
		primarystage.setTitle("Server Client");
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		 Label nameLabel = new Label("Server Client");
		 Button b = new Button("Start Simulation");
		 Button c = new Button("Close");
		 Canvas canvas = new Canvas(500,500);
		 
		 //Creating the canvas for drawing
		 GraphicsContext gc = canvas.getGraphicsContext2D();
		 
		 //Creating the animation timeline that draws the animation and the enviroment of the simulation
		 Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {gc.clearRect(0, 0, 500, 500);
		 									gc.setFill(Color.BLUE);
		 									gc.fillRect(0,0,500,500);
		 									drawPedestrians(gc);
		 									gc.setFill(Color.BLACK);
		 									gc.fillRect(0,0,50,500);
		 									gc.setFill(Color.RED);
		 									gc.fillRect(450,0,50,500);
		 									}));
	        
	     //Setup the GUI:s "functions"    
		 GridPane.setConstraints(nameLabel, 0, 0);
		 GridPane.setConstraints(b, 1, 0);
		 GridPane.setConstraints(c, 1, 1);
		 GridPane.setConstraints(canvas, 0, 2);
		 b.setOnAction(e -> {setStarted(); System.out.println(Simulation.getStarted());});
		 c.setOnAction(e -> {primarystage.close(); System.exit(0);});		 	 
		 grid.getChildren().addAll(nameLabel, b, c, canvas);
		 
		 // Play the animation and show the scene
		 timeline.setCycleCount(Animation.INDEFINITE);
		 timeline.play();
		 primarystage.setScene(new Scene(grid,800,600));
		 primarystage.show();
		 
		 
	}
public static void main(String[] args) {
		
		initialize();
		try {
		
		theServer.start();		//Network control Thread
		theSimulation.start();  //Simulation control Thread	
		launch(args); // GUI control Thread
		
		
		}
		
		finally {
			
	}
}
	

// Function for drawing pedestrians
	public void drawPedestrians(GraphicsContext gc) {
		synchronized(Simulation.pedestrianlist) {
		for(Pedestrian p: Simulation.pedestrianlist) {
			if(p.getOwner()== 1) {
				gc.setFill(Color.BLACK);
				gc.fillOval(p.getX(), p.getY(), 20, 20);
				
			}
			else {
				gc.setFill(Color.RED);
				gc.fillOval(p.getX(), p.getY(), 20, 20);
				}
			}
		}
	}
}
	
	

