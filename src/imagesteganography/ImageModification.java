package imagesteganography;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Tushar Kapoor
 */
class test {

    public static final String IMG = "paris.jpg";

    public static int parseInt(String binary) {
        if (binary.length() < Integer.SIZE) {
            return Integer.parseInt(binary, 2);
        }

        int result = 0;
        byte[] bytes = binary.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 49) {
                result = result | (1 << (bytes.length - 1 - i));
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage img = null;
        File f = null;
        try {
            f = new File("paris (1).jpg");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        String[][] pixels = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = img.getRGB(j, i);
                pixels[i][j] = Integer.toBinaryString(x);
                System.out.print(pixels[i][j] + "\t");
            }
            System.out.println("");
        }
        //Encryption Part
        AES a = new AES();
        String str = new String(Files.readAllBytes(Paths.get("input2.txt")));
        char[] message = str.toCharArray();
        char[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int originalLen = message.length;
        int lenOfPaddedMessage = originalLen;

        if (lenOfPaddedMessage % 16 != 0) {
            lenOfPaddedMessage = (lenOfPaddedMessage / 16 + 1) * 16;
        }

        char[] paddedMessage = new char[lenOfPaddedMessage];
        char[] encryptedMessage = new char[lenOfPaddedMessage];
        for (int i = 0; i < lenOfPaddedMessage; i++) {
            if (i >= originalLen) {
                paddedMessage[i] = 0;
            } else {
                paddedMessage[i] = message[i];
            }
        }
        System.out.println("ENCRYPTION:");
        for (int i = 0; i < lenOfPaddedMessage; i += 16) {
            char[] tmp = new char[16];
            for (int j = 0; j < 16; j++) {
                tmp[j] = paddedMessage[i + j];
            }
            a.AES_Encrypt(tmp, key);
            for (int j = 0; j < 16; j++) {
                encryptedMessage[i + j] = tmp[j];
            }
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length; i++) {
            String b = Integer.toBinaryString(encryptedMessage[i]);

            if (b.length() < 8) {
                b = "000000000".substring(0, 8 - b.length()).concat(b);
            } else {
                b = b.substring(b.length() - 8);
            }
            s.append(b);
        }
        int counter = 0;
        System.out.println(s);
        //Put Info in image
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                StringBuilder x = new StringBuilder(pixels[i][j]);
                x.deleteCharAt(x.length() -1);
                x.deleteCharAt(x.length() -1);
                if (counter < s.length()) {
                    x.append(s.charAt(counter++));
                    x.append(s.charAt(counter++));
                    int p = parseInt(x.toString());
                    img.setRGB(j, i, p);
                }
            }
        }
        try {
            f = new File("D:\\Image\\Output.jpg");
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            System.out.println(e);
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = img.getRGB(j, i);
                pixels[i][j] = Integer.toBinaryString(x);
                System.out.print(pixels[i][j] + "\t");
            }
            System.out.println("");
        }
        counter = 0;
        StringBuilder sentMessage = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(counter < s.length()){
                    sentMessage.append(pixels[i][j].charAt(pixels[i][j].length()-2));
                    sentMessage.append(pixels[i][j].charAt(pixels[i][j].length()-1));
                }
            }
        }
        System.out.println(sentMessage); 
        System.out.println(s);
    }
}