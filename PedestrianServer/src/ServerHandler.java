import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//The class that handles the server thread
public class ServerHandler implements Runnable{
	
public static final int PORT = 2000;
public static boolean terminated = false;


	public  void runServer() throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server socket: " + s);
		System.out.println("Server listening...");
		try { 
			while(!terminated) {
			// Blocks until a connection occurs:
			System.out.println("Server listening in while");
			Socket socket = s.accept();
			System.out.println("Connection accepted.");
			System.out.println("The new socket: " + socket);
			try {
				System.out.println("Create ClientThread");
				ClientThread t = new ClientThread(socket);
				
				System.out.println("New thread started.");
				System.out.println("The new thread: " + t);
				
				}
				catch(IOException e) {
					
					System.out.println("Couldnt Create ClientThread");
					socket.close();
					
				}
			} 
		
		}
		catch(EOFException eof){
			
		} //catch, while, try
		finally { s.close(); }
		} //main

	@Override
	public void run() {
		
		try {
		this.runServer();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
		}
	}
	
	public void messageToClient(int client, String message)
	{
		ClientThread.allClients.get(client).setMessageOut(message);
	}
	String getMessageFromClient(int client)
	{
		return ClientThread.allClients.get(client).getMessageIn();
	}
	
	
	
} 

