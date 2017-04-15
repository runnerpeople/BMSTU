class Lenofsection implements Comparable<Lenofsection>
{
    public int x1, x2, y1, y2;

    Lenofsection (int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double oper()
    {
        return  Math.sqrt(Math.pow(2, y2 - y1) + Math.pow(2, x2 - x1));
    }

    public int compareTo (Lenofsection obj)
    {
        return (int)(oper() - obj.oper());
    }

    public String toString()
    {
        return "(A=("+x1+","+y1+")"+"   B=("+x2+","+y2+")"+"\n" + "(L="+oper()+")"+")";
    }
}

import java.util.Arrays;

public class Test {
    public static void  main (String[] args) {
        Lenofsection [] oper = new Lenofsection[] {
                new Lenofsection(4, 2, 0, 1),
                new Lenofsection(3, 6, 12, 11),
                new Lenofsection(6, 1, 0, 1),
        };
        Arrays.sort(oper);
        for (Lenofsection item : oper)
            System.out.println(item);
    }
}
