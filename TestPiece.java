import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestPiece {
	public Board b;
	Piece piece1;
	Piece piece2;
	Piece piece3;

	@Before 
	public void setUp() {
		int N = 8;
		StdDrawPlus.setXscale(0, N);
		StdDrawPlus.setYscale(0, N);
		b = new Board(false);
		piece1 = new Piece(true, b, 7, 7, "king");
		piece2 = new Piece(false, b, 7, 4, "bomb");
		piece3 = new Piece(true, b, 3, 2, "shield");
	}

	@Test
	public void testIsFire() {

		assertEquals(true, piece1.isFire());
		assertEquals(false, piece2.isFire());
	}

	@Test
	public void testIsBomb() {

		assertEquals(false, piece1.isBomb());
		assertEquals(true, piece2.isBomb());
	}

	@Test 
	public void testIsKing() {

		assertEquals(true, piece1.isKing());
		assertEquals(false, piece2.isKing()); 
	}

	@Test
	public void testIsShield() {

		assertEquals(false, piece1.isShield());
		assertEquals(false, piece2.isShield());
		assertEquals(true, piece3.isShield());
	}

	@Test
	public void testHasCaptured() {

	}


	// TODO: Fill this test out.
	@Test
	public void testDoneCapturing() {

	}

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPiece.class);
    }

}