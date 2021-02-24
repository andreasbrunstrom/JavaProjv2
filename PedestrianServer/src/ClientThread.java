// Client thread class extends Thread
//Class is used for the network control on the server side

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientThread extends Thread{
	
	//Variables
	private static int numberOfClients = 0;
	static ArrayList<ClientThread> allClients = new ArrayList<ClientThread>(); // The arraylist that stores the clients threads
	private final int clientNumber = ++numberOfClients;
	private final Socket socket;
	private final BufferedReader in;	//BuffeedrReader for handling the input stream from the clients
	private final PrintWriter out;		//PrintWriter handling the outputstream 
	private String messageOut = new String();
	private String messageIn = new String();
	public ArrayList<Pedestrian> pedestrianlist = new ArrayList<Pedestrian>();  //Arraylist of the pedestrians sent from the clients
	private volatile int temp = 0;
	
	//Constructor
	public ClientThread(Socket s) throws IOException {
		socket = s;
		
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
		out.flush();
		System.out.println("Client thread " + clientNumber +" created.");
		//Sending the client number to the client
		out.println(clientNumber);
		synchronized(this) {
			allClients.add(this);
		}
		
		start(); //Starts the thread, and calls run()
	}
	
	public void run() {
		try {
			while (true) {	
				messageIn = in.readLine();
				if (messageIn == null)
					break;
				
				System.out.println(messageIn + "From inputstream");
				
				} //while, while
			System.out.println("Client thread " + clientNumber + ": exiting...");
		}
		catch(IOException e) {
			System.out.println("Client thread " + clientNumber + ": I/O error");
		} 
		finally {
			try { socket.close(); }
			catch(IOException e) {
				System.out.println("Client thread " + clientNumber + ": Socket not closed!");
			}
			synchronized(this) {
				allClients.remove(allClients.indexOf(this));
			}
		} //finally
	} //run
	
	//Methods For sending and recieving pedestrians
   public void setMessageOut(String message) {
   this.messageOut = message;
   }
   
   public String getMessageIn() {
	   return messageIn;
	   
   }
   
   public static ArrayList<ClientThread> getClientList(){
	   return allClients;
   }
   //Update the pedestrians with data from the clients
   public void updatePedestrians() {
	   System.out.println(messageIn + " messagein in updatePedestrian sent from client");
	   if(!messageIn.equals(""))
			stringToPedestrian(messageIn);
	   
	 }
 //Sending the Data from server to clients
   public void updateClient() {
	   
	   messageOut = pedestrianListToString();
	   System.out.println("Message to client " + messageOut);
	   out.flush();
	   out.println(messageOut);
	   out.flush();
   }
   
   //Methods for handling the string that is sent on the network
   public String pedestrianListToString() {
	   String x = new String();
	   x= "";
	   for(Pedestrian pedestrian : pedestrianlist) {
		   x += (pedestrian.getX() +"/" + pedestrian.getY() + "/" + pedestrian.getOwner() + ":") ;
		   temp++;
	   }
	   
	   return x;
   }
   
   public void stringToPedestrian(String s) {
	   this.pedestrianlist.clear();
	  String[] pedestrians = s.split(":");
	   for(String p : pedestrians){
		   String[] tokens = p.split("/");
		   Pedestrian t = new Pedestrian(Float.valueOf(tokens[0]), Float.valueOf(tokens[1]), Integer.valueOf(tokens[2]));	
		   temp++;
		 this.pedestrianlist.add(t);
		
		
	  }
	
   }
   	
   	
 }
   

