package tetris;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import tetris.Brain.Move;

public class JBrainTetris extends JTetris{
	
	private DefaultBrain tvini;
	private JCheckBox brainMode, fastFall;
	private int LeftMove, RightMove, DownMove, Rotation;
	
	
	JBrainTetris(int pixels) {
		super(pixels);
		tvini= new DefaultBrain();
		brainMode = new JCheckBox();
		LeftMove = RightMove = DownMove = Rotation = 0; 
	}
	
	@Override
	public JComponent createControlPanel() {
		JPanel panel = (JPanel) super.createControlPanel();
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active");
		panel.add(brainMode); 
		return panel;
	}
	
	@Override
	public void tick(int verb) {
		super.tick(verb);
		if(brainMode.isSelected()) {	
			boolean canMove = makeMove();
			if(canMove) return;
			Move move = null;
			move = tvini.bestMove(board, currentPiece, board.getHeight()-4, move);
			Piece pc = currentPiece;
			while(!pc.equals(move.piece)) {
				pc = pc.fastRotation();
				Rotation++;
			}
			LeftMove = Math.max(0, currentX - move.x);
			RightMove = Math.max(0, move.x - currentX);
			DownMove = currentY;	
		}
	}
	

	private boolean makeMove() {
		if(Rotation > 0) {
			super.tick(ROTATE);
			Rotation--;
		}else if(LeftMove > 0) {
			super.tick(LEFT);
			LeftMove--;
		}else if(RightMove > 0) {
			super.tick(RIGHT);
			RightMove--;
		}else if(DownMove > 0) {
			super.tick(DOWN);
			DownMove--;
		}else return false;
		return true;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}

}
