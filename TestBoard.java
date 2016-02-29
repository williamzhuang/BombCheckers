import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestBoard {
	public Board b;
	Piece piece1, piece2, piece3, piece4, piece5, piece6;

	@Before 
	public void setUp() {
		int N = 8;
		StdDrawPlus.setXscale(0, N);
		StdDrawPlus.setYscale(0, N);
		b = new Board(true);

		// Fire Pieces
		piece1 = new Piece(true, b, 2, 2, "pawn");
		piece2 = new Piece(true, b, 5, 4, "shield");
		piece3 = new Piece(true, b, 3, 2, "bomb");

		// Water Pieces
		piece4 = new Piece(false, b, 3, 3, "pawn");
		piece5 = new Piece(false, b, 1, 3, "shield");
		piece6 = new Piece(false, b, 2, 4, "bomb");
	}

	@Test 
	public void testCanSelectBeforeMove() {
		// Piece has not been selected
		b.place(piece1, 2, 2);

		assertEquals(true, b.canSelect(2, 2));

		// Selecting another piece when Piece selected but not moved.
		b.place(piece2, 5, 2);

		assertEquals(true, b.canSelect(5, 2));
		assertEquals(true, b.canSelect(2, 2));
	}

	@Test
	public void testCanSelectForMove() {
		b.place(piece1, 2, 2);
		b.select(2, 2);

		assertEquals(true, b.canSelect(3, 3));
		assertEquals(true, b.canSelect(1, 3));
	}


	@Test
	public void testKingCapture() {
		// Piece hasn't captured and moves to capture
		b.place(piece1, 3, 3);
		b.select(3, 3);
		// Piece has captured and moves to capture

		// Piece is king and captures backwards

		// Piece is king and moves backwards
	}

	@Test
	public void testPostCapture() {
		b.place(piece1, 2, 2);
		b.place(piece4, 3, 3);
		if (b.canSelect(2, 2)) {
			b.select(2, 2);	
		}
		if (b.canSelect(4, 4)) {
			b.select(4, 4);	
		} if (b.canSelect(6, 6)) {
			b.select(6, 6);	
		}

		assertEquals(null, b.pieceAt(6, 6));
	}

	@Test
	public void testPlace() {
		b.place(piece1, 1, 3);
		b.place(piece4, 4, 6);

		assertEquals(piece1, b.pieceAt(1, 3));
		assertEquals(piece4, b.pieceAt(4, 6));
	}

	@Test
	public void testRemove() {
		b.place(piece1, 1, 3);
		b.place(piece4, 4, 6);

		b.remove(1, 3); 
		b.remove(4, 6);

		assertEquals(null, b.pieceAt(1, 3));
		assertEquals(null, b.pieceAt(4, 6));
	}

	@Test 
	public void testWinner() {
		b.place(piece1, 2, 2);
		b.place(piece4, 3, 3);
		b.select(3, 3);
		b.select(1, 1);

		assertEquals("Water", b.winner()); 
	}

	public static void main(String[] args) {
		jh61b.junit.textui.runClasses(TestBoard.class);
	}

}