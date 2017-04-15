import java.util.*;
enum tag2 {
    MINUS, PLUS, MULTI, DIV, LPAREN, RPAREN, VAR, NUMBER, EQ, NONEQ,LESS, MORE, LESSEQ , MOREEQ, QUES, COLON, COMMA, SEP, COLONEQ
}


class Lexem2 {
    String parse_string;
    ArrayList<String> s = new ArrayList<>();
    ArrayList<tag2> tags = new ArrayList<>();
    HashMap<String,Set<String>> g = new HashMap<>();
    HashMap<String,Set<String>> map_args = new HashMap<>();
    ArrayList<String> formal_args = new ArrayList<>();
    HashMap<String, Integer> func = new HashMap<>();
    HashMap<String, Integer> buf = new HashMap<>();
    ArrayList<String> args = new ArrayList<>();
    boolean flag_func = false;
    int n = 0;
    String name_func;

    public Lexem2(String s) {
        parse_string=s;
    }

    void Lexem_parse() throws Exception {
        for(int i=0;i<parse_string.length();i++)
            switch(parse_string.charAt(i)) {
                case ' ':
                    break;
                case '\n':
                    break;
                case '-':
                    tags.add(tag2.MINUS);
                    this.s.add("-");
                    break;
                case '+':
                    tags.add(tag2.PLUS);
                    this.s.add("+");
                    break;
                case '*':
                    tags.add(tag2.MULTI);
                    this.s.add("*");
                    break;
                case '/':
                    tags.add(tag2.DIV);
                    this.s.add("/");
                    break;
                case '(':
                    tags.add(tag2.LPAREN);
                    this.s.add("(");
                    break;
                case ')':
                    tags.add(tag2.RPAREN);
                    this.s.add(")");
                    break;
                case ';':
                    tags.add(tag2.SEP);
                    this.s.add(";");
                    break;
                case '=':
                    tags.add(tag2.EQ);
                    this.s.add("=");
                    break;
                case ',':
                    tags.add(tag2.COMMA);
                    this.s.add(",");
                    break;
                case '>':
                    switch (parse_string.charAt(++i)) {
                        case '=':
                            this.s.add(">=");
                            tags.add(tag2.MOREEQ);
                            break;
                        default:
                            --i;
                            this.s.add(">");
                            tags.add(tag2.MORE);
                            break;
                    }
                    break;
                case '<':
                    switch (parse_string.charAt(++i)) {
                        case '=':
                            this.s.add("<=");
                            tags.add(tag2.LESSEQ);
                            break;
                        case '>':
                            this.s.add("<>");
                            tags.add(tag2.NONEQ);
                            break;
                        default:
                            --i;
                            this.s.add("<");
                            tags.add(tag2.LESS);
                            break;

                    }
                    break;
                case ':':
                    switch (parse_string.charAt(++i)) {
                        case '=':
                            this.s.add(":=");
                            tags.add(tag2.COLONEQ);
                            break;
                        default:
                            --i;
                            this.s.add(":");
                            tags.add(tag2.COLON);
                            break;
                    }
                    break;
                case '?':
                    tags.add(tag2.QUES);
                    this.s.add("?");
                    break;
                default:
                    if (Character.isDigit((parse_string.charAt(i)))) {
                        tags.add(tag2.NUMBER);
                        String var = "";
                        while (i < parse_string.length() && Character.isDigit(parse_string.charAt(i))) {
                            var += parse_string.charAt(i) + "";
                            i++;
                        }
                        i--;
                        this.s.add(var);
                        break;
                    }
                    if (Character.isAlphabetic(parse_string.charAt(i))) {
                        tags.add(tag2.VAR);
                        String var = parse_string.charAt(i) + "";
                        i++;
                        while (i < parse_string.length() && (Character.isAlphabetic(parse_string.charAt(i)) || Character.isDigit(parse_string.charAt(i)))) {
                            var += parse_string.charAt(i);
                            i++;
                        }
                        i--;
                        this.s.add(var);
                    }
                    else throw new Exception();
            }
    }
    void program() throws Exception {
        function();
        if(n < tags.size() && tags.get(n)==tag2.VAR) {
            program();
            return;
        }
        if(tags.size() != n)
            throw new Exception();
        for(Map.Entry<String,Set<String>> a : map_args.entrySet()) {
            for (String func : a.getValue())
                if (!formal_args.contains(func))
                    throw new Exception();
        }
        for(Map.Entry<String,Integer> a : buf.entrySet())
            if(!(func.containsKey(a.getKey()) && func.get(a.getKey()) == a.getValue()))
                throw new Exception();
    }

    void function() throws Exception {
        if(tags.get(n)==tag2.VAR){
            String func_name = s.get(n);
            n++;
            if (tags.get(n)==tag2.LPAREN) {
                flag_func = true;
                n++;
                args = formal_args_list();
                int buf;
                if (args==null)
                    buf = 0;
                else buf = args.size();
                func.put(func_name,buf);
                name_func = func_name;
                g.put(func_name,new HashSet<>());
                if (args!=null)
                    map_args.put(func_name,new HashSet<>(args));
                else map_args.put(func_name,new HashSet<>());
            }
            else throw new Exception();
            if (tags.get(n)==tag2.RPAREN) {
                flag_func = false;
                n++;
                if (tags.get(n)==tag2.COLONEQ) {
                    n++;
                    expr();
                    if (tags.get(n) == tag2.SEP) {
                        n++;
                        return;
                    }
                    else throw new Exception();
                }
                else throw new Exception();
            }
            else throw new Exception();
        }
        else throw new Exception();
    }

