/******************************************************************************
 *  Name:    Ian Boucher
 *  NetID:   ian
 *  Precept: Coursera
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data
 *                structures to determine the threshold. woot. woot.
 ******************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF ufObj;
    private int size;
    private int top = 0;
    private int nOpenSites = 0;
    private int [] siteStatus;
    private boolean percolates;

    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("'n' must be greater than 0");
        }

        size  = n;
        ufObj = new WeightedQuickUnionUF((n * n) + 1);
        siteStatus = new int [(n * n) + 1];
    }

    public void open(int row, int col) {

        int site  = getIndexFromGridRef(row, col);
        if (siteStatus[site] > 0) return;  // site already open
        else siteStatus[site] = 1; // open current site
        nOpenSites++;

        int root;
        int left  = site - 1;
        int right = site + 1;
        int above = site - size;
        int below = site + size;

        if (row == 1) ufObj.union(site, top);
        else if (siteStatus[above] > 0) joinAdjacentSite(site, above);

        if (row == size) siteStatus[site] = 2;
        else if (siteStatus[below] > 0) joinAdjacentSite(site, below);

        if (col > 1 && siteStatus[left] > 0) joinAdjacentSite(site, left);
        if (col < size && siteStatus[right] > 0) joinAdjacentSite(site, right);

        root = ufObj.find(site);
        if (isSink(site)) {
            siteStatus[root] = 2;
            if (ufObj.connected(root, top)) {
                percolates = true;
            }
        }
    }

    public boolean isOpen(int row, int col) {
        int site = getIndexFromGridRef(row, col);
        return siteStatus[site] > 0;
    }

    // site is 'full' if connected to 'top' site [0]
    public boolean isFull(int row, int col) {
        int site = getIndexFromGridRef(row, col);
        return ufObj.connected(top, site);
    }

    public int numberOfOpenSites() {
        return nOpenSites;
    }

    public boolean percolates() {
        return percolates;
    }

    public static void main(String[] args) {
        // code...
    }


    // site is a sink (connected to bottom) if status is marked as 2
    private boolean isSink(int index) {
        return siteStatus[index] > 1;
    }

    private void joinAdjacentSite(int site, int neighbour) {
        int root = ufObj.find(neighbour);
        if (isSink(root)) siteStatus[site] = 2;
        ufObj.union(site, neighbour);
    }

    private int getIndexFromGridRef(int row, int col) {
        checkValidIndex(row, col);
        return size * (row - 1) + col;
    }

    private void checkValidIndex(int row, int col) {
        if (row < 1 || row > size) {
            throw new java.lang.IndexOutOfBoundsException("row out of bounds");
        }
        if (col < 1 || col > size) {
            throw new java.lang.IndexOutOfBoundsException("col out of bounds");
        }
    }
}
