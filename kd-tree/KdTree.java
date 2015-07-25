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
    range(root, rect, inside);
    return inside;
  }

  private void range(Node x, RectHV rect, Stack<Point2D> inside)
  {
    if (rect.contains(x.p)) {
      inside.push(x.p);
    }

    if (x.lb != null && x.lb.rect.intersects(rect)) {
      range(x.lb, rect, inside);
    }

    if (x.rt != null && x.rt.rect.intersects(rect)) {
      range(x.rt, rect, inside);
    }
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

    return findNearest(root, true, p, root.p);
  }

  private Point2D findNearest(Node x, boolean leftRight, Point2D query, Point2D nearest)
  {
    double d2n = query.distanceSquaredTo(nearest);
    double d2x = query.distanceSquaredTo(x.p);

    // Is this node nearer than the current nearest? If so, update
    if (d2x < d2n) {
      nearest = x.p;
      d2n = d2x;
    }

    // No children? Return current nearest
    if (x.lb == null && x.rt == null) {
      return nearest;
    }

    // Only left/bottom child, and it might be closer? Recurse to it
    if (x.rt == null && x.lb.rect.distanceSquaredTo(query) < d2n) {
      return findNearest(x.lb, !leftRight, query, nearest);
    }

    // Only right/top child, and it might be closer? Recurse to it
    if (x.lb == null && x.rt.rect.distanceSquaredTo(query) < d2n) {
      return findNearest(x.rt, !leftRight, query, nearest);
    }

    // Not both children? Must have one child and not closer, return
    if (x.lb == null || x.rt == null) {
      return nearest;
    }

    // Both children exist, so precompute the distance to their rects
    double d2lb = x.lb.rect.distanceSquaredTo(query);
    double d2rt = x.rt.rect.distanceSquaredTo(query);

    // If this is a left-right split, check x co-ordinates to find the
    // sub-tree on the same side as the query
    if ((leftRight && x.p.x() < query.x()) || (!leftRight && x.p.y() < query.y())) {
      if (d2lb < d2n) {
        nearest = findNearest(x.lb, !leftRight, query, nearest);
        d2n = query.distanceSquaredTo(nearest);
      }

      if (d2rt < d2n) {
        nearest = findNearest(x.rt, !leftRight, query, nearest);
      }
    } else {
      if (d2rt < d2n) {
        nearest = findNearest(x.rt, !leftRight, query, nearest);
        d2n = query.distanceSquaredTo(nearest);
      }

      if (d2lb < d2n) {
        nearest = findNearest(x.lb, !leftRight, query, nearest);
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
