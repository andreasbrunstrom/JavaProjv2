import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PedestrianTest2 {
	
	@Test
	void testDetectCollision() {
		//Radius = 30
		Pedestrian p1 = new Pedestrian(0,0,1);
		Pedestrian p2 = new Pedestrian(0,0,2);
		assertEquals(p1.detectCollision(p2), true);
		p2.setX(10);
		assertEquals(p1.detectCollision(p2), true);
		p2.setX(29);
		assertEquals(p1.detectCollision(p2), true);
		p2.setX(31);
		assertEquals(p1.detectCollision(p2), false);
		
		//fail("Not yet implemented");
	}
	

}
