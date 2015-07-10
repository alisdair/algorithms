import java.util.Comparator;

public class Point implements Comparable<Point>
{
  public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();

  private final int x;
  private final int y;

  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void draw()
  {
    StdDraw.point(x, y);
  }

  public void drawTo(Point that)
  {
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  // slope between this point and that point
  public double slopeTo(Point that)
  {
    double dy = that.y - this.y;
    double dx = that.x - this.x;

    if (dx == 0 && dy == 0)
      return Double.NEGATIVE_INFINITY;

    if (dx == 0)
      return Double.POSITIVE_INFINITY;

    if (dy == 0)
      return +0.0;

    return dy/dx;
  }

  // is this point lexicographically smaller than that one?
  // comparing y-coordinates and breaking ties by x-coordinates
  public int compareTo(Point that)
  {
    if (this.x == that.x && this.y == that.y)
      return 0;

    if (this.y < that.y || (this.y == that.y && this.x < that.x))
      return -1;

    return 1;
  }

  public String toString()
  {
    return "(" + x + ", " + y + ")";
  }

  public static void main(String[] args)
  {
  }

  private class SlopeOrder implements Comparator<Point>
  {
    public int compare(Point q, Point r)
    {
      double qs = slopeTo(q);
      double rs = slopeTo(r);

      if (qs < rs)
        return -1;

      if (qs > rs)
        return 1;

      return 0;
    }
  }
}
