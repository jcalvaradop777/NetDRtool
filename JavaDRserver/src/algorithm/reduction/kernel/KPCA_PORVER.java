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
public class KPCA_PORVER {   

//int grafx=80,grafy=150,grafsw=450,grafsh=450; // constantes que definen la posicion y el tama\u00f1o del plano cartesiano
//int ds=230,m,n,bandera=0,lol=0; // variables que definen  el desfase de los botones con respecto al tam\u00f1o de la ventana,numero de filas y columnas de la base de datos y bandera para dibujar el logo  
//float [][] K1,K2,K3,Xe,Xs,KM;//se definen las matrices de los kernels (Kn) y las bases de datos Xe (Rollo suizo) y Xs ()
//float [] diagonal,diagonal_o,EMB_x,EMB_y;// se definen los vectores de la diagonal principal y los dos componentes principales del espacio embebido
//float maxc1,maxc0,minc1,minc0; // se inicializa el valor maximos y minimos de los axes 

// esto toca ver si va
//float alpha1=270,xd,yd,zd; //  se define la ganacia para la representacion 3D de la esfera y el rollo suizo  
//float [] X_a,Y_a,Z_a;      //  se define los vectores de el rollo suizo y la esfera originales
//float [] X_t,Y_t,Z_t;      //  se define los vectores de el rollo suizo y la esfera (menos el valor minimo de cada columna)
//float [] X_n,Y_n,Z_n;      //  se define los vectores de el rollo suizo y la esfera (normalizados)

int n,m;
float [] diagonal,diagonal_o,EMB_x,EMB_y;
    
public KPCA_PORVER(Table K1t, Table K2t, Table K3t, float R, float G, float B) throws Exception {
    KPCA(K1t, K2t, K3t, R, G, B);
}

public void KPCA (Table K1t, Table K2t, Table K3t, float R, float G, float B){
   
        float[][] K1 = Tab2mat(K1t);
        float[][] K2 = Tab2mat(K2t);
        float[][] K3 = Tab2mat(K3t);
        
        float [][] K1p = consMA(K1,R); // se multiplica el kernel por el factor 1
        float [][] K2p = consMA(K2,G); // se multiplica el kernel por el factor 2
        float [][] K3p = consMA(K3,B); // se multiplica el kernel por el factor 3
        
        float [][] KM = Mat.sum(K1p,K2p);  // se suman las matrices Kernel
        KM=Mat.sum(KM,K3p);   // se suman las matrices Kernel
        
        Eigenvalue eigen = new Eigenvalue(KM); // se calculan los eigen valores y eigen vectores de la matriz kernel final 
        float[][] V = Cast.doubleToFloat(eigen.getV()); // se guardan los eigen  vectores
        float[][] D = Cast.doubleToFloat(eigen.getD()); // se guardan los eigen valores
        int N=D.length; // el largo de la diagonal principal
        
       diagonal= new float [N]; // se inicializan dos vectores que contienen la diagonal principal
       diagonal_o= new float [N]; // se inicializan dos vectores que contienen la diagonal principal

       for (int i=0;i<N;i++){
         diagonal[i]= D[i][i];  // se guarda la diagonal principal
       }

       diagonal_o=sort(diagonal); // se ordena la la diagonal principal de menor a mayor
       diagonal_o=reverse(diagonal_o);// se reordena la diagonal principal de mayor a menor  
       int[] poss= new int [2]; // se inicializa la posicion de los dos eigenvectores con mayor eigenvalor    

       for(int i=0;i<2;i++){  // se busca y se guarda la poscion. 
          poss[i]=find(diagonal,diagonal_o[i]); // encuentra la posicion de los eigen vectores de mayor influencia
       }

       EMB_x=new float [m]; // se define el vector con los datos KPC1  
       EMB_y=new float [m]; // se define el vector con los datos KPC2

       for(int i=0;i<m;i++){  
          EMB_x[i]=V[i][poss[0]]; // datos embebidos
          EMB_y[i]=V[i][poss[1]]; // datos embebidos
       }
}
   
//funcion para pasar una tabla a una matriz
public float [][] Tab2mat(Table tab){   
  m=tab.getRowCount(); // numero de filas del archivo csv
  n=tab.getColumnCount(); // numero de columnas del archivo csv
  float[][] data= new float [m][n]; // matriz de datos
  for(int j=0;j<n;j++) // maneja las columnas 
   { 
     for(int i=0;i<m;i++) // maneja las filas
       { 
        data [i][j]=tab.getFloat(i,j); // se arda en la matris Data 
       }     
   } 
   return data; // retorna la matriz data 
}

//se multiplica el kernel por el factor
public float [][] consMA (float [][] Matri ,float alpha){
   float[][] alpX= new float [m][n];
   
   for (int j=0;j<n;j++){
      for (int i=0;i<m;i++){
        alpX[i][j]=alpha*Matri[i][j];
     } 
   }
   return alpX;
}

public int find(float array [],float value){ 
   int  consid=0;
  
   for(int i=0;i<array.length;i++){
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
        
        
}
