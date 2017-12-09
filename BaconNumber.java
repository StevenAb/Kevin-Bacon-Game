import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class BaconNumber {

	private String center;
	private MyGraph graph;
	private Set<String> visited;
	private int distance, reachable, unreachable;
	private Stack<String> path;
	private HashMap<String, Stack<String>> lookup;
	private HashMap<String, Integer> distLookup;
	private LinkedList<Vertex> queue;
	
	public BaconNumber(String file) {
		graph = new MyGraph();
		lookup = new HashMap<String,Stack<String>>();
		distLookup = new HashMap<String, Integer>();
		
		Scanner scanny = null;
		try {
			scanny = new Scanner( new URL("http://www.cs.oberlin.edu/~gr151/imdb/" + file).openStream() );
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int count = 0;
		
		String[] s = null;
		while (scanny.hasNextLine()) {
			s = scanny.nextLine().split("\\|");
			graph.add(s[0].toLowerCase());
			graph.add(s[1].toLowerCase());
			graph.connect(s[0].toLowerCase(), s[1].toLowerCase());
			count++;
			if (count >= 300000 && count % 300000 == 0) {
				System.out.println(count + " lines processed...");
			}
		}
		
		recenter("Kevin Bacon (I)".toLowerCase());
	}
	
	private boolean recenter(String v) {
		if (graph.contains(v.toLowerCase())) {
			center = v.toLowerCase();
			lookup = new HashMap<String, Stack<String>>();
			distLookup = new HashMap<String, Integer>();
			return true;
		} else {
			return false;
		}
	}
	
	//Vertex class inspired by the book
	class Vertex {
		String name;
		Vertex parent;
		int distance;
		
		public Vertex(String n, Vertex p, int d) {
			this.name = n;
			this.parent = p;
			this.distance = d;
		}
	}
	
	public void find(String v) {
		if (!graph.contains(v)) {
			System.out.println(v + " is not in this data set");
			return;
		}
		
		if (lookup.containsKey(v)) {
			Stack<String> pa = (Stack<String>) lookup.get(v).clone();
			System.out.print("Distance: " + Integer.parseInt(pa.pop())/2 + ", Path: ");
			System.out.print(pa.pop());
			while (!pa.isEmpty()) {
				System.out.print(" --> " + pa.pop());
			}
			System.out.println();
			return;
		}
		
		visited = new HashSet<String>();
		path = new Stack<String>();
		queue = new LinkedList<Vertex>();
		Vertex start = new Vertex(v, null, 0);
		queue.add(start);
		
		while(!queue.isEmpty()) {
			Vertex vert = queue.remove();
			visited.add(vert.name);
			
			if (vert.name.equals(center)) {
				solution(vert);
				return;
			} else {
				for (String str : graph.getConnections(vert.name)) {
					if (!visited.contains(str)) {
						Vertex ve = new Vertex(str, vert, vert.distance + 1);
						queue.add(ve);
					}
				}
			}
		}
		
		System.out.println(v + " is unreachable");
	}
	
	private void solution(Vertex v) {
		distance = v.distance;
		Vertex current = v;
		while (current != null) {
			path.push(current.name);
			if (!lookup.containsKey(current.name)) {
				@SuppressWarnings("unchecked")
				Stack<String> st = (Stack<String>) path.clone();
				st.push(Integer.toString(distance - current.distance));
				lookup.put(current.name, st);
			}
			current = current.parent;
		}
		System.out.print("Distance: " + distance/2 + ", Path: ");
		System.out.print(path.pop());
		while (!path.isEmpty()) {
			System.out.print(" --> " + path.pop());
		}
		System.out.println();
	}
	
	public void table() {
		int count = 0;
		reachable = 0;
		unreachable = 0;
		HashMap<Integer,Integer> mappy = new HashMap<Integer,Integer>();
		
		for (String v : graph.getVerticies()) {
			int current = -2;
			if (v.charAt(v.indexOf("(") + 1) != '1' && v.charAt(v.indexOf("(") + 1) != '2') {
				current = findDist(v);
			}
			if (current != -2) {
				if (!mappy.containsKey(current)) {
					mappy.put(current, 1);
				} else {
					mappy.put(current, mappy.get(current) + 1);
				}
			}
			count++;
			if (count >= 750 && count % 750 == 0) {
				System.out.println(Math.round(((float) count/graph.size())*100) + "% complete...");
			}
		}
		
		System.out.println();
		System.out.println("Table of distances for " + center);
		for (Integer in : mappy.keySet()) {
			if (in == -1) {
				System.out.println("Unreachable: " + mappy.get(in));
			} else {
				System.out.println("Number " + in + ": " + mappy.get(in));
			}
		}
	}
	
	public void topcenter(int n) {
		String prev = center;
		int count = 0;
		ArrayList<Float> avgs = new ArrayList<Float>();
		HashMap<Float, String> tab = new HashMap<Float, String>();
		
		for (String s : graph.getVerticies()) {
			if (s.charAt(s.indexOf("(") + 1) != '1' && s.charAt(s.indexOf("(") + 1) != '2') {
				float d = avgdist(s);
				avgs.add(d);
				tab.put(d, s);
			}
			count++;
			if (count >= 500 && count % 500 == 0) {
				System.out.println(Math.round(((float) count/graph.size())*100) + "% complete...");
			}
		}
		
		Collections.sort(avgs);
		for (int i = 0; i < n; i++) {
			System.out.println(avgs.get(i) + " " + tab.get(avgs.get(i)));
		}
		recenter(prev);
	}
	
	public float avgdist(String c) {
		recenter(c);
		int avg = 0;
		int count = 0;
		reachable = 0;
		unreachable = 0;
		
		for (String v : graph.getVerticies()) {
			int current = -1;
			if (v.charAt(v.indexOf("(") + 1) != '1' && v.charAt(v.indexOf("(") + 1) != '2') {
				current = findDist(v);
			}
			if (current != -1) {
				avg += current;
				count++;
			}
		}
		
		return (float) avg/count;
	}
	
	public void avgdist() {
		int avg = 0;
		int count = 0;
		reachable = 0;
		unreachable = 0;
		
		for (String v : graph.getVerticies()) {
			int current = -1;
			if (v.charAt(v.indexOf("(") + 1) != '1' && v.charAt(v.indexOf("(") + 1) != '2') {
				current = findDist(v);
			}
			if (current != -1) {
				avg += current;
				count++;
			}
			if (count >= 750 && count % 750 == 0) {
				System.out.println(Math.round(((float) count/graph.size())*100) + "% complete...");
			}
		}
		
		System.out.println("" + ((float) avg/count) + " " + center + " (" + reachable + ", " + unreachable + ")");
	}
	
	public int findDist(String v) {
		if (!graph.contains(v)) {
			return -1;
		}
		
		if (distLookup.containsKey(v)) {
			reachable++;
			return distLookup.get(v)/2;
		} else if (lookup.containsKey(v)) {
			Stack<String> pa = (Stack<String>) lookup.get(v).clone();
			reachable++;
			return Integer.parseInt(pa.pop())/2;
		}
		
		visited = new HashSet<String>();
		path = new Stack<String>();
		queue = new LinkedList<Vertex>();
		Vertex start = new Vertex(v, null, 0);
		queue.add(start);
		
		while(!queue.isEmpty()) {
			Vertex vert = queue.remove();
			visited.add(vert.name);
			
			if (vert.name.equals(center)) {
				reachable++;
				distSol(vert);
				return vert.distance/2;
			} else {
				for (String str : graph.getConnections(vert.name)) {
					if (!visited.contains(str)) {
						Vertex ve = new Vertex(str, vert, vert.distance + 1);
						queue.add(ve);
					}
				}
			}
		}
		
		unreachable++;
		return -1;
	}
	
	private void distSol(Vertex v) {
		Vertex current = v;
		int oDist = current.distance;
		while (current != null) {
			if (!distLookup.containsKey(current.name)) {
				distLookup.put(current.name, oDist - current.distance);
			}
			current = current.parent;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Loading...");
		BaconNumber bacon = new BaconNumber(args[0]);
		Scanner scanny = new Scanner(System.in);
		System.out.println();
		System.out.println("Welcome to the Kevin Bacon Game!");
		System.out.println("Use \"find [name]\" to find the Bacon number of [name]");
		System.out.println("Use \"recenter [name]\" to center the game at [name]");
		System.out.println("Use \"avgdist\" to find the average distance to the current center");
		System.out.println("Use \"table\" to display a table of how many actors there are per bacon number");
		System.out.println("Use \"topcenter [num]\" to find the top [num] centers in the data");
		System.out.println();
		while(scanny.hasNextLine()) {
			String s = scanny.nextLine().toLowerCase();
			if (s.contains("find")) {
				if (s.length() <= 5) {
					break;
				}
				System.out.println("Searching for " + s.substring(5).toLowerCase());
				bacon.find(s.substring(5).toLowerCase());
			} else if (s.contains("recenter")) {
				if (s.length() <= 8) {
					break;
				}
				bacon.recenter(s.substring(9).toLowerCase());
				System.out.println("New center: " + s.substring(9).toLowerCase());
			} else if (s.contains("avgdist")) {
				System.out.println("Finding average distance...");
				bacon.avgdist();
			} else if (s.contains("table")) {
				System.out.println("Finding distance table...");
				bacon.table();
			} else if (s.contains("topcenter")) {
				if (s.length() <= 10) {
					break;
				}
				String[] words = s.split("\\s+");
				System.out.println("Finding top " + words[1] + " centers...");
				bacon.topcenter(Integer.parseInt(words[1]));
			} else {
				System.out.println("\"" + s + "\" is an invalid command");
			}
			
			System.out.println();
		}
	}
}
