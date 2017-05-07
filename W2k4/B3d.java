import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;

public class Board {
    
    private int [][] tiles;
    private int size;
    private int blankRow;
    private int blankCol;
    
    // construct a board from an n-by-n array of blocks  
    public Board(int[][] blocks) {

        size  = blocks.length;
        tiles = new int[size][size];
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (blocks[row][col] == 0) {
                    blankRow = row; 
                    blankCol = col;
                }
                tiles[row][col] = blocks[row][col];
            }
        }
    }
    
    
    // board dimension
    public int dimension() { 
        return size;
    }
    
    
    // number of blocks out of place
    public int hamming() {     
        int dHamming = 0;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (tileNotInPlace(row, col)) dHamming += 1;
            }
        }
        return dHamming;
    }
    
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int dManhattan = 0;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (tiles[row][col] != 0) {
                    dManhattan += distanceFromGoal(row, col);
                }
            }
        } 
        return dManhattan;
    }
     
    
    // check is board is goal board
    public boolean isGoal() {
        return hamming() == 0;
    }
    
    
    // return a twin board - obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twin = copy(tiles);
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size - 1; col++) {
                if (!isBlankTile(tiles[row][col]) && !isBlankTile(tiles[row][col + 1])) {
                    exch(twin, row, col, row, col + 1);
                    return new Board(twin);
                }
            }
        }
        throw new RuntimeException(); // necessary if nothing is returned from loop;
    }
    
    
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (tiles[row][col] != that.tiles[row][col]) return false;
            }
        }
        return true;
    }
    
    
    public Iterable<Board> neighbors() {
        
        Stack<Board> stackOfNeighbors = new Stack<Board>();
        int[][] neighbor;
        
        // attempted to eleminate copy(tiles) by creating a new board and 
        // performing the exchange on the board, but the exchange after 
        // initialization caused big problems...
        
        if (blankRow - 1 >= 0) { // swap tile above;
            neighbor = copy(tiles);
            exch(neighbor, blankRow, blankCol, blankRow - 1, blankCol);
            stackOfNeighbors.push(new Board(neighbor));
        }
        
        if (blankRow + 1 < size) { // swap tile below
            neighbor = copy(tiles);
            exch(neighbor, blankRow, blankCol, blankRow + 1, blankCol);
            stackOfNeighbors.push(new Board(neighbor));
        }
        
        if (blankCol - 1 >= 0) { // swap left tile
            neighbor = copy(tiles);
            exch(neighbor, blankRow, blankCol, blankRow, blankCol - 1);
            stackOfNeighbors.push(new Board(neighbor));
        }
        
        if (blankCol + 1 < size) { // swap right tile
            neighbor = copy(tiles);
            exch(neighbor, blankRow, blankCol, blankRow, blankCol + 1);
            stackOfNeighbors.push(new Board(neighbor));
        }
        
        return stackOfNeighbors;
    }
    
    
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                s.append(String.format("%2d ", tiles[row][col]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    

    public static void main(String[] args) {
        int[][] blocks = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        Board a = new Board(blocks);
        StdOut.println("Size: " + a.dimension());
        StdOut.println("Hamming: " + a.hamming());
        StdOut.println("Manhattan: " + a.manhattan());
        StdOut.println("---------------------------");
        StdOut.println("Original Board: " + a);
        StdOut.println("Twin: " + a.twin());
        StdOut.println("---------------------------");
        StdOut.println("Original Board: " + a);
        StdOut.println("neighbors:");
        for (Board board: a.neighbors()) {
            StdOut.println(board);
            StdOut.println("board is goal?: " + board.isGoal());
            StdOut.println("---------------------------");
        }
    }

    
    // exchange two grid elements b[i][j] and b[p][q]
    private void exch(int[][] b, int i, int j, int p, int q) {
        int swap = b[i][j];
        b[i][j]  = b[p][q];
        b[p][q]  = swap;
    }
    
    
    private int[][] copy(int[][] a) {
        int[][] aCopy = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aCopy[i][j] = a[i][j];
            }
        }
        return aCopy;
    }
    
    
    private boolean tileNotInPlace(int row, int col) {
        int tile = tiles[row][col];
        
        return !isBlankTile(tile) && tile != goalValAt(row, col);
    }
    
    
    private int goalValAt(int row, int col) {
        return (row * size) + col + 1;
    }
    
    
    private boolean isBlankTile(int tile) {
        return tile == 0;
    }
    
    
    private int distanceFromGoal(int row, int col) {
        int tileVal  = tiles[row][col];
        int distance = 0;
        
        if (!isBlankTile(tileVal)) {
            int goalRow = (tileVal - 1) / size;
            int goalCol = (tileVal - 1) % size;
            distance = Math.abs(row - goalRow) + Math.abs(col - goalCol);
        }
        return distance;
    }
}