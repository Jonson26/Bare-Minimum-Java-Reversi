import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;

/*
This class contains most of the rendering code.
*/
public class Renderer{
	private BufferedImage empty, black, white, possible; //The assets
	
	public Renderer(){
		loadAssets(); //Load the assets immediately
	}
	
	public void loadAssets(){
		try{
			empty    = ImageIO.read(getClass().getClassLoader().getResource("img/empty.png"));
			black    = ImageIO.read(getClass().getClassLoader().getResource("img/black.png"));
			white    = ImageIO.read(getClass().getClassLoader().getResource("img/white.png"));
			possible = ImageIO.read(getClass().getClassLoader().getResource("img/possible.png"));
		}catch(Exception ex){
			System.out.println("Could not load resources");
		}
	}
	
	public BufferedImage renderBackGround(){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,Util.SIDE,Util.SIDE);
		
		g.setColor(Color.BLACK);
		g.drawRect(0,0,Util.SIDE-1,Util.SIDE-1);
        for(int i=0; i<8; i++){
			int h = Util.TILESIZE*i;
			g.drawLine(0, h, Util.SIDE,         h);
			g.drawLine(h, 0,         h, Util.SIDE);
		}
		
		return out;
	}
	
	//Renders the provided game board onto the provided background image
	public BufferedImage renderPieces(BufferedImage background, int[][] board){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
        for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				BufferedImage toDraw;
				switch(board[j][i]){
					case 0:
						toDraw = empty;
						break;
					case 1:
						toDraw = black;
						break;
					case 2:
						toDraw = white;
						break;
					case 3:
						toDraw = possible;
						break;
					default:
						toDraw = black;
						break;
				}
				g.drawImage(toDraw, Util.TILESIZE*i+1, Util.TILESIZE*j+1, null);
			}
		}
		
		return out;
	}
	
	//Method used to scale a game frame to the desired size. Useful because monitors are getting bigger and bigger.
	public static BufferedImage scale(BufferedImage before, double scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		// Create a new image of the proper size
		int w2 = (int) (w * scale);
		int h2 = (int) (h * scale);
		BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
		AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp 
			= new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		scaleOp.filter(before, after);
		return after;
	}
}