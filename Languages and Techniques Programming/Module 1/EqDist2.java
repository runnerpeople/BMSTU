import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by great on 09.10.15.
 */
class Graph {
    ArrayList<Graph> inc_vertex = new ArrayList<>();
    int number_vertex;
    boolean used;
    public Graph(int number_vertex) {
        this.number_vertex=number_vertex;
    }
}

public class EqDist2 {

    static int[][] dist;

    public static void bfs(ArrayList<Graph> graph,int root,int i) {
        Queue<Graph> q = new LinkedList<>();
        Graph vertex = graph.get(root);
        dist[i][vertex.number_vertex] = 0;
        vertex.used = true;
        q.add(vertex);
        while (q.size()>0) {
            Graph u = q.poll();
            for (Graph w :u.inc_vertex) {
                if (!(w.used)) {
                    w.used = true;
                    q.add(w);
                    dist[i][w.number_vertex] = dist[i][u.number_vertex] + 1;
                }
            }
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int i,n,k,number_roots,root,j;
        n = in.nextInt();
        k = in.nextInt();
        ArrayList<Graph> graph = new ArrayList<>();
        for(i=0;i<n;i++)
            graph.add(new Graph(i));
        for(i=0;i<k;i++) {
            int vertex_in,vertex_out;
            vertex_in = in.nextInt();
            vertex_out = in.nextInt();
            graph.get(vertex_in).inc_vertex.add(graph.get(vertex_out));
            graph.get(vertex_out).inc_vertex.add(graph.get(vertex_in));
        }
        number_roots = in.nextInt();
        dist= new int[number_roots][n];
        for(i=0;i<number_roots;i++) {
            root = in.nextInt();
            for(j=0;j<n;j++) {
                graph.get(j).used = false;
                dist[i][j] = Integer.MIN_VALUE;
            }
            bfs(graph,root,i);
        }
        boolean flag=false;
        for(i=0;i<n;i++) {
            for(j=1;j<number_roots;j++)
                if (dist[j][i]==Integer.MIN_VALUE || dist[j-1][i] != dist[j][i])
                    break;
            if (j==number_roots && (dist[j-1][i]!=Integer.MIN_VALUE || dist[j-1][i] == dist[j-2][i])) {
                flag = true;
                System.out.print(i + " ");
            }
        }
        if (!(flag))
            System.out.println("-");
        else
            System.out.println();
    }
}


