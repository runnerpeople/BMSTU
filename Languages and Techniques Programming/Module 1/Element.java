public class Element<T> {
        private Element<T> parent = this;
	private T v;
	private int rank;

	public Element(T x) {
		v = x;
		rank = 0;
	}

	private Element<T> findset() {
		if (this.parent == this) {
			return this;
		}
		return this.parent = this.parent.findset();
	}
	
	public T x() {
		return v;
	}

	public Boolean equivalent(Element<T> elem) {
		return this.findset() == elem.findset();
	}

	public void union(Element<T> elem) {
	
		Element<T> x = this.findset(); 
		Element<T> y = elem.findset();
		if (x != y) {		
			if (x.rank < y.rank) {
				Element<T> buf = x;
				x = y;
				y = buf;
			} 
			y.parent = x;
			if (x.rank == y.rank) {
				++x.rank;
			}
		}
	}
} 
