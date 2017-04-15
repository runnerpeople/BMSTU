public class Tests {
    public static class Polynom {
        private int[] a;

        public Polynom(int[] b) {
            a = new int[b.length+1];
            for (int i=0;i<b.length;i++) {
                a[i]= b[i];
            }
        }

        public int degree() {
            int d = 0;
            for (int i = 0; i < a.length; i++)
                if (a[i] != 0) d = i;
            return d;
        }

        public static Polynom polMod(Polynom x, Polynom y) {
            int p = x.degree(),q = y.degree(); //p=deg(x) && q=deg(y)
            int u = p-q; //p-q>0
            int w=x.a[p];
            for(int s=0;s<=u;s++)
                y.a[s]*=w;
            for(int s=u,z=p;z>0;s--,z--) {
                int help=y.a[s];
                y.a[z]=help;
            }
            for(int s=p;s>0;s--) {
                x.a[s]-=y.a[s];
            }
            return x;
        }

        public String toString() {
            if (a.length ==  0) return "" + a[0];
            if (a.length ==  1) return a[1] + "x + " + a[0];
            String s = a[degree()] + "x^" + degree();
            for (int i = degree()-1; i >= 0; i--) {
                if      (a[i] == 0) continue;
                else if (a[i]  > 0) s = s + " + " + ( a[i]);
                else if (a[i]  < 0) s = s + " - " + (-a[i]);
                else if (i >  1) s = s + "x^" + i;
            }
            return s;
        }
    }
    public static void main(String[] args) {
        int[] q=new int[]{3,4,5};
        int[] w=new int[]{2,1};
        Polynom a = new Polynom(q);
        Polynom b = new Polynom(w);
        Polynom c = Polynom.polMod(a,b);
        System.out.println(c);
    }
}
