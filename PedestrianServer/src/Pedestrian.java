// Pedestrian class Server side
public class Pedestrian {
	private float x;
	private float y;
	private  int ownerClient;
	final int radius = 30; // The radius on server side is smaller than the radius on the client side, this variable on the server is used to detect spawning collisions
	
	//Generic getters and setters plus constructor for the pedestrians
	public Pedestrian(float x, float y, int o) {
		this.x = x;
		this.y = y;
		this.ownerClient = o;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setOwner(int o)
	{
		this.ownerClient = o;
	}
	public int getOwner()
	{
		return this.ownerClient;
	}
	
	// Method for collision detection
	boolean detectCollision(Pedestrian rhs) {
		return Math.sqrt(Math.pow(x - rhs.x, 2) +
                Math.pow(y - rhs.y, 2)) < radius;
	}
}
