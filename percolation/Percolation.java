public class Percolation {
    private boolean[] grid;
    private int count;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF full;
    private int bottom;
    private int top;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N)
    {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N must be > 0");
        }
        count = N;
        grid = new boolean[count * count];
        
        uf = new WeightedQuickUnionUF(count * count + 2);
        full = new WeightedQuickUnionUF(count * count + 1);

        int last = index(count, count);
        top = last + 1;
        bottom = last + 2;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j)
    {
        grid[index(i, j)] = true;

        if (i == 1) {
            uf.union(index(i, j), top);
            full.union(index(i, j), top);
        }

        if (i == count)
            uf.union(index(i, j), bottom);

        if (i > 1 && isOpen(i - 1, j)) {
            uf.union(index(i, j), index(i - 1, j));
            full.union(index(i, j), index(i - 1, j));
        }

        if (i < count && isOpen(i + 1, j)) {
            uf.union(index(i, j), index(i + 1, j));
            full.union(index(i, j), index(i + 1, j));
        }

        if (j > 1 && isOpen(i, j - 1)) {
            uf.union(index(i, j), index(i, j - 1));
            full.union(index(i, j), index(i, j - 1));
        }

        if (j < count && isOpen(i, j + 1)) {
            uf.union(index(i, j), index(i, j + 1));
            full.union(index(i, j), index(i, j + 1));
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        return grid[index(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        return full.connected(index(i, j), top);
    }

    // does the system percolate?
    public boolean percolates()
    {
        return uf.connected(bottom, top);
    }

    private int index(int i, int j)
    {
        if (i < 1 || i > count)
            throw new java.lang.IndexOutOfBoundsException(
                    "row index i is out of bounds");

        if (j < 1 || j > count)
            throw new java.lang.IndexOutOfBoundsException(
                    "column index j is out of bounds");

        return (i - 1) * count + (j - 1);
    }

    // public String toString()
    // {
    //     StringBuilder s = new StringBuilder((count * 2 + 1) * count);

    //     for (int i = 1; i <= count; i++)
    //     {
    //         for (int j = 1; j <= count; j++)
    //         {
    //             if (grid[index(i, j)])
    //                 s.append("  ");
    //             else
    //                 s.append("\u2588 ");
    //         }
    //         s.append("\n");
    //     }

    //     return s.toString();
    // }

    public static void main(String[] args)
    {
        // Percolation p = new Percolation(4);
        // System.out.println(p);
        // p.open(1, 1);
        // p.open(2, 1);
        // p.open(2, 3);
        // p.open(3, 3);
        // System.out.println(p);
        // System.out.println("(3, 3) isFull? " + p.isFull(3, 3));

        // p.open(2, 2);
        // p.open(3, 2);
        // System.out.println(p);
        // System.out.println("(3, 3) isFull? " + p.isFull(3, 3));

        // p.open(4, 3);
        // System.out.println(p);
        // System.out.println("Percolates? " + p.percolates());
    }
}
