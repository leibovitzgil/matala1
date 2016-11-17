
import java.util.ArrayList;
import java.util.List;

public class GraphTriangleProperty {




	//checks if to vertices are connected
	public static boolean areTheyNeighbours(List<Edge>[] graph ,int v, int u){
		for(int i = 0 ; i < graph[v].size() ; i++){
			if(graph[v].get(i).to == u) return true;
		}
		return false;
	}
	//creates connectivty matrix
	public static boolean [][] neighboursMatrix(List<Edge> g[]){
		boolean[][] mat = new boolean[g.length][g.length];
		for(int i = 0 ; i < g.length ; i++){
			for(int j = 0 ; j < g.length ; j++){


				if(areTheyNeighbours(g, i, j))
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
	public static double findWeightBetween2Edges(List<Edge> g[] , int u, int v){
		for(int i = 0 ; i < g[u].size() ; i++){
			if(g[u].get(i).to == v)
				return g[u].get(i).weight;
		}
		return Double.MAX_VALUE;
	}
	// checks if a triangle is satisfying the property
	public static boolean calcTriProp(List<Edge> g[],int u, int v, int w){
		double edgeA = findWeightBetween2Edges(g, u, v);
		double edgeB = findWeightBetween2Edges(g, v, w);
		double edgeC = findWeightBetween2Edges(g, w, u);

		if(edgeA > edgeB + edgeC) return false;
		if(edgeB > edgeC + edgeA) return false;
		if(edgeC > edgeA + edgeB) return false;
		return true;
	}
	//finding all triangle if exists and checks if a graph satisfying the triangle property
	public static boolean satisfyTriProperty(boolean[][]mat, List<Edge> g[]){
		boolean flag = false;
		int n = mat.length;
		for (int i = 0;i < n;i++) {
			for (int j = i+1;j < n;j++) {
				if (mat[i][j] == true) {
					for (int k = j+1;k < n;k++) {
						if (mat[i][k] == true && mat[j][k] == true){
							if(!calcTriProp(g, i, j, k)) return false; //if found a triangle but not satisfying the property stop immediately
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
	//building a graph
	public static List<Edge>[] graphBuilder(){
		List<Edge>[] graph = new ArrayList[5];
		for(int i = 0 ; i < 5 ; i++){
			graph[i] = new ArrayList<Edge>();
		}
		graph[0].add(new Edge(1,2));
		graph[0].add(new Edge(3,8));

		graph[1].add(new Edge(0,2));
		graph[1].add(new Edge(2,4));
		graph[1].add(new Edge(3,3));

		graph[2].add(new Edge(1,4));
		graph[2].add(new Edge(3,3));

		graph[3].add(new Edge(0,8));
		graph[3].add(new Edge(1,3));
		graph[3].add(new Edge(2,3));
		graph[3].add(new Edge(4,6));

		graph[4].add(new Edge(3,6));

		return graph;
	}

	public static void main(String[] args) {

		List<Edge> g[] = graphBuilder();

		//		System.out.println(areTheyNeighbours(g, 2 ,3));

		boolean mat[][] = neighboursMatrix(g);

		//		printMat(mat);

		//		findTriangles(mat);

		//		System.out.println(findWeightBetween2Edges(g, 2, 4));

		System.out.println(satisfyTriProperty(mat, g));
	}

}



