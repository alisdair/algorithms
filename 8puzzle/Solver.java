public class Solver {
  private class Step implements Comparable<Step> {
    private Step previous;
    private Board board;
    private int moves;
    private int priority;

    Step(Step previous, Board board) {
      this.board = board;
      this.previous = previous;

      if (previous != null) {
        moves = previous.moves + 1;
      } else {
        moves = 0;
      }

      priority = board.manhattan() + moves;
    }

    public int compareTo(Step other) {
      if (priority < other.priority) {
        return -1;
      } else if (priority > other.priority) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  private Board orig;
  private Board twin;
  private MinPQ<Step> origPQ;
  private MinPQ<Step> twinPQ;
  private boolean origSolved;
  private boolean twinSolved;
  private Stack<Board> solution;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial)
  {
    orig = initial;
    twin = initial.twin();
    origPQ = new MinPQ<Step>();
    twinPQ = new MinPQ<Step>();
    origSolved = false;
    twinSolved = false;

    origPQ.insert(new Step(null, orig));
    twinPQ.insert(new Step(null, twin));

    while (!origSolved && !twinSolved) {
      Step step;

      step = origPQ.delMin();

      if (step.board.isGoal()) {
        solution = findSolution(step);
        origSolved = true;
        break;
      }

      for (Board next : step.board.neighbors()) {
        if (step.previous != null && next.equals(step.previous.board)) {
          continue;
        }

        origPQ.insert(new Step(step, next));
      }

      step = twinPQ.delMin();

      if (step.board.isGoal()) {
        solution = null;
        twinSolved = true;
        break;
      }

      for (Board next : step.board.neighbors()) {
        if (step.previous != null && next.equals(step.previous.board)) {
          continue;
        }

        twinPQ.insert(new Step(step, next));
      }
    }
  }

  private Stack<Board> findSolution(Step step) {
    Step current = step;
    Stack<Board> stack = new Stack<Board>();

    while (current != null) {
      stack.push(current.board);
      current = current.previous;
    }

    return stack;
  }

  // is the initial board solvable?
  public boolean isSolvable()
  {
    return origSolved;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves()
  {
    if (!origSolved) {
      return -1;
    }

    return solution.size() - 1;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution()
  {
    return solution;
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
