import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PedestrianTest {

	@Test
	void testGetX() {
		Pedestrian p = new Pedestrian(1,2,1);
		assertEquals(p.getX(), 0);
		//fail("Not yet implemented");
	}

}
