import java.util.*;

class OrgListsIncident {
        ArrayList<Integer> a =new ArrayList<>();
        int T1;
        int comp;
        int low;
}

class Condensation {
        ArrayList<Integer> a = new ArrayList<>();
        int min;
        static boolean[] use;
}

public class GraphBase {

        public static int time = 1;
        public static int count = 1;

        public static void Tarjan(OrgListsIncident[] graph) {
                for(int i=0;i<graph.length;i++) {
                        graph[i].T1=0;
                        graph[i].comp=0;
                }
                Stack<OrgListsIncident> s = new Stack<>();
                for(int i=0;i<graph.length;i++)
                        if(graph[i].T1==0)
                                VisitVertex_Terjan(graph, i, s);

        }

        public static void VisitVertex_Terjan(OrgListsIncident[] graph, int i, Stack<OrgListsIncident> s) {
                OrgListsIncident u;
                graph[i].T1 = time;
                graph[i].low = time;
                GraphBase.time++;
                s.push(graph[i]);
                for(int j=0;j<graph[i].a.size();j++) {
                        if (graph[graph[i].a.get(j)].T1==0)
                                VisitVertex_Terjan(graph,graph[i].a.get(j),s);
                        if (graph[graph[i].a.get(j)].comp==0 && graph[i].low > graph[graph[i].a.get(j)].low)
                                graph[i].low=graph[graph[i].a.get(j)].low;
                }
                if (graph[i].T1 == graph[i].low) {
                        do {
                                u = s.pop();
                                u.comp = GraphBase.count;
                        }
                        while (!(u.equals(graph[i])));
                        GraphBase.count++;
                }
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n,k,i,v1,v2,j;
                n=in.nextInt();
                k=in.nextInt();
                OrgListsIncident[] graph = new OrgListsIncident[n];
                for(i=0;i<n;i++)
                        graph[i] = new OrgListsIncident();
                for(j=0;j<k;j++) {
                        v1 = in.nextInt();
                        v2 = in.nextInt();
                        graph[v1].a.add(v2);
                }
                Tarjan(graph);
                Condensation[] base = new Condensation[GraphBase.count];                
                for(i=0;i<GraphBase.count;i++)
                        base[i]=new Condensation();
                base[0].use = new boolean[count];
                for(i=0;i<GraphBase.count;i++)
                        base[0].use[i]=true;
                for(i=0;i<n;i++) {
                        if (base[graph[i].comp].a.size()>0)
                                base[graph[i].comp].a.add(i);
                        else {
                                base[graph[i].comp].min=i;
                                base[graph[i].comp].a.add(i);
                        }
                }
                for(j=0;j<n;j++) {
                        for (i = 0; i < graph[j].a.size(); i++) {
                                if (graph[j].comp != graph[graph[j].a.get(i)].comp)
                                        base[0].use[graph[graph[j].a.get(i)].comp] = false;
                        }
                }
                for(i=1;i<GraphBase.count;i++)
                        if(base[0].use[i])
                                System.out.print(base[i].min + " ");
        }
}

