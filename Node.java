
public class Node implements Comparable<Node> {

			int node;
			double shortestPath, parent;

public Node(int node, double shortestPathWeight, double parent) {
				this.node = node;
				this.shortestPath = shortestPathWeight;
				this.parent = parent;
			}
			public Node(Node n) {
				this.node = n.node;
				this.shortestPath = n.shortestPath;
				this.parent = n.parent;
			}

			@Override
			public int compareTo(Node o) {
				return Double.compare(shortestPath, o.shortestPath);
			}

}
