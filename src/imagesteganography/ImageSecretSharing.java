package imagesteganography;

import Jama.Matrix;
import java.math.MathContext;
import java.util.Random;

/*
 * @author Tushar Kapoor
 */
public class ImageSecretSharing {

    public static void main(String[] args) {
        double [][] s = new double[][]{
            {2, 3, 1, 2},
            {5, 4, 6, 1},
            {8, 9, 7, 2},
            {3, 4, 1, 2}};
        Matrix S = new Matrix(s);
        int k = 2;
        int m = 4;
        if (!(m > 2 * (k - 1) - 1)) {
            System.exit(-1);
        }
        Matrix mat;
        do {
            mat = Matrix.random(4, 2);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    mat.set(i, j, mat.get(i, j) * 100);
                    mat.set(i, j, (int) mat.get(i, j) / 10);
                }
            }
        } while (mat.rank() != k);
        Matrix[] xi = new Matrix[m];
        for (int i = 0; i < m; i++) {
            xi[i] = Matrix.random(2, 1);
            for (int j = 0; j < 2; j++) {
                for (int l = 0; l < 1; l++) {
                    xi[i].set(j, l, xi[i].get(j, l) * 100);
                    xi[i].set(j, l, (int)xi[i].get(j, l) / 10);
                }
            }
        }
        Matrix[] vi = new Matrix[m];
        for (int i = 0; i < m; i++) {
            vi[i] = mat.times(xi[i]);
        }
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < 4; j++) {
//                for (int l = 0; l < 1; l++) {
//                    System.out.print(vi[i].get(j, l) + "\t");
//                }
//                System.out.println("");
//            }
//             System.out.println("");
//        }
        
        Matrix temp = mat.transpose().times(mat);
        temp = temp.inverse();
        Matrix Ss = mat.times(temp).times(mat.transpose());
        Matrix R = S.minus(Ss);
        Matrix B = new Matrix(m, 2);
        B.set(0, 0, vi[1].get(0, 0));
        B.set(1, 0, vi[1].get(1, 0));
        B.set(2, 0, vi[1].get(2, 0));
        B.set(3, 0, vi[1].get(3, 0));
        B.set(0, 1, vi[3].get(0, 0));
        B.set(1, 1, vi[3].get(1, 0));
        B.set(2, 1, vi[3].get(2, 0));
        B.set(3, 1, vi[3].get(3, 0));
        //B.print(4, 2);
        
        temp = B.transpose();
        temp = temp.times(B);
        temp = temp.inverse();
        Matrix SsNew = B.times(temp).times(B.transpose());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(Math.round(SsNew.get(i, j) + R.get(i, j)) + "\t");
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(s[i][j] + "\t");
            }
            System.out.println("");
        }
                
    }
}
