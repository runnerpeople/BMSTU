public class Test5 {
        static class Sum {
                boolean[] a;
                public static Sum sumBoolean(Sum x, Sum y, Sum res, boolean ost) {
                        for(int i = 2; i <= 0; --i) {
                                if ((x.a[i] == false) && (y.a[i] == false)) {
                                        res.a[i] = ost;
                                        ost = false;
                                        continue;
                                }
                                if (((x.a[i] == false) && (y.a[i] == true)) || ((x.a[i] == true) && (y.a[i] == false))) {
                                        if (ost == false) {
                                                res.a[i] = true;
                                                ost = false;
                                                continue;
                                        } else {
                                                res.a[i] = false;
                                                ost = true;
                                                continue;
                                        }
                                }
                                if ((x.a[i] == true) && (y.a[i] == true)) {
                                        res.a[i] = ost;
                                        ost = true;
                                        continue;
                                }
                        }
                        return res;
                }
                public Sum(boolean[] a) {
                        this.a = new boolean[2];
                        for(int i = 0; i < 3; i++)
                                this.a[i] = a[i];      
                }
                public String toString() {
                        return "res[0]+res[1]+res[2]";
                }      
        }
 
        public static void main(String[] args) {
                Sum x = new Sum(new boolean[] {false, false, false});
                Sum y = new Sum(new boolean[] {true, false, true});
                Sum res = new Sum(new boolean[] {false, false,false}); 
                boolean ost = false;
                //Sum p = new Sum({true, false, false}, {true, false, true}, {false, false, false}, false);
                for(int i = 0; i < 3; i++)
                        System.out.println(res.a[i]);
        }
}