    ArrayList<String> formal_args_list() throws Exception {
        if (tags.get(n) == tag2.VAR)
            return ident_args_list();
        else return null;
    }

    ArrayList<String> ident_args_list() throws Exception {
        ArrayList<String> args_buf = new ArrayList<>();
        args_buf.add(s.get(n));
        n++;
        if (tags.get(n)==tag2.COMMA) {
            n++;
            args_buf.addAll(ident_args_list());
        }
        return args_buf;
    }

    void expr() throws Exception {
        comparison_expr();
        if (tags.get(n)==tag2.QUES) {
            n++;
            comparison_expr();
            if(tags.get(n)==tag2.COLON) {
                n++;
                expr();
            }
            else
                throw new Exception();
        }
    }

    void comparison_expr() throws Exception {
        parse_arith_expr();
        if (comparison_op()) {
            parse_arith_expr();
        }
    }

    boolean comparison_op() {
        boolean used = false;
        switch (tags.get(n)) {
            case EQ:
                n++;
                used = true;
                break;
            case NONEQ:
                n++;
                used = true;
                break;
            case LESS:
                n++;
                used = true;
                break;
            case MORE:
                n++;
                used = true;
                break;
            case MOREEQ:
                n++;
                used= true;
                break;
            case LESSEQ:
                n++;
                used = true;
                break;
        }
        return used;
    }

    void parse_arith_expr() throws Exception {
        parse_e();
    }

    void parse_e() throws Exception {
        parse_t();
        while (n < tags.size() && (tags.get(n) == tag2.MINUS || tags.get(n) == tag2.PLUS)) {
            n++;
            parse_t();
        }
    }

    void parse_t() throws Exception {
        parse_f();
        while (n < tags.size() && (tags.get(n) == tag2.DIV || tags.get(n) == tag2.MULTI)) {
            n++;
            parse_f();
        }
    }

    void parse_f() throws Exception {
        if (tags.get(n) == tag2.MINUS) {
            n++;
            parse_f();
            return;
        } else if (tags.get(n) == tag2.LPAREN) {
            n++;
            expr();
            if (tags.get(n) != tag2.RPAREN)
                throw new Exception();
            else {
                n++;
                return;
            }
        } else if (tags.get(n) == tag2.NUMBER) {
            n++;
            return;
        }
        else if (tags.get(n) == tag2.VAR) {
            String name = s.get(n);
            n++;
            if (tags.get(n)==tag2.LPAREN) {
                n++;
                int number_argc = expr_list();
                buf.put(name,number_argc);
                g.get(name_func).add(name);
                if (tags.get(n) == tag2.RPAREN) {
                    n++;
                    return;
                } else
                    throw new Exception();
            }
            else {
                if (flag_func==false && !formal_args.contains(name))
                    formal_args.add(name);
                else if (!map_args.get(name_func).contains(name))
                        map_args.get(name_func).add(name);
            }
        }
        else throw new Exception();
    }

    int expr_list() throws Exception {
        if (tags.get(n)==tag2.RPAREN) {
            return 0;
        }
            int argc = 1;
            expr();
            while (tags.get(n) == tag2.COMMA) {
                n++;
                expr();
                argc++;
            }
            return argc;
        }
}

class OrgListIncident3 {
    ArrayList<OrgListIncident3> a = new ArrayList<>();
    int T1;
    int comp;
    int low;
    int n;

    public OrgListIncident3(int n) {
        this.n = n;
    }
}

public class Modules {

    public static int time = 1;
    public static int count = 1;

    public static void Tarjan(OrgListIncident3[] graph) {
        for(int i=0;i<graph.length;i++) {
            graph[i].T1=0;
            graph[i].comp=0;
        }
        Stack<OrgListIncident3> s = new Stack<>();
        for(int i=0;i<graph.length;i++)
            if(graph[i].T1==0)
                VisitVertex_Terjan(graph, graph[i], s);

    }

    public static void VisitVertex_Terjan(OrgListIncident3[] graph, OrgListIncident3 v, Stack<OrgListIncident3> s) {
        OrgListIncident3 u;
        v.T1 = time;
        v.low = time;
        Modules.time++;
        s.push(v);
        for(int j=0;j<v.a.size();j++) {
            if (v.a.get(j).T1==0)
                VisitVertex_Terjan(graph,v.a.get(j),s);
            if (v.a.get(j).comp==0 && v.low > v.a.get(j).low)
                v.low=v.a.get(j).low;
        }
        if (v.T1 == v.low) {
            do {
                u = s.pop();
                u.comp = Modules.count;
            }
            while (!(u.equals(v)));
            Modules.count++;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String exprs = "";
        ArrayList<String> programm = new ArrayList<>();
        int count = 0,k=0;
        while(in.hasNextLine()) {
            String str = in.nextLine();
            if (str.isEmpty())
                break;
            programm.add(str);
            exprs = exprs + programm.get(count++) + "\n";
        }
        Lexem2 lex = new Lexem2(exprs);
        try {
            lex.Lexem_parse();
            lex.program();
        }
        catch(Exception e) {
            System.out.println("error");
            System.exit(1);
        }
        ArrayList<OrgListIncident3> graph = new ArrayList<>();
        for(int i=0;i<count;i++,graph.add(new OrgListIncident3(i)));
        Map<String, Integer> buf = new HashMap<String, Integer>();
        for (String i : lex.g.keySet())
            buf.put(i, k++);
        for (String i : lex.g.keySet()) {
            int ind = buf.get(i);
            for (String s : lex.g.get(i))
                graph.get(ind).a.add(graph.get(buf.get(s)));
        }
        Tarjan(graph.toArray(new OrgListIncident3[1]));
        System.out.print(Modules.count-1);
    }
}
