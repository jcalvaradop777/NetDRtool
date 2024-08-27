/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import papaya.Cast;
import papaya.Eigenvalue;
import papaya.Mat;
import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static processing.core.PApplet.reverse;
import static processing.core.PApplet.sort;
import processing.data.Table;
 
/**
 *
 * @author Juan Carlos Alvarado
 */
public class DDRmix {   

int fK,cK; //filas columnas

Eigenvalue eigen;
float[] diagonal, diagonal_o;
float[][] EMB; //matriz de incrustramiento, datos reducidos a d dimensiones

Matrix V ; // vectores propios
Matrix D ; // valores propios
double[][] Yd; // matriz reducida a d dimensiones

public DDRmix(double[][] Xin, double[][] K1, double[][] K2, float R, float B, int d){
   
    try {
        fK = K1.length;
        cK = K1[0].length;
        
        int fm = Xin.length;
        int cm = Xin[0].length;
        
        //Previamente X tiene que estar centrada
        double[] mu;
        mu = Utils.MachineLearning.math.math.Math.colMean(Xin);
        double[][] X = Utils.MachineLearning.math.math.Math.clone(Xin);
        for (int i = 0; i < fm; i++) {
            for (int j = 0; j < cm; j++) {
                X[i][j] = Xin[i][j] - mu[j];
            }
        }
        
        //0) se pondera las matrices kernel con el RGB
        double [][] K1p = Matriz.ProductoEscalar(K1,R); //prodPesos(K1,R); // se multiplica el kernel por el factor 1
        double [][] K2p = Matriz.ProductoEscalar(K2,B); //prodPesos(K2,G); // se multiplica el kernel por el factor 2
        
        double [][] KM = Matriz.Suma(K1p,K2p);  // se suman las matrices Kernel

         // 1) C = (K*X)' * (K*X);
        
        double[][] T = Matriz.Multiplicar(KM,X);
        double[][] T2 = Matriz.Transpuesta(T);
        double[][] C = Matriz.Multiplicar(T2,T);
        
        //2) eigens
        Matrix A = Matrix.constructWithCopy(C);
        EigenvalueDecomposition e = A.eig();
        V = e.getV(); // eigenvectores
        D = e.getD(); // eigenvalores
        
        // 3)Y = K*X*V;
        double[][] Y;
        Y = Matriz.Multiplicar(T,V.getArray());
        
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
    } catch (Exception ex) {
        Logger.getLogger(DDRmix.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public DDRmix(double[][] Xin, double[][] K1, double[][] K2, double[][] K3, float R, float G, float B, int d){
   
    try {
        fK = K1.length;
        cK = K1[0].length;
        
        int fm = Xin.length;
        int cm = Xin[0].length;
        
        //Previamente X tiene que estar centrada
        double[] mu;
        mu = Utils.MachineLearning.math.math.Math.colMean(Xin);
        double[][] X = Utils.MachineLearning.math.math.Math.clone(Xin);
        for (int i = 0; i < fm; i++) {
            for (int j = 0; j < cm; j++) {
                X[i][j] = Xin[i][j] - mu[j];
            }
        }
        
        // se pondera las matrices kernel con el RGB
        double [][] K1p = Matriz.ProductoEscalar(K1,R); //prodPesos(K1,R); // se multiplica el kernel por el factor 1
        double [][] K2p = Matriz.ProductoEscalar(K2,G); //prodPesos(K2,G); // se multiplica el kernel por el factor 2
        double [][] K3p = Matriz.ProductoEscalar(K3,B); //prodPesos(K3,B); // se multiplica el kernel por el factor 3
        
        double [][] KM = Matriz.Suma(K1p,K2p);  // se suman las matrices Kernel
        KM = Matriz.Suma(KM,K3p);   // se suman las matrices Kernel
        
        // 1) C = (K*X)' * (K*X);
        
        double[][] T = Matriz.Multiplicar(KM,X);
        double[][] T2 = Matriz.Transpuesta(T);
        double[][] C = Matriz.Multiplicar(T2,T);
        
        //2) eigens
        Matrix A = Matrix.constructWithCopy(C);
        EigenvalueDecomposition e = A.eig();
        V = e.getV(); // eigenvectores
        D = e.getD(); // eigenvalores
        
        // 3)Y = K*X*V;
        double[][] Y;
        Y = Matriz.Multiplicar(T,V.getArray());
        
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
    } catch (Exception ex) {
        Logger.getLogger(DDRmix.class.getName()).log(Level.SEVERE, null, ex);
    }
}

//se multiplica el kernel por el factor

    /**
     *
     * @param Matri: matriz de datos a multiplicar
     * @param alpha: es el factor de ponderacion 
     * @return
     */
//public double[][] prodPesos(double [][] Matri ,double alpha){ 
//   double[][] alpX= new double [fK][cK];
//   
//   for (int j=0;j<cK;j++){
//      for (int i=0;i<fK;i++){
//        alpX[i][j]=alpha*Matri[i][j];
//     } 
//   }
//   return alpX;
//}

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
