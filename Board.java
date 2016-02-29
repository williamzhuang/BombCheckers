/**
 * @Author William Zhuang 
 */

// GRADE THIS VERSION.

public class Board {

	private static Piece[][] pieces;
	private boolean fireTurn;
	private boolean selected;
	private boolean moved;
	private boolean exploded;
	private int selX, selY;
	private int mouseX, mouseY;

	public Board(boolean shouldBeEmpty) {
		pieces = new Piece[8][8];
		fireTurn = true;
		moved = false;
		selected = false;
		selX = -1;
		selY = -1;

		if (!shouldBeEmpty) {
			initDefaultBoard();
		} 
	}

	public static void main(String[] args) {
		int N = 8;
		StdDrawPlus.setXscale(0, N);
		StdDrawPlus.setYscale(0, N);
		Board b = new Board(false);
		while (true) {
			drawBoard();
			// When mouse is over a selectable piece, highlight it.
			if ((StdDrawPlus.mouseX() >= 0) && (StdDrawPlus.mouseX() < 8) && (StdDrawPlus.mouseY() >= 0) && (StdDrawPlus.mouseY() < 8)) {
				b.mouseX = (int) StdDrawPlus.mouseX();
				b.mouseY = (int) StdDrawPlus.mouseY();

				if (b.canSelect(b.mouseX, b.mouseY)) {
					StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
					StdDrawPlus.filledSquare(b.mouseX + .5, b.mouseY + .5, .5);

					if (StdDrawPlus.mousePressed()) {
						b.select(b.mouseX, b.mouseY);
					}
				}
			}
			// Highlight the selected piece. 
			if (b.selX != -1) {
				if (pieces[b.selX][b.selY] != null) {
					StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
					StdDrawPlus.filledSquare(b.selX + .5, b.selY + .5, .5);
				}
			}
			// Draw pieces at their respective positions.
			drawPieces();
			StdDrawPlus.show(10);
			if (StdDrawPlus.isSpacePressed()) {
				b.endTurn();
			}
		}
	}

	public Piece pieceAt(int x, int y) {
		if ((x >= 0) && (y >= 0) && (x < 8) && (y < 8)) {
			return pieces[x][y];
		} else {
			return null;
		}
	}

	// Determines whether the player can select a position.
	public boolean canSelect(int x, int y) {
		if (exploded) {
			return false;
		}

		if (fireTurn) {
			return canSelectFire(x, y);
		} else {
			return canSelectWater(x, y);
		}
	}

