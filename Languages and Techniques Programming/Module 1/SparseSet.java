import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SparseSet<T extends Hintable> extends AbstractSet<T> {

        private ArrayList<T> dense;
        private int n, count;

        public SparseSet() {;
                dense = new ArrayList<>();
                count = 0;
        }

        public boolean contains(T x) {
                if (count>0 && dense.get(x.hint())==x)
                        return true;
                else return false;
        }

        public boolean add(T x) {
                if (!contains(x)) {
                        dense.add(x);
                        x.setHint(count++);
                        return true;
                }
                else return false;
        }

        public boolean remove(T x) {
                if (contains(x)) {
                        dense.set(x.hint(),dense.get(--count));
                        dense.get(count).setHint(x.hint());
                        return true;
                }
                else return false;
        }

        public void clear() {
                n=0;
                count=0;
        }

        public int size() {
                return count;
        }

        private class TIterator implements Iterator<T> {
                private int pos;

                public TIterator() {
                        pos=0;
                }

                public boolean hasNext() {
                        return pos < count;
                }

                public T next() {
                        return dense.get(pos++);
                }

                public void remove() {
                        SparseSet.this.remove(dense.get(pos-1));
                }

        }
        
        public Iterator<T> iterator() {
                return new TIterator();
        }
}
