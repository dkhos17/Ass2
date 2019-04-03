package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(1, b.dropHeight(sRotated, 1));
		assertEquals(2, b.dropHeight(new Piece(Piece.L1_STR), 0));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(4, b.dropHeight(pyr1, 0));
		assertEquals(3, b.dropHeight(new Piece(Piece.S2_STR).computeNextRotation(), 0));
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	@Test
	public void testSample3() {
		b.undo();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getMaxHeight());
	}
	
	@Test
	public void testSample4() {
		b.commit();
		int result = b.place(s, 1, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	@Test
	public void testSample5() {
		b.commit();
		int result = b.place(new Piece(Piece.STICK_STR).computeNextRotation(), 1, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	@Test
	public void testSample6() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.clearRows());
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(3, b.getMaxHeight());
	}
	
	@Test
	public void testSample7() {
		b.undo();
		b.place(pyr1, 0, 0);
		b.commit();
		b.place(sRotated, 1, 1);
		b.undo();
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	@Test
	public void testSample8() {
		b.commit();
		int res1 = b.place(new Piece(Piece.S2_STR).computeNextRotation(), 0, 1);
		assertEquals(Board.PLACE_OK, res1);
		b.commit();
		int res2 = b.place(new Piece(Piece.STICK_STR), 2, 1);
		assertEquals(Board.PLACE_ROW_FILLED, res2);
		b.commit();
		int res3 = b.place(new Piece(Piece.STICK_STR), 0, 3);
		assertEquals(Board.PLACE_OUT_BOUNDS, res3);
		b.undo();
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(5, b.getColumnHeight(2));
		assertEquals(5, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(1, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
		assertEquals(3, b.clearRows());
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
	}
	
	@Test
	public void testSample9() {
		b.commit();
		int res0 = b.place(pyr1, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, res0);
		assertEquals(2, b.clearRows());
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
	}
	
	@Test
	public void testSample10() {
		b.commit();
		int res0 = b.place(pyr1, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, res0);
		b.commit();
		int res1 = b.place(new Piece(Piece.S2_STR).computeNextRotation(), 0, 3);
		assertEquals(Board.PLACE_OK, res1);
		assertEquals(2, b.clearRows());
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
	}
}