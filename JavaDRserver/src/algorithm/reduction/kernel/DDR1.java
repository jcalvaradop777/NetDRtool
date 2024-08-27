
package algorithm.reduction.kernel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
 
/**
 *
 * @author Juan Carlos Alvarado   Metodo anterior
 */
public class DDR1 {

    Matrix V ; // vectores propios
    Matrix D ; // valores propios
    double[][] Yd; // matriz reducida a d dimensiones
    
    public DDR1(double[][] Xin, double[][] Mk, int d) throws Exception {

        int f = Xin.length;
        int c = Xin[0].length;
        
        //Previamente X tiene que estar centrada
        double[] mu;
        mu = Utils.MachineLearning.math.math.Math.colMean(Xin);
        double[][] X = Utils.MachineLearning.math.math.Math.clone(Xin);
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                X[i][j] = Xin[i][j] - mu[j];
            }
        }
        
        // 1) Xt * K * X
        double[][] Xt = Matriz.Transpuesta(X);
        double[][] C = Matriz.Multiplicar(Xt,Mk);
        C = Matriz.Multiplicar(C,X);
        
        //2) eigens
        Matrix A = Matrix.constructWithCopy(C);
        EigenvalueDecomposition e = A.eig();
        V = e.getV(); // eigenvectores
        D = e.getD(); // eigenvalores
        
        // 3) Y = XV (pca)
        double[][] Y = Matriz.Multiplicar(X,V.getArray());
        
        // 4) Se crea la matriz resultante con las d dimensiones reducidas asociadas al mayor eigenvalor
        int[] a;
        double[][] vectores;
 
        a = Matriz.ColumnasValores(getEigValores(), d); // columnas con mayor eigenValues
//        a = Matriz.getColsMaxEigValues(getEigValores(), dimensiones);
//        vectores = getEigVectores();
        Yd = new double[Y.length][d];
 
        for (int i = 0; i < Y.length; i++) {
            for (int j = 0; j < d; j++) {
                Yd[i][j] = Y[i][ a[j] ];
            }
        }
    }
    
    public double[][] getEigValores() {
        return D.getArray();
    }

    public double[][] getEigVectores() {
        return V.getArray();
    }

    public double[][] getLowMatrix(){
        return Yd;
    }
}
