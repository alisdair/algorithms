public class Solver {
  private class Step {
    Board board;
    int manhattan;
    int steps;
  }

  private Board orig;
  private Board twin;
  private MinPQ<Step> origPQ;
  private MinPQ<Step> twinPQ;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    orig = initial;
    twin = initial.twin();
    origPQ = new MinPQ<Board>();
    twinPQ = new MinPQ<Board>();
  }

  // is the initial board solvable?
  public boolean isSolvable()
  {
    return solution() != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves()
  {
    Iterable<Board> solution = solution();

    if (solution == null) {
      return -1;
    }

    return solution.length();
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution()
  {
    return null;
  }

  // solve a slider puzzle (given below)
  public static void main(String[] args)
  {
    // create initial board from file
    In in = new In(args[0]);

    int N = in.readInt();
    int[][] blocks = new int[N][N];

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        blocks[i][j] = in.readInt();
      }
    }

    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
