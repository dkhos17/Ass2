package tetris;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

import tetris.Brain.Move;

public class JBrainTetris extends JTetris{
	
	private DefaultBrain tvini;
	private JCheckBox BrainMode, ColorMode;
	private JSlider advers;
	private JLabel status;
	private Move move;
	private int dropped;
	
	
	JBrainTetris(int pixels) {
		super(pixels);
		tvini= new DefaultBrain();
		BrainMode = new JCheckBox("Brain active");
		ColorMode = new JCheckBox("Color");
		advers = new JSlider(0, 200, 0);
		status = new JLabel("ok");
	}
	
	@Override
	public JComponent createControlPanel() {
		JPanel panel = (JPanel) super.createControlPanel();
		
		JPanel row = new JPanel();
		panel.add(Box.createVerticalStrut(8));
		row.add(new JLabel("Adversary:"));
		advers.setPreferredSize(new Dimension(100,15)); 
		row.add(advers);
		row.add(status);
		panel.add(row);
		
		
		panel.add(ColorMode);
		panel.add(new JLabel("Brain:"));
		panel.add(BrainMode); 	
		
		return panel;
	}
	
	@Override
	public void tick(int verb) {
		if(!gameOn) return;
		if(ColorMode.isSelected()) colorMode = true;
		else colorMode = false;
		
		if(BrainMode.isSelected() && verb == DOWN) {
			board.undo();
			if(dropped != count) {
				move = tvini.bestMove(board, currentPiece, board.getHeight()-4, move);
				dropped++;
			}
			makeMove();
		}
		super.tick(verb);
	}
	

	private void makeMove() {
		if(move == null) {
			stopGame();
			return;
		}
		
		if(!currentPiece.equals(move.piece)) super.tick(ROTATE);
		if(currentX > move.x) super.tick(LEFT);
		else if(currentX < move.x) super.tick(RIGHT);
	}
	
	@Override
	public Piece pickNextPiece() {
		int slider = advers.getValue();
		int rand = (int)random.nextDouble();
		if(rand >= slider) {
			status.setText("ok");
			return super.pickNextPiece();
		}
		status.setText("*ok*");
		
		Piece worst[] = Piece.getPieces();
		int worst_idx = 0, worst_rot = 0;
		double worst_scr = 0;
		for(int i = 0; i < worst.length; i++) {
			for(int j = 0; j < 4; j++) {
				Move worstMove = null;
				worstMove = tvini.bestMove(board,getRot(worst[i], j), getHeight()-4, worstMove);
				if(worstMove.score > worst_scr) {
					worst_scr = worstMove.score;
					worst_idx = i; worst_rot = j;
				}
			}
		}
		return getRot(worst[worst_idx], worst_rot);
		
	}

	private Piece getRot(Piece piece, int rotation) {
		switch(rotation%4) {
			case 1: return piece.fastRotation();
			case 2: return piece.fastRotation().fastRotation();
			case 3: return piece.fastRotation().fastRotation().fastRotation();
			default:
				return piece;
		}
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
