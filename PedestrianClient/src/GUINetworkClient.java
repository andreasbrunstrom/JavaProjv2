import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
// GUI and Network class on the client side, also handle the algorithm for moving the pedestrians
public class GUINetworkClient extends Application  {

	public static final int PORT = 2000;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	public static ArrayList<Pedestrian> pedestrianlist = new ArrayList<Pedestrian>();
	private static int id;
	final int vel = 1;
	BufferedReader kbd_reader;

	Label label = new Label("Pedestrian Client");
	
	

	private class ServerListener extends Thread {
		public void run() {
			String lineFromServer;
			try {
			    while ((lineFromServer = in.readLine()) != null && !lineFromServer.equals("quit")) {
			   
			   if(lineFromServer != null) {
			   System.out.println("Line from Server " +lineFromServer);
			   stringToPedestrian(lineFromServer);
			   //Calculate new value and sen to the server
			   doMove();
			   
			   SendToServer();
			   lineFromServer = "";
			    }
			   }
			 
			    
		}
			catch (IOException e) {
				System.out.println("Exception captured: " + e);
			} 
		}
	} //class ServerListener

	//Handles the network
	private void setupClientConnection ()  {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(null);
		socket = new Socket(addr, PORT);
		System.out.println("the new socket: " + socket);
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		kbd_reader = new BufferedReader(new InputStreamReader(System.in));
		ServerListener t = new ServerListener();
		t.start();
		out.flush();
		id = Integer.parseInt(in.readLine());
		System.out.println(id + " = id");
		
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		}
	public void doMove() {
		synchronized(pedestrianlist) {
		for(Pedestrian p: pedestrianlist) {
			if(p.getOwner()== getClientID() ) {
				for(Pedestrian op: pedestrianlist) {
					//if(op.getOwner()!= getClientID()) {
						if(p.detectCollision(op)) {
							if(op.getOwner()!= getClientID()){
							//Do Collsioion here
							//Check boundies and near region then move
							if(p.getOwner() == 1) {
								if(p.getY() > op.getY()) {
								p.setX(p.getX());
								p.setY(p.getY() +vel*3);
								}
								else
								{
									p.setX(p.getX());
									p.setY(p.getY() -vel*3);
								}
							}
							if(p.getOwner() == 2) {
								if(p.getY() < op.getY()) {
								p.setX(p.getX()  );
								p.setY(p.getY() - vel*3);
								}
								else
								{
									p.setX(p.getX());
									p.setY(p.getY() +vel*3);
								}
							}
						}
							else { // Colliding with pedestrian from own list
								if(p.getY() > op.getY()) {
									p.setX(p.getX());
									p.setY(p.getY() +vel);
									op.setX(op.getX());
									op.setY(op.getY() -vel);
									}
									else
									{
										p.setX(p.getX());
										p.setY(p.getY() -vel);
										op.setX(op.getX());
										op.setY(op.getY() +vel);
									}
							}
					}
				else {
					if(p.getOwner() == 1)
						p.setX(p.getX()+vel);
					if(p.getOwner() == 2)
						p.setX(p.getX() - vel);
					// move straight
						}
					}
					
				}
			}
		}
		
		
				}
			
		
	
	
	@Override
	public void start(Stage primStage) throws Exception {

		

		Group root = new Group();
		Scene scene = new Scene(root);
		primStage.setTitle("Pedestrian Client");
		primStage.setWidth(280);
		primStage.setHeight(150);

		Button connectButton = new Button("Connect");
		connectButton.setOnAction(ae -> {setupClientConnection();});

		Button quitButton = new Button("Close");
		quitButton.setOnAction(ae -> {primStage.close();});

		VBox vbox = new VBox();
        vbox.setLayoutX(20);
        vbox.setLayoutY(20);
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        hbox1.getChildren().add(connectButton);
		hbox1.getChildren().add(quitButton);
		hbox1.setAlignment(Pos.BOTTOM_LEFT);
		hbox1.setSpacing(10);

		hbox2.getChildren().add(label);
		hbox2.setAlignment(Pos.BOTTOM_LEFT);

		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(hbox2);
		vbox.setSpacing(10);

		root.getChildren().add(vbox);

		primStage.setScene(scene);
		primStage.show();


	}
	// methods for sending and recieving data on the network
	public void updatePedestrians() {
		   try {
		   String x = in.readLine();
		   stringToPedestrian(x);
		   }catch (IOException e) {
				
				e.printStackTrace();
			}
	   }
	   
	   public void SendToServer() {
		   //out.flush();
		   if(!pedestrianlist.isEmpty()) {
		   System.out.println("Sent to Server " + pedestrianListToString());
		   out.println(pedestrianListToString());
		   out.flush();
		   }
	   }
	   
	   public String pedestrianListToString() {
		   String x = new String();
		   x = "";
		   //System.out.println("Size in pedestrianListToString " + pedestrianlist.size());
		   for(Pedestrian pedestrian : pedestrianlist) {
			   x += (pedestrian.getX() +"/" + pedestrian.getY() + "/" + pedestrian.getOwner() + ":") ;
		   }
		   
		   return x;
	   }
	   
	   public void stringToPedestrian(String s) {
		   pedestrianlist.clear();
		  //System.out.println("Clear in stringToPedestrian " + pedestrianlist.size());
		  String[] pedestrians = s.split(":");
		   for(String p : pedestrians){
			   String[] tokens = p.split("/");
			   Pedestrian t = new Pedestrian(Float.valueOf(tokens[0]), Float.valueOf(tokens[1]), Integer.valueOf(tokens[2]));	
			 pedestrianlist.add(t);
		  }
		    
	   }
	   public static int getClientID() {
		   return id;
	   }


    public static void main(String[] args) {
        launch(args);
   }

}