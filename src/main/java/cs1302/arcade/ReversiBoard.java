package cs1302.arcade;

/**
 * The class {@code ReversiBoard} provides utility functions for the Reversi
 * game. It has several methods that determine whether a particular move is
 * valid after taking all the directions into consideration.
 *
 * @author Sahil Balhara & Vincent Bui
 */
public class ReversiBoard {

    private int[][] reversiArray;
    private int currentPlayer;
    private int currentOpponent;

    private static final int GRID_SIZE = 8;

    /**
     * Constructor of the ReversiBoard class.
     *
     * @param revArray   the array which hold information about all the squares
     * @param currPlayer the current player who is making the move
     */
    public ReversiBoard(int[][] revArray, int currPlayer) {

        reversiArray = revArray;
        currentPlayer = currPlayer;

        if (currentPlayer == 1) {
            currentOpponent = 2;
        } else {
            currentOpponent = 1;
        }
    }

    /**
     * Method for determining if the the move is valid for the current player.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean isValidMove(int row, int col, boolean bTakeAction) {

        boolean bValid = false;

        // check if there is a valid move in any direction
        if (checkNorth(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkSouth(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkEast(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkWest(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkNorthEast(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkSouthEast(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkNorthWest(row, col, bTakeAction)) {
            bValid = true;
        }
        if (checkSouthWest(row, col, bTakeAction)) {
            bValid = true;
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the North direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkNorth(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;

        // check in North direction
        for (int n = row - 1; n >= 0; n--) {
            if (n == row - 1) {
                if (reversiArray[n][col] != currentOpponent) {
                    break;
                }
            } else {
                if (reversiArray[n][col] != currentOpponent) {
                    if (reversiArray[n][col] == currentPlayer) {
                        bValid = true;
                        stop = n;
                    }
                    break;
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = row - 1; n > stop; n--) {
                reversiArray[n][col] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the South direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkSouth(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;

        // check in South direction
        for (int n = row + 1; n < GRID_SIZE; n++) {
            if (n == row + 1) {
                if (reversiArray[n][col] != currentOpponent) {
                    break;
                }
            } else {
                if (reversiArray[n][col] != currentOpponent) {
                    if (reversiArray[n][col] == currentPlayer) {
                        bValid = true;
                        stop = n;
                    }
                    break;
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = row + 1; n < stop; n++) {
                reversiArray[n][col] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the East direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkEast(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;

        // check in East direction
        for (int n = col + 1; n < GRID_SIZE; n++) {
            if (n == col + 1) {
                if (reversiArray[row][n] != currentOpponent) {
                    break;
                }
            } else {
                if (reversiArray[row][n] != currentOpponent) {
                    if (reversiArray[row][n] == currentPlayer) {
                        bValid = true;
                        stop = n;
                    }
                    break;
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = col + 1; n < stop; n++) {
                reversiArray[row][n] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the West direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkWest(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;

        // check in West direction
        for (int n = col - 1; n >= 0; n--) {
            if (n == col - 1) {
                if (reversiArray[row][n] != currentOpponent) {
                    break;
                }
            } else {
                if (reversiArray[row][n] != currentOpponent) {
                    if (reversiArray[row][n] == currentPlayer) {
                        bValid = true;
                        stop = n;
                    }
                    break;
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = col - 1; n > stop; n--) {
                reversiArray[row][n] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the NorthEast direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkNorthEast(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;
        int numSteps = 0;

        if (row < (GRID_SIZE - 1 - col)) {
            numSteps = row;
        } else {
            numSteps = (GRID_SIZE - 1 - col);
        }
        // check in North East direction
        if (numSteps > 1) {
            for (int n = 1; n <= numSteps; n++) {
                if (n == 1) {
                    if (reversiArray[row - n][col + n] != currentOpponent) {
                        break;
                    }
                } else {
                    if (reversiArray[row - n][col + n] != currentOpponent) {
                        if (reversiArray[row - n][col + n] == currentPlayer) {
                            bValid = true;
                            stop = n;
                        }
                        break;
                    }
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = 1; n < stop; n++) {
                reversiArray[row - n][col + n] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the NorthWest direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkNorthWest(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;
        int numSteps = 0;

        if (row < col) {
            numSteps = row;
        } else {
            numSteps = col;
        }
        // check in North West direction
        if (numSteps > 1) {
            for (int n = 1; n <= numSteps; n++) {
                if (n == 1) {
                    if (reversiArray[row - n][col - n] != currentOpponent) {
                        break;
                    }
                } else {
                    if (reversiArray[row - n][col - n] != currentOpponent) {
                        if (reversiArray[row - n][col - n] == currentPlayer) {
                            bValid = true;
                            stop = n;
                        }
                        break;
                    }
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = 1; n < stop; n++) {
                reversiArray[row - n][col - n] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the SouthWest direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkSouthWest(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;
        int numSteps = 0;

        if (col < (GRID_SIZE - 1 - row)) {
            numSteps = col;
        } else {
            numSteps = (GRID_SIZE - 1 - row);
        }
        // check in South West direction
        if (numSteps > 1) {
            for (int n = 1; n <= numSteps; n++) {
                if (n == 1) {
                    if (reversiArray[row + n][col - n] != currentOpponent) {
                        break;
                    }
                } else {
                    if (reversiArray[row + n][col - n] != currentOpponent) {
                        if (reversiArray[row + n][col - n] == currentPlayer) {
                            bValid = true;
                            stop = n;
                        }
                        break;
                    }
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = 1; n < stop; n++) {
                reversiArray[row + n][col - n] = currentPlayer;
            }
        }

        return bValid;
    }

    /**
     * Method for determining if the the move is valid in the SouthEast direction.
     *
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean checkSouthEast(int row, int col, boolean bTakeAction) {

        boolean bValid = false;
        int stop = 0;
        int numSteps = 0;

        if ((GRID_SIZE - 1 - row) < (GRID_SIZE - 1 - col)) {
            numSteps = (GRID_SIZE - 1 - row);
        } else {
            numSteps = (GRID_SIZE - 1 - col);
        }
        // check in South East direction
        if (numSteps > 1) {
            for (int n = 1; n <= numSteps; n++) {
                if (n == 1) {
                    if (reversiArray[row + n][col + n] != currentOpponent) {
                        break;
                    }
                } else {
                    if (reversiArray[row + n][col + n] != currentOpponent) {
                        if (reversiArray[row + n][col + n] == currentPlayer) {
                            bValid = true;
                            stop = n;
                        }
                        break;
                    }
                }
            }
        }

        // if there were some pieces captured
        if (bValid && bTakeAction) {
            for (int n = 1; n < stop; n++) {
                reversiArray[row + n][col + n] = currentPlayer;
            }
        }

        return bValid;
    }
}
