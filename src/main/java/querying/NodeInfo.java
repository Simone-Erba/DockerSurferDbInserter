package querying;

import java.io.Serializable;
import java.util.List;

import org.neo4j.graphdb.Node;

public class NodeInfo implements Serializable {
	List<String> l;
	long n;
	public List<String> getL() {
		return l;
	}
	public void setL(List<String> l) {
		this.l = l;
	}
	public long getN() {
		return n;
	}
	public void setN(long n) {
		this.n = n;
	}
	public NodeInfo(List<String> l, long n) {
		super();
		this.l = l;
		this.n = n;
	}
}
