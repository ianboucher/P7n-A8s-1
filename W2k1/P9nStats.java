
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double SITE_CONST = 1.96;
    private int trials;
    private double [] percThresholds;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int t) {
        if (n < 1 || t < 1) {
            throw new java.lang.IllegalArgumentException("n and T must be greater than 0");
        }

        trials = t;
        percThresholds = new double [trials];

        for (int trial = 0; trial < trials; trial += 1) {

            Percolation percolation = new Percolation(n);
            int nSitesOpened = 0;

            while (!percolation.percolates()) {

                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    nSitesOpened += 1;
                }
            }

            percThresholds[trial] = (double) nSitesOpened / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percThresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (SITE_CONST * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (SITE_CONST * stddev() / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        // test client (described below)
    }
}
