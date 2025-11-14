import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Font;

public class ScoreDisplay extends JPanel{
	private int bScore, wScore;
	private String message;
	private JLabel bsLabel, wsLabel, msgLabel;
	
	public ScoreDisplay(){
		bScore = 2;
		wScore = 2;
		message = "Black's turn";
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel titleLabel = new JLabel("REVERSI");
		titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
		
		JLabel copyrightLabel = new JLabel("©2025 Filip Jamroga");
		copyrightLabel.setFont(new Font("Courier New", Font.ITALIC, 12));
		
		bsLabel = new JLabel("");
		wsLabel = new JLabel("");
		msgLabel = new JLabel("");
		
		Font contentFont = new Font("Courier New", Font.PLAIN, 18);
		bsLabel.setFont(contentFont);
		wsLabel.setFont(contentFont);
		msgLabel.setFont(contentFont);
		
		add(titleLabel);
		add(copyrightLabel);
		add(bsLabel);
		add(wsLabel);
		add(msgLabel);
		
		update();
	}
	
	private void update(){
		bsLabel.setText("Black: "+bScore);
		wsLabel.setText("White: "+wScore);
		msgLabel.setText(message);
		repaint();
		revalidate();
	}
	
	public void setScore(int bScore, int wScore){
		this.bScore = bScore;
		this.wScore = wScore;
		update();
	}
	
	public void setMessage(String message){
		this.message = message;
		update();
	}
	
}