import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.jupiter.api.Test;

class ClientThreadTest {

	@Test
	void testSetMessageOut() throws IOException {
		Socket s = new Socket();
		ClientThread t = new ClientThread(s);
		t.setMessageOut("Test");
		assertEquals(t.getMessageIn(), "Test");
		//fail("Not yet implemented");
	}

}
