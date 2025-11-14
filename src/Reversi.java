import javax.swing.JFrame;

public class Reversi{
	public static void main(String[] args){
		JFrame f = new JFrame("Reversi");
		f.setSize(700, 505);
		f.setResizable(false);
		
		f.add(new Board());
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}