	private boolean canSelectFire(int x, int y) {
		if (selected) {
			if (pieces[x][y] != null) {
				if (pieces[x][y].isFire() && !moved) {
					return true;
				} 
			} else if ((!moved || pieces[selX][selY].hasCaptured()) && (Math.abs(x - selX) == 2)) {
				if (y == selY + 2) {
					if (canCapture(x, y)) {
						return true;
					}
				} else if ((y == selY - 2) && pieces[selX][selY].isKing()) {
					if (canCapture(x, y)) {
						return true;
					}
				}
			} else if (!moved && (Math.abs(x - selX) == 1)) {
				if (y == selY + 1) {
					return true;
				} else if (pieces[selX][selY].isKing() && (y == selY - 1)) {
					return true;
				}
			}
		} else {
			if (pieces[x][y] != null) {
				if (pieces[x][y].isFire() && !moved) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canSelectWater(int x, int y) {
		if (selected) {
			if (pieces[x][y] != null) {
				if (!pieces[x][y].isFire() && !moved) {
					return true;
				} 
			} else if ((!moved || pieces[selX][selY].hasCaptured()) && (Math.abs(x - selX) == 2)) {
				if (y == selY - 2) {
					if (canCapture(x, y)) {
						return true;
					}
				} else if ((y == selY + 2) && pieces[selX][selY].isKing()) {
					if (canCapture(x, y)) {
						return true;
					}
				}
			} else if (!moved && (Math.abs(x - selX) == 1)) {
				if (y == selY - 1) {
					return true;
				} else if (!pieces[selX][selY].isKing() && (y == selY + 1)) {
					return true;
				}
			}
		} else {
			if (pieces[x][y] != null) {
				if (!pieces[x][y].isFire() && !moved) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canCapture(int x, int y) {
		if ((x >= 0) && (y >= 0) && (x < 8) && (y < 8)) {
			int indX = (x + selX)/2;
			int indY = (y + selY)/2;
			if (pieces[indX][indY] != null) {
				if ((pieces[indX][indY].isFire() && !fireTurn) || (!pieces[indX][indY].isFire() && fireTurn)) {
					return true;
				}
			}
		}
		return false;
	}
	// Selects the position and colors the 
	// background of the selected square white. 
	public void select(int x, int y) {
		// If it's your turn and you're selecting your own piece, it'll change the current selection to that piece. 
		if (pieces[x][y] != null) {
			if (!moved) {
				selected = true;
				selX = x;
				selY = y;
			}
		} else if (selected && (!moved || pieces[selX][selY].hasCaptured())) {

			pieces[selX][selY].move(x, y);

			if ((Math.abs(selX - x) == 2) && (Math.abs(selY - y) == 2)) {
				if (pieces[x][y].isBomb()) {
					explode(x, y);
				}
			}
			selX = x;
			selY = y;
			moved = true;
		} 
		
	}

	// Handles the explosion of a bomb.
	private void explode(int x, int y) {
		int maxX = x + 1;
		int maxY = y + 1;
		int minX = x - 1;
		int minY = y - 1;
		selected = false;
		remove(x, y);

		for (int i = minX; i <= maxX ; i++) {
			for (int j = minY; j <= maxY; j++) {
				if ((i >= 0) && (j >= 0) && (i < 8) && (j < 8)) {
					if (pieces[i][j] != null) {
						if (!pieces[i][j].isShield()) {
							remove(i, j);
						}
					}
				}
			}
		}	
	}

	// Comment to fix glitch.
	// Places a piece p at (x,y) by removing it from its initial 
	// Also replaces the piece at (x,y) if an enemy piece already exists.

	public void place(Piece p, int x, int y) {
		// If the exact same piece existed, it is removed.

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] == p) {
					remove(i, j);
				}
			}
		}

		pieces[x][y] = p;
	}

	// Removes piece at (x,y).
	public Piece remove(int x, int y) {
		if ((x >= 0) && (y >= 0) && (x < 8) && (y < 8)) {
			if (pieces[x][y] != null) {
				Piece removed = pieces[x][y];
				pieces[x][y] = null;
				return removed;
			} else {
				return null;
			}
		} else {
			System.out.println("X and Y are invalid.");
			return null;
		}
	}

	// Returns whether or not the current player can end their turn. 
	// This requires the movement or capture of a piece. 
	public boolean canEndTurn() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] != null) {
					if (pieces[i][j].hasCaptured()) {
						return true;
					}
				}
			}
		}

		if (moved) {
			return true;
		}

		return false;
	}

	// Handles the switching of players and anything else that happens 
	// at the end of a turn. 
	public void endTurn() {
		if (canEndTurn()) {
			
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (pieces[i][j] != null) {
						if (pieces[i][j].hasCaptured()) {
							pieces[i][j].doneCapturing();
						}
					}
				}
			}

			if (fireTurn) {
				fireTurn = false;
			} else {
				fireTurn = true;
			}

			selected = false;
			moved = false;
			selX = -1;
			selY = -1;
		}
	}

	// Returns the winner of the game.
	public String winner() {
		int fire = 0;
		int water = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] != null) {
					if (pieces[i][j].isFire()) {
						fire = fire + 1;
					} else {
						water = water + 1;
					}
				}
			}
		}

		if (water > fire) {
			return "Water";
		} else if (water < fire) {
			return "Fire";
		} else if ((water == 0) && (fire == 0)) {
			return "No one";
		} else if (water == fire) { 
			return null;
		} else {
			return null;
		}
	}

	// Initializes an empty board.
	private static void drawBoard() {
		String filename = "img/";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
				} else {
					StdDrawPlus.setPenColor(StdDrawPlus.RED);
				}
				StdDrawPlus.filledSquare(i + .5, j + .5, .5);
				StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
			}
		}
	}

	// Initializes a board with pieces in default positions.
	private void initDefaultBoard() {
		for (int i = 0; i < 8; i += 2) {
			pieces[i][0] = new Piece(true, this, i, 0, "pawn");
			pieces[i + 1][1] = new Piece(true, this, i + 1, 1, "shield");
			pieces[i][2] = new Piece(true, this, i, 2, "bomb");

			pieces[i + 1][7] = new Piece(false, this, i + 1, 7, "pawn");
			pieces[i][6] = new Piece(false, this, i, 6, "shield");
			pieces[i + 1][5] = new Piece(false, this, i + 1, 5, "bomb");
		} 
	}

	private static void drawPieces() {
		String filename = "img/";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pieces[i][j] != null) {
					filename = "img/";
					if (pieces[i][j].isBomb()) {
						filename = filename + "bomb";
					} else if (pieces[i][j].isShield()) {
						filename = filename + "shield";
					} else {
						filename = filename + "pawn";
					}

					if (pieces[i][j].isFire()) {
						filename = filename + "-fire";
					} else {
						filename = filename + "-water";
					}

					if (pieces[i][j].isKing()) {
						filename = filename + "-crowned";
					}

					filename = filename + ".png";
					StdDrawPlus.picture(i + .5, j + .5, filename, 1, 1);
				}
			}
		}
	}
}