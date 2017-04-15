import java.util.ArrayList;
import java.util.Scanner;

class ListsIncidence {
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> length =new ArrayList<>();
}

class Vertex {
        int index,key,value,indexofvertex;
}

class MyPriorityQueue {
        int nel;
        int size;
        Vertex[] arr;

        public MyPriorityQueue(int n) {
                size=n;
                arr = new Vertex[n];
                nel=0;
        }
}

public class Prim {
        public static void heapify(Vertex[] help,int pos,int nel) {
                int left,right,j;
                while(true) {
                        left=2*pos+1;
                        right=left+1;
                        j=pos;
                        if ((left<nel) && (help[pos].key > help[left].key))
                                pos=left;
                        if ((right<nel) && (help[pos].key > help[right].key))
                                pos=right;
                        if (help[pos].key == help[j].key)
                                break;
                        Vertex buf=help[pos];
                        help[pos]=help[j];
                        help[j]=buf;
                        help[pos].index=pos;
                        help[j].index=j;
                }
        }

        public static void insert(MyPriorityQueue queue,Vertex[] help, int ptr) {
                int i=queue.nel;
                queue.nel++;
                queue.arr[i]=help[ptr];
                while(i>0 && (queue.arr[(i-1)/2].key > queue.arr[i].key)) {
                        Vertex buf =queue.arr[(i-1)/2];
                        queue.arr[(i-1)/2]=queue.arr[i];
                        queue.arr[i]=buf;
                        queue.arr[i].index=i;
                        i=(i-1)/2;
                }
                queue.arr[i].index=i;
        }

        public static Vertex extractmin(MyPriorityQueue queue) {
                Vertex ptr=queue.arr[0];
                queue.nel--;
                if(queue.nel>0) {
                        queue.arr[0]=queue.arr[queue.nel];
                        queue.arr[0].index=0;
                        heapify(queue.arr,0,queue.nel);
                }
                return ptr;
        }

        public static void increasekey(MyPriorityQueue queue,Vertex help, int k) {
                int i=help.index;
                help.key=k;
                while(i>0 && (queue.arr[(i-1)/2].key > k)) {
                        Vertex buf =queue.arr[(i-1)/2];
                        queue.arr[(i-1)/2]=queue.arr[i];
                        queue.arr[i]=buf;
                        queue.arr[i].index=i;
                        i=(i-1)/2;
                }
                help.index=i;
        }

        public static int MST_Prim(ListsIncidence[] result, Vertex[] help, MyPriorityQueue queue) {
                int i=0,j=0,count=0;
                for(int k=0;k<help.length;k++) {
                        help[k].indexofvertex=k;
                }
                Vertex u = help[0];
                while(true) {
                        u.index=-2;
                        for(i=0;i<result[j].a.size();i++) {
                                Vertex g = help[result[j].a.get(i)];
                                int length=result[j].length.get(i);
                                if (help[result[j].a.get(i)].index == -1) {
                                        Vertex w = g;
                                        w.key = length;
                                        w.value=j;
                                        insert(queue, help, result[j].a.get(i));
                                }
                                else if (g.index != -2 && length < g.key) {
                                        g.value=j;
                                        increasekey(queue,g,length);
                                }
                        }
                        if (queue.nel<=0) {
                                break;
                        }
                        u=extractmin(queue);
                        count+=u.key;
                        j=u.indexofvertex;
                }
                return count;
        }
        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n = in.nextInt();
                int k = in.nextInt();
                int i, j, count=0;
                ListsIncidence[] result = new ListsIncidence[n];
                Vertex[] help = new Vertex[n];
                for (i = 0; i < n; i++) {
                        result[i] = new ListsIncidence();
                        help[i]=new Vertex();
                        help[i].index=-1;
                }
                for(i=0;i<k;i++) {
                        int x=in.nextInt();
                        int y=in.nextInt();
                        int length=in.nextInt();
                        result[x].a.add(y);
                        result[x].length.add(length);
                        result[y].a.add(x);
                        result[y].length.add(length);
                }
                MyPriorityQueue queue=new MyPriorityQueue(n);
                count=MST_Prim(result,help,queue);
                System.out.println(count);
        }
}
