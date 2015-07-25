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
  private int size;

  public KdTree()
  {
    size = 0;
  }

  public boolean isEmpty()
  {
    return size == 0;
  }

  public int size()
  {
    return size;
  }

  public void insert(Point2D p)
  {
    if (p == null) {
      throw new java.lang.NullPointerException();
    }

    root = insert(root, p, true, null, 0);
  }

  private Node insert(Node x, Point2D p, boolean leftRight, Node parent, int c) {
    if (x == null) {
      RectHV rect;

      if (parent == null) {
        rect = new RectHV(0.0, 0.0, 1.0, 1.0);
      } else {
        rect = subdivide(parent.rect, parent.p, c, !leftRight);
      }

      size++;
      return new Node(p, rect);
    }

    Comparator<Point2D> comparator;
    
    if (leftRight) {
      comparator = Point2D.X_ORDER;
    } else {
      comparator = Point2D.Y_ORDER;
    }

    int cmp = comparator.compare(p, x.p);

    if (cmp < 0) {
      x.lb = insert(x.lb, p, !leftRight, x, cmp);
    } else if (cmp > 0 || !x.p.equals(p)) {
      x.rt = insert(x.rt, p, !leftRight, x, cmp);
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

    if (x.p.equals(p)) {
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

    if (root != null) {
      range(root, rect, inside);
    }

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

    return findNearest(root, p, root.p);
  }

  private Point2D findNearest(Node x, Point2D query, Point2D nearest)
  {
    if (x == null) {
      return nearest;
    }

    double d2n = query.distanceSquaredTo(nearest);
    double d2r = x.rect.distanceSquaredTo(query);

    // Is the nearest point closer than this node's rectangle? If so, this
    // node can't contain anything nearer, so return
    if (d2n < d2r) {
      return nearest;
    }

    double d2p = query.distanceSquaredTo(x.p);

    // Is this node nearer than the current nearest? If so, update
    if (d2p < d2n) {
      nearest = x.p;
      d2n = d2p;
    }

    // No children? Return current nearest
    if (x.lb == null && x.rt == null) {
      return nearest;
    }

    // Only left/bottom child? Recurse to it
    if (x.rt == null) {
      return findNearest(x.lb, query, nearest);
    }

    // Only right/top child? Recurse to it
    if (x.lb == null) {
      return findNearest(x.rt, query, nearest);
    }

    // Not both children? Must have one child and not closer, return
    if (x.lb == null || x.rt == null) {
      return nearest;
    }

    // Try both children, the one containing the query point first
    if (x.lb.rect.contains(query)) {
      nearest = findNearest(x.lb, query, nearest);
      nearest = findNearest(x.rt, query, nearest);
    } else {
      nearest = findNearest(x.rt, query, nearest);
      nearest = findNearest(x.lb, query, nearest);
    }

    return nearest;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args)
  {
    String filename = args[0];
    In in = new In(filename);

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
