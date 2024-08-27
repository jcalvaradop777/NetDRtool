/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
        

public class KLLE {
    
    private double[][] X; // Datos iniciales
    private double[][] K;
    private int knn;
    double tol;
    private EigenvalueDecomposition e;
    int n;
    int D;

    public KLLE(double[][] X,int kn)
    {
        this.X = X;
        this.knn = kn;  
        tol = 0.0f;
    }

    
    public double[][] getKernelLLE(){
        n = X.length;
        D = X[0].length;
        if(knn>D){
            tol = 0.00001;
        }
        int[][] neighborNN = new int[n][knn];
        
        double[][] X_1 = Matriz.PotenciaEbyE(X, 2);        
        double[][] X_1Sum = SumaColumnas(X_1);        
        double[][] X_2Sum = PlusVector(Matriz.Transpuesta(X_1Sum),Matriz.ProductoEscalar(Matriz.Multiplicar(X, Matriz.Transpuesta(X)), -2));
        double[][] DD = PlusVector(X_1Sum, X_2Sum);
        double[][] DD1 = Matriz.Clonar(DD);

        neighborNN = IndicesVecinosCercanos(DD,DD1);
        double [][] W = CalculoPesos(neighborNN);
        double [][] M=sparse(neighborNN,W);
        double [][] eigM=EigValores(M);
        double [] diageig=Matriz.Diagonal(eigM);
        double lamb=Max(diageig);
        double[][] I = Matriz.Identidad(n);
        K=Matriz.Resta(Matriz.ProductoEscalar(I, lamb),M);
        
//        Matriz.Normalizar(K);
        
        return K;  
        
    }
    
    public double[][] SumaColumnas(double [][] data){
        double[][] dataOut = new double[data.length][1];
        for(int i=0;i<data.length;i++){
            dataOut[i][0] = 0;
            for(int j=0;j<data[0].length;j++){
                dataOut[i][0] += data[i][j];
            }       
        }
        return dataOut;        
    }
    
    public double[][] PlusVector(double [][] V,double [][] M){
        double[][] dataOut = new double[M.length][M[0].length];
        if(V.length == M.length && V[0].length == 1){
            for(int i=0;i<M[0].length;i++){
                for(int j=0;j<M.length;j++){
                    dataOut[i][j] = M[i][j] + V[j][0];
                }
            }
        }else if(V[0].length ==M[0].length && V.length == 1){            
            for(int i=0;i<M.length;i++){
                for(int j=0;j<M[0].length;j++){
                    dataOut[i][j] = M[i][j] + V[0][j];
                }
            }
        }else{
            System.err.println("Error de dimensiones");
            return null;
        }
        return dataOut;
        
    }
    
    int[][] IndicesVecinosCercanos(double [][] DDist,double [][] DDist1){
        int[][] dataOut = new int[DDist.length][knn];
        for(int i=0;i<DDist.length;i++){
            double[] fila = DDist[i];            
            Arrays.sort(fila);            
            for(int j = 1; j<=knn;j++){
                dataOut[i][j-1] = Matriz.BusquedaSecuencial(DDist1[i], fila[j]);
                
            }
        }
        return dataOut;
    }
    
    double[][] CalculoPesos(int [][] MD){
        double[][] dataOut = new double[n][knn];
        for(int i=0;i<n;i++){
            double[][] z = new double[knn][D];
            for(int j=0;j<knn;j++){
                z[j] = X[MD[i][j]];
            }
            
            double[][] miPunto = new double[1][D];
            miPunto[0]=X[i];
            z = PlusVector(Matriz.ProductoEscalar(miPunto, -1), z);
            double[][] z2 = Matriz.Transpuesta(z);
            double[][] C = Matriz.Multiplicar(z, z2);
            C = Matriz.Suma(C, Matriz.ProductoEscalar(Matriz.Identidad(knn), tol*Matriz.Trace(C)));
            Matrix CC = new Matrix(C);
            double[][] W = Matriz.Multiplicar(Matrices.pinv(CC).getArray(), Matriz.Unos(knn,1));
            W = Matriz.ProductoEscalar(W, 1/Matriz.SumaCol(W));
            double[][] W1 = Matriz.Transpuesta(W);
            dataOut[i] = W1[0];
            
        }
        
        return Matriz.Transpuesta(dataOut);
    }
    
      double[][] sparse (int [][] Nei, double [][]W ){
      double[][] I = Matriz.Identidad(n);
      double[][] w1=Matriz.Resta(Matriz.Unos(n),Matriz.Unos(n));
          for(int i=0;i<n;i++){
            for(int j=0;j<knn;j++){
              w1[i][Nei[i][j]]=W[j][i];
         
            }           
        }
          double[][]M=Matriz.Multiplicar(Matriz.Transpuesta(Matriz.Resta(I, w1)), Matriz.Resta(I, w1));
        // Matriz.Imprimir(M);
          
        return M;
}
        
      public double[][] EigValores(double [][]k) {

        if (e == null) { // Sino se ha calculado aun descomposición espectral
            //Matrix Libreria JAMA
            Matrix A = Matrix.constructWithCopy(k);
            

            // Calcular la descomposición espectral
            e = A.eig();
        }
        Matrix D = e.getD();
        return D.getArray();
    }
  public double Max(double []vector) {
        double max, min;
        max=min=vector[0];
     for (int i = 0; i < vector.length; i++) {        
        if(vector[i]>max){
            max=vector[i];   
        }
        if(vector[i]<min){
            min=vector[i];
        }
     }
        return max;
  }
  
  
      public double[][] EigValores() {

        if (e == null) { // Sino se ha calculado aun descomposición espectral
            //Matrix Libreria JAMA
            Matrix A = Matrix.constructWithCopy(K);
            

            // Calcular la descomposición espectral
            e = A.eig();
        }
        Matrix D = e.getD();
        return D.getArray();
    }

    public double[][] EigVectores() {
        if (e == null) { // Sino se ha calculado aun descomposición espectral
            
            //Matrix Libreria JAMA
            Matrix A = Matrix.constructWithCopy(K);
            
            // Calcular la descomposición espectral
            e = A.eig();
        }
        Matrix V = e.getV();

        return V.getArray();
    }
    
      /**
     * Obtiene arreglo Bi-dimensional con las dimensiones necesarias
     *
     * @param dimensiones Número de dimensiones a reducir.
     * @return
     */
    public double[][] getLLERD(int dimensiones) throws Exception {
        int[] a;
        double[][] vectores, valores;
        double[][] lleRd;
        
        valores = EigValores();
        //Matriz.EscribirCsv("D:/LERD-valores.csv", valores);
        a = Matriz.ColumnasValores(valores, dimensiones);
        vectores = EigVectores();
        lleRd = new double[vectores.length][dimensiones];

//        for (int i = 0; i < a.length; i++) {
//            System.out.println("Columnas a Tomar:  " + a[i]);
//
//        }

        // Obtener de la matriz de vectores solo las columnas que 
        // me indican el arrglo a.

        for (int i = 0; i < vectores.length; i++) {
            for (int j = 0; j < dimensiones; j++) {
                lleRd[i][j] = vectores[i][a[j]];
            }
        }
     
        return lleRd;
    }
  
}
  