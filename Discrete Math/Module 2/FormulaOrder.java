import java.util.*;

enum tag{ MINUS, PLUS, MULTI, DIV, LPAREN, RPAREN, VAR, NUMBER, SEM, COMMA, EQUAL}


class Pair2 {
        String key;
        HashSet<String> value;
        Character separator = ':';
        String separator2 = "->";
        int n;

        public String toString() {
                return key + separator + n + " " + separator2 + value;
        }

        public Pair2(String key,HashSet<String> value, int n) {
                this.key=key;
                this.value=value;
                this.n=n;
        }
}

class Lexem {
        ArrayList<String> s = new ArrayList<String>();
        ArrayList<tag> tags = new ArrayList<tag>();
        HashSet<String> vars = new HashSet<>();
        HashSet<String> dep_vars = new HashSet<>();
        HashSet<String> buf = new HashSet<>();
        ArrayList<Pair2> depend = new ArrayList<>();
        int n = 0;
        int line = 0;

        void print() {
                for(tag i : tags)
                        System.out.println(i);
        }

        void findLexem(String s) throws Exception{
                for (int i = 0; i < s.length(); i++) {
                        switch (s.charAt(i)) {
                                case ' ':
                                        break;
                                case '\n':
                                        tags.add(tag.SEM);
                                        this.s.add(";");
                                        break;
                                case '-':
                                        tags.add(tag.MINUS);
                                        this.s.add("-");
                                        break;
                                case '+':
                                        tags.add(tag.PLUS);
                                        this.s.add("+");
                                        break;
                                case '*':
                                        tags.add(tag.MULTI);
                                        this.s.add("*");
                                        break;
                                case '/':
                                        tags.add(tag.DIV);
                                        this.s.add("/");
                                        break;
                                case '(':
                                        tags.add(tag.LPAREN);
                                        this.s.add("(");
                                        break;
                                case ')':
                                        tags.add(tag.RPAREN);
                                        this.s.add(")");
                                        break;
                                case ';':
                                        tags.add(tag.SEM);
                                        this.s.add(";");
                                        break;
                                case '=':
                                        tags.add(tag.EQUAL);
                                        this.s.add("=");
                                        break;
                                case ',':
                                        tags.add(tag.COMMA);
                                        this.s.add(",");
                                        break;
                                default:
                                        if (Character.isDigit((s.charAt(i)))) {
                                                tags.add(tag.NUMBER);
                                                String var = "";
                                                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                                                        var += s.charAt(i) + "";
                                                        i++;
                                                }
                                                i--;
                                                this.s.add(var);
                                                break;
                                        }
                                        if (Character.isAlphabetic(s.charAt(i))) {
                                                tags.add(tag.VAR);
                                                String var = s.charAt(i) + "";
                                                i++;
                                                while (i < s.length() && (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
                                                        var += s.charAt(i);
                                                        i++;
                                                }
                                                i--;
                                                this.s.add(var);
                                        }
                                        else throw new Exception();
                        }
                }
        }

        void forms() throws Exception {
                form();
                while (n < tags.size() && tags.get(n) == tag.VAR)
                        form();
                if (tags.size() != n || !(vars.containsAll(dep_vars)))
                        throw new Exception();
                else return;
        }


        void form() throws Exception {
                ArrayList<String> vars=new ArrayList<>();
                vars_parse_left(vars);
                if (tags.get(n++) != tag.EQUAL)
                        throw new Exception();
                parse();
                if (this.vars.size() == 0)
                        this.vars.addAll(vars);
                else {
                        for(int i=0;i<vars.size();i++)
                                if(this.vars.contains(vars.get(i)))
                                        throw new Exception();
                        this.vars.addAll(vars);
                }
                depend.add(new Pair2(vars.get(0),buf,line));
                if (tags.get(n)==tag.COMMA) {
                        int i;
                        for (i = 1; i < vars.size(); i++) {
                                n++;
                                buf = new HashSet<>();
                                parse();
                                depend.add(new Pair2(vars.get(i), buf, line));
                                if (tags.get(n) != tag.COMMA) {
                                        i++;
                                break;
                        }
                }
                if (tags.get(n) == tag.COMMA || i < vars.size())
                        throw new Exception();
                }
                if (tags.get(n)==tag.SEM)
                        n++;
                line++;
                buf = new HashSet<>();
                return;
        }

