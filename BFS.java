import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Breadth-first search
 * Complexity: O(|V|+|E|)
 */
public class BFS {
	private final int infinity = Integer.MAX_VALUE;
	public static final int white = 0 , gray = 1, black = 2;
	private int[] color, dist, pred;
	private ArrayList<Integer>[] graph;
	private ArrayBlockingQueue<Integer> queue;
	private int size;
	
	public BFS(ArrayList<Integer>[] graph) {
		this.graph = copy(graph);
		size = graph.length;
		init();
	}
	
	private void init() {
		color = new int[size];
		dist = new int[size];
		pred = new int[size];
		Arrays.fill(dist, -1);
		Arrays.fill(pred, -1);
	}
	
	public void AlgoBFS(int s) {
		init();
		queue = new ArrayBlockingQueue<Integer>(size);
		queue.add(s);
		color[s] = gray;
		dist[s] = 0;
		while(!queue.isEmpty()) {
			int u = queue.poll();
			for(int v : graph[u]) {
				if(color[v] == white) {
					color[v] = gray;
					dist[v] = dist[u] + 1;
					pred[v] = u;
					queue.add(v);
				}
			}
			color[u] = black;
		}
	}
	
	private ArrayList<Integer>[] copy(ArrayList<Integer>[] g) {
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] copy = new ArrayList[g.length];
		for (int i = 0; i < g.length; i++) {
			copy[i] = new ArrayList<Integer>();
			for (int j = 0; j < g[i].size(); j++) {
				copy[i].add(g[i].get(j));
			}
		}
		return copy;
	}
	
	public String bestPath(int src, int dest) {
		if(src >= size || dest >= size || src < 0 || dest < 0) return "";
		AlgoBFS(src);
		String ans = "";
		while(dest != src) {
			ans = "->" + dest + ans;
			dest = pred[dest];
		}
		ans = src + ans;
		return ans;
	}
	
	/** the diameter of the tree is the largest of all shortest-path
	 * distances in the tree. 
	 * */
	/**
	 * @return true if the graph is connected,
	 * otherwise return false
	 */
	public boolean isConnected(){
		boolean ans = true;
		for (int i=1; ans && i<size; i++){
			if  (dist[i] == infinity) ans = false;
		}
		return ans;
	}
	
	public int diameter(){
		int diam;
		if(!isConnected()) return -1;
		if (size == 2) return 1;
		if (size < 2) return -1;
		AlgoBFS(0);
		int ind = maxDistanceIndex();
		AlgoBFS(ind);
		diam = maxDistanceIndex();
		return dist[diam];
	}

	
	private int maxDistanceIndex(){
		int index = 0;
		for (int i=1; i<dist.length; i++){
			if (dist[index] < dist[i]) index = i;
		}
		return index;
	}
	
	public int radius(){
		if(!isConnected()) return -1;
		int radius=Integer.MAX_VALUE;
		int tempRadius=Integer.MAX_VALUE;
		for(int i=0; i<graph.length; i++){
			AlgoBFS(i);
			tempRadius =maxDistance();
			if(tempRadius<radius) radius=tempRadius;
		}
		return radius;
	}
	
	private int maxDistance(){
		int index = 0;
		for (int i=0; i<dist.length; i++){
			if (dist[index] < dist[i]) index = i;
		}
		return dist[index];
	}
}
