import java.util.*;

class ListsIncidence {
        ArrayList<Integer> a = new ArrayList<>();
        static int[] used;
        static int[] parents;
        static int[] comp;
}

class QueueNum {
        Queue a = new LinkedList<Integer>();
}

public class BridgeNum {
        public static void dfs1(ListsIncidence[] graph, QueueNum queue,int v) {
                graph[v].used[v]=0;
                queue.a.add(v);
                for(int i=0;i<graph[v].a.size();i++) {
                        if(graph[v].used[graph[v].a.get(i)]==1) {
                                graph[i].parents[graph[v].a.get(i)]=v;
                                dfs1(graph,queue,graph[v].a.get(i));
                        }
                }
        }

        public static int dfs2(ListsIncidence[] graph, QueueNum queue,int component) {
                int v;
                while(queue.a.size()>0) {
                        v= (int)queue.a.remove();
                        if(graph[v].comp[v]==-1) {
                                VisitVertex(graph, queue, v, component);
                                component+=1;
                        }
                }               
                return component;
        }

        public static void VisitVertex(ListsIncidence[] graph, QueueNum queue,int v,int component) {
                graph[v].comp[v]=component;
                for(int i=0;i<graph[v].a.size();i++) {
                        if (graph[v].comp[graph[v].a.get(i)]==-1 && graph[v].parents[graph[v].a.get(i)]!=v)
                                VisitVertex(graph, queue, graph[v].a.get(i), component);
                }
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n,k,i,v1,v2,component=1,j;
                n=in.nextInt();
                k=in.nextInt();
                ListsIncidence[] graph = new ListsIncidence[n];
                for(i=0;i<n;i++) {
                        graph[i]=new ListsIncidence();
                }
                graph[i-1].used = new int[n];
                graph[i-1].parents = new int[n];
                graph[i-1].comp = new int[n];
                for(j=0;j<n;j++) {
                        graph[i-1].used[j] = 1;
                        graph[i-1].comp[j] = -1;
                        graph[i-1].parents[j] = -1;
                }
                for(i=0;i<k;i++) {
                        v1=in.nextInt();
                        v2=in.nextInt();
                        graph[v1].a.add(v2);
                        graph[v2].a.add(v1);
                }
                QueueNum b = new QueueNum();
                for(i=0;i<n;i++) {
                        if(graph[i].used[i]==1) {
                                component--;
                                dfs1(graph, b, i);
                        }
                        component = dfs2(graph, b, component);
                }
                System.out.println(component-1);
        }
}