        ArrayList<String> vars_parse_left(ArrayList<String> buf) throws Exception {
                if (buf.contains(s.get(n)) || tags.get(n) != tag.VAR)
                        throw new Exception();
                else buf.add(s.get(n++));
                if (tags.get(n)==tag.COMMA) {
                        n++;
                        vars_parse_left(buf);
                }
                return buf;
        }

        void parse() throws Exception {
                parse_e();
        }


        void parse_e() throws Exception {
                parse_t();
                while (n < tags.size() && (tags.get(n) == tag.MINUS || tags.get(n) == tag.PLUS)) {
                        n++;
                        parse_t();
                }
        }


        void parse_t() throws Exception {
                parse_f();
                while (n < tags.size() && (tags.get(n) == tag.DIV || tags.get(n) == tag.MULTI)) {
                        n++;
                        parse_f();
                }
        }

        void parse_f() throws Exception {
                if (tags.get(n) == tag.MINUS) {
                        n++;
                        parse_f();
                        return;
                } else if (tags.get(n) == tag.LPAREN) {
                        n++;
                        parse_e();
                        if (tags.get(n++) != tag.RPAREN)
                                throw new Exception();
                        else return;
                } else if (tags.get(n) == tag.NUMBER) {
                        n++;
                        return;
                }
                else if (tags.get(n) == tag.VAR) {
                        dep_vars.add(s.get(n));
                        buf.add(s.get(n));
                        n++;
                        return;
                }
                else throw new Exception();
        }
}

class OrgListsIncident2 {
        int index;
        String color = "White";
        ArrayList<OrgListsIncident2> a = new ArrayList<>();
}

class MyMap {
        String key;
        int value;
        String separator = "->";

        public MyMap(String key,int value) {
                this.key=key;
                this.value=value;
        }

        public String toString() {
                return key + separator + value;
        }
}


public class FormulaOrder {
        public static void dfs(ArrayList<OrgListsIncident2> queue,ArrayList<OrgListsIncident2> graph,int i) throws Exception {
                for(;i<graph.size();i++) {
                        if (graph.get(i).color.equals("White")) {
                                dfs_second(queue,graph.get(i));
                        }
                }
         }

        public static void dfs_second(ArrayList<OrgListsIncident2> queue,OrgListsIncident2 v) throws Exception{
                v.color = "Grey";
                for (int j = 0; j < v.a.size(); j++) {
                        if (v.a.get(j).color.equals("White"))
                                dfs_second(queue, v.a.get(j));
                        else if (v.a.get(j).color.equals("Grey"))
                                throw new Exception();
                        }
                v.color = "Black";
                queue.add(v);
        }
                
        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                String exprs = "";
                ArrayList<String> result = new ArrayList<>();
                int i =0;
                while(in.hasNextLine()) {
                        result.add(in.nextLine());
                        exprs = exprs + result.get(i++) + "\n";
                }
                Lexem lex = new Lexem();
                try {
                        lex.findLexem(exprs);
                        lex.forms();
                }
                catch(Exception e) {
                        System.out.println("syntax error");
                        System.exit(1);
                }
                ArrayList<OrgListsIncident2> graph = new ArrayList<>();
                for(i=0;i<lex.line;i++) {
                        graph.add(new OrgListsIncident2());
                        graph.get(i).index=i;
                }
                ArrayList<MyMap> help =new ArrayList<>();
                ArrayList<String> help2 = new ArrayList<>();
                for (Pair2 pair : lex.depend) {
                        help.add(new MyMap(pair.key,pair.n));
                        help2.add(pair.key);
                }
                for (Pair2 pair2 : lex.depend) {
                        for(String str : pair2.value) {
                                graph.get(pair2.n).a.add(graph.get(help.get(help2.indexOf(str)).value));
                        }
                }
                ArrayList<OrgListsIncident2> tarjan = new ArrayList<>();
                try {
                        dfs(tarjan,graph,0);
                }
                catch(Exception e) {
                        System.out.println("cycle");
                        System.exit(1);
                }
                OrgListsIncident2 buf = tarjan.remove(0);
                System.out.println(result.get(buf.index));
                while (tarjan.size()>0) {
                        OrgListsIncident2 e = tarjan.remove(0);
                        System.out.println(result.get(e.index));
                }
        }
}

