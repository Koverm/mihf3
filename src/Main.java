import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

    private int reviewn;
    private int usern;
    private int filmn;
    private float[][] mask;

    public float[][] read() throws IOException {
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String tmp = br.readLine();
        while (tmp==null){
            tmp = br.readLine();
        }
        String[] tmp2 = tmp.split("\t");
        reviewn = Integer.parseInt(tmp2[0]);
        usern = Integer.parseInt(tmp2[1]);
        filmn = Integer.parseInt(tmp2[2]);
        float[][] tmpm = new float[usern][filmn];
        mask = new float[usern][filmn];
        int i = 0;
        while ( i < reviewn) {
           tmp = br.readLine();
           if (tmp!=null) {
               tmp2 = tmp.split("\t");
               tmpm[Integer.parseInt(tmp2[0])][Integer.parseInt(tmp2[1])] = Float.parseFloat(tmp2[2]);
               mask[Integer.parseInt(tmp2[0])][Integer.parseInt(tmp2[1])] = 1;
               ++i;
           }
        }
        return tmpm;
    }
    public float[][] calc(float[][] m){
        float[][] p = new float[usern][3];
        float[][] q = new float[3][filmn];

        Random random = new Random();
        random.setSeed(1);

        for (int i = 0; i < p.length  ; i++) {
            for (int j = 0; j < p[0].length; j++) {
                p[i][j] = random.nextFloat();
            }
        }
        for (int i = 0; i < q.length  ; i++) {
            for (int j = 0; j < q[0].length; j++) {
                q[i][j] = random.nextFloat();
            }
        }

        float[][] prediction;
        float[][] prev_p = p;
        float[][] prev_q = q;

        for (int i = 0; i < 4000; i++) {
            prediction = mul(mask,sub(m,dot(p,q)));
            prev_p = p;
            prev_q = q;
            p = add(p,mul(0.0002f,sub(mul(2,dot(prediction,transpose(prev_q))), mul(0.02f, prev_p))));
            q = add(q,mul(0.0002f,sub(mul(2,dot(transpose(prev_p),prediction)), mul(0.02f,prev_q))));
        }

        return dot(p,q);
    }

    public int[][] max(float[][] matrix){

        int [][] tmp = new int [usern][10];
        for (int i = 0; i < tmp[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int maxi=0;
                for (int k = 1; k < matrix[0].length; k++) {
                    if (mask[j][k]!=1 && (matrix[j][k]>matrix[j][maxi])){
                        maxi = k;
                    }
                }
                tmp[j][i] = maxi;
                matrix[j][maxi] = 0;
            }
        }
        return tmp;
    }

    public void write(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    private float[][] dot(float[][] a, float[][] b){
        float[][] product = new float[a.length][b[0].length];

        for (int col = 0; col < product.length; col++){
            for (int row = 0; row < product[0].length; row++){
                for (int m_row = 0; m_row < a[0].length; m_row++) {
                    product[col][row] += a[col][m_row]*b[m_row][row];
                }
            }
        }

        return product;
    }

    private float[][] transpose(float[][] matrix){
        float[][] matrix_t = new float[matrix[0].length][matrix.length];
        for (int col = 0; col < matrix.length; col++){
            for (int row = 0; row < matrix[0].length; row++){
                matrix_t[row][col] = matrix[col][row];
            }
        }

        return matrix_t;
    }

    private float[][] add(float[][] matrix_a, float[][] matrix_b){
        float[][] product = new float[matrix_a.length][matrix_a[0].length];

        for(int col = 0; col < product.length; col++){
            for(int row = 0; row < product[0].length; row++) {
                product[col][row] = matrix_a[col][row]+matrix_b[col][row];
            }
        }

        return product;
    }

    private float[][] sub(float[][] matrix_a, float[][] matrix_b){
        float[][] product = new float[matrix_a.length][matrix_a[0].length];

        for(int col = 0; col < product.length; col++){
            for(int row = 0; row < product[0].length; row++) {
                product[col][row] = matrix_a[col][row]-matrix_b[col][row];
            }
        }

        return product;
    }

    private float[][] mul(float[][] matrix_a, float[][] matrix_b){
        float[][] product = new float[matrix_a.length][matrix_a[0].length];

        for(int col = 0; col < product.length; col++){
            for(int row = 0; row < product[0].length; row++) {
                product[col][row] = matrix_a[col][row]*matrix_b[col][row];
            }
        }

        return product;
    }

    private float[][] mul(float scalar, float[][] matrix){
        float[][] product = new float[matrix.length][matrix[0].length];

        for(int col = 0; col < product.length; col++){
            for(int row = 0; row < product[0].length; row++) {
                product[col][row] = scalar*matrix[col][row];
            }
        }

        return product;
    }


    public static void main(String[] args) throws IOException {
        Main m=new Main();
        float[][] matrix =m.read();
        matrix = m.calc(matrix);
        int [][] matrixi = m.max(matrix);
        m.write(matrixi);
    }
}
