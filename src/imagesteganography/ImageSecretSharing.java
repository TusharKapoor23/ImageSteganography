package imagesteganography;

import Jama.Matrix;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * @author Tushar Kapoor
 */
public class ImageSecretSharing {

    public static void makeShares(double[][] image, int height) throws IOException {
        Matrix S = new Matrix(image);
        int k = 2;
        int m = 4;
        if (!(m > 2 * (k - 1) - 1)) {
            System.exit(-1);
        }
        Matrix mat;
        do {
            mat = Matrix.random(300, 2);
            for (int i = 0; i < 300; i++) {
                for (int j = 0; j < 2; j++) {
                    mat.set(i, j, mat.get(i, j) * 100);
                    mat.set(i, j, (int) mat.get(i, j) / 10);
                }
            }
        } while (mat.rank() != k);
        Matrix[] xi = new Matrix[m];
        for (int i = 0; i < m; i++) {
            xi[i] = Matrix.random(2, 300);
            for (int j = 0; j < 2; j++) {
                for (int l = 0; l < 1; l++) {
                    xi[i].set(j, l, xi[i].get(j, l) * 100);
                    xi[i].set(j, l, (int) xi[i].get(j, l) / 10);
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

        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("Shares.txt"));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 300; j++) {
                for (int l = 0; l < 1; l++) {
                    outputWriter.write(vi[i].get(j, l) + "\t");
                }
                outputWriter.newLine();
            }
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();

        Matrix temp = mat.transpose().times(mat);
        temp = temp.inverse();
        Matrix Ss = mat.times(temp).times(mat.transpose());
        Matrix R = S.minus(Ss);
        Matrix B = new Matrix(300, 2);
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < vi[1].getRowDimension(); j++) {
                for (int l = 0; l < vi[1].getColumnDimension(); l++) {
                    B.set(i, 0, vi[1].get(j, l));
                }
            }
        }
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < vi[1].getRowDimension(); j++) {
                for (int l = 0; l < vi[1].getColumnDimension(); l++) {
                    B.set(i, 1, vi[2].get(j, l));
                }
            }
        }

        temp = B.transpose();
        temp = temp.times(B);
        temp = temp.inverse();
        Matrix SsNew = B.times(temp).times(B.transpose());
        double[][] newImage = new double[300][300];
        System.out.println("NEW IMAGE");
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                newImage[i][j] = Math.round(SsNew.get(i, j) + R.get(i, j));
                System.out.print(newImage[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("OG IMAGE");
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                System.out.print(S.get(i, j) + "\t");
            }
            System.out.println("");
        }
        System.out.println("SUBTRACTION");
        int sum = 0;
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                sum += (newImage[i][j] - S.get(i, j));
            }
        }
        System.out.println("Sum = " + sum);

    }

    public static void main(String[] args) {
        double[][] s = new double[][]{
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
        double[][] A = new double[][]{
            {10, 1},
            {7, 2},
            {8, 4},
            {1, 1}};
        Matrix mat = new Matrix(A);
        /*do {
            mat = Matrix.random(4, 2);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    mat.set(i, j, mat.get(i, j) * 100);
                    mat.set(i, j, (int) mat.get(i, j) / 10);
                }
            }
        } while (mat.rank() != k);*/
        Matrix[] xi = new Matrix[m];
        xi[0] = Matrix.random(2, 1);
        xi[0].set(0, 0, 1);
        xi[0].set(1, 0, 17);
        xi[1] = Matrix.random(2, 1);
        xi[1].set(0, 0, 1);
        xi[1].set(1, 0, 7);
        xi[2] = Matrix.random(2, 1);
        xi[2].set(0, 0, 1);
        xi[2].set(1, 0, 1);
        xi[3] = Matrix.random(2, 1);
        xi[3].set(0, 0, 1);
        xi[3].set(1, 0, 9);
        /*for (int i = 0; i < m; i++) {
            xi[i] = Matrix.random(2, 1);
            for (int j = 0; j < 2; j++) {
                for (int l = 0; l < 1; l++) {
                    xi[i].set(j, l, xi[i].get(j, l) * 100);
                    xi[i].set(j, l, (int) xi[i].get(j, l) / 10);
                }
            }
        }*/
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

        Matrix step1 = mat.transpose();
        Matrix step2 = mat.times(step1);//step1.times(mat);
        Matrix step3 = step2.inverse();
        Matrix step4 = mat.times(step3);
        Matrix step5 = mat.transpose();
        Matrix step6 = step4.times(step5);
        Matrix Ss = step6;
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
        Matrix temp;
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
