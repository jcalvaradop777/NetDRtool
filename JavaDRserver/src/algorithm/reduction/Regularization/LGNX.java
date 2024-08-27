/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.reduction.Regularization;

import SortArrays.SortArrays;
import algorithm.reduction.kernel.Matriz;

/**
 *
 * @author Juan Carlos Alvarado
 */
public class LGNX {

    float mu;
    String levelDepthReg;
    double valDepthReg;
    private double[][] Yr;
    private double[][] YdrBari;
    boolean isNXseparated;
            
    public LGNX(float mu,double[][] X,double[][] Yr, boolean isNXseparated, String levelDepthReg, double valDepthReg) {
        
        this.mu = mu;
        this.levelDepthReg = levelDepthReg;
        this.valDepthReg = valDepthReg;
        this.Yr = Yr;
        this.isNXseparated = isNXseparated;
        
//        Matriz.Normalizar(X);
//        Matriz.Normalizar(Yr);
        
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
   
        if(isNXseparated){
            BaricentroNOX(rankBehavior);
        }else{
            BaricentroNYX(rankBehavior);
        }
 }
     /**
     * 
     * Genera una matriz con las nuevas coordenadas producto del calculo de baricentros de puntos extrusos O intrusos por separado
     * 
     * @param rankBehavior Matriz de diferencias N-X
     *       
     */
    public void BaricentroNOX(double[][] rankBehavior){
        
        //Calcula el baricentro de cada punto acercandolo a los puntos de los que fue extrusado
//        YdrBari = Yr; //YrBe Nueva matriz con los baricentro calculados, inicialmente recibe las coordenadas originales
       
        YdrBari = Matriz.Clonar1(Yr);
        int[]ptsConNoX = new int[rankBehavior.length];
        int conNoX,denNoX;  
        double nuevaCor;
        
        for (int f = 0; f < rankBehavior.length; f++) { 
  
            //Utiliza la matriz rankBehavior
            Matriz.Clean(ptsConNoX); // limpia el vector   

            ptsConNoX[0] = f; // Se tienen encuenta las coordenadas del mismo punto
            conNoX = 1; // sino se quiere tener las coordenadas del mismo punto "conNoX" se debería iniciar en 0
            denNoX = 0; // denominador (suma por fila de extrusiones)     
            
            // se busca las incidencias de intrusiones o extrusiones con el punto en cuestión es decir, por ejemplo, para el pto 1 se busca cuanto se acerco o lejo del punto 1, 2, 3 etc.... y asi con todos los puntos pues se recorre la matriz de ranking que es cuadrada ya que es de dimension puntos X puntos
            for (int c = 0; c < rankBehavior[0].length; c++) { // para cada columna

                if (mu > 0){ // Interpolacion, lo que quiero es acercar el punto por que estaba alejado en RD
                    if (rankBehavior[f][c] < 0){ // si es una extrusion
                       ptsConNoX[conNoX] = c; // toma los indices de los puntos donde exite extrusion
                       conNoX = conNoX+1; // solo cuenta las apariciones sin importar la magnitud
                       denNoX = denNoX + (int)Math.abs(rankBehavior[f][c]); // es el denominador que acumula la suma de los extrusos, (acumula toda la magnitud, por ejemplo si el punto se alejo 174 acumula este valor con todas las demás magnitudes)
                    }

                }else if (mu < 0){ // Extrapolacion, se quiere alejar el punto que estba como intruso. 
                    if (rankBehavior[f][c] > 0) { // si es una intrusion
                       ptsConNoX[conNoX] = c; // toma los indices de los puntos donde exite intrusion
                       conNoX = conNoX+1; // solo cuenta las apariciones sin importar la magnitud
                       denNoX = denNoX + (int)rankBehavior[f][c]; // es el denominador que acumula la suma de los intrusos(acumula toda la magnitud, por ejemplo si el punto se acercó en 174 acumula este valor con todas las demás magnitudes)
                    }
                }
            }
    
        //CALCULO DE NUEVAS COORDENADAS
        
        // levelDepthReg es el denominador que divide a cada una de las coorenadas en cuestión. Es un parametro que sirve para controlar la profundidad de la regulariazación. Desde la interfaz grafica (cofigureLGNX) el usuario puede 
        // seleccionar el nivel de profundidad que puede ser leve, moderada o alta (si selecciona modo automatico). Si selecciona modo manual, el usuario por medio de 
        // un slider puede seleccionar un valor etre 1 y 100.000.
        if(levelDepthReg.compareTo("Slight")==0){
            this.valDepthReg = 1;  // nivel leve (Slight) significa que el denominador levelDepthReg es igual a 1, dejando el valor rankBehavior[f][ptsConNoX[s]]) sin modificación, es decir toma tal cual el valor de la diferencia de ranking en dicho punto
        }else if(levelDepthReg.compareTo("Middle")==0){
            this.valDepthReg = conNoX; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada
        }else if(levelDepthReg.compareTo("High")==0){
            this.valDepthReg = denNoX; // nivel alto (High) significa que el denominador levelDepthReg es igual al conteo total de denNoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre denNoX, es como hacer un promedio (valor ponderado) para cada coordenada, pero con el total de extrusione o intrusiones  
        }
        
        // Utiliza la matriz Ydr, suma todos las cordenadas (por columna) que correspoden a los puntos con extrusion
        if(conNoX > 1){ // Si encontró alguna intrusion o extrusion entonces ingresa, si Contador de NOX mayor que 1

            for (int t = 0; t < Yr[0].length; t++) {// recorre las columnas de Ydr para sumar las coordenadas respectivas
            
                nuevaCor = 0;
                
                //ptsConNoX.length
                //toma los puntos en donde hubo NoX, de cada punto toma la misma columna (que representa una coordenada) y recalcula la nueva coordenada mediante interpolación homotopica, incluyendo su respectiva coordenada original en Yr. Esto lo hace con cada coordenada
                for (int s = 0; s < conNoX; s++) {// recorre el vector de puntos con extrusion, que contiene la fila correspondiente al punto

                    // nuevaCor = nuevaCor + (Ydr(ptsConNoX(s),t)); % si se quisiera que los puntos de influencia tuvieran el mismo peso

                    // Se regulariza el acercamiento al punto original RD (Yr): Se toma cordenada por cordenada (x, y, ...) de todos los puntos de influencia (con N o X) respecto a un punto en particular (determinado por el recorrido de filas), con lo cual se calcula la nueva coordenada baricentrica
                   
                    //segunda parte homotopica
                    if(ptsConNoX[s] == f){// representa la cordenada original (en Yr) con la   que es ponderada con el resto de la función homotopica (1-mu)
                       // (mu/abs(mu))*(1-abs(mu)) permite encontrar el complemento del peso mu al restar de 1 el valo absoluto de mu (de lo contrario, si mu fuera negativo en lugar de positivo, lo restaria). mu/abs(mu) Permite conservar el signo de mu y con esto se en que intervalo de la recta de centros esta (negativa o positiva)
                       nuevaCor = nuevaCor + ( (mu/Math.abs(mu))*(1-Math.abs(mu)) * (Yr[ptsConNoX[s]][t]) ); // Cuando se pondera el punto original, cuyo factor de ponderación es suministrado por el usuario (mu) es como la ponderacion homotopica
                       // nuevaCor = nuevaCor + ( (mu/abs(mu))*( (1-exp(1-mu))/1-exp(1) )*(Ydr(ptsConNoX(s),t)) ); % Es otra forma de homotopia (Diego)
                    // primera parte homotopica
                    }else{ // representa el resto de cordenadas de los puntos de influencia, se pondera  mu en la función homotopica
                       // De los puntos que inciden (extrusos o intrusos), se multiplican sus respectivos valores de la matriz RD (Ydr) por sus respectivos pesos (rankBehavior/denNox), los cuales son regularizados por un factor de control suministrado por el usuario (controlE = control de ponderación de acercamiento al punto original en presencia de extrusos)
                       // 1-controlE significa que el resto de puntos extrusos incidentes se reparten el excedente de probabilidad otorgada por el control de usuario
                       nuevaCor = nuevaCor + (  ( mu * (Math.abs(rankBehavior[f][ptsConNoX[s]])/valDepthReg) ) * ( Yr[ptsConNoX[s]][t] )); // Se suma acumulativamente los valores de la componente (X, Y, Z, etc...) multiplicado por su respectivo peso, en los distintos puntos
//                      parece que al quitar el denNox se soluciona el problema
                       //nuevaCor = nuevaCor + ( ( ( (1-exp(mu))/1-exp(1) ) * (abs(rankAltaMenosBaja(f,ptsConNoX(s)))/denNoX) ) * (Ydr(ptsConNoX(s),t)) ); Es otra forma de homotopia (Diego)
                       // PROBAR CON LA MATRIZ LD ORIGINAL (Ydr) Y CON LA QUE VA QUEDANDO CON BARICENTROS (YrBari)
                    } 
                }
                // YdrBari(f,t) = nuevaCor/size(Ydr,2); % la nueva cordenada calculada con baricentro con igual peso la hubico en la matriz de datos reducida
                 
                YdrBari [f][t] = nuevaCor;
                }
            }
        }    
    }
    
