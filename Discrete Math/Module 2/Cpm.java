import java.util.*;

class Graph {
    String name;
    int dist;
    int totaldist;
    ArrayList<Graph> a = new ArrayList<>();
    int color=0;

    public Graph(String name,int dist) {
        this.name = name;
        this.dist = dist;
        this.totaldist=dist;
    }
    ArrayList<Graph> parent = new ArrayList<>();
    int T1=0;
    int comp=0;
    int low;
    int help=0;
}

public class Cpm {

    public static int time = 1;
    public static int count = 1;


    public static void Tarjan(Graph[] graph) {
        Stack<Graph> s = new Stack<>();
        for(int i=0;i<graph.length;i++)
            if(graph[i].T1==0)
                VisitVertex_Terjan(graph[i], s);

    }

    public static void VisitVertex_Terjan(Graph i, Stack<Graph> s) {
        Graph u;
        ArrayList<Graph> buf;
        i.T1 = time;
        i.low = time;
        Cpm.time++;
        s.push(i);
        for(int j=0;j<i.a.size();j++) {
            if (i.a.get(j).T1==0)
                VisitVertex_Terjan(i.a.get(j),s);
            if (i.a.get(j).comp==0 && i.low > i.a.get(j).low)
                i.low=i.a.get(j).low;
        }
        if (i.T1 == i.low) {
            buf = new ArrayList<>();
            do {
                u = s.pop();
                u.comp = Cpm.count;
                buf.add(u);
            }
            while (!(u.equals(i)));
            if (buf.size()>1) {
                for(Graph bad_vertex : buf) {
                    bad_vertex.color = -1;
                    cycle(bad_vertex);
                }
            }
            if (buf.size()==1) {
                for (Graph b : buf.get(0).a)
                    if (b.equals(buf.get(0)))
                        b.color=-1;
            }

            Cpm.count++;
        }
    }

    public static void cycle(Graph a) {
        a.color=-1;
        a.help=-1;
        a.totaldist=0;
        for(Graph parent : a.a) {
            if (parent.color==0) {
                cycle(parent);
            }
        }
    }

    public static void RelaxModified(Graph u,Graph w) {
        if (u.totaldist<w.totaldist + u.dist) {
            u.totaldist = w.totaldist + u.dist;
            u.parent.clear();
            u.parent.add(w);
        }
        else if (u.totaldist==w.totaldist+u.dist) {
            u.parent.add(w);
        }
    }

    public static void BellmanFord(LinkedList<Graph> graph) {
        while (!graph.isEmpty()) {
            Graph u = graph.remove();
            for(Graph w : u.a) {
                if (w.help>=0) {
                    RelaxModified(w, u);
                    if (--w.help == 0)
                        graph.add(w);
                }
            }
        }
    }

    public static void maxcolor(Graph a) {
        a.color=1;
        for(Graph parent : a.parent) {
                maxcolor(parent);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String lines = "";
        ArrayList<String> line = new ArrayList<>();
        while(in.hasNextLine()) {
            lines += in.nextLine().replaceAll(" ","");
            if (lines.charAt(lines.length()-1)==';' || !in.hasNextLine()) {
                if (!in.hasNextLine())
                    line.add(lines.substring(0,lines.length()));
                else {
                    line.add(lines.substring(0,lines.length()-1));
                }
                lines="";
            }
        }
        Map<String,Graph> graph = new HashMap<>();
        ArrayList<Graph> map_graph = new ArrayList<>();
        for(String str : line) {
            ArrayList<Graph> vertex = new ArrayList<>();
            for(String tok : str.split("<")) {
                ArrayList<String> token = new ArrayList<>(Arrays.asList(tok.split("\\(|\\)")));
                if (token.size() == 1) {
                    vertex.add(graph.get(token.get(0)));
                } else {
                    Graph v = new Graph(token.get(0), Integer.parseInt(token.get(token.size() - 1)));
                    graph.put(token.get(0), v);
                    map_graph.add(v);
                    vertex.add(graph.get(token.get(0)));
                }
            }
            for(int i=0;i<=vertex.size()-2;i++) {
                vertex.get(i).a.add(vertex.get(i+1));
                vertex.get(i+1).help++;
            }
        }
        Tarjan(map_graph.toArray(new Graph[1]));
        LinkedList<Graph> graph_modified = new LinkedList<>();
        for (Graph vertex : map_graph) {
            if (vertex.help==0) {
                graph_modified.add(vertex);
            }
        }
        BellmanFord(graph_modified);
        map_graph.sort(new Comparator<Graph>() {
            @Override
            public int compare(Graph o1, Graph o2) {
                return o2.totaldist-o1.totaldist;
            }
        });
        int maxc=1;
        for (; maxc < map_graph.size(); maxc++)
            if (map_graph.get(0).totaldist != map_graph.get(maxc).totaldist)
                break;
        for (int i = 0; i < maxc; i++)
           if (map_graph.get(i).color != -1)
               maxcolor(map_graph.get(i));
        System.out.println("digraph {");
        Set<Graph> result = new HashSet<>(map_graph);
        for (Graph vertex :result) {
            if (vertex.color == 0)
                System.out.println("  " + vertex.name + " " + "[label = \"" + vertex.name + "(" + vertex.dist + ")" + "\"]");
            else if (vertex.color == -1)
                System.out.println("  " + vertex.name + " " + "[label = \"" + vertex.name + "(" + vertex.dist + ")" + "\", color = blue]");
            else if (vertex.color == 1)
                System.out.println("  " + vertex.name + " " + "[label = \"" + vertex.name + "(" + vertex.dist + ")" + "\", color = red]");
        }
        for (Graph vertex :result) {
            Set<Graph> edge = new HashSet<>(vertex.a);
            for(Graph vertex_out : edge) {
                if (vertex.color==1 && vertex_out.color==1 && vertex_out.parent.contains(vertex))
                    System.out.println("  " + vertex.name + " " + "-> " + vertex_out.name + " [color = red]");
                else if (vertex_out.color==-1 && vertex.color==-1) {
                    System.out.println("  " + vertex.name + " " + "-> " + vertex_out.name + " [color = blue]");
                }
                else System.out.println("  " + vertex.name + " " + "-> " + vertex_out.name);
            }
        }
        System.out.println("}");
    }
}
