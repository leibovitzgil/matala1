
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
import java.util.concurrent.ArrayBlockingQueue;

import javax.crypto.CipherInputStream;

public class Graph {

	final double infinity = Double.POSITIVE_INFINITY;
	public static final int white = 0 , gray = 1 , black = 2;
	int  numOfNodes, numOfEdges,startNode,numOfQueries;
	double[] distance;
	List<Edge>[] graph;
	List<Edge>[] wholeGraph;
	double maxWeights;


	public int getNumOfNodes() {
		return numOfNodes;
	}
	public int getNumOfEdges() {
		return numOfEdges;
	}
	List<Edge>[] graphCopy;
	double [] blackList;

	/*
	 * 
	 * Triangle Functions !!
	 * 
	 */



	public boolean areTheyNeighbours(int v, int u){
		for(int i = 0 ; i < wholeGraph[v].size() ; i++){
			if(wholeGraph[v].get(i).to == u) return true;
		}
		return false;
	}
	//creates connectivty matrix
	public boolean [][] neighboursMatrix(){
		boolean[][] mat = new boolean[wholeGraph.length][wholeGraph.length];
//		System.out.println(graph.length);
//		System.out.println(graph[99].size());
		for(int i = 0 ; i < wholeGraph.length ; i++){
			for(int j = 0 ; j < wholeGraph.length ; j++){

//				System.out.println(i + " " + j);
				if(areTheyNeighbours( i, j))
					mat[i][j] = true;
			}
		}
		return mat;
	}
	//prints all triangle exists in a graph
	public static void findTriangles(boolean[][]mat){
		int n = mat.length;
		for (int i = 0;i < n;i++) 
			for (int j = i+1;j < n;j++) 
				if (mat[i][j] == true) 
					for (int k = j+1;k < n;k++) 
						if (mat[i][k] == true && mat[j][k] == true){
							System.out.println("triangle is: " + i + " " + j + " " + k);

						}
	}

	//finding the weight between 2 edges
	public double findWeightBetween2Edges( int u, int v){
		for(int i = 0 ; i < wholeGraph[u].size() ; i++){
			if(wholeGraph[u].get(i).to == v)
				return wholeGraph[u].get(i).weight;
		}
		return Double.MAX_VALUE;
	}
	// checks if a triangle is satisfying the property
	public boolean calcTriProp(List<Edge> g[],int u, int v, int w){
		double edgeA = findWeightBetween2Edges( u, v);
		double edgeB = findWeightBetween2Edges( v, w);
		double edgeC = findWeightBetween2Edges( w, u);

		if(edgeA > edgeB + edgeC) return false;
		if(edgeB > edgeC + edgeA) return false;
		if(edgeC > edgeA + edgeB) return false;
		return true;
	}
	//finding all triangle if exists and checks if a graph satisfying the triangle property
	public boolean satisfyTriProperty(){
		boolean mat[][] = neighboursMatrix();
		boolean flag = false;
		int n = mat.length;
		for (int i = 0;i < n;i++) {
			for (int j = i+1;j < n;j++) {
				if (mat[i][j] == true) {
					for (int k = j+1;k < n;k++) {
						if (mat[i][k] == true && mat[j][k] == true){
							if(!calcTriProp(wholeGraph, i, j, k)) return false; //if found a triangle but not satisfying the property stop immediately
							else flag = true; //if found a tri that satisfying has to check other possibilites before return true
						}
					}
				}
			}
		}
		//if has no triangles or has triangles and satisfying the property
		return flag;
	}

	//printing matrix 
	public static void printMat(boolean [][] mat){
		for(int i = 0 ; i < mat.length ; i++){
			for(int j = 0 ; j < mat[0].length ; j++){
				System.out.print(mat[i][j]+ " ");
			}
			System.out.println();
		}
	}




	/*
	 * 
	 * End of Triangle functions!!
	 * 
	 */
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
	 * This function calculates the path between two vertex
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
	
