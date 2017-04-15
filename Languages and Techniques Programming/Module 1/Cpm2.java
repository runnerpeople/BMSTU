import java.util.*;

class Vertex4 {
    String name,color;
    int value;
    int totalvalue;
    int parent_size;
    ArrayList<Vertex4> child = new ArrayList<>();
    ArrayList<Vertex4> max_way = new ArrayList<>();
    ArrayList<Vertex4> parent = new ArrayList<>();

    public Vertex4(String name,int value) {
        this.name=name;
        this.value=value;
        this.totalvalue = value;
        this.color="white";
    }

    @Override
    public String toString() {
        if (color.equals("blue") || color.equals("red"))
            return "\t" + this.name + " " + "[label = \"" + this.name + "(" + this.value  + ")\", color = " + this.color + "]";
        else {
            return "\t" + this.name + " " + "[label = \"" + this.name + "(" + this.value  + ")\"]";
        }
    }
}


public class Cpm2 {

    public static void Relax(Vertex4 u, Vertex4 v) {
        boolean changed = (u.totalvalue + v.value) >= v.totalvalue;
        if (changed) {
            if (u.totalvalue + v.value > v.totalvalue) {
                v.max_way.clear();
                v.totalvalue = u.totalvalue + v.value;
            }
            v.max_way.add(u);
        }
    }

    public static void Dijkstra(Queue<Vertex4> graph) {
        while(!graph.isEmpty()) {
            Vertex4 max_vertex = graph.poll();
            for(Vertex4 inc_max_vertex : max_vertex.child) {
                if(!inc_max_vertex.color.equals("blue"))
                    Relax(max_vertex,inc_max_vertex);
                if (--inc_max_vertex.parent_size==0)
                    graph.add(inc_max_vertex);
            }
        }
    }

    public static void max_way(Vertex4 vertex) {
        vertex.color = "red";
        for(Vertex4 inc_vertex : vertex.max_way) {
            max_way(inc_vertex);
        }
    }

    public static void dfs(List<Vertex4> graph) {
        Queue<Vertex4> answer = new LinkedList<>();
        for (Vertex4 vertex : graph) {
            if (vertex.color.equals("white")) {
                dfs1(answer, vertex);
            }
        }
    }

    public static void cycle(Vertex4 vertex) {
        vertex.color="blue";
        vertex.totalvalue = 0;
        for(Vertex4 inc_vertex : vertex.child)
            if (!inc_vertex.color.equals("blue"))
                cycle(inc_vertex);
    }

    public static void dfs1(Queue<Vertex4> queue, Vertex4 vertex) {
        vertex.color="grey";
        for (Vertex4 inc_vertex : vertex.child) {
            if (inc_vertex.color.equals("white"))
                dfs1(queue, inc_vertex);
            else if (inc_vertex.color.equals("grey"))
                cycle(inc_vertex);
        }
        if (!vertex.color.equals("blue"))
            vertex.color="black";
        queue.add(vertex);
    }

    public static void print_graph(HashMap<String,Vertex4> graph) {
        System.out.println("digraph {");
        for(Vertex4 v : graph.values()) {
            System.out.println(v);
        }
        for(Vertex4 v_in : graph.values()) {
            Set<Vertex4> edge = new HashSet<>(v_in.child);
            for(Vertex4 v_out : edge)
                if(v_in.color.equals("red") && v_out.color.equals("red") && v_out.max_way.contains(v_in))
                    System.out.println("\t" + v_in.name + " -> " + v_out.name + " [color = red]");
                else if (v_in.color.equals("blue") && v_out.color.equals("blue"))
                    System.out.println("\t" + v_in.name + " -> " + v_out.name + " [color = blue]");
                else
                    System.out.println("\t" + v_in.name + " -> " + v_out.name);
        }
        System.out.println("}");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HashMap<String,Vertex4> graph = new HashMap<>();
        for(String line = null; in.hasNextLine();) {
            line = in.nextLine().replaceAll(" ","");
            while (line.indexOf(";")==-1 && in.hasNextLine()) {
                line += in.nextLine().replaceAll(" ", "");
                if (line.indexOf(";")!=-1 || !in.hasNextLine())
                    break;
            }
            if (line.indexOf(";")!=-1)
                line = line.substring(0,line.length()-1);
            ArrayList<Vertex4> way = new ArrayList<>();
            for(String vertices: line.split("<")) {
                int begin_index = vertices.indexOf("(");
                String name = vertices;
                if(begin_index!=-1) {
                    int last_index = vertices.lastIndexOf(")");
                    int value = Integer.parseInt(vertices.substring(begin_index+1, last_index));
                    name = vertices.substring(0,begin_index);
                    graph.put(name,new Vertex4(name,value));
                    way.add(graph.get(name));
                }
                else {
                    way.add(graph.get(name));
                }
            }
            for(int i=0;i<=way.size()-2;i++) {
                way.get(i).child.add(way.get(i + 1));
                way.get(i+1).parent.add(way.get(i));
            }
        }
        List<Vertex4> list_graph = new ArrayList<Vertex4>(graph.values());
        dfs(list_graph);
        Queue<Vertex4> graph_modify = new LinkedList<>();
        for(Vertex4 vertex : list_graph) {
            list_graph.get(list_graph.indexOf(vertex)).parent_size = list_graph.get(list_graph.indexOf(vertex)).parent.size();
            if(list_graph.get(list_graph.indexOf(vertex)).parent_size==0) {
                graph_modify.add(vertex);
            }
        }
        Dijkstra(graph_modify);
        List<Vertex4> max_graph = new ArrayList<>(list_graph);
        Map<Integer,ArrayList<String>> totaldist = new TreeMap<Integer,ArrayList<String>> (new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        for(int i=0;i<max_graph.size();i++) {
            int key = max_graph.get(i).totalvalue;
            if (key==0)
                continue;
            String value = max_graph.get(i).name;
            if (!totaldist.containsKey(key))
                totaldist.put(key, new ArrayList<>(Arrays.asList(max_graph.get(i).name)));
            else {
                ArrayList<String> new_value = totaldist.get(key);
                new_value.add(value);
            }
        }
        for(Map.Entry<Integer,ArrayList<String>> e: totaldist.entrySet()) {
            for(String vertex : e.getValue()) {
                if (!graph.get(vertex).color.equals("blue"))
                    max_way(graph.get(vertex));
            }
            break;
        }
        print_graph(graph);
    }
}
