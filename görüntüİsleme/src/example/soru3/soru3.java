package example.soru3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class soru3 {
    public static void main(String[] args) {
        BufferedImage img =null;
        File f =null;
        int[] histogram = new int[255];
        try {
            f = new File("src/images/Lenna.png");
            img = ImageIO.read(f);
            System.out.println(img);
        }catch (Exception e){
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        int size =width*height;
        Color[][] result = new Color[width][height];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                int intRGB = img.getRGB(row, col);
                int red = (intRGB >> 16)&255;
                int green = (intRGB >> 8)&255;
                int blue = intRGB&255;
                result[row][col] = new Color(red, green, blue);
            }
        }
        Color[][] grey = greyscale(result);
        BufferedImage imgf = convertToBufferedFrom2D(grey);
        write(imgf,"greyscale");
        //histogram denkleme bölme
        int numBands = imgf.getRaster().getNumBands();
        int[] iarray = new int[numBands];
        //piksel yoğunluklarını histograma okuma
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = imgf.getRaster().getPixel(x, y, iarray)[0];
                histogram[valueBefore]++;
            }
        }
        // Tüm değerleri içeren tablo
        int sum = 0;
        float[] lut = new float[size];
        for (int i = 0; i < 255; ++i) {
            sum += histogram[i];
            lut[i] = sum * 255 / size;
        }
        // histogramı kullanarak görüntüyü dönüştürün
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = imgf.getRaster().getPixel(x, y, iarray)[0];
                int valueAfter = (int) lut[valueBefore];
                iarray[0] = valueAfter;
                imgf.getRaster().setPixel(x, y, iarray);
            }
        }
        write(imgf,"histogram");

    }
    public static Color[][] greyscale(Color [][] img){
        Color [][] temp = new Color[img.length][img[0].length];
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[i].length; j++) {
                temp[i][j] = img[i][j];
            }
        }
        for(int i=0; i<temp.length; i++) {
            for ( int j=0; j<temp[i].length;j++) {

                if(i<temp.length) {
                    Color pixel = temp[i][j];
                    int r = pixel.getRed();
                    int g = pixel.getGreen();
                    int b = pixel.getBlue();
                    int avg= ((r+g+b)/3);
                    temp[i][j]= new Color(avg,avg,avg);
                }
            }
        }

        return temp;
    }
    private static BufferedImage convertToBufferedFrom2D(Color[][] img) {
        // Create new BufferedImage of specified width and height
        int width = img.length;
        int height = img[0].length;
        BufferedImage bufImg = new BufferedImage(width, height,5);

        // Set the RGB value of each pixel in the BufferedImage
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                bufImg.setRGB(x,  y, img[x][y].getRGB());
            }
        }

        // Return the BufferedImage
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
