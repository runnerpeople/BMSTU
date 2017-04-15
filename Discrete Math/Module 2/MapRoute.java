import java.util.*;

class Pair {
        int key;
        int value;

        public Pair(int key,int value) {
                this.key=key;
                this.value=value;
        }
}

class Vertex2 {
        int index;
        int key;
        int dist;
        ArrayList<Pair> a = new ArrayList<>();
        Vertex2 parent;

        public Vertex2(int key) {
                dist = Integer.MAX_VALUE;
                this.key=key;
                parent = null;
        }
}

class MyPriorityQueue2 {
        int nel;
        int size;
        Vertex2[] arr;

        public MyPriorityQueue2(int n) {
                size = n;
                arr = new Vertex2[n];
                nel = 0;
        }
}

public class MapRoute {
        public static void heapify(Vertex2[] help,int pos,int nel) {
                int left,right,j;
                while(true) {
                        left=2*pos+1;
                        right=left+1;
                        j=pos;
                        if ((left<nel) && (help[pos].dist > help[left].dist))
                                pos=left;
                        if ((right<nel) && (help[pos].dist > help[right].dist))
                                pos=right;
                        if (help[pos].dist == help[j].dist)
                                break;
                        Vertex2 buf=help[pos];
                        help[pos]=help[j];
                        help[j]=buf;
                        help[pos].index=pos;
                        help[j].index=j;
                }
        }

        public static void insert(MyPriorityQueue2 queue,Vertex2[] help, int ptr) {
                int i=queue.nel;
                queue.nel++;
                queue.arr[i]=help[ptr];
                while(i>0 && (queue.arr[(i-1)/2].dist > queue.arr[i].dist)) {
                        Vertex2 buf =queue.arr[(i-1)/2];
                        queue.arr[(i-1)/2]=queue.arr[i];
                        queue.arr[i]=buf;
                        queue.arr[i].index=i;
                        i=(i-1)/2;
                }
                queue.arr[i].index=i;
        }

        public static Vertex2 extractmin(MyPriorityQueue2 queue) {
                Vertex2 ptr=queue.arr[0];
                queue.nel--;
                if(queue.nel>0) {
                        queue.arr[0]=queue.arr[queue.nel];
                        queue.arr[0].index=0;
                        heapify(queue.arr,0,queue.nel);
                }
                return ptr;
        }

        public static void increasekey(MyPriorityQueue2 queue,Vertex2 help, int k) {
                int i=help.index;
                help.key=k;
                while(i>0 && (queue.arr[(i-1)/2].dist > k)) {
                        Vertex2 buf =queue.arr[(i-1)/2];
                        queue.arr[(i-1)/2]=queue.arr[i];
                        queue.arr[i]=buf;
                        queue.arr[i].index=i;
                        i=(i-1)/2;
                }
                help.index=i;
        }

        public static boolean Relax(Vertex2 u, Vertex2 v) {
                boolean changed;
                if (u.dist == Integer.MAX_VALUE)
                        changed = false;
                else changed = (u.dist + v.key) < v.dist;
                if (changed) {
                        v.dist = u.dist + v.key;
                        v.parent = u;
                }
                return changed;
        }

        public static void Dijkstra(Vertex2[][] a, MyPriorityQueue2 queue) {
                while(queue.nel > 0) {
                        Vertex2 u = extractmin(queue);
                        u.index = -1;
                        for(Pair pair : u.a)
                                if(a[pair.key][pair.value].index != -1 && Relax(u,a[pair.key][pair.value])) {
                                        increasekey(queue,a[pair.key][pair.value],a[pair.key][pair.value].dist);
                                }
                }
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int k = in.nextInt(),v;
                Vertex2[][] a = new Vertex2[k][k];
                MyPriorityQueue2 queue = new MyPriorityQueue2(k*k);
                for(int i=0;i<k;i++) {
                        for (int j = 0; j < k; j++) {
                                a[i][j] = new Vertex2(in.nextInt());                                
                        }
                }
                a[0][0].dist=a[0][0].key;
                for(int i=0;i<k;i++) {
                        for (int j = 0; j < k; j++) {
                                if(i+1<k) {
                                        a[i][j].a.add(new Pair(i+1,j));
                                        a[i+1][j].a.add(new Pair(i,j));
                                }
                                if(j+1<k) {
                                        a[i][j].a.add(new Pair(i,j+1));
                                        a[i][j+1].a.add(new Pair(i,j));
                                }
                                insert(queue,a[i],j);
                        }
                }
                Dijkstra(a,queue);
                System.out.println(a[k-1][k-1].dist);
        }
}

