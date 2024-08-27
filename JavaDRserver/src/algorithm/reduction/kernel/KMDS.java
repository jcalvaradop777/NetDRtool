/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import javax.swing.table.AbstractTableModel;


public class KMDS {
    
    double[][] X; // Datos de entrada en forma de matriz de objeto

    private AbstractTableModel dataIn;
    private AbstractTableModel dataOut;
    private double[][] K;
    private EigenvalueDecomposition e;


    public KMDS(AbstractTableModel dataIn) throws Exception {
        this.dataIn = dataIn;
        X = LeerDataIn();
        KernelMDS();
    }
    
    public KMDS(double[][] dataIn) throws Exception {
        X = dataIn;
//        this.etiquetas = etiquetas;
        KernelMDS();
    }

    private boolean Etiqueta() {
        if (dataIn.getValueAt(0, dataIn.getColumnCount() - 1) instanceof String) {
            return true;
        } else {
            return false;
        }
    }

    private double[][] LeerDataIn() {
        double[][] data;

        if (Etiqueta()) {
            data = new double[dataIn.getRowCount()][dataIn.getColumnCount() - 1];
        } else {
            data = new double[dataIn.getRowCount()][dataIn.getColumnCount()];
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = (double) dataIn.getValueAt(i, j);
            }

        }
        return data;
    }

    private String[] LeerEtiquetas() {
        String[] etiquetas;

        if (Etiqueta()) {
            etiquetas = new String[dataIn.getRowCount()];
            for (int i = 0; i < etiquetas.length; i++) {
                etiquetas[i] = (String) dataIn.getValueAt(i, dataIn.getColumnCount() - 1);
            }
            return etiquetas;
        } else {
            return null;
        }
    }

    public void KernelMDS() throws Exception {
        int N;
//        double[][] X;
        double[][] D;
        double[][] N1;
        double[][] I;
        double[][] Itmp;
//        String[] etiquetas;
        
        // Para asegurarse de que se vuelva a calcular vectores y valores propios
        // si se vuelve a generar un kernel e=null;
        e = null; 

        N = X.length;

        D = Matriz.Dist(X);

        N1 = Matriz.ProductoEscalar(Matriz.Unos(N, 1), Math.pow(N, -0.5));

        I = Matriz.Identidad(N);

        //Itmp = (I-N1*N1t);
        Itmp = Matriz.Resta(I, Matriz.Multiplicar(N1, Matriz.Transpuesta(N1)));

        K = Matriz.Multiplicar(Matriz.Multiplicar(Matriz.ProductoEscalar(Itmp, -0.5), D), Itmp);

//        etiquetas = LeerEtiquetas();

//        setDataOut(K, etiquetas);
    }

    
    public double[][] getEigValores() {

        if (e == null) { // Sino se ha calculado aun descomposicion espectral
            //Matrix Libreria JAMA
            Matrix A = Matrix.constructWithCopy(K);

            // Calcular la descomposicion espectral
            e = A.eig();
        }
        Matrix D = e.getD();
        return D.getArray();
    }

    public double[][] getEigVectores() {
        if (e == null) { // Sino se ha calculado aun descomposición espectral
            
            //Matrix Libreria JAMA
            Matrix A = Matrix.constructWithCopy(K);

            // Calcular la descomposición espectral
            e = A.eig();
        }
        Matrix V = e.getV();

        return V.getArray();
    }
    
    public double[][] getKernelMDS(){
        return K;
    }

//    private void setDataOut(double[][] data, String[] etiquetas) {
//
//        Object[][] datos;
//
//        if (etiquetas != null) {
//            if (data.length != etiquetas.length) {
//                throw new UnsupportedOperationException("Numero de filas es diferente a números de etiquetas......");
//            }
//            datos = new Object[data.length][data[0].length + 1]; // Crear Objecto con una columna mas, para almacenar las etiquetas.
//            // Asignos las etiquetas en la ultima columna del objeto Object[][]
//            for (int i = 0; i < etiquetas.length; i++) {
//                datos[i][datos[i].length - 1] = etiquetas[i];
//            }
//        } else {
//            datos = new Object[data.length][data[0].length];
//        }
//
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < data[i].length; j++) {
//                datos[i][j] = data[i][j];
//            }
//        }
//
//        dataOut = new Datos(datos);
//    }

    /**
     * Obtiene arreglo Bi-dimensional con las dimensiones necesarias
     *
     * @param dimensiones Numero de dimensiones a reducir.
     * @return
     */
    public double[][] getMDSRD(int dimensiones) throws Exception {
        int[] a;
        double[][] vectores, valores;
        double[][] mdsRd;
        
        
        valores = getEigValores();
        a = Matriz.ColumnasValores(valores, dimensiones);
        vectores = getEigVectores();
        mdsRd = new double[vectores.length][dimensiones];

//        for (int i = 0; i < a.length; i++) {
//            System.out.println("Columnas a Tomar:  " + a[i]);
//
//        }

        // Obtener de la matriz de vectores solo las columnas que 
        // me indican el arrglo a.

        for (int i = 0; i < vectores.length; i++) {
            for (int j = 0; j < dimensiones; j++) {
                mdsRd[i][j] = vectores[i][a[j]];
            }
        }
     
//        Matriz.Normalizar(mdsRd);
        
        return mdsRd;
    }

    
//    public AbstractTableModel getDataOut() {
//        return dataOut;
//    }

}
