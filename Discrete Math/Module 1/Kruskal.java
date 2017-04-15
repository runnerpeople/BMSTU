import java.util.*;

class Roads {
        int x, y;
        int depth = 0;
        Roads parent = this;
}

class Road implements Comparable<Road> {
        int first, second;
        float length;

        public int compareTo(Road obj) {
                if (this.length>obj.length) return 1;
                else if (this.length==obj.length) return 0;
                else return -1;
        }
}

public class Kruskal { 

        public static Roads find(Roads x) {
                if (x.parent == x)
                        return x;
                else return x.parent = find(x.parent);
        }

        public static void union(Roads x, Roads y) {
                Roads root_x = find(x);
                Roads root_y = find(y);
                if (root_x.depth < root_y.depth)
                        root_x.parent = root_y;
                else {
                        root_y.parent = root_x;
                        if (root_y.depth == root_x.depth && root_x != root_y) {
                                root_x.depth++;
                        }
                }
        }

        public static float SpanningTree(Roads[] a, Road[] help) {
                int i = 0;
                float result = 0;
                while (i < help.length) {
                        int x = help[i].first;
                        int y = help[i].second;
                        if (find(a[x]) != find(a[y])) {
                                result += help[i].length;
                                union(a[x], a[y]);
                        }
                        i++;
                }
                return result;
        }

        public static float MST_Kruskal(Roads[] a, Road[] help) {
                Arrays.sort(help);
                float b = SpanningTree(a, help);
                return b;
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n = in.nextInt();
                int i, j, count = 0;
                float sum;
                Roads[] result = new Roads[n];
                for (i = 0; i < n; i++) {
                        result[i] = new Roads();
                        result[i].x = in.nextInt();
                        result[i].y = in.nextInt();
                }
                Road[] help = new Road[n*(n-1)/2];
                for (i = 0; i < n*(n-1)/2; i++)
                        help[i] = new Road();
                for (i = 0; i < n; i++) {
                        for (j = i + 1; j < n; j++) {
                                help[count].first = i;
                                help[count].second = j;
                                help[count++].length = (float)Math.sqrt(Math.pow(result[j].x - result[i].x, 2) + Math.pow(result[j].y - result[i].y, 2));
                        }
                }
                sum = MST_Kruskal(result, help);
                System.out.printf(Locale.US,"%.2f",sum);
        }
}