	public static boolean hasTriangleProp(String nameGraph){
		Graph g = new Graph(nameGraph);
		return g.satisfyTriProperty();
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
		//		String a =   " AnsTo"+BLname;
		String a =   " AnsTo";


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
	//end static Function

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
			wholeGraph = new ArrayList[numOfNodes];
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
//				System.out.println("from:" + from + ", to:" + to);
				if(from <0 || to < 0)
					throw new Exception("the V is negative! ");

				if (graph[from] == null) {
					graph[from] = new ArrayList<Edge>();
				}
				
				if (wholeGraph[from] == null) {
					wholeGraph[from] = new ArrayList<Edge>();
				}
				
				wholeGraph[from].add(new Edge(to, xyWeightDouble));
				graph[from].add(new Edge(to, xyWeightDouble));


				if (graphCopy[to] == null) {
					graphCopy[to] = new ArrayList<Edge>();
//					System.out.println(to);
				}
				if(wholeGraph[to] == null) wholeGraph[to] = new ArrayList<Edge>();

				wholeGraph[to].add(new Edge(from, xyWeightDouble));
				graphCopy[to].add(new Edge(from, xyWeightDouble));
				
//				if(to == 100 || from == 100){
//					System.out.println(to + " " + from);
//					System.out.println(graph[40].size());
//				}
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
			//			System.out.println("got here!!!!!!");

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

				//				System.out.println("got here!!!!!!");

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

	/*
	 * 
	 * Diameter and radius func
	 * 
	 * 
	 * */
	
	
	private double dijks(int start) {
		this.startNode=start;
		double[] dis = new double[numOfNodes];
//		this.distance = new double[numOfNodes];
		int max = 0;

		for (int i = 0; i < numOfNodes; i++) {
			dis[i] = Double.POSITIVE_INFINITY;
		}

		dis[start] = 0;

		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(new Node(start, 0, -1));

		while (priorityQueue.size() > 0) {

			Node min = priorityQueue.poll();
			if ( wholeGraph[min.node]!=null){
				Iterator<Edge> iterator = wholeGraph[min.node].iterator();

				while (iterator.hasNext()) {
					Edge curr = iterator.next();

					if (dis[min.node] + curr.weight < dis[curr.to]) {
						dis[curr.to] = dis[min.node] + curr.weight;
						priorityQueue.add(new Node(curr.to, dis[curr.to], min.node));

					}
				}
			}
		}
//		System.out.println(Arrays.toString(dis));
		return findMaxNumberInArray(dis);
	}
	
	
	
	public double findDiameterNew(){
		double diam = 0;
		for(int i = 0 ;i < wholeGraph.length ; i++){
			double temp = dijks(i);
			if( temp > diam) diam=temp;
		}
		
		return diam;
	}
	
	public double findRadiusNew(){
		double rad = Double.MAX_VALUE;
		for(int i = 0 ;i < wholeGraph.length ; i++){
			double temp = dijks(i);
			if( temp < rad) rad=temp;
		}
		
		return rad;
	}

	//converting Edge graph to Integer graph
	public ArrayList<Integer>[] edgeToIntGraph(){
		int size = graph.length;
		ArrayList<Integer> gInt[] = new ArrayList[size];
		for(int i = 0 ; i < size ; i ++){
			gInt[i] = new ArrayList<Integer>();
			for (int j = 0 ; j < graph[i].size() ; j++){
				gInt[i].add(graph[i].get(j).to);
			}
		}
		return gInt;
	}
/*
 * printing array list
 */
	public void printArrayList(){
		for(int i = 0 ; i < wholeGraph.length ; i ++){
			System.out.println(Arrays.toString(wholeGraph[i].toArray()));
		}
	}
/*
 * return the farest index of vertex in the graph
 */
	public int bfsAlgoIndex(int v){

		int size = graph.length;
		ArrayList<Integer>[] t = edgeToIntGraph();
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(size);
		int[] parent = new int[size];
		int[] color = new int[size];
		int[] dist = new int[size];
		Arrays.fill(parent, -1);
		Arrays.fill(dist, -1);
		//first time bfs
		queue.add(v);
		color[v] = gray;
		dist[v] = 0;
		while(!queue.isEmpty()){
			int v2 = queue.poll();
			for(int u : t[v2]){
				if(color[u] == white){
					color[u] = gray;
					parent[u] = v2;
					dist[u] = dist[v2] + 1;
					queue.add(u);
				}
			}
			color[v2] = black;
		}

		//find farest vertex
		return findMinWeight(dist);

	}
/*
 * returns the diameter of the graph
 */
	public int bfsAlgoDiameter(int v){

		int size = graph.length;
		ArrayList<Integer>[] t = edgeToIntGraph();
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(size);
		int[] parent = new int[size];
		int[] color = new int[size];
		int[] dist = new int[size];
		Arrays.fill(parent, -1);
		Arrays.fill(dist, -1);
		//first time bfs
		queue.add(v);
		color[v] = gray;
		dist[v] = 0;
		while(!queue.isEmpty()){
			int v2 = queue.poll();
			for(int u : t[v2]){
				if(color[u] == white){
					color[u] = gray;
					parent[u] = v2;
					dist[u] = dist[v2] + 1;
					queue.add(u);
				}
			}
			color[v2] = black;
		}

		//find farest vertex
		int index = findMinWeight(dist);
		return dist[index];

	}
	/*
	 * returns the minimum number in the array
	 */
	public double findMinimalNumberInArray(double[] arr){
		double min = Double.MAX_VALUE;
		for(int i = 0 ; i < arr.length ; i++){
			if(arr[i] < min && arr[i] > 0) min = arr[i];
		}
		return min;
	}
	
	public double findMaxNumberInArray(double[] arr){
		double max = 0;
		for(int i = 0 ; i < arr.length ; i++){
			if(arr[i] > max) max = arr[i];
		}
		return max;
	}

	/*
	 * finding the diameter by using twice bfs algo
	 */
	public int findDiameter(){
		int index = bfsAlgoIndex(0);
		int diameterIndex = bfsAlgoDiameter(index);
		return diameterIndex;
	}


	/*
	 * finding minimum weight in graph and returns it's index
	 */
	private static int findMinWeight(int[]dist){
		int max = -1;
		int index = -1;
		for(int i = 0 ; i < dist.length ; i++){
			if(dist[i] > max){
				max = dist[i];
				index = i;
			}
		}
		return index;
	}




	/*
	 * end of diameter and radius func
	 * */
	
	public void info(String file_name,String name_file_BL){
		long start = new Date().getTime();
		System.out.println(" Ex1: partual solution");
		System.out.println("Loading graph file: " + file_name + " runing a test " + name_file_BL);
		long s1 = new Date().getTime();
		Graph g = new Graph(file_name);
		System.out.println("Diameter is: " + g.findDiameterNew());
		System.out.println("Radius is: " + g.findRadiusNew());
		System.out.print("is the graph satisfying the triangle property?:");
		if(g.satisfyTriProperty())  System.out.print("yes");
		else System.out.print("no");
		System.out.println();
		g.BL(name_file_BL);
		long s2 = new Date().getTime();
		System.out.println("Done!!!  Total time: " + (s2 - start) + "  ms");
	}

}
