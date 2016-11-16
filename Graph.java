
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import javax.crypto.CipherInputStream;

public class Graph {

	final double infinity = Double.POSITIVE_INFINITY;
	int  numOfNodes, numOfEdges,startNode,numOfQueries;
	double[] distance;
	List<Edge>[] graph;
	
	
	public int getNumOfNodes() {
		return numOfNodes;
	}
	public int getNumOfEdges() {
		return numOfEdges;
	}
	List<Edge>[] graphCopy;
	double [] blackList;


	/**
	 * calculatating the minimum distance between vertex "start" to vertex "end"
	 * @param nameGraph the name of the file
	 * @param start the start vertex
	 * @param end the end vertex
	 * @return the distance between tow vertex
	 */
	public static double MinPrice(String nameGraph,int start,int end){
		Graph g = new Graph(nameGraph);
		g.findShortestPaths(start);
		return g.MinDistanceTwoNode(end);
	}
	/**
	 * This function calculates the path between tow vertex
	 * @param nameGraph the name of the file
	 * @param start the start vertex
	 * @param end the end vertex
	 * @return string of the path
	 */
	public static String GetPath(String nameGraph,int start,int end){
		Graph g = new Graph(nameGraph);
		g.findShortestPaths(start);
		return g.getPath(start,end);
	}

