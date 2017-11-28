
package utilities;

/**  *  */
/** * @author Alexander Seitz * */
public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A,B>>{
	private A first;
	private B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return this.first;
	}

	public B getSecond() {
		return this.second;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Pair<A, B> o) {
		int res = this.first.compareTo(o.getFirst());
		if(res == 0){
			res = this.second.compareTo(o.getSecond());
		}
		return res;
	}
	
	public String toString() {
		return "("+this.first.toString()+","+this.second.toString()+")";
	}
}
