package Diablo;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class SpriteMaker {
	
	public static void main(String[] args) throws IOException
	{
		int picX = 200;
		int picY = 200;
		int numDirection = 8;
		int numPicPer = 19;
		
		int width = picX * numPicPer;
		int height = picY * numDirection;
		//int[][] rgbMatrix = new int[picX * numPicPer][picY * numDirection];
		int[] rgbMatrix = new int[width * height];
		
		String root = Paths.get(System.getProperty("user.dir")).getParent()+"/Portfolio/resources/images/sprite/Direction";
		//String root = "C:\\Users\\Fan\\eclipse-workspace/Portfolio/resources/images/sprite/Direction";
		
		String newRoot;
		String imageDir;
		
		for(int i = 1; i <= 8 ; i++)
		{
			newRoot = root + i +"/";
			
			for(int a = 0; a < numPicPer ; a++)
			{
				if(a < 10)
				{
					imageDir = newRoot + "000" +a +".png";
				}
				else
				{
					imageDir = newRoot + "00" +a +".png";
				}
				//C:\Users\Fan\eclipse-workspace/Portfolio/resources/images/
				//System.out.println(imageDir);
					BufferedImage image = ImageIO.read(new File(imageDir));		
					
					for(int y = 0; y < picY; y++)
					{
						for(int x = 0; x < picX; x++)
						{
							int color = image.getRGB(x, y);
							
							if((color != 0)&&(color != 65793))
							{
								//System.out.println(color);
								rgbMatrix[ a * picX + x + ((i - 1)*picY + y) * width] = color;
								//rgbMatrix[ a * picX + x + (i - 1)* width + y* width] = color;
							}
							//System.out.println(color);
							//rgbMatrix[ a * picX + x + ((i - 1) + y) * picY] = image.getRGB(x, y);
							//System.out.println(image.getRGB(x, y));
						}
					}
					
			
			}

		}
		
		BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		int[] temp = ((DataBufferInt)finalImage.getRaster().getDataBuffer()).getData();
		
		//temp = rgbMatrix;
		
		for(int i = 0; i < width * height; i++)
		{
			//System.out.println(rgbMatrix);
			
			temp[i] = rgbMatrix[i];
		}
		
		ImageIO.write(finalImage, "png", new File("C:\\Users\\Fan\\eclipse-workspace/Portfolio/resources/images/sprite.png"));
		
		
		
	}

}
