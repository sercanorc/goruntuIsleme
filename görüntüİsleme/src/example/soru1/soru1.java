package example.soru1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class soru1 {
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
        Color[][] convertedhsv=new Color[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                int intRGB = img.getRGB(row, col);
                int red = (intRGB >> 16)&255;
                int green = (intRGB >> 8)&255;
                int blue = intRGB&255;
                double[] tm = rgb_to_hsv(red,green,blue);
                double hue= tm[0];
                double saturation= tm[1];
                double value = tm[2];
                int[] cv =hsv_to_rgb(hue,saturation,value);
                convertedhsv[row][col] =new Color(cv[0],cv[1],cv[2]);
                result[row][col] = new Color(red, green, blue);
            }
        }
        BufferedImage imgf = convertToBufferedFrom2D(convertedhsv);
        write(imgf);

    }
    public static double[] rgb_to_hsv(double r, double g, double b) {
        r = r / 255.0;
        g = g / 255.0;
        b = b / 255.0;

        double cmax = Math.max(r, Math.max(g, b));
        double cmin = Math.min(r, Math.min(g, b));
        double diff = cmax - cmin;
        double h=0,s=0;

        if (cmax == r) {
            h = (60 * ((g - b) / diff) + 360) % 360;
        }
        else if (cmax == g) {
            h = (60 * ((b - r) / diff) + 120) % 360;
        }
        else if (cmax == b){
            h = (60 * ((r - g) / diff) + 240) % 360;
        }
        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax);

        double v = cmax;
        double [] result = new double[3];
        result[0]=h;
        result[1]=s;
        result[2]=v;
        return result;
    }
    public static int[] hsv_to_rgb(double h, double s, double v){
        if (h < 0 || h > 360 || s < 0 || s > 100 || v < 0 || v > 100)
        {
            System.out.println("hatali");
        }
        double r=0,g=0,b=0;
        double c= v*s;
        double x=c * (1-(Math.abs(((h/60)%2)-1)));
        double m = v-c;
        if(h<60){
            r=c; g=x; b=0;
        }else if (h<120){
            r=x;g=c;b=0;
        }else if (h<180){
            r=0;g=c;b=x;
        }else if (h<240){
            r=0;g=x;b=c;
        }else if (h<300){
            r=x;g=0;b=c;
        }else if (h<360){
            r=c;g=0;b=x;
        }
        r=(r+m)*255;
        g=(g+m)*255;
        b=(b+m)*255;
        System.out.println("(r:" + r + " g:" + g + " b:" + b + ")");
        int [] result = new int[3];
        result[0]=(int)r;
        result[1]=(int)g;
        result[2]=(int)b;
        return result;
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
    public static void write(BufferedImage image){
        try {
            ImageIO.write(image, "jpg",new File("src/images/converted.png"));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
