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
 
/**
 *
 * @author MacArthur    Metodo anterior
 */
public class KMDS1 {

    double[][] X; // Datos de entrada en forma de matriz de objeto
//    String[] etiquetas; 
    
    private double[][] K; //Matriz kernel, datos de salida
    
    public KMDS1(double[][] dataIn) throws Exception {
        this.X = dataIn;
//        this.etiquetas = etiquetas;
        KernelMDS();
    }

    public void KernelMDS() throws Exception {
        int N;
        
        double[][] D;
        double[][] N1;
        double[][] I;
        double[][] Itmp;

        N = X.length;

        D = Matriz.Potencia(Matriz.Dist(X), 2);

        N1 = Matriz.ProductoEscalar(Matriz.Unos(N, 1), Math.pow(N, -0.5));

        I = Matriz.Identidad(N);

        //Itmp = (I-N1*N1t);
        Itmp = Matriz.Resta(I, Matriz.Multiplicar(N1, Matriz.Transpuesta(N1)));

        K = Matriz.Multiplicar(Matriz.Multiplicar(Matriz.ProductoEscalar(Itmp, -0.5), D), Itmp);

//        System.out.println("Matriz KERNEL");
//        Matriz.Imprimir(getKernelMDS());

//        System.out.println("Matriz KERNEL");
//        Matriz.Imprimir(kmds.getKernelMDS());
//        System.out.println("Matriz Vectores");
//        Matriz.Imprimir(kmds.EigVectores());
//        System.out.println("Matriz Valores");
//        Matriz.Imprimir(kmds.EigValores());
    }

    public double[][] getEigValores() {

        //Matrix Libreria JAMA
        Matrix A = Matrix.constructWithCopy(K);

        // compute the spectral decomposition
        EigenvalueDecomposition e = A.eig();

        Matrix D = e.getD();
        return D.getArray();
    }

    public double[][] getEigVectores() {
        //Matrix Libreria JAMA
        Matrix A = Matrix.constructWithCopy(K);

        // compute the spectral decomposition
        EigenvalueDecomposition e = A.eig();

        Matrix V = e.getV();

        return V.getArray();
    }

    public double[][] getKernelMDS(){
        return K;
    }
    
    /**
     * Obtiene arreglo Bi-dimensional con las dimensiones necesarias
     * @param dimensiones Número de dimensiones a reducir.
     * @return
     */
    public double[][] getMDSRD(int dimensiones){
        int[] a = null;
        double[][] vectores;
        double[][] mdsRd;
 
        try {
            a = Matriz.ColumnasValores(getEigValores(), dimensiones); // columnas con mayor eigenValues
//        a = Matriz.getColsMaxEigValues(getEigValores(), dimensiones);
        } catch (Exception ex) {
            Logger.getLogger(KMDS1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        vectores = getEigVectores();
        mdsRd = new double[vectores.length][dimensiones];
 
        for (int i = 0; i < a.length; i++) {
            System.out.println("pos "+a[i]);           
        }
        // Obtener de la matriz de vectores solo las columnas que
        // me indican el arrglo a.
        for (int i = 0; i < vectores.length; i++) {
            for (int j = 0; j < dimensiones; j++) {
                mdsRd[i][j]=vectores[i][ a[j] ];
            }
        }
        return mdsRd;
    }
    
        public double[][] getMDSRD2(int dimensiones){
        int[] a = null;
        double[][] vectores;
        double[][] mdsRd;
 
        try {
            a = Matriz.ColumnasValores(getEigValores(), dimensiones); // columnas con mayor eigenValues
        } catch (Exception ex) {
            Logger.getLogger(KMDS1.class.getName()).log(Level.SEVERE, null, ex);
        }
        vectores = getEigVectores();
        mdsRd = new double[vectores.length][dimensiones];
 
        for (int i = 0; i < a.length; i++) {
            System.out.println("a "+a[i]);           
        }
        // Obtener de la matriz de vectores solo las columnas que
        // me indican el arrglo a.
        for (int i = 0; i < vectores.length; i++) {
            for (int j = 0; j < dimensiones; j++) {
                mdsRd[i][j]=vectores[i][ a[j] ];
            }
        }
        return mdsRd;
    }

}
