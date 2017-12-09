import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class BaconNumber {

	private String center;
	private MyGraph graph;
	private Set<String> visited;
	private int distance;
	private HashMap<String,String> lookup;
	private Stack<String> path;
	
	public BaconNumber(String file) {
		graph = new MyGraph();
		lookup = new HashMap<String,String>();
		
		Scanner scanny = null;
		try {
			scanny = new Scanner( new URL("http://www.cs.oberlin.edu/~gr151/imdb/" + file).openStream() );
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] s = null;
		while (scanny.hasNextLine()) {
			s = scanny.nextLine().split("\\|");
			graph.add(s[0]);
			graph.add(s[1]);
			graph.connect(s[0], s[1]);
		}
		
		System.out.println(graph.size());
	}
	
	private boolean recenter(String v) {
		if (graph.contains(v)) {
			center = v;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * thisDistance
	 * queue.addall(getConnections)
	 * dist(v, distance+1)
	 */
	
	private boolean dist(String v, int thisDist) {
		if (v.equals(center)) {
			this.distance = thisDist;
			path.push(v);
			return true;
		}
		
		for (String node : graph.getConnections(v)) {
			if (!visited.contains(node)) {
				visited.add(node);
				if (dist(v, thisDist + 1)) {
					path.push(v);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void find(String v) {
		if (!graph.contains(v)) {
			return;
		}
		
		distance = Integer.MAX_VALUE;
		visited = new HashSet<String>();
		path = new Stack<String>();
		
		if (dist(v, 0)) {
			System.out.print("Distance: " + distance + ", Path: ");
			for (String string : path) {
				System.out.println(path.pop() + " --> ");
			}
		}
	}
	
	public void main(String[] args) {
		BaconNumber bacon = new BaconNumber(args[0]);
	}
}
