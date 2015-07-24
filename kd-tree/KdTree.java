public class KdTree {
  private SET<Point2D> points;

  // construct an empty set of points 
  public KdTree()
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
    points.add(p);
  }

  // does the set contain point p? 
  public boolean contains(Point2D p)
  {
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
    Stack<Point2D> inside = new Stack<Point2D>();

    for (Point2D point : points) {
      if (rect.contains(point)) {
        inside.push(point);
      }
    }

    return inside;
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p)
  {
    MinPQ<Point2D> queue = new MinPQ<Point2D>();

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