	/**
	 * This function calculates the minimum distance between vertex "start" to vertex "end" without pass between some vertax
	 * @param nameGraph the name of the file of the graph
	 * @param BL the name of the file of the black list
	 * @param start the start vertex
	 * @return the distance between tow vertex
	 */
	public static double[] GetMinPriceWithBL(String nameGraph,String BL){
		Graph g = new Graph(nameGraph);
		g.BL(BL);
		return g.GETBL();
	}
	public static boolean is_eqFile(String name1,String name2){
		FileReader f1;
		FileReader f2;
		try {
			f1 = new FileReader(name1);
			f2 = new FileReader(name2);


			BufferedReader bf1 = new BufferedReader(f1);
			BufferedReader bf2 = new BufferedReader(f2);
			String s1 = "";
			String s2 = "";
			while(s1!=null || s2!=null){

				if(!s1.equalsIgnoreCase(s2)) return false;
				s1 = bf1.readLine();
				s2 = bf2.readLine();
			}
			if((s1==null&& s2!=null) || (s1!=null&& s2==null)) return false;
			else return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}
	public static void printToFile(String BLname,double [] arr){
		FileReader f;
		File fww ;
		String a =   " AnsTo"+BLname;
		try {
			f = new FileReader(BLname);
			BufferedReader bf = new BufferedReader(f);
			fww= new File(a);
			fww.createNewFile();
			FileWriter fw = new FileWriter(fww);
			String s = "";
			int i=0;
			s = bf.readLine();
			fw.write(s + "\n");
			s = bf.readLine();
			while(s!=null && i<arr.length){

				if(arr[i]==Double.POSITIVE_INFINITY){
					fw.write(s +"  " + "inf = no path!\n");
					i++;
					//continue;
				}
				else{
					fw.write(s + "  " + arr[i++] +"\n");	
				}
				s = bf.readLine();
			}
			bf.close();
			f.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	//end static Fanction


	///////////////////////////////////////////////////////////////////////////////////
	//constructor of the graph, get the name of the file and a start vertex
	public Graph(String name_file){
		createGraph(name_file);
		//this.startNode = start;

	}

	//create the graph, get name of the file and a start vertex
	private void createGraph(String Graph_name_file) {
		String line = "";
		int from=0, to=0;
		FileReader in;
		try {
			in = new FileReader(Graph_name_file);
			BufferedReader bf = new BufferedReader(in);
			line = bf.readLine();
			numOfNodes = Integer.valueOf(line);
			graph = new ArrayList[numOfNodes];
			graphCopy = new ArrayList[numOfNodes];
			line = bf.readLine();
			numOfEdges = Integer.valueOf(line);

			for (int i = 0; i < numOfEdges; i++) {
				line = bf.readLine();
				from=0; to=0;

				String nodeXInput= "", nodeYInput = "", xyWeight = "";
				StringTokenizer st = new StringTokenizer(line);
				nodeXInput = st.nextToken();
				nodeYInput = st.nextToken();
				xyWeight = st.nextToken();

				double xyWeightDouble = Double.valueOf(xyWeight);
				if (xyWeightDouble < 0) {
					throw new Exception("Error , the weight cant be negative ");
				}

				from = Integer.valueOf(nodeXInput);
				to = Integer.valueOf(nodeYInput) ;
				if(from <0 || to < 0)
					throw new Exception("the V is negative! ");

				if (graph[from] == null) {
					graph[from] = new ArrayList<Edge>();
				}

				graph[from].add(new Edge(to, xyWeightDouble));


				if (graphCopy[to] == null) {
					graphCopy[to] = new ArrayList<Edge>();
				}

				graphCopy[to].add(new Edge(from, xyWeightDouble));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//initialize the graph lengths to be infinity
		this.distance = new double[numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			distance[i] = Double.POSITIVE_INFINITY;
		}
	}
	//black list. get the names of the files, one for the graph, the other for the black list
	public void BL(String name_file_BL){
		FileReader in;
		String line = "",blackNode="";
		try {
			in = new FileReader(name_file_BL);
			BufferedReader bf = new BufferedReader(in);
			line = bf.readLine();

			this.numOfQueries = Integer.valueOf(line);
			blackList = new double[numOfQueries];

			for(int i=0;i<blackList.length;i++){
				line = bf.readLine();
				String xNode = "", yNode = "", numOfBlackNodes = "";
				int from = 0, to = 0, blackListLen = 0;
				StringTokenizer st1 = new StringTokenizer(line);
				StringTokenizer st2 = new StringTokenizer(line);
				xNode = st1.nextToken();
				yNode = st1.nextToken();
				numOfBlackNodes = st1.nextToken();
				from = Integer.valueOf(xNode);
				to = Integer.valueOf(yNode) ;
				blackListLen = Integer.valueOf(numOfBlackNodes);
				if (blackListLen < 0 || from <0 || to < 0) {				
					throw new Exception("can't be negative.");
				}

				ArrayList<Double> rememeberGraphModify = new ArrayList<Double>();
				for(int j=0;j<blackListLen;j++){
					blackNode = st1.nextToken();
					int index = Integer.valueOf(blackNode);
					int size = graph[index].size();
					if(size>0){
						for(int a =0;a<size;a++){
							rememeberGraphModify.add(graph[index].get(a).getWeight());//save
							graph[index].get(a).setWeight(Double.POSITIVE_INFINITY);//remove
						}
					}
				}
				//djikstra
				findShortestPaths2(from);
				
				//the new distance when not passing through black nodes
				blackList[i]=distance[to];
				//clear distance
				for(int c=0;c<distance.length;c++){
					distance[c]=infinity;
				}
				st2.nextToken();
				st2.nextToken();
				st2.nextToken();
				for(int j=0;j<blackListLen;j++){
					blackNode = st2.nextToken();
					int index = Integer.valueOf(blackNode);
					int size = graph[index].size();
					if(size>0){
						for(int a =0;a<size;a++){
							double temp= rememeberGraphModify.get(a);//list[index].get(a).getWeight();//save
							graph[index].get(a).setWeight(temp);//return the val
						}
					}
				}
			}
			in.close();
		}

		catch(Exception e ){}

		printToFile(name_file_BL,blackList);
	}

	public double [] GETBL(){
		return blackList;
	}
	public double MinDistanceTwoNode(int end){
		return distance[end];
	}

	// return string of the shortest path
	public String getPath(int st,int end){
		findShortestPaths(st);
		if(distance[end]==Double.POSITIVE_INFINITY)
			return "inf = no path!";	
		String path="";
		int pathFinder = end;
		path=path+end;
		Iterator<Edge> iterator ;

		while (pathFinder!=startNode ){

			iterator = graphCopy[pathFinder].iterator();
			boolean flag = true;
			while (iterator.hasNext()&&flag) {

				Edge curr = iterator.next();

				if (Math.abs((this.distance[pathFinder]-curr.weight)-(this.distance[curr.to]))<0.00001)
				{
					pathFinder=curr.to;
					path=curr.to+"->"+path;
					flag = false;
				}
			}	
		}
		return path;
	}
	// dijkstra, find the shortest path. fill the array "distance"
	private void findShortestPaths(int start) {
		this.startNode=start;
		this.distance = new double[numOfNodes];

		for (int i = 0; i < numOfNodes; i++) {
			distance[i] = Double.POSITIVE_INFINITY;
		}

		distance[start] = 0;

		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(new Node(start, 0, -1));

		while (priorityQueue.size() > 0) {

			Node min = priorityQueue.poll();
			if ( graph[min.node]!=null){
				Iterator<Edge> iterator = graph[min.node].iterator();

				while (iterator.hasNext()) {
					Edge curr = iterator.next();

					if (distance[min.node] + curr.weight < distance[curr.to]) {
						distance[curr.to] = distance[min.node] + curr.weight;
						priorityQueue.add(new Node(curr.to, distance[curr.to], min.node));

					}
				}
			}
		}
	}
	// function of the black list, update the price of the edges
	private void findShortestPaths2(int start) {

		distance[start] = 0;
		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(new Node(start, 0, -1));

		while (priorityQueue.size() > 0) {

			Node min = priorityQueue.poll();
			if ( graph[min.node]!=null){
				Iterator<Edge> iterator = graph[min.node].iterator();

				while (iterator.hasNext()) {
					Edge curr = iterator.next();

					if (distance[min.node] + curr.weight < distance[curr.to]) {
						distance[curr.to] = distance[min.node] + curr.weight;
						priorityQueue.add(new Node(curr.to, distance[curr.to], min.node));

					}
				}
			}
		}
	}
}