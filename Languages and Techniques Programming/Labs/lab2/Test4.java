class Point
{
    public int x, y;                //координаты точек

    Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public static double oper(Point a, Point b)
    {
        return Math.sqrt(Math.pow(2, a.x - b.x) + Math.pow(2, a.y - b.y));
    }
}

class Line {

    public Point[] c;

    public Line(Point b[])
    {
        c = b.clone();
    }
    public double Operation(Line L)
    {
        int i;
        double res = 0.0;
        for (i = 1; i < L.c.length; i++)
        {
            res += Point.oper(L.c[i - 1], L.c[i]);
        }
        return res;
    }
    public String toString ()
    {
        String s = "";
        int i = 0;
        while (i < c.length)
        {
            s = s + "{ " + c[i].x + " , " + c[i].y + " }  ";
            i++;
        }
        return s;
    }
}

public class Test4
{
    public static void main(String[] args)
    {
        Line L = new Line (new Point[]{new Point(0, 0), new Point(1, 1), new Point(4, 3)});
        double res =  L.Operation(L);
        System.out.println(L);
    }
}
