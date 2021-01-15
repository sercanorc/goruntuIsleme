package example.soru2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class soru2 {

    public static void main(String[] args) {

        BufferedImage img =null;
        File f =null;
        try {
            f = new File("src/images/Lenna.png");
            img = ImageIO.read(f);
            System.out.println(img);
        }catch (Exception e){
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        Color[][] result = new Color[width][height];
        Color[][] result2;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                int intRGB = img.getRGB(row, col);
                int red = (intRGB >> 16)&255;
                int green = (intRGB >> 8)&255;
                int blue = intRGB&255;
                result[row][col] = new Color(red, green, blue);
            }
        }
        result2=zoomIn(result);
        BufferedImage zoomi = convertToBufferedFrom2D(result2);
        write(zoomi,"zoomi");
        result2=zoomOut(result);
        BufferedImage zoomo = convertToBufferedFrom2D(result2);
        write(zoomo,"zoomo");
    }
    public static Color[][] zoomIn(Color[][] cls){
        int width = cls.length*2;
        int height = cls[0].length*2;
        Color [][] cls2= new Color[width][height];

        for (int row = 0; row < width; row=row+2) {
            for (int col = 0; col < height; col=col+2) {
                Color value = cls[row/2][col/2];
                cls2[row][col]=value;
                cls2[row][col+1]=value;
                cls2[row+1][col]=value;
                cls2[row+1][col+1]=value;
            }
        }

        return cls2;
    }
    public static Color[][] zoomOut(Color[][] cls){
        int width = cls.length;
        int height = cls[0].length;
        Color [][] cls2= new Color[width/2][height/2];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                Color value = cls[row][col];
                cls2[row/2][col/2]=value;
            }
        }

        return cls2;
    }
    private static BufferedImage convertToBufferedFrom2D(Color[][] img) {
        int width = img.length;
        int height = img[0].length;
        BufferedImage bufImg = new BufferedImage(width, height,1);
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                bufImg.setRGB(x,  y, img[x][y].getRGB());
            }
        }

        return bufImg;
    }
    public static void write(BufferedImage image,String name){
        try {
            ImageIO.write(image, "jpg",new File("src/images/"+name+".png"));
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
