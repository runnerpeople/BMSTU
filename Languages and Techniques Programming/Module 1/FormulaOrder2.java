import java.util.*;

enum Tag {
    Plus, Minus, Mul, Div, L_Paren, R_Paren, Var, Comma, Number, Eq, Sep
}


class Lex {
    String parse_string;
    int index;
    LinkedList<Tag> tags = new LinkedList<>();
    LinkedList<String> lexems = new LinkedList<>();
    Map<String,ArrayList<String>> dependance = new Hashtable<>();
    Map<String,Integer> mapping_index = new HashMap<>();
    ArrayList<String> args =new ArrayList<>();
    ArrayList<String> depend_vars =new ArrayList<>();
    public Lex(String parse_string,int index) {
        this.parse_string=parse_string;
        this.index = index;
    }

    public void parse_lexems(String parse_string) throws Exception {
        for(int i=0;i<parse_string.length();i++) {
            char c = parse_string.charAt(i);
            if (c==' ' || c=='\n') continue;
            lexems.add(Character.toString(c));
            switch (c) {
                case '+':
                    tags.add(Tag.Plus);
                    break;
                case '-':
                    tags.add(Tag.Minus);
                    break;
                case '*':
                    tags.add(Tag.Mul);
                    break;
                case '/':
                    tags.add(Tag.Div);
                    break;
                case '(':
                    tags.add(Tag.L_Paren);
                    break;
                case ')':
                    tags.add(Tag.R_Paren);
                    break;
                case '=':
                    tags.add(Tag.Eq);
                    break;
                case ',':
                    tags.add(Tag.Comma);
                    break;
                default:
                    lexems.removeLast();
                    if (Character.isDigit(c)) {
                        int k=i;
                        tags.add(Tag.Number);
                        for(;i<parse_string.length() && Character.isDigit(parse_string.charAt(i));i++);
                        lexems.add(parse_string.substring(k, i--));
                        break;
                    }
                    if (Character.isLetter(c)) {
                        int k=i;
                        tags.add(Tag.Var);
                        for(;i<parse_string.length() && (Character.isDigit(parse_string.charAt(i)) || Character.isLetter(parse_string.charAt(i)));i++);
                        lexems.add(parse_string.substring(k, i--));
                        break;
                    }
                    else throw new Exception();
            }
        }
    }

    public void parse() throws Exception {
        parse_variables();
        int i;
        for(i=1;i<args.size();i++) {
            if (!tags.isEmpty() && tags.poll()!=Tag.Comma)
                throw new Exception();
            lexems.poll();
            depend_vars = new ArrayList<>();
            parse_expr();
            dependance.put(args.get(i),depend_vars);
        }
        if (!tags.isEmpty())
            throw new Exception();
        for (String arg : args)
            mapping_index.put(arg,index);
    }

    public void parse_variables() throws Exception {
        parse_var();
        if (tags.peek() == Tag.Eq && tags.peek() != Tag.Comma) {
            tags.poll();
            lexems.poll();
            parse_expr();
            dependance.put(args.get(0),depend_vars);
        }
        else throw new Exception();
    }

    public void parse_var() throws Exception {
        if (tags.poll()!= Tag.Var)
            throw new Exception();
        else {
            String var = lexems.poll();
            if (!(args.contains(var)))
                args.add(var);
            else throw new Exception();
        }
        if (tags.peek() == Tag.Comma) {
            lexems.poll();
            tags.poll();
            parse_var();
        }
    }

    public void parse_expr() throws Exception {
        parse_term();
        while(!tags.isEmpty() && ((tags.peek()==Tag.Minus) || tags.peek()==Tag.Plus)) {
            tags.poll();
            lexems.poll();
            parse_term();
        }
    }

    public void parse_term() throws Exception {
        parse_factor();
        while (!tags.isEmpty() && ((tags.peek()==Tag.Mul) || tags.peek()==Tag.Div)) {
            tags.poll();
            lexems.poll();
            parse_factor();
        }
    }

    public void parse_factor() throws Exception {
        switch (tags.poll()) {
            case Minus:
                lexems.poll();
                parse_factor();
                break;
            case L_Paren:
                lexems.poll();
                parse_expr();
                if (tags.poll()!=Tag.R_Paren)
                    throw new Exception();
                lexems.poll();
                break;
            case Number:
                lexems.poll();
                break;
            case Var:
                depend_vars.add(lexems.poll());
                break;
            default:
                throw new Exception();
        }
    }
}

class Graph {
    int index;
    int color = 0;  // Color = "White"
    ArrayList<Graph> incidence_vertex = new ArrayList<>();

    public Graph(int index) {
        this.index = index;
    }
}

public class FormulaOrder2 {
    public static void error(String message) {
        System.out.println(message);
        System.exit(1);
    }

    public static Queue<Graph> TopSort() {
        Queue<Graph> answer = new LinkedList<>();
        for (int i=0;i<graph.size();i++) {
            if (graph.get(i).color == 0) {
                dfs(answer,graph.get(i));
            }
        }
        return answer;
    }

    public static void dfs(Queue<Graph> queue, Graph vertex) {
        vertex.color=1;
        for (int i=0;i<vertex.incidence_vertex.size();i++) {
            if (vertex.incidence_vertex.get(i).color==0)
                dfs(queue, vertex.incidence_vertex.get(i));
            else if (vertex.incidence_vertex.get(i).color==1)
                error("cycle");
        }
        vertex.color=2;
        queue.add(vertex);
    }

    static ArrayList<Graph> graph = new ArrayList<>();


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Lex> formulas = new ArrayList<>();
        ArrayList<String> buf = new ArrayList<>();
        int i=0;
        while (in.hasNextLine()) {
            String parse_formula = in.nextLine();
            Lex formula = new Lex(parse_formula,i++);
            formulas.add(formula);
            buf.add(parse_formula);
        }
        try {
            for (Lex formula : formulas) {
                try {
                    formula.parse_lexems(formula.parse_string);
                    formula.parse();
                }
                catch (Exception e) {
                    error("syntax error");
                }
            }
            ArrayList<String> vars = new ArrayList<>();
            ArrayList<String> depend_vars = new ArrayList<>();
            for (Lex formula : formulas) {
                vars.addAll(formula.args);
                depend_vars.addAll(formula.depend_vars);
            }
            Set<String> used_vars = new HashSet<>(vars);
            if (!vars.containsAll(depend_vars) || used_vars.size() != vars.size())
                throw new Exception();
        }
        catch (Exception e) {
            error("syntax error");
        }
        Hashtable<String,ArrayList<String>> all_dependance = new Hashtable<>();
        Hashtable<String,Integer> dependance_index = new Hashtable<>();
        Enumeration<String> keys;
        for (Lex formula : formulas) {
            all_dependance.putAll(formula.dependance);
            dependance_index.putAll(formula.mapping_index);
        }
        keys = all_dependance.keys();
        i=0;
        for(;i<buf.size();i++)
            graph.add(new Graph(i));
        keys = all_dependance.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            for (String depend_vars : all_dependance.get(key))  {
                graph.get(dependance_index.get(key)).incidence_vertex.add(graph.get(dependance_index.get(depend_vars)));
            }
        }
        Queue<Graph> answer = TopSort();
        while(!answer.isEmpty())
            System.out.println(buf.get(answer.remove().index));
    }
}

