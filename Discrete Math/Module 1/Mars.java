import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

class ListsIncidence5 {
    ArrayList<Integer> b = new ArrayList<>();
    static boolean[] used;
    static char[] team;
    static int[] number_team;
    static boolean error = false;
}

class Teams {
    ArrayList<ListsIncidence5> team1;
    ArrayList<ListsIncidence5> team2;
}

class Tmp {
    ArrayList<Character> b= new ArrayList<>();
    int size;
}

public class Mars {
    public static void make_teams(ListsIncidence5[] result,ListsIncidence5[] graph,Teams teams,Tmp tmp,int size) {
        int i,j,k,help=0;
        for(i=0;i<size;i++) {
            if (graph[0].used[i]==false) {
                graph[0].used[i]=true;
                help=1;
                Tmp team1 = new Tmp();
                Tmp team2= new Tmp();
                boolean[] used_help =new boolean[graph[0].used.length];
                for(j=0;j<used_help.length;j++)
                    used_help[j]=graph[0].used[j];
                for(j=0;j<tmp.size;j++) {
                    char y = tmp.b.get(j);
                    team1.b.add(y);
                    team2.b.add(y);
                }
                team1.size=tmp.size;
                team2.size=tmp.size;
                for(j=0;j<teams.team1.size();j++) {
                    team1.b.remove(team1.size-1);
                    int help1=(teams.team1.get(i).b.get(j));
                    team1.b.add(team1.size++,(char)(help1));

                }
                for(j=0;j<teams.team2.size();j++) {
                    team2.b.remove(team2.size);
                    int help1 = (teams.team2.get(i).b.get(j));
                    team2.b.add(team1.size++,(char)(help1));
                }
                make_teams(result,graph,teams,team1,size);
                make_teams(result,graph,teams,team2,size);
            }
        }
        if(help==0 && tmp.size>0) {
            for(k=0;k<tmp.size;k++) {
                result[0].b.add((int)tmp.b.get(k));
            }
        }
    }

    public static char invert(char sign) {
        String help=sign+"";
        if (help.equals("+"))
            return '-';
        else return '+';
    }

    public static void dfs(ListsIncidence5[] graph,int v,char team) {
        graph[v].used[v]=true;
        graph[v].team[v]=team;
        for(int i=0;i<graph[v].b.size();i++) {
            if (graph[v].b.get(i)== 1 && graph[v].used[i]==false)
                dfs(graph,i,invert(team));
            else if(graph[v].b.get(i)== 1 && (graph[v].team[i]+"").equals(graph[v].team[v]+""))
                    graph[0].error = true;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, k, i, v1, v2, j, r;
        char sign;
        n=in.nextInt();
        ListsIncidence5[] graph = new ListsIncidence5[n];
        for (i = 0; i < n; i++)
            graph[i] = new ListsIncidence5();
        graph[i - 1].used = new boolean[n];
        graph[i - 1].team = new char[n];
        for(i=0;i<n;i++) {
            for (j=0;j<n;j++) {
                sign=in.next().charAt(0);
                String help=sign+"";
                if(help.equals("+"))
                    graph[i].b.add(1);
                else graph[i].b.add(0);
            }
        }
        for(j=0;j<n;j++) {
            graph[i - 1].used[j] = false;
            graph[i - 1].team[j] = '+';
        }
        for(i=0;i<n;i++) {
            if (graph[i].used[i] == false) {
                dfs(graph, i,'+');
            }
        }
        if (graph[i-1].error) {
            System.out.println("No solution");
            System.exit(0);
        }
        graph[i - 1].number_team = new int[n];
        int numberteam1=1;int numberteam2=-1;
        for(i=0;i<n;i++) {
            if((graph[0].team[i]+"").equals("+"))
                graph[0].number_team[i]=numberteam1++;
            else graph[0].number_team[i]=numberteam2--;
        }
        int max;
        if(numberteam1<Math.abs(numberteam2))
            max = Math.abs(numberteam2);
        else max=numberteam1;
        Teams teams = new Teams();
        teams.team1=new ArrayList<>();
        teams.team2=new ArrayList<>();
        for(i=1;i<max;i++) {
            for(j=0;j<n;j++) {
                if(graph[0].number_team[j]==i) {
                    teams.team1.add(graph[j]);
                }
                else if (graph[0].number_team[j]==-i) {
                    teams.team2.add(graph[j]);
                }
            }
        }
        Tmp tmp = new Tmp();
        tmp.size=n;
        for(j=0;j<n;j++) {
            graph[i - 1].used[j] = false;
            tmp.b.add((char)0);
        }
        ListsIncidence5[] result = new ListsIncidence5[n];
        for (i = 0; i < n; i++)
            result[i] = new ListsIncidence5();
        make_teams(result,graph,teams,tmp,n);
        System.out.println("not error");
    }
}

