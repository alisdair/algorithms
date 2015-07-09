public class PercolationStats {
    private double[] thresholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T)
    {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException("must have N > 0 and T > 0");

        int sites = N * N;
        thresholds = new double[T];
        for (int i = 0; i < T; i++)
        {
            double opened = 0;
            Percolation p = new Percolation(N);

            while (!p.percolates())
            {
                int row, col;

                do {
                    row = StdRandom.uniform(N) + 1;
                    col = StdRandom.uniform(N) + 1;
                } while (p.isOpen(row, col));

                p.open(row, col);
                opened++;
            }
            thresholds[i] = opened / sites;
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(thresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo()
                + ", " + ps.confidenceHi());
    }
}
