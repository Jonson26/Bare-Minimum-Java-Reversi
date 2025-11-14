import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.ImageIO;

public class Renderer{
	private static final int SIDE = 29*8+1;
	
	private BufferedImage empty, black, white, possible;
	
	public Renderer(){
		loadAssets();
	}
	
	public void loadAssets(){
		try{
			empty    = ImageIO.read(getClass().getClassLoader().getResource("img/empty.png"));
			black    = ImageIO.read(getClass().getClassLoader().getResource("img/black.png"));
			white    = ImageIO.read(getClass().getClassLoader().getResource("img/white.png"));
			possible = ImageIO.read(getClass().getClassLoader().getResource("img/possible.png"));
		}catch(Exception ex){
			System.out.println("Could not load resource");
		}
	}
	
	public BufferedImage renderBackGround(){
		BufferedImage out = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,SIDE,SIDE);
		
		g.setColor(Color.BLACK);
		g.drawRect(0,0,SIDE-1,SIDE-1);
        for(int i=0; i<8; i++){
			int h = 29*i;
			g.drawLine(0, h, SIDE,    h);
			g.drawLine(h, 0,    h, SIDE);
		}
		
		return out;
	}
	
	public BufferedImage renderPieces(BufferedImage background, int[][] board){
		BufferedImage out = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
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
				g.drawImage(toDraw, 29*i+1, 29*j+1, null);
			}
		}
		
		return out;
	}
	
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