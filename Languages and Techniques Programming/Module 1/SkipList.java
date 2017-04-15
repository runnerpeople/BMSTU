import java.util.*;


public class SkipList<K extends Comparable<K>, V> extends AbstractMap<K, V> {
        private Node head;
        private final Random random;
        private int size =0;
        private int levels;

        private class Node implements Map.Entry<K,V> {
                Map.Entry a;
                ArrayList<Node> next;

                public Node(Map.Entry a,int levels) {
                        this.a=a;
                        next = new ArrayList<>();
                        for(int i=0;i<levels;i++) {
                                next.add(null);
                        }
                }

                public ArrayList<Node> getNext() {
                        return next;
                }

                @Override
                public K getKey() {
                        return (K)a.getKey();
                }

                @Override
                public V getValue() {
                        return (V)a.getValue();
                }

                @Override
                public V setValue(V value) {
                        a.setValue(value);
                        return null;
                }
        }

        public SkipList(int levels) {
                random = new Random();
                this.levels=levels;
                head = new Node(null,levels);
        }


        public boolean isEmpty() {
                return size == 0;
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
                return new NodeSet(size);
        }

        private class NodeSet<K,V> extends AbstractSet {
                private int size;

                private class NodeIterator implements Iterator {
                        private Node list;

                        public NodeIterator() {
                                list=SkipList.this.head;
                        }

                        @Override
                        public boolean hasNext() {
                                return list.getNext().get(0) != null;
                        }

                        @Override
                        public Node next() {
                                list = list.getNext().get(0);
                                return list;
                        }

                        public void remove() {
                                SkipList.this.remove(list.getKey());
                        }
                }

                public Iterator iterator() {
                        return new NodeIterator();
                }

                @Override
                public int size() {
                        return size;
                }

                public NodeSet(int size) {
                        this.size=size;
                }
        }

        private ArrayList<Node> skip(K k) {
                Node x = head;
                ArrayList<Node> help = new ArrayList<>();
                for(int i=0;i<levels;i++) {
                        help.add(null);
                }
                for(int i=levels-1;i>=0;i--) {
                        while(x.getNext().get(i) != null && x.getNext().get(i).getKey().compareTo(k)<0)
                                x=x.getNext().get(i);
                        help.set(i,x);
                }
                return help;
        }

        public Node Succ(Node x) {
                return x.getNext().get(0);
        }

        public boolean containsKey(Object k) {
                ArrayList<Node> help = skip((K) k);
                Node x = Succ(help.get(0));
                boolean verdict =  x != null && x.getKey().equals((K) k);
                return verdict;
        }

        public V put(K k, V v) {
                Map.Entry<K, V> a = new AbstractMap.SimpleEntry<K, V>(k, v);
                Node x = new Node(a, levels);
                ArrayList<Node> help = skip(k);
                if (help.get(0).getNext().get(0) != null && help.get(0).getNext().get(0).getKey().equals((K) k)) {
                        V buf = (V)help.get(0).getNext().get(0).a.getValue();
                        help.get(0).getNext().get(0).a.setValue(v);
                        return buf;
                }
                else {
                        V n = get(k);
                        int r = random.nextInt() << 1, i = 0;
                        for (; i < levels && r % 2 == 0; i++, r /= 2) {
                                x.getNext().set(i, help.get(i).getNext().get(i));
                                help.get(i).getNext().set(i, x);
                        }
                        while (i < levels) {
                                x.getNext().set(i, null);
                                i++;
                        }
                        size++;
                        return n;
                }
        }

        public V get(Object k) {
                ArrayList<Node> help = skip((K)k);
                Node x=Succ(help.get(0));
                if (x != null && x.getKey().equals((K) k))
                        return x.getValue();
                else
                        return null;
        }

        public V remove(Object k) {
                ArrayList<Node> help = skip((K) k);
                Node x = Succ(help.get(0));
                if (x == null || !x.getKey().equals((K)k))
                        return null;
                for (int i = 0; i < levels && help.get(i).getNext().get(i) == x; i++)
                        help.get(i).getNext().set(i,x.getNext().get(i));
                size--;
                return (V)x.getValue();
        }
}
