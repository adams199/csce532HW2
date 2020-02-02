package AbstractGames;


public class MinimaxSearch<BOARD extends Board, MOVE extends Move> implements Search<BOARD,MOVE>  {
  BOARD board;
  int totalNodesSearched;
  int totalLeafNodes;

  @Override
  public MOVE findBestMove(BOARD board, int depth) {
    MOVE best_move = null;
    int runningNodeTotal = 0;
    long startTime = System.currentTimeMillis();
    long elapsedTime = 0;
    long currentPeriod;
    long previousPeriod = 0;
    int i = 1;

    this.board = board;

    // Including the iterative deepening for consistency.
    while ( i <= depth ) {
      totalNodesSearched = totalLeafNodes = 0;

      best_move = Minimax(i); // Min-Max alpha beta with transposition tables

      elapsedTime = System.currentTimeMillis()-startTime;
      currentPeriod = elapsedTime-previousPeriod;
      double rate = 0.0;
      if ( i > 3 && previousPeriod > 50 )
        rate = (currentPeriod - previousPeriod)/previousPeriod;
      previousPeriod = elapsedTime;

      runningNodeTotal += totalNodesSearched;
      System.out.println("Depth: " + i +" Time: " + elapsedTime/1000.0 + " " + currentPeriod/1000.0 + " Nodes Searched: " +
          totalNodesSearched + " Leaf Nodes: " + totalLeafNodes + " Rate: " + rate);

      // increment indexes;
      i = i + 2;
    }

    System.out.println("Nodes per Second = " + runningNodeTotal/(elapsedTime/1000.0));
    if (best_move == null ) {
      throw new Error ("No Move Available - Search Error!");
    }
    return best_move;
  }

  /**
   * TODO Write Minimax here!
   *
   * @param depth Depth to search to
   * @return best move found at this node
   */
  private MOVE Minimax(int depth) {
    Move bestMove = null;
    double bestMoveVal = AlphaBeta(this.board, depth, -9, +9, true, bestMove);
    return (MOVE)bestMove; // ??

  }

  private double AlphaBeta(BOARD currentBoard, int depth, double alpha, double beta, boolean maxTurn, Move bestMove)
  {
    if ((depth == 0) || (currentBoard.endGame() != -3)) {
      currentBoard.heuristicEvaluation(); }
    
    if (maxTurn)
    {
      Move moveList = currentBoard.generateMoves();
      double Val = -9;
      while (moveList != null)
      {
        currentBoard.makeMove(moveList);
        double oldVal = Val;
        Val = Math.max(Val, AlphaBeta(currentBoard, depth-1, alpha, beta, false, bestMove));
        if (oldVal != Val) {
          bestMove = moveList;}
        currentBoard.reverseMove(moveList);
        alpha = Math.max(alpha, Val);
        moveList.value = Val;
        if (alpha >= beta) {
          break;  }
        moveList = moveList.next;
      }
      return Val;
    }
    else
    {
      Move moveList = currentBoard.generateMoves();
      double Val = +9;
      while (moveList != null)
      {
        currentBoard.makeMove(moveList);
        double oldVal = Val;
        Val = Math.max(Val, AlphaBeta(currentBoard, depth-1, alpha, beta, true, bestMove));
        if (oldVal != Val) {
          bestMove = moveList;}
        currentBoard.reverseMove(moveList);
        beta = Math.min(beta, Val);
        moveList.value = Val;
        if (alpha >= beta) {
          break;  }
        moveList = moveList.next;
      }
      return Val;
    }
  }

}
