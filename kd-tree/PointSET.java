import java.util.Comparator;

public class PointSET {
  private SET<Point2D> points;

  // construct an empty set of points 
  public PointSET()
  {
    points = new SET<Point2D>();
  }

  // is the set empty? 
  public boolean isEmpty()
  {
    return points.isEmpty();
  }

  // number of points in the set 
  public int size()
  {
    return points.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    points.add(p);
  }

  // does the set contain point p? 
  public boolean contains(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    return points.contains(p);
  }

  // draw all points to standard draw 
  public void draw()
  {
    for (Point2D point : points) {
      point.draw();
    }
  }

  // all points that are inside the rectangle 
  public Iterable<Point2D> range(RectHV rect)
  {
    if (rect == null) {
      throw new java.lang.NullPointerException();
    }

    Stack<Point2D> inside = new Stack<Point2D>();

    for (Point2D point : points) {
      if (rect.contains(point)) {
        inside.push(point);
      }
    }

    return inside;
  }

  private class DistanceToOrder implements Comparator<Point2D> {
    private Point2D point;

    public DistanceToOrder(Point2D point) {
      this.point = point;
    }

    public int compare(Point2D p, Point2D q) {
      double dist1 = point.distanceSquaredTo(p);
      double dist2 = point.distanceSquaredTo(q);
      if      (dist1 < dist2) return -1;
      else if (dist1 > dist2) return +1;
      else                    return  0;
    }
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    if (size() == 0) {
      return null;
    }

    Comparator<Point2D> distanceComparator = new DistanceToOrder(p);
    MinPQ<Point2D> queue = new MinPQ<Point2D>(size(), distanceComparator);

    for (Point2D point : points) {
      queue.insert(point);
    }

    return queue.min();
  }

  // unit testing of the methods (optional)
  public static void main(String[] args)
  {
  }
}
