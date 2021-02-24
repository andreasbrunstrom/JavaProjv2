import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
// Class that handles the simulation
public class Simulation implements Runnable{
	
	private static volatile boolean started = false;
	static ArrayList<Pedestrian> pedestrianlist = new ArrayList<Pedestrian>();
	private Pedestrian temp = null;
	public static int timestep = 0;
	private int rnd = 0;
	private volatile int i = 0;
	private volatile int j = 0;
	private int blacks =0;
	private int reds =0;
	private final int MAXPEDESTRIANS = 10;
	private final double cof = 1.5;
	static void setStarted()
	{
		started = true;
	}
	public static boolean getStarted() {
		return started;
	}
	//Method for spawning the pedestrians. Checking the total number of pedestrian and try to spawn relativly equally but with some random element to it.
	void spawn() {
						if((blacks+reds)< MAXPEDESTRIANS) {
						
						Random randgen = new Random();
						rnd = randgen.nextInt(100);
						if(rnd%5 == 0) {
							rnd = 0;
							rnd = randgen.nextInt(300);
							// Spawn red
							if((rnd %2 == 0 && reds < blacks*cof) || (blacks > reds*cof) ) {		
								synchronized(pedestrianlist) {
								reds++;
								Pedestrian temp = new Pedestrian(480,rnd + 100,2);
								for(Pedestrian p: pedestrianlist)
								{
									// Trying not to spawn to close to an already existing pedestrian 
									if(p.detectCollision(temp))
										temp.setY(50);	
								}
								pedestrianlist.add(temp);
								}
							}
							//Spawn black
							else {
								synchronized(pedestrianlist) {
									blacks++;
									Pedestrian temp = new Pedestrian(20,rnd+100,1);
									for(Pedestrian p: pedestrianlist)
									{
										// Trying not to spawn to close to an already existing pedestrian 
										if(p.detectCollision(temp))
											temp.setY(450);	
									}
									pedestrianlist.add(temp);
								}
						}
					}
				}
	}
	// Method that despawns the pedestrian when they are on the other side
	void deSpawn() {
		synchronized(pedestrianlist) {
			Iterator<Pedestrian> iter = pedestrianlist.iterator();

			while (iter.hasNext()) {
			    Pedestrian ped = iter.next();

			    if (ped.getOwner() == 1)
			    	if(ped.getX() >= 445) {
			            iter.remove();
			            blacks--;
			    	}
			    if (ped.getOwner() == 2)
			    	if(ped.getX() <= 45) {
			            iter.remove();
			            reds--;
			    	}
			}
				
			}
	}
	
	@Override
	public void run() {
		// Spawning two initial pedestrian to get everything up and running correctly
		Pedestrian A = new Pedestrian(20,100,1);
		Pedestrian B = new Pedestrian(490,200,2);
		pedestrianlist.add(A);
		pedestrianlist.add(B);
		reds++;
		blacks++;
		try {
			
		
		while(true) {
			// Starting the simulation	
			if(started) {
				if(timestep == 0)
					sendDataToCLients();// Sends the initial data to the clients
				Thread.sleep(100);
				// Get data from clients
				getDatafromClients();
				//Update handles Spawning and despawning
				Thread.sleep(100);
				update();
				// Send the new data to clients
				Thread.sleep(100);
				sendDataToCLients();
				++timestep;
				Thread.sleep(100);
				
				
			}
			
		}
		
		
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	}
	finally
	{
		
	}
	

	}
	// get the data from the clients over the network
	public void getDatafromClients() {		
		synchronized(pedestrianlist) {
		for( i = 0; i < ClientThread.getClientList().size(); i++) {	// add it to the servers pedestrianlist
			ClientThread t = ClientThread.getClientList().get(i);
			
			// Remove the pedestrian with the threads id on
			Iterator<Pedestrian> iter = pedestrianlist.iterator();

			while (iter.hasNext()) {
			    Pedestrian ped = iter.next();

			    if (ped.getOwner() == i+1)
			        iter.remove();
			}	
			
			t.updatePedestrians();
			
			for( j = 0; j < t.pedestrianlist.size(); j++) {
				
				if(t.pedestrianlist.get(j).getOwner() == i+1)
					pedestrianlist.add(t.pedestrianlist.get(j));
				
			}
			
		}
		}
	}
	
	public void update() {
		
		synchronized(pedestrianlist) {
		deSpawn();
		spawn();
		
		}
	}
	
	public void sendDataToCLients() {		
		
		synchronized(pedestrianlist) {
		for( i = 0; i < ClientThread.getClientList().size(); i++) {	// add it to the servers pedestrianlist
			ClientThread t = ClientThread.getClientList().get(i);
			t.pedestrianlist.clear();
			for(Pedestrian p : pedestrianlist )
			  t.pedestrianlist.add(p); 
			if (t.pedestrianlist.size() != 0)
				t.updateClient();
			else
				System.out.println("t.pedestrianlist.size() != 0");
			
		}
		}
	}
	
}
