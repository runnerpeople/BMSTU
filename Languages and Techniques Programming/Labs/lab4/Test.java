import java.util.Iterator;
import java.util.Scanner;
class Point
{
    private double x, y;

    Point(int r, double fi)
    {
        this.x = r * Math.sin(fi);
        this.y = r * Math.cos(fi);
    }

    public String toString ()
    {
        return "( " + this.x + " , " + this.y + " ) ";
    }
}

class Circle implements Iterable<Point>
{
    private int r, k;	//r - радиус окружности; k - кол-во точек

    public Circle(int r, int k)
    {
        this.r = r;
        this.k = k;
    }

    public Iterator<Point> iterator()
    {
        return new CircleIterator();
    }
    private class CircleIterator implements Iterator<Point>
    {
        private int pos;
        public CircleIterator()
        {
            pos = 0;	        //в начальный момент
        }
        public boolean hasNext()
        {
            return pos < k;	        //номер в данный момент меньше кол-ва точек
        }
        public Point next()
        {
            Point a = new Point(r, pos * 2 * Math.PI / k);
            pos++;
            return a;
        }
    }
}

public class Test
{
    public static void main( String[] args)
    {
        Scanner in = new Scanner(System.in);
        int r = in.nextInt(),k=in.nextInt();
        Circle a = new Circle(r,k);
        for(Point b : a)System.out.println(b);
    }
}
