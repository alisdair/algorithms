public class Board
{
  private int[][] blocks;
  private int N;

  // construct a board from an N-by-N array of blocks (where blocks[i][j] =
  // block in row i, column j)
  public Board(int[][] blocks)
  {
    if (blocks == null) {
      throw new java.lang.NullPointerException();
    }

    this.blocks = blocks;
    N = blocks.length;
  }

  // board dimension N
  public int dimension()
  {
    return N;
  }

  // number of blocks out of place
  public int hamming()
  {
    int count = 0;

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        int block = blocks[i][j];

        if (block == 0) {
          continue;
        }

        if (block != i * N + j) {
          count += 1;
        }
      }
    }

    return count;
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan()
  {
    int count = 0;

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        int block = blocks[i][j];

        if (block == 0) {
          continue;
        }

        int col = block % N;
        int row = block / N;
        int score = Math.abs(row - i) + Math.abs(col - j);

        count += score;
      }
    }

    return count;
  }

  // is this board the goal board?
  public boolean isGoal()
  {
    return hamming() == 0;
  }

  private Board exchangedBoard(int a, int b, int c, int d) {
    int[][] bs = new int[N][];
    
    for (int i = 0; i < N; i++) {
      bs[i] = blocks[i].clone();
    }

    int t = bs[a][b];
    bs[a][b] = bs[c][d];
    bs[c][d] = t;
    return new Board(bs);
  }

  // a board that is obtained by exchanging two adjacent blocks in the same row
  public Board twin()
  {
    // Find first two consecutive blocks on same row which aren't zero, and
    // return a new board with those two exchanged
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N - 1; j++) {
        if (blocks[i][j] != 0 && blocks[i][j + 1] != 0) {
          return exchangedBoard(i, j, i, j + 1);
        }
      }
    }

    return null;
  }

  // does this board equal y?
  public boolean equals(Object y)
  {
    if (y == this) {
      return true;
    }

    if (y == null) {
      return false;
    }

    if (y.getClass() != this.getClass()) {
      return false;
    }

    Board that = (Board) y;

    if (that.N != this.N) {
      return false;
    }

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (that.blocks[i][j] != blocks[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors()
  {
    Stack<Board> boards = new Stack<Board>();

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        if (blocks[i][j] == 0) {
          if (i > 0) {
            boards.push(exchangedBoard(i, j, i - 1, j));
          }
          if (i < N - 1) {
            boards.push(exchangedBoard(i, j, i + 1, j));
          }
          if (j > 0) {
            boards.push(exchangedBoard(i, j, i, j - 1));
          }
          if (j < N - 1) {
            boards.push(exchangedBoard(i, j, i, j + 1));
          }

          return boards;
        }
      }
    }

    return boards;
  }

  // string representation of this board (in the output format specified below)
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append(N + "\n");
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        s.append(String.format("%2d ", blocks[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  }

  public static void main(String[] args)
  {
    int[][] blocks = { {1, 5, 4},
                       {3, 7, 0},
                       {6, 8, 2} };

    Board board = new Board(blocks);

    StdOut.println("Board:");
    StdOut.println(board);

    StdOut.printf("Hamming score should be 6: %d\n", board.hamming());
    StdOut.printf("Manhattan score should be 9: %d\n", board.manhattan());

    StdOut.printf("Goal? %s\n", board.isGoal());

    StdOut.println("Twin:");
    StdOut.println(board.twin());

    StdOut.println("Neighbors:");
    for (Board b : board.neighbors()) {
      StdOut.println(b);
    }
  }
}
