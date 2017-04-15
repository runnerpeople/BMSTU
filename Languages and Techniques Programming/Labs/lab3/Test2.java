import java.util.Arrays;
     
public class Test2 {
            public static class Line {
                    private String a;
                   
                    public Line(String a) {
                            this.a = a;
                    }
                   
                    public static float wordNumber(Line str) {
                            float n = 1, q = 0;
                            for(int i = 0; i < str.a.length(); i++) {
                                    if (str.a.charAt(i) != ' ')
                                            q++;
                                    else
                                            n++;
                            }
                            return q/n;
                    }
     
                    public int compareTo(Line obj) {
                            if (wordNumber(obj) == wordNumber(this))
                                    return 0;
                            else if (wordNumber(obj) > wordNumber(this))
                                    return 1;
                            else
                                    return -1;
                    }
                   
                    public String toString() {
                            return a;
                    }
            }
     
    public static void main(String[] args) {
            Line[] p = new Line[] {
                    new Line("My car won’t start"),
                    new Line("Dad has come")
            };
            Arrays.sort(p);
            for (Line s : p)
                    System.out.println(s);
        }
    }
