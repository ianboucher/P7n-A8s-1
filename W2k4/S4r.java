import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack; // std Java Stack implementation has a bug - items not LIFO when iterating

public class Solver {
    
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int moves;
        
        public SearchNode(Board board) {
            this.board = board;
            this.prev  = null;
            this.moves = 0;
        }
        
        public int compareTo(SearchNode node) { // necessary for Comparable interface
            return (this.board.manhattan() - node.board.manhattan()) + 
                (this.moves - node.moves);
        }
    }
    
    
    private SearchNode solution = null;
    private SearchNode main = null;
    private SearchNode twin = null;
    
    // find a solution to the initial board (using the A* algorithm). Solution
    // for main board exists if there is no solution for its twin
    public Solver(Board initial) {
        
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();

        mainPQ.insert(new SearchNode(initial));
        twinPQ.insert(new SearchNode(initial.twin()));
        
        while (main == null && twin == null) {
            main = nextMove(mainPQ);
            twin = nextMove(twinPQ);
        }
        
        solution = main;
    }
    
    
    private SearchNode nextMove(MinPQ<SearchNode> pq) {
        if (pq.isEmpty()) return null;
        SearchNode node = pq.delMin();
        if (node.board.isGoal()) return node;
        
        for (Board board : node.board.neighbors()) {
            if (node.prev == null || !board.equals(node.prev.board)) { // critial to ensure neighbors are not queued repeatedly
                SearchNode newNode = new SearchNode(board);            // N.B. using "board != node.prev.board" doesn't work
                newNode.moves = node.moves + 1;                        // difference between 69/100 and 100/100!
                newNode.prev = node;
                pq.insert(newNode);
            }
        }
        return null;
    }
    
        
    public boolean isSolvable() {
        return solution != null;
    }
    
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return (solution == null) ? -1 : solution.moves;
    }
    
    
    // sequence of boards for a shortest solution;
    public Iterable<Board> solution() {   
        if (!isSolvable()) return null;
        
        Stack<Board> boardSequence = new Stack<Board>();
        SearchNode node = solution;
        while (node != null) {
            boardSequence.push(node.board);
            node = node.prev;
        }
        return boardSequence;
    }
    
    
    public static void main(String[] args) {
        // solve a slider puzzle (given below)
    } 
}