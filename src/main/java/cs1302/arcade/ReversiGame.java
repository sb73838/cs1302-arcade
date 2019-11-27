package cs1302.arcade;

public class ReversiGame {

	ReversiPlayer player1;
	ReversiPlayer player2;
	ReversiPlayer currentPlayer;

	public void players() {
		player1 = new ReversiPlayer("B");
		player2 = new ReversiPlayer("W");
		currentPlayer = new ReversiPlayer("B");
	}

	public void currentPlayer() {

	}
}
