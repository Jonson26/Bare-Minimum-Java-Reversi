import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.BoxLayout;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Thread;
/*
This class contains most of the game logic.
It is also used to "glue" all the other classes together
*/
public class Board extends JPanel implements MouseListener {
	private static final String[] aiModes = {"Human", "Dumb", "Smart", "Smart+"}; //List of the possible AI modes
	
	private String[] aiMode; //Stores the current AI mode of both players ([0]->Black, [1]->White)
	/*
	8 by 8 array of integers storing the current state of the game board
	Allowed values are:
		0 - Empty
		1 - Black
		2 - White
	*/
	private int[][] board; 
	private int player; //The player whose turn it currently is
	private boolean win; //Determines whether the current game session is finished
	
	private Renderer renderer;
	private BufferedImage background; //Empty game board saved to save time on rendering
	private ScoreDisplay scoreDisplay;
	private JComboBox bSelector, wSelector;
	
	public Board(){
		setLayout(new BorderLayout());
		
		addMouseListener(this);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		
		scoreDisplay = new ScoreDisplay();
		controlPanel.add(scoreDisplay, BorderLayout.NORTH);
		
		controlPanel.add(initControlPanel(), BorderLayout.SOUTH);
		
		add(controlPanel, BorderLayout.EAST);
		
		renderer = new Renderer();
		background = renderer.renderBackGround(); //Immediately prerender the background
		
		String[] aiMode = {"Human", "Dumb"};
		this.aiMode = aiMode;
		
		initGameState();
	}
	
	//Method to initialize the game state
	public void initGameState(){
		int[][] board = {
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,2,1,0,0,0},
			{0,0,0,1,2,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0}
			};
		this.board = board;
		player = 1;
		win = false;
	}
	
	//Method that initializes the control panel of the game
	public JPanel initControlPanel(){
		JPanel selectorPanel = new JPanel();
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
		
		Font contentFont = new Font("Courier New", Font.PLAIN, 18); //Font for use by the normal components
		
		bSelector = new JComboBox(aiModes);
		wSelector = new JComboBox(aiModes);
		
		bSelector.setSelectedIndex(0);
		wSelector.setSelectedIndex(1);
		
		bSelector.addActionListener(new AIModeListener());
		wSelector.addActionListener(new AIModeListener());
		
		bSelector.setFont(contentFont);
		wSelector.setFont(contentFont);
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new BoxLayout(bPanel, BoxLayout.X_AXIS));
		JLabel l1 = new JLabel("Black AI: ");
		l1.setFont(contentFont);
		bPanel.add(l1);
		bPanel.add(bSelector);
		
		JPanel wPanel = new JPanel();
		wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.X_AXIS));
		JLabel l2 = new JLabel("White AI: ");
		l2.setFont(contentFont);
		wPanel.add(l2);
		wPanel.add(wSelector);
		
		JPanel ngPanel = new JPanel();
		ngPanel.setLayout(new BoxLayout(ngPanel, BoxLayout.X_AXIS));
		
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new NewGameListener());
		newGame.setFont(contentFont);
		ngPanel.add(newGame);
		
		selectorPanel.add(new JSeparator());
		selectorPanel.add(bPanel);
		selectorPanel.add(wPanel);
		selectorPanel.add(ngPanel);
		selectorPanel.add(new JSeparator());
		
		return selectorPanel;
	}
	
	
	//Method where the rendering happens
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		
		int[][] pBoard = Util.prepareForRender(board, player); //Prepare a version of the board with the available moves marked with a "3"
		BufferedImage frame = renderer.renderPieces(background, pBoard); //Render the pieces onto the prerendered background
		BufferedImage scaledFrame = renderer.scale(frame, Util.SCALE); //Scale the resulting frame
		g.drawImage(scaledFrame, 0, 0, this); //Copy the frame onto the screen
    }
	
	private void performMove(int x, int y){
		if(!win && Util.movePossible(board, x, y, player)){
			Util.flipTiles(board, x, y, player, true);
			player = (player==1)? 2 : 1;
			updateGameState();
			scheduleAITurn();
		}
	}
	
	private void scheduleAITurn(){
		new AIThread().start();
	}
	
	//This method updates the game's score display and checks whether the game has ended
	private void updateGameState(){
		int bs = Util.countScore(board, 1);
		int ws = Util.countScore(board, 2);
		scoreDisplay.setScore(bs, ws);
		scoreDisplay.setMessage(((player==1)? "Black" : "White")+"'s turn");
		if(!Util.movesPossible(board, player)){
			win = true;
			if(bs>ws){
				scoreDisplay.setMessage("Black wins!");
			}else if(bs<ws){
				scoreDisplay.setMessage("White wins!");
			}else{
				scoreDisplay.setMessage("Stalemate!");
			}
		}
	}
	
	private boolean isHumanTurn(){
		return aiMode[player-1]=="Human";
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		if(isHumanTurn()) performMove(Util.adjustCoordinate(e.getX(), Util.SCALE), Util.adjustCoordinate(e.getY(), Util.SCALE));
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		
	}
	
	//Internal class responsible for starting a new game session
	private class NewGameListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			initGameState();
			updateGameState();
			repaint();
			scheduleAITurn();
		}
	}
	
	//Internal class responsible for changing the AI modes of the players to the ones selected by the user
	private class AIModeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			String[] tempMode = {
				aiModes[bSelector.getSelectedIndex()], 
				aiModes[wSelector.getSelectedIndex()]
				};
			aiMode = tempMode;
			scheduleAITurn();
		}
	}
	
	//Simple thread meant to perform a single AI turn
	private class AIThread extends Thread{
		@Override
		public void run(){
			if(!isHumanTurn()){
				try{
					sleep(25);
				}catch(Exception ex){
					System.out.println("Insomnia!");
				}
				int[] move = AI.decide(board, player, aiMode[player-1]);
				int x = move[0];
				int y = move[1];
				if(!isHumanTurn()) performMove(x, y);
				repaint();
			}
		}
	}
}