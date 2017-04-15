import java.util.*;

class Graph {
    int value;
    int operand;
    int n=0;
    boolean operand_;
    boolean used = false;
    Graph sdom=this;
    Graph label=this;
    Graph parent = null;
    Graph ancestor = null;
    Graph dom = null;
    ArrayList<Graph> bucket = new ArrayList<Graph>();
    ArrayList<Graph> a = new ArrayList<Graph>();
    ArrayList<Graph> b = new ArrayList<Graph>();
}



public class Loops {
    public static void dfs_(Graph graph) {
       graph.n=Loops.N++;
       graph.used = true;
       for(Graph vertex: graph.a) {
           if (vertex.used==false) {
               vertex.parent=graph;
               dfs_(vertex);
           }
       }

    }
    public static void dfs(ArrayList<Graph> graph) {
        dfs_(graph.get(0));
        for(int i = 0; i < graph.size(); i++) {
            if (graph.get(i).used==false) {
                graph.remove(i);
                i--;
            }
            else {
                for(int j = 0; j < graph.get(i).b.size(); j++) {
                    if (!graph.get(i).b.get(j).used) {
                        graph.get(i).b.remove(j);
                        j--;
                    }
                }
            }
        }
    }

    public static int findloops(ArrayList<Graph> graph) {
        int result=0;
        for(int i=0;i<graph.size();i++)  {
            for(int j=0;j<graph.get(i).b.size();j++) {
                while(graph.get(i).b.get(j)!=graph.get(i) && graph.get(i).b.get(j)!=null) {
                    graph.get(i).b.set(j, graph.get(i).b.get(j).dom);
                }
                if (graph.get(i).b.get(j)==graph.get(i)) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }

    public static int N=0;

    public static Graph findmin(ArrayList<Graph> graph, Graph v) {
        if (v.ancestor==null)
            return v;
        else {
            Stack<Graph> stack = new Stack<Graph>();
            Graph u = v;
            while (u.ancestor.ancestor!=null) {
                stack.push(u);
                u = u.ancestor;
            }
            while (!stack.isEmpty()) {
                v = stack.pop();
                if (v.ancestor.label.sdom.n <v.label.n)
                    v.label=v.ancestor.label;
                v.ancestor=u.ancestor;
            }
            return v.label;
        }
    }

    public static void dominators(ArrayList<Graph> graph) {
        int n=graph.size()-1;
        for(int i=n;i>0;i--) {
            Graph w = graph.get(i);
            for(Graph v : w.b) {
                Graph u = findmin(graph,v);
                if (u.sdom.n<w.sdom.n)
                    w.sdom=u.sdom;
            }
            w.ancestor = w.parent;
            w.sdom.bucket.add(w);
            for(Graph v : w.parent.bucket) {
                Graph u = findmin(graph, v);
                if (u.sdom==v.sdom) {
                    v.dom = w.parent;
                }
                else v.dom=u;
            }
            w.parent.bucket.clear();
        }
        n++;
        for(int i=1;i<n;i++) {
            Graph w = graph.get(i);
            if(w.dom!=w.sdom)
                w.dom=w.dom.dom;
        }
        graph.get(0).dom=null;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt();
        ArrayList<Graph> loop = new ArrayList<Graph>();
        ArrayList<String> programm = new ArrayList<String>();
        Map<Integer, Integer> g = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (int i = 0; i < k; i++, loop.add(new Graph())) ;
        in.nextLine();
        for (int i = 0; i < k; i++) {
            programm.add(in.nextLine());
            ArrayList<String> buf = new ArrayList<String>(Arrays.asList(programm.get(i).split(" ")));
            loop.get(i).value = Integer.parseInt(buf.get(0));
            g.put(loop.get(i).value,i);
            if((buf.get(1).equals("ACTION") || buf.get(1).equals("BRANCH")) && i <k-1) {
                loop.get(i).a.add(loop.get(i+1));
                loop.get(i+1).b.add(loop.get(i));
            }
            if (buf.size()!=2) {
                loop.get(i).operand=Integer.parseInt(buf.get(2));
                loop.get(i).operand_=true;
            }
            else {
                loop.get(i).operand_=false;
                loop.get(i).operand=-1;
            }
        }
        for(Graph graph:loop) {
            if (graph.operand_ && graph.operand!=-1) {
                graph.a.add(loop.get(g.get(graph.operand)));
                loop.get(g.get(graph.operand)).b.add(graph);
            }
        }
        dfs(loop);
        Collections.sort(loop, new Comparator<Graph>() {
            @Override
            public int compare(Graph o1, Graph o2) {
                if (o1.n>o2.n)
                    return 1;
                else if (o1.n<o2.n)
                    return -1;
                else
                    return 0;
            }
        });
        dominators(loop);
        int loops = findloops(loop);
        System.out.println(loops);

    }
}
