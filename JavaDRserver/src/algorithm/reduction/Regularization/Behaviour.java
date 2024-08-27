/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.Regularization;

import algorithm.reduction.kernel.Matriz;

/**
 *
 * @author Juan Carlos Alvarado
 */
public class Behaviour {

    private double[][] YdrBeahaviour;
               
    public Behaviour(double mu,double[][] X,double[][] Yr) {
       
       Matriz.Normalizar(X);
       Matriz.Normalizar(Yr);
       
        double[][] DX =  Matriz.GetMatrizDistancia( Matriz.Transpuesta(X));
        double[][] sortIndexDX = Matriz.SortIndexCol(DX);
//        double[][] sortIndexDX = Matriz.fillIndex(DX.length, DX[0].length);
//        Matriz.QuickSortIndexCol(DX,sortIndexDX,0,DX.length-1); // se ordena los indices según las distancias en sortIndexDX
        double[][] rankingX = Matriz.GetRanking(sortIndexDX);
                
        double[][] DYr =  Matriz.GetMatrizDistancia( Matriz.Transpuesta(Yr));
        double[][] sortIndexDYr = Matriz.SortIndexCol(DYr);
//        double[][] sortIndexDYr = Matriz.fillIndex(DYr.length, DYr[0].length);
//        Matriz.QuickSortIndexCol(DYr,sortIndexDYr,0,DYr.length-1); // se ordena los indices según las distancias en sortIndexDX
        double[][] rankingYr = Matriz.GetRanking(sortIndexDYr);
        
        double[][] rankBehavior = Matriz.Resta(rankingX, rankingYr); // matriz de rankin LD - matriz de ranking HD, Intrusiones (valores positivos) y Extrusiones (valores negativos) para estar acorde a la literatura
     
        
        // COMO QUE SE DEBE NORMALIZAR TODO KERNEL X y rankBehavior 
        
       Matriz.Normalizar(rankBehavior);

        // DRlg = ((kerneRD*kerneRD')+(mu*rankAltaMenosBaja))*kerneRD; %
         YdrBeahaviour = Matriz.Multiplicar( Matriz.Suma( (Matriz.Multiplicar(Yr,Matriz.Transpuesta(Yr)) ),(Matriz.ProductoEscalar(rankBehavior,mu)) ), Yr); 
 }

    public double[][] getNewCoordinates(){
        return YdrBeahaviour;
    }

}
