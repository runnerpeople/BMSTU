import java.util.*;

enum Const {
    Var, Number, Left_paran , Right_paran, Sum, Min, Mul ,Div
}

class Lexer {
        ArrayList<String> a;
        HashMap<String, Integer> b;
        ArrayList<Const> c;
        Scanner in;
        int count;

        public Lexer(Scanner in1, ArrayList<String> b) {
                in = in1;
                a = b;
                ArrayList<Const> t = new ArrayList<Const>();
                this.b = new HashMap<String, Integer>();
                for (int i = 0; i < b.size(); i++) {
                        if (Character.isLetter(b.get(i).charAt(0))) {
                                t.add(Const.Var);
                                if (!this.b.containsKey(b.get(i))) {
                                        this.b.put(b.get(i), in.nextInt());
                                }
                        } else if (Character.isDigit(b.get(i).charAt(0))) {
                                t.add(Const.Number);
                        } else if (b.get(i).charAt(0) == '+') {
                                t.add(Const.Sum);
                        } else if (b.get(i).charAt(0) == '-') {
                                t.add(Const.Min);
                        } else if (b.get(i).charAt(0) == '*') {
                                t.add(Const.Mul);
                        } else if (b.get(i).charAt(0) == '/') {
                                t.add(Const.Div);
                        } else if (b.get(i).charAt(0) == '(') {
                                t.add(Const.Left_paran);
                        } else if (b.get(i).charAt(0) == ')') {
                                t.add(Const.Right_paran);
                        }
                        c = t;
                }       
        }
}



public class Calc {
        private static ArrayList<String> parse_token(String expr) {
                ArrayList<String> a = new ArrayList<String> ();
                String help = "";
                String sign = "()+-*/";
                for (int i=0; i < expr.length(); i++) {
                        Character r = expr.charAt(i);
                        if (r.compareTo(' ')==0) {
                                if (!help.equals("")) {
                                        a.add(help);
                                        help = "";
                                }
                                continue;
                        }
                        if (sign.indexOf(r)!=-1) {
                                if (!help.equals("")) {
                                        a.add(help);
                                        help = "";
                                }
                                a.add(r + "");
                        }
                        else {
                                help = help + r;
                        }
                }
                if (!help.equals("")) {
                        a.add(help);
                        help = "";
                }
                return a;
        }

        public static int parse_factor(Lexer d) throws Exception {
                if(d.count < d.c.size() && d.c.get(d.count).equals(Const.Number)) {
                        return Integer.parseInt(d.a.get(d.count++));
                }
                else if (d.count < d.c.size() && d.c.get(d.count).equals(Const.Var)) {
                        return d.b.get(d.a.get(d.count++)).intValue();
                }
                else if (d.count < d.c.size() && d.c.get(d.count).equals(Const.Left_paran)) {
                        d.count++;
                        int help=parse_expr(d);
                        if (d.count < d.c.size() && d.c.get(d.count).equals(Const.Right_paran)) {
                                d.count++;
                                return help;
                        }
                        else throw new Exception();
                }
                else if (d.count < d.c.size() && d.c.get(d.count).equals(Const.Min)) {
                        d.count++;
                        int help=parse_factor(d);
                        return help*(-1);
                }
                else throw new Exception();
        }

        public static int parse_term(Lexer a) throws Exception {
                int result=parse_factor(a);
                for(;a.count < a.c.size() && (a.c.get(a.count).equals(Const.Div) || a.c.get(a.count).equals(Const.Mul));) {
                        if(a.count < a.c.size() && a.c.get(a.count).equals(Const.Div)) {
                                a.count++;
                                result/=parse_factor(a);
                        }
                        if(a.count < a.c.size() && a.c.get(a.count).equals(Const.Mul)) {
                                a.count++;
                                result*=parse_factor(a);
                        }
                }
                return result;
        }

        public static int parse_expr(Lexer a) throws Exception {
                int result=parse_term(a);
                for(;a.count < a.c.size() && (a.c.get(a.count).equals(Const.Sum) || a.c.get(a.count).equals(Const.Min));) {
                        if(a.count < a.c.size() && a.c.get(a.count).equals(Const.Sum)) {
                                a.count++;
                                result+=parse_term(a);
                        }
                        if(a.count < a.c.size() && a.c.get(a.count).equals(Const.Min)) {
                                a.count++;
                                result-=parse_term(a);
                        }
                }
                return result;
        }
        
        public static int parse(Lexer a) throws Exception {
                int result = parse_expr(a);
                if (a.count > a.b.size())
                        return result;
                else throw new Exception();
        }



        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                String expr = in.nextLine();
                ArrayList<String> help = parse_token(expr);
                Lexer expression=new Lexer(in,help);
                try {
                        System.out.println(parse(expression));
                }
                catch (Exception e) {
                        System.out.println("error");
                }
        }
}
