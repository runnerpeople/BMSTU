import java.util.*;

class ListsIncidence {
        ArrayList<Integer> a = new ArrayList<>();
        static boolean[] used;
        int[] dist;
}

class QueueNum {
        Queue a = new LinkedList<Integer>();
}       

public class EqDist {
        public static void bfs(ListsIncidence[] graph, QueueNum queue, int v, int y) {
                int j;
                for (j = 0; j < graph.length; j++) {
                        graph[0].used[j] = false;
                        graph[y].dist[j] = -1;
                }
                graph[y].dist[v] = 0;
                graph[y].used[v] = true;
                queue.a.add(v);
                while (queue.a.size() > 0) {
                        int u = (int) queue.a.remove();
                        for (int i = 0; i < graph[u].a.size(); i++) {
                                if (!graph[0].used[graph[u].a.get(i)]) {
                                        graph[0].used[graph[u].a.get(i)] = true;
                                        queue.a.add(graph[u].a.get(i));
                                        graph[y].dist[graph[u].a.get(i)] = graph[y].dist[u] + 1;
                                }

                        }
                }
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n, k, i, v1, v2, j, root, r;
                String s = "";
                n = in.nextInt();
                k = in.nextInt();
                ListsIncidence[] graph = new ListsIncidence[n];
                for (i = 0; i < n; i++) {
                        graph[i] = new ListsIncidence();
                        graph[i].dist = new int[n];
                }
                graph[0].used = new boolean[n];
                for (i = 0; i < k; i++) {
                        v1 = in.nextInt();
                        v2 = in.nextInt();
                        graph[v1].a.add(v2);
                        graph[v2].a.add(v1);
                }
                int help = in.nextInt();
                QueueNum b = new QueueNum();
                for (i = 0; i < help; i++) {
                        root = in.nextInt();
                        bfs(graph, b, root, i);
                }
                for (i = 0; i < n; i++) {
                        r = 1;
                        for (j = 1; j < help; j++) {
                                if (graph[j].dist[i] == -1 || graph[j - 1].dist[i] != graph[j].dist[i]) {
                                        r = 0;
                                        break;
                                }
                        }
                        if (r != 0)
                                s = s + i + " ";
                }
                if (s.equals(""))
                        System.out.println("-");
                else
                        System.out.println(s);
        }   
}
