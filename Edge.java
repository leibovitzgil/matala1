public class Edge implements Comparable<Edge> {
			int to;
			double weight;

			public Edge(int to, double weight) {
				this.to = to;
				this.weight = weight;
			}

			public Edge(Edge e){
				this.to = e.to;
				this.weight = e.weight;
			} 

			public double getWeight() {
				return weight;
			}

			public void setWeight(double weight) {
				this.weight = weight;
			}

			@Override
			public int compareTo(Edge ed) {
				return Double.compare(this.weight,ed.weight );
			}
}
