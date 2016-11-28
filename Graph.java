
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
	List<Edge>[] graphCopy;
	double [] blackList;

	public int getNumOfNodes() {
		return numOfNodes;
	}
	public int getNumOfEdges() {
		return numOfEdges;
	}

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
		for(int i = 0 ; i < wholeGraph.length ; i++){
			for(int j = 0 ; j < wholeGraph.length ; j++){
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
		double ans1 = g.MinDistanceTwoNode(end);
		g.findShortestPaths(end);
		double ans2 = g.MinDistanceTwoNode(start);
		if( ans1>ans2) return ans2;
		return ans1;
	}



	public static double MinPrice2(String nameGraph,int start,int end){
		Graph g = new Graph(nameGraph);
		g.dijks2(start);
		return g.MinDistanceTwoNode(end);
	}
	/**
	 * This function calculates the path between tow vertex
	 * @param nameGraph the name of the file
	 * @param start the start vertex
	 * @param end the end vertex
	 * @return string of the path
	 */

	public static String GetPath2(String nameGraph,int start,int end){
		Graph g = new Graph(nameGraph);
		g.dijks2(start);
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
		g.BL2(BL);
		return g.GETBL();
	}

	public static boolean hasTriangleProp(String nameGraph){
		Graph g = new Graph(nameGraph);
		return g.satisfyTriProperty();
	}


	public static void printToFile(String BLname,double [] arr,int numOfNodes,int numOfEdges,boolean tie,double dia, double rad, long runtime){
		FileReader f;
		File fww ;
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
				}
				else{
					fw.write(s + "  " + arr[i++] +"\n");	
				}
				s = bf.readLine();
			}
			fw.write("Graph: |V| = " +numOfNodes + ", |E| = " + numOfEdges + ", TIE?" + tie + ", Radius:" + rad + ", Diameter:" + dia + " runtime:" + runtime +" ms." );
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
		createGraph2(name_file);
	}


	private void createGraph2(String Graph_name_file) {
		String line = "";
		int from=0, to=0;
		FileReader in;
		try {
			in = new FileReader(Graph_name_file);
			BufferedReader bf = new BufferedReader(in);
			line = bf.readLine();
			numOfNodes = Integer.valueOf(line);
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
				if(from <0 || to < 0)
					throw new Exception("the V is negative! ");


				if (wholeGraph[from] == null) {
					wholeGraph[from] = new ArrayList<Edge>();
				}
				
//				System.out.println("from:" + to + " to:" + from);
				wholeGraph[from].add(new Edge(to, xyWeightDouble));
				

				if(wholeGraph[to] == null) wholeGraph[to] = new ArrayList<Edge>();
//				System.out.println("to:" + to + " from" + from);
				wholeGraph[to].add(new Edge(from, xyWeightDouble));

			}
			in.close();
			bf.close();     //added now!
//			printArrayList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//initialize the graph lengths to be infinity
		this.distance = new double[numOfNodes];
		for (int i = 0; i < numOfNodes; i++) {
			distance[i] = Double.POSITIVE_INFINITY;
		}
	}

	//the black list function!!
	public void BL2(String name_file_BL){
		FileReader in;
		String line = "",blackNode="";
		try {
			in = new FileReader(name_file_BL);
			BufferedReader bf = new BufferedReader(in);
			line = bf.readLine();

			this.numOfQueries = Integer.valueOf(line);
			blackList = new double[numOfQueries];
			for(int i=0;i<blackList.length;i++){
				System.out.println(i);
				line = bf.readLine();
				System.out.println(line);
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
					int size = wholeGraph[index].size();
					if(size>0){
						for(int a =0;a<size;a++){
							int neiNum = wholeGraph[index].get(a).to;
//							System.out.println(neiNum);
							rememeberGraphModify.add(wholeGraph[index].get(a).getWeight());//save
							wholeGraph[index].get(a).setWeight(Double.MAX_VALUE);//remove
							for( int k = 0 ; k < wholeGraph[neiNum].size(); k++){
								if(wholeGraph[neiNum].get(k).to == index){
									wholeGraph[neiNum].get(k).setWeight(Double.MAX_VALUE);
//									System.out.println(wholeGraph[neiNum].get(k).getWeight());
								}
							}
						}
					}
				}


				//djikstra
				dijks2(from);
				//the new distance when not passing through black nodes
				System.out.println(distance[to]);
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
					int size = wholeGraph[index].size();
					if(size>0){
						for(int a =0;a<size;a++){
							int neiNum = wholeGraph[index].get(a).to;
//							System.out.println(neiNum);
							double temp= rememeberGraphModify.get(a);//list[index].get(a).getWeight();//save
							wholeGraph[index].get(a).setWeight(temp);
//							System.out.println(wholeGraph[index].get(a).getWeight());
							int k = 0;
							for( k = 0 ; k < wholeGraph[neiNum].size(); k++){
								if(wholeGraph[neiNum].get(k).to == index){
									wholeGraph[neiNum].get(k).setWeight(temp);
//									System.out.println(wholeGraph[neiNum].get(k).getWeight());
								}
							}
						}

					}
					in.close();
				}
			}
		}


		catch(Exception e ){}
	}

	public double [] GETBL(){
		return blackList;
	}
	public double MinDistanceTwoNode(int end){
		return distance[end];
	}

	// return string of the shortest path
	public String getPath(int st,int end){
		dijks2(st);
		if(distance[end]==Double.POSITIVE_INFINITY)
			return "inf = no path!";	
		String path="";
		int pathFinder = end;
		path=path+end;
		Iterator<Edge> iterator ;

		while (pathFinder!=startNode ){

			iterator = wholeGraph[pathFinder].iterator();
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

	/*
	 * 
	 * Diameter and radius func
	 * 
	 * 
	 * */

	private double dijks(int start) {
		this.startNode=start;
		double[] dis = new double[numOfNodes];
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
		return findMaxNumberInArray(dis);
	}


	private double dijks2(int start) {
		this.startNode=start;
		distance = new double[numOfNodes];
		int max = 0;

		for (int i = 0; i < numOfNodes; i++) {
			distance[i] = Double.POSITIVE_INFINITY;
		}

		distance[start] = 0;

		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		priorityQueue.add(new Node(start, 0, -1));

		while (priorityQueue.size() > 0) {

			Node min = priorityQueue.poll();
			if ( wholeGraph[min.node]!=null){
				Iterator<Edge> iterator = wholeGraph[min.node].iterator();

				while (iterator.hasNext()) {
					Edge curr = iterator.next();

					if (distance[min.node] + curr.weight < distance[curr.to]) {
						distance[curr.to] = distance[min.node] + curr.weight;
						priorityQueue.add(new Node(curr.to, distance[curr.to], min.node));

					}
				}
			}
		}
		return findMaxNumberInArray(distance);
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
	 * end of diameter and radius func
	 * */
	public void printArrayList(){
		for(int i = 0 ; i < wholeGraph.length ; i ++){
			System.out.println(Arrays.toString(wholeGraph[i].toArray()));
		}
	}

	public void info(String file_name,String name_file_BL){
		long start = new Date().getTime();
		long s1 = new Date().getTime();
		Graph g = new Graph(file_name);
		double dia = findDiameterNew();
		double rad = findRadiusNew();
		boolean tie = satisfyTriProperty();
		BL2(name_file_BL);
		long s2 = new Date().getTime();
		long runtime = s2-start;
		printToFile(name_file_BL, blackList, numOfNodes, numOfEdges, tie, dia, rad, runtime);
	}

}