// Board.java
package tetris;

import java.util.ArrayList;
import java.util.List;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] board, undo_board;
	private int[] fill_row, fill_col;
	private int[] undo_row, undo_col;
	private boolean DEBUG = true;
	boolean committed;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		fill_row = new int[height];
		undo_row = new int[height];
		fill_col = new int[width];
		undo_col = new int[width];
		board = new boolean[width][height];
		undo_board = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int max = 0;
		for(int h : fill_col) max = Math.max(max, h);
		return max; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int new_max_height = 0;
			for(int i = 0; i < height; i++) {
				int max_x = 0;
				for(int j = 0; j < width; j++) {
					if(board[j][i]) max_x++;
				}
				if(getRowWidth(i) != max_x) throw new RuntimeException("Row Count Problem");
				if(max_x > 0) new_max_height = i+1;
			}
			if(new_max_height != getMaxHeight())throw new RuntimeException("Max Height problem");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int Y = 0;
		for(int i = 0; i < skirt.length; i++) {
			Y = Math.max(Y, fill_col[x+i] - skirt[i]);
		}
		return Y; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return fill_col[x]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return fill_row[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return true; 
		return board[x][y]; // YOUR CODE HERE
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		untilComm();
		TPoint[] body = piece.getBody();
		for(TPoint p : body) {
			int x0 = x+p.x;
			int y0 = y+p.y;
			if(x0 < 0 || x0 >= width) return PLACE_OUT_BOUNDS;
			if(y0 < 0 || y0 >= height) return PLACE_OUT_BOUNDS;
			
			if(board[x0][y0]) return PLACE_BAD;
			else board[x0][y0] = true;
			
			fill_row[y0]++;
			fill_col[x0] = Math.max(fill_col[x0], y0+1);
		}
		for(int i = y; i < y+piece.getHeight(); i++) {
			if(fill_row[i] == width) return PLACE_ROW_FILLED;
		}
		return PLACE_OK;
	}
	
	
	private void untilComm() {
		for(int i = 0; i < height; i++) undo_row[i] = fill_row[i];
		for(int i = 0; i < width; i++) undo_col[i] = fill_col[i];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				undo_board[i][j] = board[i][j];
			}
		}
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed) {
			committed = false;
			untilComm();
		}
		int clearRows = 0;
		int max_h = getMaxHeight();
		for(int y = 0; y < height; y++) {
			if(fill_row[y] == width) {
				for(int i = y; i < height; i++) {
					for(int j = 0; j < width; j++) {
						if(i+1 < height) board[j][i] = board[j][i+1];
						else board[j][i] = false;
					}
					if(i+1 < height) fill_row[i] = fill_row[i+1];
					else fill_row[i] = 0;
				} 
				clearRows++; y--;
			}
			
		}
		for(int i = 0; i < width; i++) {
			fill_col[i] = 0;
			for(int j = 0; j < height; j++) {
				if(board[i][j]) fill_col[i] = j+1;
			}
		}
//		sanityCheck();
		return clearRows;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
		commit();
		for(int i = 0; i < height; i++) fill_row[i] = undo_row[i];
		for(int i = 0; i < width; i++) fill_col[i] = undo_col[i];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				board[i][j] = undo_board[i][j];
			}
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


