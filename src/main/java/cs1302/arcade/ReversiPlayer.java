package cs1302.arcade;

public class ReversiPlayer {

	private String color;
	private int score;

	public ReversiPlayer(String color) {
			color = this.color;
	}

	public String setColor(String s) {
		if (s.equals("B")) {
			this.color = "Black";
		}
		if (s.equals("W")) {
			this.color = "White";
		}
		return color;
	}

	public String getColor() {
		return color;
	}

}
