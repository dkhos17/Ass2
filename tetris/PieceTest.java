package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece L1, L2, S1, S2, SQ, ST;
	private Piece s, sRotated;
	private Piece[] pc;

	@Before
	public void setUp() throws Exception {
		pc = Piece.getPieces();
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		L1 = new Piece(Piece.L1_STR);
		L2 = new Piece(Piece.L2_STR);
		S1 = new Piece(Piece.S1_STR);
		S2 = new Piece(Piece.S2_STR);
		SQ = new Piece(Piece.SQUARE_STR);
		ST = new Piece(Piece.STICK_STR);
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
		//Width
		assertEquals(3, S1.getWidth());
		assertEquals(3, S2.getWidth());
		assertEquals(2, L1.getWidth());
		assertEquals(2, L2.getWidth());
		assertEquals(1, ST.getWidth());
		assertEquals(2, SQ.getWidth());
		//Height
		assertEquals(2, S1.getHeight());
		assertEquals(2, S2.getHeight());
		assertEquals(3, L1.getHeight());
		assertEquals(3, L2.getHeight());
		assertEquals(4, ST.getHeight());
		assertEquals(2, SQ.getHeight());
		
	}
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
		assertFalse(Arrays.equals(new int[] {0, 1}, S1.getSkirt()));
		assertFalse(Arrays.equals(new int[] {1, 1}, S2.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0}, SQ.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, ST.getSkirt()));
		assertFalse(Arrays.equals(new int[] {0, 1}, L1.getSkirt()));
		assertFalse(Arrays.equals(new int[] {1, 0}, L2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, L1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, L2.getSkirt()));
	}
	
	@Test
	public void testEquals() {
		assertTrue(S1.equals(S1));
		assertFalse(S1.equals(S2));
		assertTrue(pc[0].equals(ST));
		assertTrue(pc[1].equals(L1));
		assertTrue(pc[2].equals(L2));
		assertTrue(pc[3].equals(S1));
		assertTrue(pc[4].equals(S2));
		assertTrue(pc[5].equals(SQ));
		assertTrue(pc[6].equals(pyr1));
		assertFalse(pc[4].equals(ST));
		assertFalse(pc[2].equals(L1));
		assertFalse(pc[3].equals(S2));
		assertTrue(pc[6].fastRotation().equals(pyr2));
		assertTrue(SQ.equals(SQ.computeNextRotation()));
	}
	
	@Test
	public void testPiece() {
		assertEquals(1, ST.getWidth());
		assertEquals(2, SQ.getWidth());
		assertEquals(3, S1.getWidth());
		assertEquals(2, L1.getWidth());
		assertEquals(3, L2.computeNextRotation().getWidth());
		
		assertEquals(S1.getWidth(), S1.computeNextRotation().getHeight());
		assertEquals(L1.getWidth(), L1.computeNextRotation().getHeight());
		assertEquals(L1.getWidth(), L1.computeNextRotation().computeNextRotation().getWidth());
		assertEquals(S2.getHeight(), S1.computeNextRotation().computeNextRotation().getHeight());
		assertFalse(S1.equals(S1.computeNextRotation()));
		
		assertFalse(pc[1].equals(pc[1].fastRotation().fastRotation().fastRotation()));
		assertTrue(pc[2].equals(pc[2].fastRotation().fastRotation().fastRotation().fastRotation()));
		assertTrue(pc[5].equals(pc[5].fastRotation()));
		assertTrue(pc[5].equals(pc[5].fastRotation().fastRotation()));
		assertTrue(pc[5].equals(pc[5].fastRotation().fastRotation().fastRotation()));
		assertTrue(pc[6].equals(pc[6].fastRotation().fastRotation().fastRotation().fastRotation()));
		assertTrue(pyr2.equals(pc[6].fastRotation()));
		assertTrue(pyr3.equals(pc[6].fastRotation().fastRotation()));
		assertTrue(pyr4.equals(pc[6].fastRotation().fastRotation().fastRotation()));
		assertTrue(pyr1.equals(pc[6].fastRotation().fastRotation().fastRotation().fastRotation()));
		assertTrue(pc[5].equals(pc[5].fastRotation().fastRotation().fastRotation().fastRotation()));
		assertFalse(pc[0].equals(pc[0].fastRotation().fastRotation().fastRotation()));
		assertTrue(pc[0].equals(pc[0].fastRotation().fastRotation()));
	}
	
		
}
