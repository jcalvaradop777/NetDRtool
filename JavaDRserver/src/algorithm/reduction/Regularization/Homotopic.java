/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.Regularization;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import algorithm.reduction.kernel.Matriz;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Carlos Alvarado
 */
public class Homotopic {

    private double[][] YdrHomotopic;
               
    public Homotopic(int d, double mu,double[][] Xin,double[][] YrKernelLocal){
   
//       Matriz.Normalizar(X);
//       Matriz.Normalizar(Yr);

        double[][] X = Matriz.Centrar(Xin);

        // kernelocal = kernelocal*Proyeccion(X,d,1,0,0); Codigo en matlab de testRegLG-kpca
        double[][] kernelLocal = Matriz.Multiplicar(YrKernelLocal,PCA(X,d));   
        double[][] kernelglobal = proyeccionGlobal(X,d),

        // RegLG = (mu*kernelocal) + ((1-mu)*kernelglobal); Codigo en matlab de testRegLG-kpca
        ydr = Matriz.Suma( Matriz.ProductoEscalar(kernelLocal,mu), Matriz.ProductoEscalar(kernelglobal,(1-mu)) );
        YdrHomotopic = ydr;
    }
    
    public double[][] PCA(double[][] X, int d) {
     
        Matrix V ; // vectores propios
        Matrix D ; // valores propios
       double[][] Ypca;
    
        // 1) C = X'*X;
        double[][] Xt = Matriz.Transpuesta(X);
        double[][] C = Matriz.Multiplicar(Xt,X);
        
        //2) eigens
        Matrix A = Matrix.constructWithCopy(C);
        EigenvalueDecomposition e = A.eig();
        V = e.getV(); // eigenvectores
        D = e.getD(); // eigenvalores
        
        // 3)Y = X*V;
        double[][] Y;
        Y = Matriz.Multiplicar(X,V.getArray());
        
        // 4) Se crea la matriz resultante con las d dimensiones reducidas asociadas al mayor eigenvalor
        int[] a = null;
 
        try {
            a = Matriz.ColumnasValores(D.getArray(), d); // columnas con mayor eigenValues
        } catch (Exception ex) {
            Logger.getLogger(Homotopic.class.getName()).log(Level.SEVERE, null, ex);
        }
//        a = Matriz.getColsMaxEigValues(getEigValores(), dimensiones);
//        vectores = getEigVectores();
        Ypca = new double[Y.length][d];
 
        for (int i = 0; i < Y.length; i++) {
            for (int j = 0; j < d; j++) {
                Ypca[i][j] = Y[i][ a[j] ];
            }
        }
        
        return Ypca;
    }
    
    // hace una proyeccion similar a PCA
    public double[][] proyeccionGlobal(double[][] X, int d) {
     
        Matrix V ; // vectores propios
        Matrix D ; // valores propios
       double[][] Yglobal;
    
        // 1) C = X*X';
        double[][] Xt = Matriz.Transpuesta(X);
        double[][] C = Matriz.Multiplicar(X,Xt);
        
        //2) eigens
        Matrix A = Matrix.constructWithCopy(C);
        EigenvalueDecomposition e = A.eig();
        V = e.getV(); // eigenvectores
        D = e.getD(); // eigenvalores
        
        // 3)Y = X*X'*V
        double[][] Y;
        Y = Matriz.Multiplicar(C,V.getArray());
        
        // 4) Se crea la matriz resultante con las d dimensiones reducidas asociadas al mayor eigenvalor
        int[] a = null;
 
        try {
            a = Matriz.ColumnasValores(D.getArray(), d); // columnas con mayor eigenValues
        } catch (Exception ex) {
            Logger.getLogger(Homotopic.class.getName()).log(Level.SEVERE, null, ex);
        }
//        a = Matriz.getColsMaxEigValues(getEigValores(), dimensiones);
//        vectores = getEigVectores();
        Yglobal = new double[Y.length][d];
 
        for (int i = 0; i < Y.length; i++) {
            for (int j = 0; j < d; j++) {
                Yglobal[i][j] = Y[i][ a[j] ];
            }
        }
        
        return Yglobal;
    }

    public double[][] getNewCoordinates(){
        return YdrHomotopic;
    }

}
