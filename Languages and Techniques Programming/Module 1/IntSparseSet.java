import java.util.AbstractSet;
import java.util.Iterator;

public class IntSparseSet extends AbstractSet<Integer> {
        private int sparse[],dense[];
        private int n, count;
        private int low, high;

        public IntSparseSet(int low,int high) {
                n=high-low;
                this.low=low;
                this.high=high;
                sparse = new int[n];
                dense = new int[n];
                count = 0;
        }

        public boolean contains(int x) {
                if ((dense[x-low]<high && dense[x-low]>=low) && dense[sparse[x-low]]==x)
                        return true;
                else return false;
         }

        public boolean add(Integer x) {
                if ((x<high && x>=low) && contains(x)==false) {
                        dense[count] = x;
                        sparse[x - low] = count++;
                        return true;
                }
                else return false;
        }

        public boolean remove(Integer x) {
                if ((x>=low && x < high) && contains(x)==true) {
                        dense[sparse[x - low]] = dense[--count];
                        sparse[dense[count] - low] = sparse[x - low];
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

        private class IntIterator implements Iterator<Integer> {
                private int pos;

                public IntIterator() {
                        pos=0;
                }

                public boolean hasNext() {
                        return pos < count;
                }

                public Integer next() {
                        return dense[pos++];
                }

                public void remove() {
                        IntSparseSet.this.remove(dense[pos-1]);
                }

}


    public Iterator<Integer> iterator() {
        return new IntIterator();
    }
}
