/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.kernel;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
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
public class Kmix {   

int f,c; //filas columnas

Eigenvalue eigen;
float[] diagonal, diagonal_o;
float[][] EMB; //matriz de incrustramiento, datos reducidos a d dimensiones
float[][] V; // eigen vectores
float[][] D; // eigen valores

public Kmix(double[][] dataValuesK1, double[][] dataValuesK2, float R,  float B, int d){
   
//        float[][] K1 = matDouble2Float(dataValuesK1);
//        float[][] K2 = matDouble2Float(dataValuesK2);

        f = dataValuesK1.length; 
        c = dataValuesK1[0].length;
        
        float[][] K1 = Cast.doubleToFloat(dataValuesK1);
        float[][] K2 = Cast.doubleToFloat(dataValuesK2);
        
        float [][] K1p = prodPesos(K1,R); // se multiplica el kernel por el factor 1
        float [][] K2p = prodPesos(K2,B); // se multiplica el kernel por el factor 2
        
        float [][] KM = Mat.sum(K1p,K2p);  // se suman las matrices Kernel
        
        eigen = new Eigenvalue(KM); // se calculan los eigen valores y eigen vectores de la matriz kernel final 
        V = Cast.doubleToFloat(eigen.getV()); // se guardan los eigen  vectores
        D = Cast.doubleToFloat(eigen.getD()); // se guardan los eigen valores
        int N = D.length; // el largo de la diagonal principal
        
       diagonal= new float [N]; // se inicializan dos vectores que contienen la diagonal principal
       diagonal_o= new float [N]; // se inicializan dos vectores que contienen la diagonal principal

       for (int i=0;i<N;i++){
         diagonal[i]= D[i][i];  // se guarda la diagonal principal
       }

       diagonal_o=sort(diagonal); // se ordena la la diagonal principal de menor a mayor
       diagonal_o=reverse(diagonal_o);// se reordena la diagonal principal de mayor a menor  
       int[] poss = new int [d]; // se inicializa la posicion de los eigenvectores con mayor eigenvalor    

       for(int i=0;i<d;i++){  // se busca y se guarda la poscion. 
          poss[i]=find(diagonal, diagonal_o[i]); // encuentra la posicion de los eigen vectores de mayor influencia
       }

       EMB = new float[f][d]; // se define el vector con los datos KPC1  
       for(int i=0; i<f; i++){  
           for(int j=0; j<d; j++){  
              EMB[i][j]=V[i][poss[j]]; // datos embebidos
           }
       }
}

public Kmix(double[][] dataValuesK1, double[][] dataValuesK2, double[][] dataValuesK3, float R, float G, float B, int d){
   
        f = dataValuesK1.length; 
        c = dataValuesK1[0].length;
        
        float[][] K1 = Cast.doubleToFloat(dataValuesK1);
        float[][] K2 = Cast.doubleToFloat(dataValuesK2);
        float[][] K3 = Cast.doubleToFloat(dataValuesK3);
        
        float [][] K1p = prodPesos(K1,R); // se multiplica el kernel por el factor 1
        float [][] K2p = prodPesos(K2,G); // se multiplica el kernel por el factor 2
        float [][] K3p = prodPesos(K3,B); // se multiplica el kernel por el factor 3
        
        float [][] KM = Mat.sum(K1p,K2p);  // se suman las matrices Kernel
        KM=Mat.sum(KM,K3p);   // se suman las matrices Kernel
        
        eigen = new Eigenvalue(KM); // se calculan los eigen valores y eigen vectores de la matriz kernel final 
        V = Cast.doubleToFloat(eigen.getV()); // se guardan los eigen  vectores
        D = Cast.doubleToFloat(eigen.getD()); // se guardan los eigen valores
        int N=D.length; // el largo de la diagonal principal
        
       diagonal= new float [N]; // se inicializan dos vectores que contienen la diagonal principal
       diagonal_o= new float [N]; // se inicializan dos vectores que contienen la diagonal principal

       for (int i=0;i<N;i++){
         diagonal[i]= D[i][i];  // se guarda la diagonal principal
       }

       diagonal_o=sort(diagonal); // se ordena la la diagonal principal de menor a mayor
       diagonal_o=reverse(diagonal_o);// se reordena la diagonal principal de mayor a menor  
       int[] poss= new int [d]; // se inicializa la posicion de los dos eigenvectores con mayor eigenvalor    

       for(int i=0;i<d;i++){  // se busca y se guarda la poscion. 
          poss[i]=find(diagonal,diagonal_o[i]); // encuentra la posicion de los eigen vectores de mayor influencia
       }

       EMB = new float[f][d]; // se define el vector con los datos KPC1  
       for(int i=0; i<f; i++){  
           for(int j=0; j<d; j++){  
              EMB[i][j] = V[i][poss[j]]; // datos embebidos
           }
       }
}

//se multiplica el kernel por el factor
public float [][] prodPesos (float [][] Matri ,float alpha){
   float[][] alpX= new float [f][c];
   
   for (int j=0;j<c;j++){
      for (int i=0;i<f;i++){
        alpX[i][j]=alpha*Matri[i][j];
     } 
   }
   return alpX;
}

public int find(float array [], float value){ 
   int  consid=0;
  
   for(int i=0; i<array.length; i++){
      if(array[i]==value){
         consid = i;
      } 
   }
 return consid;
}

//public void norm_d (Table TXn){
//   int kx=0,ky=1,kz=2;
//   
//   if (bandera==2){ kx=1;ky=0;kz=2; }
//  
//   X_a=new float [m];
//   Y_a=new float [m];
//   Z_a=new float [m];
//  
//   X_t=new float [m];
//   Y_t=new float [m];
//   Z_t=new float [m];
//  
//   X_n=new float [m];
//   Y_n=new float [m];
//   Z_n=new float [m];
//  
//  for (int i=0; i<m; i++) {
//    X_a[i]=TXn.getFloat(i,kx);
//    Y_a[i]=TXn.getFloat(i,ky);
//    Z_a[i]=TXn.getFloat(i,kz);
//  }
//  
//  for (int i=0; i<m; i++) {
//    X_t[i]=X_a[i]-min(X_a);
//    Y_t[i]=Y_a[i]-min(Y_a);
//    Z_t[i]=Z_a[i]-min(Z_a);
//  }
//  
//  for (int i=0; i<m; i++) {
//    X_n[i]=alpha1*((X_t[i]/max(X_t))+1);
//    Y_n[i]=alpha1*((Y_t[i]/max(Y_t))+1);
//    Z_n[i]=alpha1*((Z_t[i]/max(Z_t))+1);
//  }
//
//   xd=(max(X_n)-min(Y_n))/2;
//   yd=(max(Y_n)-min(Y_n))/2;
//   zd=(max(Z_n)-min(Y_n))/2;
//}  
       
public double[][] getLowMatrix(){
    double[][] EMBd = Cast.floatToDouble(EMB);
    return EMBd;
}

public double[][] getEigenVectores(){
    return eigen.getV();
}

public double[][] getEigenValores(){
    return eigen.getD();
}
        
}
