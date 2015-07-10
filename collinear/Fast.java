import java.util.Arrays;

public class Fast
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

  private static void extractSeg(Point p, Point[] points, int start, int length)
  {
    Point[] line = new Point[length + 1];

    line[0] = p;
    for (int i = 0; i < length; i++)
    {
      line[i + 1] = points[start + i];
    }

    Arrays.sort(line);
    if (p != line[0])
      return;

    printSeg(line);

    line[0].drawTo(line[line.length - 1]);
  }

  public static void main(String[] args)
  {
    Point[] points = readPoints(args[0]);
    Point[] sorted = points.clone();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);

    for (int i = 0; i < points.length; i++)
    {
      Point p = points[i];
      p.draw();

      Arrays.sort(sorted, 0, sorted.length, p.SLOPE_ORDER);
      assert sorted[0] == p;

      int start = 0;
      double runSlope = p.slopeTo(p);
      for (int j = start; j < sorted.length; j++)
      {
        Point q = sorted[j];

        double slope = p.slopeTo(q);
        if (slope == runSlope) {
          continue;
        }

        runSlope = slope;

        int length = j - start;
        if (length >= 3) {
          extractSeg(p, sorted, start, length);
        }

        start = j;
      }

      int length = sorted.length - start;
      if (length >= 3) {
        extractSeg(p, sorted, start, length);
      }
    }
  }
}
