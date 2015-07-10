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
      StdOut.println(p);

      Arrays.sort(sorted, i, sorted.length, p.SLOPE_ORDER);
      assert sorted[i] == p;

      double slope = p.slopeTo(p);
      int start = i;
      for (int j = i; j < sorted.length; j++)
      {
        Point q = sorted[j];
        double newSlope = p.slopeTo(q);

        if (p.slopeTo(q) == slope) {
          StdOut.println("Same slope, continuing");
          continue;
        }

        int length = j - start;
        StdOut.printf("Found segment: %d to %d (length %d, %f != %f)\n",
            start, j, length, slope, newSlope);

        if (length >= 3)
        {
          Point[] seg = new Point[length + 1];
          seg[0] = p;

          for (int k = 0; k < length; k++)
            seg[k + 1] = sorted[start + k];

          Arrays.sort(seg);
          seg[0].drawTo(seg[seg.length - 1]);
          printSeg(seg);
        }

        start = j;
        slope = newSlope;
      }
    }

    StdOut.println("Done");
  }
}
