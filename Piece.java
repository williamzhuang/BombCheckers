public class Piece {
	private boolean fire;
	private Board b;
	private int x, y;
	private String type;
	private boolean captured;
	private boolean king;

	public Piece(boolean isFire, Board b, int x, int y, String type) {
		fire = isFire;
		this.b = b;
		this.x = x;
		this.y = y;
		this.type = type;
		captured = false;
		king = false;
	}

	public boolean isFire() {
		return fire;
	}

	public int side() {
		if (fire == true) {
			return 0;
		} else {
			return 1;
		}
	}

	public boolean isKing() {
		if ((isFire() && (y == 7)) || (!isFire() && (y == 0))) {
			king = true;
		}

		return king;
	}

	public boolean isBomb() {
		if (type.equals("bomb")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isShield() {
		if (type.equals("shield")) {
			return true;
		} else {
			return false;
		}
	}

	public void move(int x, int y) {
		// Removes original. 
		b.remove(this.x, this.y);
		int remX = (this.x + x) / 2;
		int remY = (this.y + y) / 2; 
		this.x = x; 
		this.y = y; 
		// If something was removed at target, something has been captured.
		if (b.remove(remX, remY) != null) {
			captured = true;
			b.remove(remX, remY);
		}

		b.place(this, x, y); 
	}

	public boolean hasCaptured() {
		return captured;
	}

	public void doneCapturing() {
		captured = false;
	}


}
