import java.util.Comparator;

public class KdTree {
  private static class Node {
    private Point2D p;
    private RectHV rect;
    private Node lb;
    private Node rt;

    private Node(Point2D point, RectHV rectangle) {
      p = point;
      rect = rectangle;
    }
  }

  private Node root;

  public KdTree()
  {
  }

  public boolean isEmpty()
  {
    return root == null;
  }

  public int size()
  {
    if (root == null) {
      return 0;
    }

    Stack<Node> nodes = new Stack<Node>();
    int size = 0;

    nodes.push(root);

    while (!nodes.isEmpty()) {
      Node node = nodes.pop();

      size += 1;

      if (node.lb != null) {
        nodes.push(node.lb);
      }

      if (node.rt != null) {
        nodes.push(node.rt);
      }
    }

    return size;
  }

  public void insert(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    root = insert(root, p, true, rect);
  }

  private Node insert(Node x, Point2D p, boolean leftRight, RectHV rect) {
    if (x == null) {
      return new Node(p, rect);
    }

    Comparator<Point2D> comparator;
    
    if (leftRight) {
      comparator = Point2D.X_ORDER;
    } else {
      comparator = Point2D.Y_ORDER;
    }

    int cmp = comparator.compare(p, x.p);
    RectHV nextRect = subdivide(rect, x.p, cmp, leftRight);

    if (cmp < 0) {
      x.lb = insert(x.lb, p, !leftRight, nextRect);
    } else if (cmp >= 0) {
      x.rt = insert(x.rt, p, !leftRight, nextRect);
    }

    return x;
  }

  private RectHV subdivide(RectHV rect, Point2D p, int cmp, boolean leftRight)
  {
    if (leftRight)
    {
      if (cmp < 0)
      {
        return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
      }
      else
      {
        return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
      }
    }
    else
    {
      if (cmp < 0)
      {
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
      }
      else
      {
        return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
      }
    }
  }

  public boolean contains(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    return contains(root, p, true);
  }

  private boolean contains(Node x, Point2D p, boolean leftRight)
  {
    if (x == null) {
      return false;
    }

    if (x.p == p) {
      return true;
    }

    Comparator<Point2D> comparator;
    
    if (leftRight) {
      comparator = Point2D.X_ORDER;
    } else {
      comparator = Point2D.Y_ORDER;
    }

    int cmp = comparator.compare(p, x.p);

    if (cmp < 0) {
      return contains(x.lb, p, !leftRight);
    } else {
      return contains(x.rt, p, !leftRight);
    }
  }

  private static class NodeSplit {
    private Node node;
    private boolean leftRight;

    private NodeSplit(Node n, boolean lr) {
      node = n;
      leftRight = lr;
    }
  }

  // draw all points to standard draw 
  public void draw()
  {
    if (root == null) {
      return;
    }

    Stack<NodeSplit> nodes = new Stack<NodeSplit>();
    int size = 0;

    nodes.push(new NodeSplit(root, true));

    while (!nodes.isEmpty()) {
      NodeSplit ns = nodes.pop();

      StdDraw.setPenRadius(.01);
      StdDraw.setPenColor(StdDraw.BLACK);
      ns.node.p.draw();

      RectHV r = ns.node.rect;
      StdDraw.setPenRadius();

      if (ns.leftRight) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(ns.node.p.x(), r.ymin(), ns.node.p.x(), r.ymax());
      } else {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(r.xmin(), ns.node.p.y(), r.xmax(), ns.node.p.y());
      }

      if (ns.node.lb != null) {
        nodes.push(new NodeSplit(ns.node.lb, !ns.leftRight));
      }

      if (ns.node.rt != null) {
        nodes.push(new NodeSplit(ns.node.rt, !ns.leftRight));
      }
    }
  }

  // all points that are inside the rectangle 
  public Iterable<Point2D> range(RectHV rect)
  {
    if (rect == null) {
      throw new java.lang.NullPointerException();
    }

    Stack<Point2D> inside = new Stack<Point2D>();

    if (root == null) {
      return inside;
    }

    Stack<Node> nodes = new Stack<Node>();
    nodes.push(root);

    while (!nodes.isEmpty()) {
      Node node = nodes.pop();

      if (!node.rect.intersects(rect)) {
        continue;
      }

      if (rect.contains(node.p)) {
        inside.push(node.p);
      }

      if (node.lb != null) {
        nodes.push(node.lb);
      }

      if (node.rt != null) {
        nodes.push(node.rt);
      }
    }

    return inside;
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    if (root == null) {
      return null;
    }

    Stack<NodeSplit> nodes = new Stack<NodeSplit>();
    Point2D nearest = root.p;
    double nearestDistanceSquared = nearest.distanceSquaredTo(p);
    nodes.push(new NodeSplit(root, true));

    while (!nodes.isEmpty()) {
      NodeSplit ns = nodes.pop();
      Node node = ns.node;

      double distanceSquared = node.p.distanceSquaredTo(p);
      if (distanceSquared < nearestDistanceSquared) {
        nearest = node.p;
        nearestDistanceSquared = distanceSquared;
      }

      double rectDistanceSquared = node.rect.distanceSquaredTo(p);
      if (rectDistanceSquared >= nearestDistanceSquared) {
        continue;
      }

      NodeSplit nslb = new NodeSplit(node.lb, !ns.leftRight);
      NodeSplit nsrt = new NodeSplit(node.rt, !ns.leftRight);

      if (node.lb == null && node.rt == null) {
        continue;
      }

      if (node.rt == null) {
        nodes.push(nslb);
        continue;
      }

      if (node.lb == null) {
        nodes.push(nsrt);
        continue;
      }

      // Compare point to each child so that we can insert the closest
      // one first
      Comparator<Point2D> comparator;

      if (ns.leftRight) {
        comparator = Point2D.X_ORDER;
      } else {
        comparator = Point2D.Y_ORDER;
      }

      int cmp = comparator.compare(p, node.p);

      if (cmp < 0) {
        nodes.push(nslb);
        nodes.push(nsrt);
      } else {
        nodes.push(nsrt);
        nodes.push(nslb);
      }
    }

    return nearest;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args)
  {
    String filename = args[0];
    In in = new In(filename);

    // initialize the two data structures with point from standard input
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }

    kdtree.draw();
  }
}
