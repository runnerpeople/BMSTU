import java.util.Iterator;
import java.util.regex.*;

class StringWord5 implements Iterable<String> {
	private StringBuilder s;
	
	public StringWord5(StringBuilder s) {
		this.s=s;
	}
	
	public Iterator<String> iterator() {
		return new StringIterator();
	}
 
	private class StringIterator implements Iterator<String> {
		private int pos;
		
		public StringIterator() {
			pos=0;
		}
		
		public boolean hasNext() {
			return (pos+5)<s.length();
		}

		public String next() {
                    	for (; s.charAt(pos) == ' ' && pos < s.length(); pos++) ;
                    	String help = s.substring(pos, pos + 5);
                    	pos += 5;
                    	return help;
                }
	}
}
public class Test2 {
	public static void main(String[] args) {
		StringBuilder b = new StringBuilder("abcdf  sdfgh    yruem fhkss        fjshj hjhjh jkjkj   ");
		StringWord5 a = new StringWord5(b);
		for(String s : a)
			System.out.println(s);
	}
}