     /**
     * 
     * Genera una matriz con las nuevas coordenadas producto del calculo de baricentros de puntos extrusos Y intrusos al mismo tiempo
     * 
     * @param rankBehavior Matriz de diferencias N-X
     *       
     */
    public void BaricentroNYX(double[][] rankBehavior){
        
        //Calcula el baricentro de cada punto acercandolo a los puntos de los que fue extrusado
//        YdrBari = Yr; //YrBe Nueva matriz con los baricentro calculados, inicialmente recibe las coordenadas originales
        double valDepthRegN = 1; // Denominadores de división, profundidad de la N o X
        double valDepthRegX = 1;
       
        YdrBari = Matriz.Clonar1(Yr);
        
        int[]ptsConN = new int[rankBehavior.length];
        int conN,denN;  
        
        int[]ptsConX = new int[rankBehavior.length];
        int conX,denX;  
        
        double nuevaCor;
        
        for (int f = 0; f < rankBehavior.length; f++) { 
  
            //Utiliza la matriz rankBehavior
            Matriz.Clean(ptsConN); // limpia el vector   
            
            ptsConN[0] = f; // Se tienen encuenta las coordenadas del mismo punto
            conN = 1; // sino se quiere tener las coordenadas del mismo punto "conNoX" se debería iniciar en 0
            denN = 0; // denominador (suma por fila de extrusiones)     
            
            Matriz.Clean(ptsConX);
            ptsConX[0] = f; // Se tienen encuenta las coordenadas del mismo punto
            conX = 1; // sino se quiere tener las coordenadas del mismo punto "conNoX" se debería iniciar en 0
            denX = 0; // denominador (suma por fila de extrusiones)     
            
            // se busca las incidencias de intrusiones o extrusiones con el punto en cuestión es decir, por ejemplo, para el pto 1 se busca cuanto se acerco o lejo del punto 1, 2, 3 etc.... y asi con todos los puntos pues se recorre la matriz de ranking que es cuadrada ya que es de dimension puntos X puntos
            for (int c = 0; c < rankBehavior[0].length; c++) { // para cada columna

                if (rankBehavior[f][c] < 0){ // si es una extrusion
                   ptsConN[conN] = c; // toma los indices de los puntos donde exite extrusion
                   conN = conN+1; // solo cuenta las apariciones sin importar la magnitud
                   denN = denN + (int)Math.abs(rankBehavior[f][c]); // es el denominador que acumula la suma de los extrusos, (acumula toda la magnitud, por ejemplo si el punto se alejo 174 acumula este valor con todas las demás magnitudes)
                }

               if (rankBehavior[f][c] > 0) { // si es una intrusion
                   ptsConX[conX] = c; // toma los indices de los puntos donde exite intrusion
                   conX = conX+1; // solo cuenta las apariciones sin importar la magnitud
                   denX = denX + (int)rankBehavior[f][c]; // es el denominador que acumula la suma de los intrusos(acumula toda la magnitud, por ejemplo si el punto se acercó en 174 acumula este valor con todas las demás magnitudes)
                }
            }
    
        //CALCULO DE NUEVAS COORDENADAS
        
        // levelDepthReg es el denominador que divide a cada una de las coorenadas en cuestión. Es un parametro que sirve para controlar la profundidad de la regulariazación. Desde la interfaz grafica (cofigureLGNX) el usuario puede 
        // seleccionar el nivel de profundidad que puede ser leve, moderada o alta (si selecciona modo automatico). Si selecciona modo manual, el usuario por medio de 
        // un slider puede seleccionar un valor etre 1 y 100.000.
        if(levelDepthReg.compareTo("None")==0){ // Cuando en la interfaz se decidio el valor de la profundidad manualmente (de 1 a 100.000)
            valDepthRegN = valDepthReg; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada
            valDepthRegX = valDepthReg;
        }else if(levelDepthReg.compareTo("Slight")==0){
            valDepthRegN = 1; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada
            valDepthRegX = 1;
        }else if(levelDepthReg.compareTo("Middle")==0){
            valDepthRegN = conN; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada
            valDepthRegX = conX; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada    
        }else if(levelDepthReg.compareTo("High")==0){
            valDepthRegN = denN; // nivel medio (middle) significa que el denominador levelDepthReg es igual al conteo de NoX, al dividir rankBehavior[f][ptsConNoX[s]]) entre conNoX, es como hacer un promedio (valor ponderado) para cada coordenada
            valDepthRegX = denX;
        }
        
        // Utiliza la matriz Ydr, suma todos las cordenadas (por columna) que correspoden a los puntos con extrusion
        if(conN > 1){ // Si encontró alguna intrusion o extrusion entonces ingresa, si Contador de NOX mayor que 1

            for (int t = 0; t < Yr[0].length; t++) {// recorre las columnas de Ydr para sumar las coordenadas respectivas
            
                nuevaCor = 0;
                
                //ptsConNoX.length
                //toma los puntos en donde hubo NoX, de cada punto toma la misma columna (que representa una coordenada) y recalcula la nueva coordenada mediante interpolación homotopica, incluyendo su respectiva coordenada original en Yr. Esto lo hace con cada coordenada
                for (int s = 0; s < conN; s++) {// recorre el vector de puntos con extrusion, que contiene la fila correspondiente al punto

                    // nuevaCor = nuevaCor + (Ydr(ptsConNoX(s),t)); % si se quisiera que los puntos de influencia tuvieran el mismo peso

                    // Se regulariza el acercamiento al punto original RD (Yr): Se toma cordenada por cordenada (x, y, ...) de todos los puntos de influencia (con N o X) respecto a un punto en particular (determinado por el recorrido de filas), con lo cual se calcula la nueva coordenada baricentrica
                   
                    //segunda parte homotopica
                    if(ptsConN[s] == f){// representa la cordenada original (en Yr) con la   que es ponderada con el resto de la función homotopica (1-mu)
                       // (mu/abs(mu))*(1-abs(mu)) permite encontrar el complemento del peso mu al restar de 1 el valo absoluto de mu (de lo contrario, si mu fuera negativo en lugar de positivo, lo restaria). mu/abs(mu) Permite conservar el signo de mu y con esto se en que intervalo de la recta de centros esta (negativa o positiva)
                       nuevaCor = nuevaCor + ( (1-Math.abs(mu)) * (Yr[ptsConN[s]][t]) ); // Cuando se pondera el punto original, cuyo factor de ponderación es suministrado por el usuario (mu) es como la ponderacion homotopica
                       // nuevaCor = nuevaCor + ( (mu/abs(mu))*( (1-exp(1-mu))/1-exp(1) )*(Ydr(ptsConNoX(s),t)) ); % Es otra forma de homotopia (Diego)
                    // primera parte homotopica
                    }else{ // representa el resto de cordenadas de los puntos de influencia, se pondera  mu en la función homotopica
                       // De los puntos que inciden (extrusos o intrusos), se multiplican sus respectivos valores de la matriz RD (Ydr) por sus respectivos pesos (rankBehavior/denNox), los cuales son regularizados por un factor de control suministrado por el usuario (controlE = control de ponderación de acercamiento al punto original en presencia de extrusos)
                       // 1-controlE significa que el resto de puntos extrusos incidentes se reparten el excedente de probabilidad otorgada por el control de usuario
                       nuevaCor = nuevaCor + (  ( mu * (Math.abs(rankBehavior[f][ptsConN[s]])/valDepthRegN) ) * ( Yr[ptsConN[s]][t] )); // Se suma acumulativamente los valores de la componente (X, Y, Z, etc...) multiplicado por su respectivo peso, en los distintos puntos
//                      parece que al quitar el denNox se soluciona el problema
                       //nuevaCor = nuevaCor + ( ( ( (1-exp(mu))/1-exp(1) ) * (abs(rankAltaMenosBaja(f,ptsConNoX(s)))/denNoX) ) * (Ydr(ptsConNoX(s),t)) ); Es otra forma de homotopia (Diego)
                       // PROBAR CON LA MATRIZ LD ORIGINAL (Ydr) Y CON LA QUE VA QUEDANDO CON BARICENTROS (YrBari)
                    } 
                }
                // YdrBari(f,t) = nuevaCor/size(Ydr,2); % la nueva cordenada calculada con baricentro con igual peso la hubico en la matriz de datos reducida
                 
                YdrBari [f][t] = nuevaCor;
                }
            }
        
            
        // Utiliza la matriz Ydr, suma todos las cordenadas (por columna) que correspoden a los puntos con extrusion
        if(conX > 1){ // Si encontró alguna intrusion o extrusion entonces ingresa, si Contador de NOX mayor que 1

            for (int t = 0; t < Yr[0].length; t++) {// recorre las columnas de Ydr para sumar las coordenadas respectivas
            
                nuevaCor = 0;
                
                //ptsConNoX.length
                //toma los puntos en donde hubo NoX, de cada punto toma la misma columna (que representa una coordenada) y recalcula la nueva coordenada mediante interpolación homotopica, incluyendo su respectiva coordenada original en Yr. Esto lo hace con cada coordenada
                for (int s = 0; s < conX; s++) {// recorre el vector de puntos con extrusion, que contiene la fila correspondiente al punto

                    // nuevaCor = nuevaCor + (Ydr(ptsConNoX(s),t)); % si se quisiera que los puntos de influencia tuvieran el mismo peso

                    // Se regulariza el acercamiento al punto original RD (Yr): Se toma cordenada por cordenada (x, y, ...) de todos los puntos de influencia (con N o X) respecto a un punto en particular (determinado por el recorrido de filas), con lo cual se calcula la nueva coordenada baricentrica
                   
                    //segunda parte homotopica
                    if(ptsConX[s] == f){// representa la cordenada original (en Yr) con la   que es ponderada con el resto de la función homotopica (1-mu)
                       // (mu/abs(mu))*(1-abs(mu)) permite encontrar el complemento del peso mu al restar de 1 el valo absoluto de mu (de lo contrario, si mu fuera negativo en lugar de positivo, lo restaria). mu/abs(mu) Permite conservar el signo de mu y con esto se en que intervalo de la recta de centros esta (negativa o positiva)
                       nuevaCor = nuevaCor - ( (1-Math.abs(mu)) * (Yr[ptsConX[s]][t]) ); // Cuando se pondera el punto original, cuyo factor de ponderación es suministrado por el usuario (mu) es como la ponderacion homotopica
                       // nuevaCor = nuevaCor + ( (mu/abs(mu))*( (1-exp(1-mu))/1-exp(1) )*(Ydr(ptsConNoX(s),t)) ); % Es otra forma de homotopia (Diego)
                    // primera parte homotopica
                    }else{ // representa el resto de cordenadas de los puntos de influencia, se pondera  mu en la función homotopica
                       // De los puntos que inciden (extrusos o intrusos), se multiplican sus respectivos valores de la matriz RD (Ydr) por sus respectivos pesos (rankBehavior/denNox), los cuales son regularizados por un factor de control suministrado por el usuario (controlE = control de ponderación de acercamiento al punto original en presencia de extrusos)
                       // 1-controlE significa que el resto de puntos extrusos incidentes se reparten el excedente de probabilidad otorgada por el control de usuario
                       nuevaCor = nuevaCor - (  ( mu * (Math.abs(rankBehavior[f][ptsConX[s]])/valDepthRegX) ) * ( Yr[ptsConX[s]][t] )); // Se suma acumulativamente los valores de la componente (X, Y, Z, etc...) multiplicado por su respectivo peso, en los distintos puntos
//                      parece que al quitar el denNox se soluciona el problema
                       //nuevaCor = nuevaCor + ( ( ( (1-exp(mu))/1-exp(1) ) * (abs(rankAltaMenosBaja(f,ptsConNoX(s)))/denNoX) ) * (Ydr(ptsConNoX(s),t)) ); Es otra forma de homotopia (Diego)
                       // PROBAR CON LA MATRIZ LD ORIGINAL (Ydr) Y CON LA QUE VA QUEDANDO CON BARICENTROS (YrBari)
                    } 
                }
                // YdrBari(f,t) = nuevaCor/size(Ydr,2); % la nueva cordenada calculada con baricentro con igual peso la hubico en la matriz de datos reducida
                 
                YdrBari [f][t] = nuevaCor;
                }
            }
        
        
        }    
    }

    public double[][] getNewCoordinates(){
        return YdrBari;
    }

}
