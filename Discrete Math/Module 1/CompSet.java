import java.util.*;

class ListsIncidence {
        ArrayList<Integer> a = new ArrayList<>();
        static int[] used;
}

public class CompSet {
        public static void dfs(ListsIncidence[] graph,int v) {
                graph[v].used[v]=0;
                for(int i=0;i<graph[v].a.size();i++) {
                        if (graph[v].used[graph[v].a.get(i)]==1)
                                dfs(graph,graph[v].a.get(i));
                }
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n,k,i,v1,v2,compset=0,j;
                n=in.nextInt();
                k=in.nextInt();
                ListsIncidence[] graph = new ListsIncidence[n];
                for(i=0;i<n;i++) {
                        graph[i]=new ListsIncidence();
                }
                graph[0].used = new int[n];
                for(j=0;j<n;j++)
                        graph[0].used[j]=1;
                for(i=0;i<k;i++) {
                        v1=in.nextInt();
                        v2=in.nextInt();
                        graph[v1].a.add(v2);
                        graph[v2].a.add(v1);
                }
                for(i=0;i<n;i++) {
                        if(graph[i].used[i]==1) {
                                dfs(graph, i);
                                compset++;
                        }
                }
                System.out.println(compset);
        }
}

