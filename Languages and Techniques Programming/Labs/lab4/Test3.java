import java.util.Iterator;
 
public class Test2 {
    static public class Point
    {
        public double x, y, z;
 
        Point(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z=z;
        }
    }
    public static class Points implements Iterable<Double> {
        private Point[] a;
        private int count = 0;
 
        public Points(Point[] y) {
            this.a=y;
            count = y.length;
        }
 
        public Iterator<Double> iterator() {
            return new PointIterator();
        }
 
        private class PointIterator implements Iterator<Double> {
            private int n;
 
            public PointIterator() {
                n = 0;
            }
 
            public boolean hasNext() {
                return (n < count);
            }
 
            public Double next() {
                n++;
                if (n<a.length)
                    return Math.sqrt(Math.pow(a[n].x-a[n-1].x,2)+Math.pow(a[n].y-a[n-1].y,2)+Math.pow(a[n].z-a[n-1].z,2));
                else return Math.sqrt(Math.pow(a[0].x-a[n-1].x,2)+Math.pow(a[0].y-a[n-1].y,2)+Math.pow(a[0].z-a[n-1].z,2));
            }
        }
    }
    public static void main(String[] args) {
        Point[] b = new Point[] {new Point(0.0, 0.0, 0.0),
                new Point(1.7, 2.5, 6.7),
                new Point(5.7, 7.2, 3.1)};
        Points a = new Points(b);
        for(Double s : a)
            System.out.println(s);
 
    }
}
