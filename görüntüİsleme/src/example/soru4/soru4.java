package example.soru4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class soru4 {
    public static void main(String[] args) {
        BufferedImage img =null;
        File f =null;
        try {
            f = new File("src/images/Lenna.png");
            img = ImageIO.read(f);
        }catch (Exception e){
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        Color[][] result = new Color[width][height];
        int filitre[][]={{1,0,-1}, {1,0,-1}, {1,0,-1}};

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
        filitre(filitre);
        konvolüsyon(grey,filitre);
        korelasyon(grey,filitre);

    }
    public static void filitre(int[][]filter){
        BufferedImage bufImg = new BufferedImage(filter.length, filter[0].length,1);

        for (int x = 0; x < filter.length; x++) {
            for(int y = 0; y < filter[0].length; y++) {
                bufImg.setRGB(x,  y, filter[x][y]);
            }
        }

        write(bufImg,"filter");
    }
    public static void konvolüsyon (Color[][] img,int[][] filter){
        Color [][] temp = new Color[img.length+2][img[0].length+2];
        int [][] cor = new int[img.length][img[0].length];
        int [][]rtrnFilter=new int[filter.length][filter[0].length];
        for (int i = 0; i < rtrnFilter.length; i++) {      //korelasyon işleminden farklı olarak burada maskeyi ters
            for (int j = 0; j < rtrnFilter[0].length; j++) {//çevirek işleme sokuyoruz
                rtrnFilter[i][j] = filter[filter.length-i-1][filter[0].length-1];
            }
        }
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = new Color(0x000000);
            }
        }
        for (int i = 1; i < temp.length-1; i++) {
            for (int j = 1; j < temp[0].length-1; j++) {
                temp[i][j] = img[i-1][j-1];
            }
        }
        for (int i = 1; i < temp.length-2; i++) {
            for (int j = 1; j < temp[0].length-2; j++) {
                int[][] multipl = new int[][]{
                        {temp[i - 1][j - 1].getRGB(), temp[i - 1][j].getRGB(), temp[i - 1][j + 1].getRGB()},
                        {temp[i ][j - 1].getRGB(), temp[i][j].getRGB(), temp[i][j + 1].getRGB()},
                        {temp[i + 1][j - 1].getRGB(), temp[i +1][j].getRGB(), temp[i + 1][j + 1].getRGB()}};

                cor[i-1][j-1]=multiplication(multipl,rtrnFilter);
            }
        }
        BufferedImage bufImg = new BufferedImage(cor.length, cor[0].length,1);

        for (int x = 0; x < cor.length; x++) {
            for(int y = 0; y < cor[0].length; y++) {
                bufImg.setRGB(x,  y, cor[x][y]);
            }
        }

        write(bufImg,"konvolüsyon");
    }
    public static void korelasyon (Color[][] img,int[][] filter){
        Color [][] temp = new Color[img.length+2][img[0].length+2]; //etrafı sıfırdolucak o yüzden yükseklik ve genişlik 2şer hücre artacak
        int [][] cor = new int[img.length][img[0].length];// sonuc matrisi
        for (int i = 0; i < temp.length; i++) { //sıfır maatrisi oluşturuldu
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = new Color(0x000000);
            }
        }
        for (int i = 1; i < temp.length-1; i++) {      // input olarak alınan matris ile sıfır matrisin içi dolduruldu
                                                        // filitreleme işlemi için ilk satır ve sütün 0 olarak kaldı
            for (int j = 1; j < temp[0].length-1; j++) { //işlem görecek matrisi 1e1 hücresi input matrsin 0a0 hücresi olarak input matristekiki tüm color değerleri ile dolduruldu
                temp[i][j] = img[i-1][j-1];
            }
        }
        for (int i = 1; i < temp.length-2; i++) {       //korelasyon işlemi görüntü ile filitredeki hücreli çarpıp toplamlarının sonucunu ortadaki merkez hücreye  yerleştirme işlemi oluyor
            for (int j = 1; j < temp[0].length-2; j++) { // burda Matrisimden maske boyutunda matris alarak her hücredeki değerin piksel değerleri ile matris oluşturuldu
                int[][] multipl = new int[][]{          //ve filter ile multiplication fonksiyonuna gönderildi geri dönen piksel değerleri yerini doldurarak ilerledi
                        {temp[i - 1][j - 1].getRGB(), temp[i - 1][j].getRGB(), temp[i - 1][j + 1].getRGB()},
                        {temp[i ][j - 1].getRGB(), temp[i][j].getRGB(), temp[i][j + 1].getRGB()},
                        {temp[i + 1][j - 1].getRGB(), temp[i +1][j].getRGB(), temp[i + 1][j + 1].getRGB()}};

                cor[i-1][j-1]=multiplication(multipl,filter);
            }
        }
        // int tipindeki piksel değerlerini taşıyan matrisi BufferdImage yaparak kaydetme işlemi yapıldı

        BufferedImage bufImg = new BufferedImage(cor.length, cor[0].length,1);

        for (int x = 0; x < cor.length; x++) {
            for(int y = 0; y < cor[0].length; y++) {
                bufImg.setRGB(x,  y, cor[x][y]);
            }
        }

        write(bufImg,"korelasyon");

    }
    public static int multiplication(int[][] multipl,int[][] filter){
        int result=0;
        for(int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                result+=multipl[i][j]*filter[i][j];
            }
        }

        return result;
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
    public static void write(BufferedImage image,String name){
        try {
            ImageIO.write(image, "jpg",new File("src/images/"+name+".png"));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
