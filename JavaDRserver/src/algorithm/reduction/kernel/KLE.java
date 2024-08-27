/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


public class KLE{

    private double[][] X; // Datos iniciales
    private double[][] K;
    private EigenvalueDecomposition e;
    

    public KLE(double[][] X)
    {
        super();
        this.X = X;
    }
    
    public double [][]getKernelLE() {
        // prueba commit
        int sigma = 50;
        int N;
        double [][]  W,D,L,Lt,M,MInv,Kt;
        double sumatoria;
        
        
        sumatoria=0;
        N = X.length;
        
        W = new double[N][N]; // Matriz de Similitud weithing matrix
        D = new double[N][N]; // Matriz Degree
        
        for (int i = 0; i < N; i++) {
            sumatoria=0;
            for (int j = 0; j < N; j++) {
                
                // Calculo de vector = fila i - fila j
                double[] vecTmp = new double[X[0].length];
                for (int k = 0; k < vecTmp.length; k++) {
                        vecTmp[k] = X[i][k]- X[j][k];
                }
                W[i][j] =  Math.pow(Math.E,-Math.pow(Matriz.Norma(vecTmp),2)/(sigma*sigma));
                sumatoria += W[i][j];
            }
            D[i][i]= sumatoria;
        }
             
        L=Matriz.Resta(D, W); // L = D - W
         
        
        
        /* 
        //Calculo del Kernel Metodo 1
        //        Lt = L';
        //        M = L * Lt;
        //        Linv = (M)^(-1);
        //        KLE1 = Lt * Linv; % Porque por la derecha puedo hacer por la izquierda
        
        // Calculamos la ranspuesta
        Lt = Matriz.Transpuesta(L);
        
        // M = L * Lt
        M = Matriz.Multiplicar(L, Lt);

        //Uso de Matrix - Libreria JAMA
        // Calcular la Inversa MInv = (L * Lt)^-1
        Matrix A = new Matrix(M); // M pasa a aser los datos de A
        MInv=A.inverse().getArray();  // devuelve la eferencia a A (datos de Matrix)
        Matriz.Imprimir(MInv,17,8);
        
        */
       
        //Calcular el kernel
        //K = Matriz.Multiplicar(Lt, MInv);
       // Matriz.Imprimir(L);
        Matrix Lmatrix = new Matrix(L);
         //Matriz.Imprimir(Lmatrix.getArray());
        K = Matrices.pinv(Lmatrix).getArray();
        Kt = Matriz.Transpuesta(K); 
        K = Matriz.Suma(K, Kt);
        K = Matriz.ProductoEscalar(K, 0.5);
        
//        Matriz.Normalizar(K);
        
        return K;
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
    public double[][] getLERD(int dimensiones) throws Exception {
        int[] a;
        double[][] vectores, valores;
        double[][] leRd;
        
        
        valores = EigValores();
        //Matriz.EscribirCsv("D:/LERD-valores.csv", valores);
        a = Matriz.ColumnasValores(valores, dimensiones);
        vectores = EigVectores();
        leRd = new double[vectores.length][dimensiones];

//        for (int i = 0; i < a.length; i++) {
//            System.out.println("Columnas a Tomar:  " + a[i]);
//
//        }

        // Obtener de la matriz de vectores solo las columnas que 
        // me indican el arrglo a.

        for (int i = 0; i < vectores.length; i++) {
            for (int j = 0; j < dimensiones; j++) {
                leRd[i][j] = vectores[i][a[j]];
            }
        }
     
        return leRd;
    }
}
