import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MyGraphTest {

	/**
	 * Tests add, size, contains, and getVerticies methods
	 */
	@Test
	public void containsTest() {
		MyGraph graphy = new MyGraph();
		
		for (int i = 0; i < 5; i++) {
			graphy.add("" + i);
			//System.out.println(graphy.size());
		}
		
		for (int i = 4; i >= 0; i--) {
			if (!graphy.contains("" + i)) {
				fail("Graph does not contain " + i);
			}
		}
		
		Set<String> testy = new HashSet<String>();
		for (int i = 0; i < 5; i++) {
			testy.add("" + i);
		}
		if (!graphy.getVerticies().equals(testy)) {
			fail();
		}
	}
	
	/**
	 * Tests connect, getOutConnections, and getInConnections
	 */
	@Test
	public void connectionsTest() {
		MyGraph graphy = new MyGraph();
		
		for (int i = 0; i < 5; i++) {
			graphy.add("" + i);
		}
		
		graphy.connect("0", "1");
		graphy.connect("3", "1");
		graphy.connect("1", "3");
		graphy.connect("1", "2");
		
		System.out.println("1 all connections: " + graphy.getConnections("1"));
	}

}
