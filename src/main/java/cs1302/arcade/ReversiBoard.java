package cs1302.arcade;

public class ReversiBoard {

	private String reversiBoard[][];
	private int rows = 8;
	private int cols = 8;

	public Board() {
		reversiBoard = new String[rows][cols];
		rows = reversiBoard.length;
		cols = reversiBoard[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				reversiBoard[i][j] = "*";
			} // for
		} // for

		reversiBoard[3][3] = "B";
		reversiBoard[3][4] = "W";
		reversiBoard[4][3] = "W";
		reversiBoard[4][4] = "B";
	}
}
