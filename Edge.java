

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

		public int getTo() {
			return to;
		}

		public void setTo(int to) {
			this.to = to;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		@Override
		public int compareTo(Edge a) {
			return Double.compare(this.weight,a.weight );
		}

		@Override
		public String toString() {
			return "Edge [to=" + to + ", weight=" + weight + "]";
		}
		
		
	}
