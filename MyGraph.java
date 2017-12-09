import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyGraph {
		private Set<String> verticies;
		private HashMap<String, Set<String>> connections;
		
		public MyGraph() {
			verticies = new HashSet<String>();
			connections = new HashMap<String,Set<String>>();
		}
		
		public void add(String vertex) {
			verticies.add(vertex);
		}
		
		public int size() {
			return verticies.size();
		}
		
		public void connect(String v1, String v2) {
			if (!verticies.contains(v1) || !verticies.contains(v2)) {
				return;
			}
			
			if (connections.get(v1) == null) {
				Set<String> s = new HashSet<String>();
				s.add(v2);
				connections.put(v1, s);
			} else {
				connections.get(v1).add(v2);
			}
			
			if (connections.get(v2) == null) {
				Set<String> s = new HashSet<String>();
				s.add(v1);
				connections.put(v2, s);
			} else {
				connections.get(v2).add(v1);
			}
		}
		
		public boolean contains(String v) {
			return verticies.contains(v);
		}
		
		public Set<String> getVerticies() {
			return verticies;
		}
		
		public Set<String> getConnections(String v) {
			if (verticies.contains(v)) {
				return connections.get(v);
			} else {
				return null;
			}
		}
}