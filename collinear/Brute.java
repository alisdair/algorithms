import java.util.Arrays;

public class Brute
{
  private static Point[] readPoints(String file)
  {
    In in = new In(file);
    int N = in.readInt();

    Point[] points = new Point[N];

    for (int i = 0; i < N; i++)
    {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    return points;
  }

  private static void printSeg(Point[] seg)
  {
    for (int i = 0; i < seg.length; i++)
    {
      StdOut.print(seg[i]);
      if (i < seg.length - 1)
        StdOut.print(" -> ");
    }
    StdOut.println();
  }

  public static void main(String[] args)
  {
    Point[] points = readPoints(args[0]);
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);

    for (int i = 0; i < points.length; i++)
    {
      Point p = points[i];
      p.draw();

      for (int j = i + 1; j < points.length; j++)
      {
        Point q = points[j];
        double pq = p.slopeTo(q);

        for (int k = j + 1; k < points.length; k++)
        {
          Point r = points[k];
          double pr = p.slopeTo(r);

          if (pq != pr)
            continue;

          for (int l = k + 1; l < points.length; l++)
          {
            Point s = points[l];
            double ps = p.slopeTo(s);

            if (pr != ps)
              continue;

            Point[] t = { p, q, r, s };
            Arrays.sort(t);

            t[0].drawTo(t[3]);
            printSeg(t);
          }
        }
      }
    }
  }
}
