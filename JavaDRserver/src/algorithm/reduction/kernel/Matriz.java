/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;

import SortArrays.SortArrays;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MacArthur, Juan Carlos Alvarado P
 */
public class Matriz{

//    private static double[][] Datos;
    public static double Norma(double[] vector) {
        double resp;

        resp = 0;
        for (int i = 0; i < vector.length; i++) {
            resp += Math.pow(vector[i], 2);
        }

        return Math.sqrt(resp);
    }

    private static double[] VectorDiferencia(double[] vi, double[] vj) {
        double[] resp;

        if (vi.length != vj.length) {
            System.out.println("Error:  Diferencia de vectores NULL, dimensiones diferentes...");
            return null;
        } else {
            resp = new double[vi.length];
            for (int i = 0; i < vi.length; i++) {
                resp[i] = vi[i] - vj[i];
            }
            return resp;
        }
    }

    public static double[][] Dist(double[][] datos) {
        // N = datos.lenght
        int N;
        double[][] dist;

        N = datos.length;
        dist = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dist[i][j] = Math.pow(Norma(VectorDiferencia(datos[i], datos[j])),2);
            }

        }
        return dist;
    }

    public static double[][] Transpuesta(double[][] datos) {
        double[][] r;
        r = new double[datos[0].length][datos.length];

        for (int x = 0; x < datos.length; x++) {
            for (int y = 0; y < datos[x].length; y++) {
                r[y][x] = datos[x][y];
            }
        }
        return r;

    }

    public static double[][] Clonar1(double[][] m1) {
        double[][] r;

        r = new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                r[i][j] = m1[i][j];
            }
        }

        return r;
    }
    
    public static double[][] Clonar(double[][] m1){
        
         double[][] r;
         
         r=(double[][])m1.clone();
        System.out.println("m1 "+m1);
        System.out.println("m1 "+m1[0]);
        System.out.println("m1 "+m1[1]);
        System.out.println("r "+r);
        System.out.println("r "+r[0]);
        System.out.println("r "+r[1]);
        return r;
    }
    

    public static double[][] Potencia(double[][] m1, int exp) {
        double[][] r;

        if (m1.length == m1[0].length) {
            r = Clonar(m1);

            // for inicia desde 2, potencia a la 1 queda la matriz original
            for (int i = 2; i <= exp; i++) {
                r = Multiplicar(r, m1);
            }
            return r;
        } else {
            return null;
        }
    }

    public static double[][] Multiplicar(double[][] m1, double[][] m2) {
        double[][] r;

        if (m1[0].length == m2.length) {
            r = new double[m1.length][m2[0].length];

            for (int x = 0; x < m1.length; x++) {
                for (int y = 0; y < m2[0].length; y++) {
                    r[x][y] = 0;
                    for (int i = 0; i < m1[0].length; i++) {
                        r[x][y] += m1[x][i] * m2[i][y];
                    }
                }
            }

            return r;
        } else {
            return null;
        }
    }

    public static double[][] Unos(int x) {
        return Unos(x, x);
    }

    public static double[][] Unos(int x, int y) {
        double[][] r;

        r = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                r[i][j] = 1;
            }
        }
        return r;
    }

    public static double[][] Identidad(int x) {
        return Identidad(x, x);
    }

    public static double[][] Identidad(int x, int y) {
        double[][] r;

        r = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                r[i][j] = (i == j ? 1.0 : 0.0);
            }
        }
        return r;
    }

    public static double[][] Resta(double[][] m1, double[][] m2) {
        double[][] resp;

        resp = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                resp[i][j] = m1[i][j] - m2[i][j];
            }

        }
        return resp;
    }

    public static double[][] ProductoEscalar(double[][] datos, double escalar) {
        double[][] resp;

        resp = new double[datos.length][datos[0].length];

        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < datos[0].length; j++) {
                resp[i][j] = datos[i][j] * escalar;
            }

        }
        return resp;

    }

    private static double redondearDecimales(double valor, int numeroDecimales) {
        double resultado;

        resultado = valor * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = resultado / Math.pow(10, numeroDecimales);
        return resultado;
    }

    public static void QuickSort(double A[][], int izq, int der) {

        double pivote = A[0][izq]; // tomamos primer elemento como pivote
        double pivote1 = A[1][izq]; // Lo propio para cambiar los valores de las posiciones
        
        int i = izq; // i realiza la búsqueda de izquierda a derecha
        int j = der; // j realiza la búsqueda de derecha a izquierda
        double aux;

        while (i < j) {            // mientras no se crucen las búsquedas
            while (A[0][i] <= pivote && i < j) {
                i++; // busca elemento mayor que pivote
            }
            while (A[0][j] > pivote) {
                j--;         // busca elemento menor que pivote
            }
            if (i < j) {                      // si no se han cruzado                      
                aux = A[0][i];                  // los intercambia
                A[0][i] = A[0][j];
                A[0][j] = aux;
                
                aux = A[1][i];        // Intercambia tambien las posiciones
                A[1][i] = A[1][j];
                A[1][j] = aux;
            }
        }
        A[0][izq] = A[0][j]; // se coloca el pivote en su lugar de forma que tendremos
        A[0][j] = pivote; // los menores a su izquierda y los mayores a su derecha
        
        A[1][izq] = A[1][j]; // Intercambiamos posiciones de acuerdo segun como  
        A[1][j] = pivote1; // cambian los valores
        
        
        if (izq < j - 1) {
            QuickSort(A, izq, j - 1); // ordenamos subarray izquierdo
        }
        if (j + 1 < der) {
            QuickSort(A, j + 1, der); // ordenamos subarray derecho
        }
    }
    
     /**
     * Llena una matriz de indices consecutivos por columna
     * 
     * @param f numero de filas
     * @param c numero de columnas
     * 
     * @return matriz con indices consecutivos
     *       
     */
    public static double [][] fillIndex(int f, int c) {
        
        double[][] index = new double[f][c];
        
        for (int i = 0; i < c; i++) {
             for (int j = 0; j < f; j++) {
               index[j][i] = j+1;
             }
        }
        return index;
    }
    
         /**
     * Ordena los indices de las distancias por columna, DE MENOR A MAYOR
     * 
     * @param A matriz de datos
     * @param Index matriz de indices
     * 
     *       
     */
    public static double [][] SortIndexCol(double A[][]) {
        
        double [][] B = new double[A.length][A[0].length];
        int [] indices;
        
        Matriz.Transpuesta(A);
        
        for (int i = 0; i < A.length; i++) {
     
            SortArrays sortArrays = new SortArrays(A[i]);	        // Creamos el objeto pasandole el array al constructor
            sortArrays = sortArrays.increasingOrderDouble();	// Llamamos al m�todo para que ordene el array de menor a mayor
            indices = sortArrays.getIndexArray();		// Obtenemos los indices del array ordenado
    //        double [] arrayOrdenado = sortArrays.getDoubleArray();	// Obtenemos el array ordenado de doubles de menor a mayor

            for (int j = 0; j < indices.length; j++) {
                B[j][i] = indices[j]+1;
            }
        } 
        return B;
    }
    
     /**
     * Ordena los indices de las distancias por columna con el metodo quickSort, metodo recursivo
     * 
     * @param A matriz de datos
     * @param Index matriz de indices
     * 
     *       
     */
    public static void QuickSortIndexCol(double A[][], double index[][], int izq, int der) {
 
        for (int col = 0; col < A[0].length; col++) {

            double pivote = A[izq][col]; // tomamos primer elemento como pivote

            int i = izq; // i realiza la búsqueda de izquierda a derecha
            int j = der; // j realiza la búsqueda de derecha a izquierda
            double aux;
            double auxIndex;

            while (i < j) {            // mientras no se crucen las búsquedas
                while (A[i][col] <= pivote && i < j) {
                    i++; // busca elemento mayor que pivote
                }
                while (A[j][col] > pivote) {
                    j--;         // busca elemento menor que pivote
                }
                if (i < j) {                      // si no se han cruzado                      
                    aux = A[i][col];                  // los intercambia
                    A[i][col] = A[j][col];
                    A[j][col] = aux;

                    auxIndex = index[i][col];                  // los intercambia
                    index[i][col] = index[j][col];
                    index[j][col] = auxIndex;
                }
            }
            A[izq][col] = A[j][col]; // se coloca el pivote en su lugar de forma que tendremos
            A[j][col] = pivote; // los menores a su izquierda y los mayores a su derecha

            auxIndex = index[izq][col];
            index[izq][col] = index[j][col]; // Intercambiamos posiciones de acuerdo segun como  
            index[j][col] = auxIndex;

            if (izq < j - 1) {
                QuickSortIndexCol(A,index, izq, j - 1); // ordenamos subarray izquierdo
            }
            if (j + 1 < der) {
                QuickSortIndexCol(A,index, j + 1, der); // ordenamos subarray derecho
            }
        }
    }

    public static int BusquedaBinaria(double A[], double dato, int inicio, int fin) {
        int medio;
        if (inicio > fin) {
            return -1;
        } else {
            medio = inicio + (inicio + fin) / 2;
            if (A[medio] < dato) {
                return BusquedaBinaria(A, dato, medio + 1, fin);
            } else if (A[medio] > dato) {
                return BusquedaBinaria(A, dato, inicio, medio - 1);
            }
            else return medio;
        }
    }
    
     public static int BusquedaSecuencial(double A[], double dato) {
         for (int i = 0; i < A.length; i++) {
             if (A[i]== dato) return i;
         }
         
         return -1;// Si no existe el dato
    }

    /**
     * Determina las columnas a tomar para la reducción de dimensiones
     * 
     * @param valores Matriz de EigenValores a analizar
     * @param dimensiones Número de Dimensiones a obtener
     * 
     * @return Arreglo de numeros enteros con las posiciones mayores segun
     *         el numero de dimensiones
     */
    public static int[] ColumnasValores(double[][] valores, int dimensiones) throws Exception {

        double[][] b;// arreglo de valores a ordenar y posiciones original
        int[] resp;
    

        b = new double[2][valores.length]; // Obtener # de filas.
        

        // obtenemos los valores de la Diagonal de la matriz de valores
        for (int i = 0; i < valores.length; i++) {
            b[0][i] = valores[i][i]; // Guardamos los valores
            b[1][i] = i;          // Asiganamos las posiciones   
        }
        // ordenamos el arreglo de valores.
        QuickSort(b, 0, b[0].length - 1);
        
        //Aumentado solo para control
//        c = new double[2][];
//        c[0]=a;
//        c[1]=b;
//        Matriz.EscribirCsv("D:/pp.csv", c);
//        //Find e aumentado
        
        resp = new int[dimensiones];
      
        for (int i = 0; i < dimensiones ; i++) {
            resp[i] = (int)b[1][b[1].length-1-i];
        }
        return resp;
    }
    
     /**
     * Determina las columnas a tomar para la reducción de dimensiones
     * 
     * @param valores Matriz de EigenValores a analizar
     * @param dimensiones Número de Dimensiones a obtener
     * 
     * @return Arreglo de numeros enteros con las posiciones mayores segun
     *         el número de dimensiones
     */
    public static int[] ColumnasValores1(double[][] valores, int dimensiones) throws Exception {

        double[] a;// arreglo de valores a ordenar
        double[] b;// arreglo de valores original
        int[] resp;
    

        a = new double[valores.length]; // Obtener # de filas.
        b = new double[valores.length]; // Obtener # de filas.
        
        // obtenemos los valores de la Diagonal de la matriz de valores
        for (int i = 0; i < valores.length; i++) {
            a[i]=b[i] = valores[i][i];
        }
        // ordenamos el arreglo de valores.
//        QuickSort(a, 0, a.length - 1);
        
        //Aumentado solo para control
//        c = new double[2][];
//        c[0]=a;
//        c[1]=b;
//        Matriz.EscribirCsv("D:/pp.csv", c);
//        //Find e aumentado
        
        resp = new int[dimensiones];
      
        for (int i = 0; i < dimensiones ; i++) {
            resp[i] = BusquedaSecuencial(b, a[a.length-1-i]);
        }
        return resp;
    }

    /**
     * LeerCsv permite leer un archivo .csv y cargar los datos en un arreglo
     * bidimencional tipo double
     *
     * @param nombreArchivo Nombre del archivo a leer, ingresar la ruta
     * completa. Ejemplo: "D:/MatrizDatos.csv"
     * @param fil Ingresar el número de filas a leer en el archivo .csv
     * @param col Ingresar el número de columnas a leer en el archivo .csv
     * @param cabecera true si existe cabezera o false sino existe cabezera de
     * los datos en el archivo .csv
     * @return Matriz double con los datos leidos desde el archivo .csv
     */
    public static double[][] LeerCsv(String nombreArchivo, int fil, int col, boolean cabecera) {
        double[][] datos;

        datos = new double[fil][col];
        try {
            int cont = 0;
            CsvReader matrizImport = new CsvReader(nombreArchivo);

            //Si existe cabeceras usar readHeaders()
            if (cabecera) {
                matrizImport.readHeaders();
            }

            while (matrizImport.readRecord()) {
                for (int i = 0; i < col; i++) {
                    datos[cont][i] = Double.parseDouble(matrizImport.get(i));
                }
                cont++;
            }

            matrizImport.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos;
    }

    /**
     * LeerCsv permite leer un archivo .csv y cargar los datos en un arreglo
     * bidimencional tipo double
     *
     * @param nombreArchivo Nombre del archivo a leer, ingresar la ruta
     * completa. Ejemplo: "D:/MatrizDatos.csv"
     * @param fil Ingresar el número de filas a leer en el archivo .csv
     * @param col Ingresar el número de columnas a leer en el archivo .csv
     * @param cabecera "true" si existe cabezera o false sino existe cabezera de
     * los datos en el archivo .csv
     * @param etiqueta "true" si existe etiquetas para cada una de las filas, en
     * la última columna del archivo .csv
     * @return Matriz double con los datos leidos desde el archivo .csv
     */
    public static Object[][] LeerCsvObject(String nombreArchivo, int fil, int col, boolean cabecera, boolean etiqueta) {
        Object[][] datos;

        if (etiqueta) {
            datos = new Object[fil][col + 1];
        } else {
            datos = new Object[fil][col];
        }
        try {
            int cont = 0;
            int i;
            CsvReader matrizImport = new CsvReader(nombreArchivo);

            //Si existe cabeceras usar readHeaders()
            if (cabecera) {
                matrizImport.readHeaders();
            }

            while (matrizImport.readRecord()) {
                for (i = 0; i < col; i++) {
                    datos[cont][i] = Double.parseDouble(matrizImport.get(i));
                }
                if (etiqueta) {
                    datos[cont][i] = matrizImport.get(i);
                }
                cont++;
            }

            matrizImport.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public static boolean EscribirCsv(String archivoSalida, double[][] datos, int numeroDecimales) throws Exception {
        return EscribirCsv(archivoSalida, datos, numeroDecimales, null);
    }

    public static boolean EscribirCsv(String archivoSalida, double[][] datos) throws Exception {
        return EscribirCsv(archivoSalida, datos, -1, null);
    }

    public static boolean EscribirCsv(String archivoSalida, double[][] datos, String[] cabeceras) throws Exception {
        return EscribirCsv(archivoSalida, datos, -1, cabeceras);
    }

    /**
     * EscribirCsv permite almacenar un arreglo bidimencional tipo double en un
     * archivo .csv
     *
     * @param archivoSalida Nombre del archivo en que se va a escribir el
     * arreglo, ingresar la ruta completa. Ejemplo: "D:/MatrizDatos.csv"
     * @param datos Arreglo bidimensional tipo double con los datos a escribir.
     * @param numeroDecimales Redondear a numeroDecimales los valores double.
     * Poner -1 para no redondeo.
     * @param cabeceras Arreglo de cabeceras de ser necesario escribir un
     * archivo .csv con cabeceras. Si no se desea cabeceras, especificar null.
     * @return true si la escritura del archivo tuvo éxito. SI fallo retorna
     * false.
     */
    public static boolean EscribirCsv(String archivoSalida, double[][] datos, int numeroDecimales, String[] cabeceras) throws Exception {
        boolean alreadyExists = new File(archivoSalida).exists();

        if ((cabeceras != null) && (datos[0].length != cabeceras.length)) {
            throw new Exception("Error : Número de cabeceras incompatible con número de columnas...");
        }
        if (alreadyExists) {
            File ArchivoEmpleados = new File(archivoSalida);
            ArchivoEmpleados.delete();
        }

        try {

            CsvWriter csvOutput = new CsvWriter(new FileWriter(archivoSalida, true), ',');

            if (cabeceras != null) {
                for (String cabecera : cabeceras) {
                    csvOutput.write(cabecera);
                }
                csvOutput.endRecord();
            }

            for (double[] dato : datos) {

                for (double d : dato) {
                    if (numeroDecimales >= 0) {
                        csvOutput.write(String.valueOf(redondearDecimales(d, numeroDecimales)));
                    } else {
                        csvOutput.write(String.valueOf(d));
                    }

                }
                csvOutput.endRecord();
            }
            csvOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * *
     * Imprimir un AbstractTableModel en consola.
     *
     * @param data AbstractTableModel a imprimir
     * @param tamano Espacio reservado para imprimir cada dato. Formateo de
     * impresión
     * @param decimales Numero de decimales de los datos que se imprimen
     */
    public static void Imprimir(AbstractTableModel data, int tamano, int decimales) {
        int y;
        Object dato;

        for (int x = 0; x < data.getRowCount(); x++) {
            for (y = 0; y < data.getColumnCount(); y++) {
                dato = data.getValueAt(x, y);
                if (dato instanceof Double) {
                    System.out.printf("%" + tamano + "." + decimales + "f", data.getValueAt(x, y));
                }
                if (dato instanceof String) {
                    System.out.print("  " + data.getValueAt(x, y));
                }
            }
            System.out.println("");
        }
    }

    /**
     * *
     * Imprimir un AbstractTableModel en consola.
     *
     * @param data AbstractTableModel a imprimir
     */
    public static void Imprimir(AbstractTableModel data) {
        Imprimir(data, 10, 4);
    }

    /**
     * *
     * Imprimir un arreglo tipo double[][] en consola.
     *
     * @param datos double[][] a imprimir
     * @param tamano Espacio reservado para imprimir cada dato. Formateo de
     * impresion
     * @param decimales Numero de decimales de los datos que se imprimen
     */
    public static void Imprimir(double[][] datos, int tamano, int decimales) {
        for (int x = 0; x < datos.length; x++) {
            System.out.printf("%2d",x+1);
            for (int y = 0; y < datos[x].length; y++) {
                System.out.printf("%" + tamano + "." + decimales + "e", datos[x][y]);
            }
            System.out.println("");
        }
    }

    /**
     *
     * Imprimir un arreglo tipo double[][] en consola.
     *
     * @param datos double[][] a imprimir
     */
    public static void Imprimir(double[][] datos) {
        Imprimir(datos, 15, 4);
    }
    
    //////JCAP
    
    /**
     *
     * @param matriz: matriz original a la que se le sacara la matriz de covarianza
     * @return
     */
    public double[][] getMatrizCovarianza(double[][] matriz){

        int m = matriz.length;
        int n = matriz[0].length;
        
        double[] mud = Utils.MachineLearning.math.math.Math.colMean(matriz); // se obtiene las medias
        double[][] x = Utils.MachineLearning.math.math.Math.clone(matriz); // se clona la matriz
        for (int i = 0; i < m; i++) { //para todas las filas
            for (int j = 0; j < n; j++) { // para todas las columnas
                x[i][j] = matriz[i][j] - mud[j]; //para cada valor se resta la media de su fila
            }
        }  

        //una forma de calcular es A*At(t es transpueta)
        double[][] cov = new double[n][n];
        for (int t = 0; t < m; t++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    cov[i][j] += x[t][i] * x[t][j];
                }
            }
        }
        
        // divide por 1/n-1
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                cov[i][j] /= m; // divide m instead of m-1 for S-PLUS compatibilit
                cov[j][i] = cov[i][j];
            }
        }
        
        return cov;
    }
    
     /**
     * *
     * Genera una matriz de distancia.
     *
     * @param matriz double[][] sobre la que se quiere generar la matriz de distancia
     * @return matriz de distancia
     */
    public static double[][] GetMatrizDistancia(double[][] matriz){
        int n = matriz[0].length;
        matriz = Utils.MachineLearning.math.math.Math.transpose(matriz);
        double[][] pro = new double[n][n];
            
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) { 
                pro[i][j] = Utils.MachineLearning.math.math.Math.distance(matriz[i], matriz[j]);
            }
        }
        return pro;
    }
    
    public static double[][] Suma(double[][] m1, double[][] m2) {
        double[][] resp;

        resp = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                resp[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return resp;
    }
    
    public static int[] getColsMaxEigValues(double[][] valores, int dimensiones) {

        double[] maxVls = new double[dimensiones];;
        int [] pos = new int[dimensiones];
        
        //los valores se inician con una posicion inicial 
        for(int j=0; j<dimensiones; j++){    
            maxVls[j] =  valores[0][0];
        }

        // obtenemos los valores de la Diagonal de la matriz de valores
        for (int i = 1; i < valores.length; i++) {
            
            for(int k=0; k<dimensiones; k++){   
            
                if(maxVls[k] < valores[i][i]){
                    // recorre y baja una posicion en el vector
                    for(int j=k; j<dimensiones; j++){
                        if(j+1<dimensiones){
                            maxVls[j+1] = maxVls[j];
                            pos[j+1] = pos[j];
                        }
                    }
                    maxVls[k] = valores[i][i];
                    pos[k] = i;
                    break;
                }
            }
        }
      
        return pos;
    }
           
 ////// fin JCAP   
    
    /**
     *
     * Elevar cada elemento de la matriz m1 al exponente exp.
     *
     * @param m1 double[][] matriz
     * @param exp int exponente
     */
    public static double[][] PotenciaEbyE(double[][] m1, double exp) {
        double[][] r =new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                r[i][j] = Math.pow(m1[i][j],exp);
            }
        }        
        return r;        
    }
    
    
    public static double[] Diagonal(double [][] m1){
        double[]diag=new double[m1.length];
        for (int i = 0; i < m1.length; i++) {
           diag[i]=m1[i][i];
        }
        return diag;
        
    }
    
    public static double Trace(double [][] m1){
        double out = 0;  
        if(m1.length == m1[0].length){
            for (int i = 0; i < m1.length; i++) {
                out+=m1[i][i];
            }
            return out;
        }else{
            System.out.println("La matriz debe ser cuadrada");
            return 0;
        }   
    }
    
    public static double SumaCol(double [][] m1){
        double sum =0;
        for (int i = 0; i < m1.length; i++) {
            sum += m1[i][0];
        }
        return sum;
        
    }
    
    public static void Normalizar(double[][] A){
         double[] maximos;
         maximos = new double[A[0].length];
         maximos=A[0].clone();
        // System.out.println("maximos  "+maximos);
        // System.out.println("A[0]"+A[0]);
         for (int i = 1; i < A.length; i++) {
             for (int j = 0; j < A[0].length; j++) {
                 if (Math.abs(A[i][j]) > maximos[j]) maximos[j]=Math.abs(A[i][j]);
             }
         }
         
         
         for (int i = 0; i < A.length; i++) {
             for (int j = 0; j < A[0].length; j++) {
                 A[i][j] = A[i][j]/maximos[j];
             }
         }
     }
    
     /**
     * Genera una matriz de ranking discreta respecto a las distancias
     * 
     * @param A Matriz ordenada de indices
     * 
     * @return matriz de Ranking
     *       
     */
    public static double[][] GetRanking(double[][] A){
        double[][] ranking = new double[A.length][A[0].length];
        int aij;
      
        for (int j = 0; j < A[0].length; j++) {
            for (int i = 0; i < A.length; i++) {
                aij = (int)A[i][j];
                ranking[aij-1][j] = i+1; // le resto 1 porque en la funcion fillIndex lo inicio desde 1 y no desde 0 como en matlab
            }
        }
        return ranking;
    }
    
    /**
     * Limpia un vector
     * 
     * @param A vector
     * 
     * @return verctor lleno de ceros
     *       
     */
    public static void Clean(int A[]) {
         for (int i = 0; i < A.length; i++) {
                A[i] = 0; // le resto 1 porque en la funcion fillIndex lo inicio desde 1 y no desde 0 como en matlab
            }
    }
    
    
   public static double[][] Centrar(double[][] Xin) {

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
        
        return X;
    }
}
