/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package gui.Icons.Filters.Proximity;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;

/**
 * This class is used for remplace a vaule in the table.<br>  
 * This in particular contains information about AbstractTableModel 
 * whit output data.
 *
 * 
 */
public class Proximity extends AbstractTableModel{
    /** The input data that arrive from a connection. */
    public AbstractTableModel dataIn;
    double[][] datosEntrada;
    
    /** Data after to have applied the filter. */
    double[][] dataOut;
    Object[][] dataOutConCnom; // Es el mismo dataOut pero con una columna adicional con los nombres de las columnas
    
    /** Names of the attributes. */
    String[] columnsName;
    String[] columnsNameConCnom; // Es el mismo columnsName pero con una columna adicional con los nombres de las columnas
    
    /** Selected Colmn. */
    int ColSel;
    
    /** Number of Attributes.*/
    int numatri;
    int filen;
    
    /** Selected Attributes.*/
    ArrayList atrisel;
    
    /** Value for remplace. */
    String remcon;
    public int replaceCount; 
    
    /** Class of Columns. */
    Class colsClas[];
    
    String proxType = "xatributos";
    
     int rows = 0;
     int columns = 0;
     
     String[] etiquetas;
     ArrayList etiquetasDif;
     
     int colObj = 0;
     int[] colsSel;
    
   /** 
   * This initializes and resizes the variables
   * and constructs the new table.
   *
   * @param dataIn input data that arrive from a connection.
   * @param colsel Selected Column.
   * @param atri Selected Attributes.
   * @param rem Value for remplace.
   */
     
    public Proximity(AbstractTableModel dataIn, String proxType, int colObj, int[] colsSel, int numCols){
        this.dataIn = dataIn;  
        this.proxType = proxType;
        this.colsSel = colsSel;
        this.colObj = colObj;

        rows = dataIn.getRowCount();
        columns = numCols;

        datosEntrada = new double[rows][columns];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                datosEntrada[i][j] = Double.parseDouble(dataIn.getValueAt(i ,colsSel[j]).toString());
            }               
        }
        
        nuevaTabla();
    }
    
   /**
   * This function constructs the new table  
   * whit filtered data
   */
    
    
    public void nuevaTabla() {

        switch (proxType) {
            
            case "xatributos":
              xAtributos();
             genEtiquetasAtributo();
            break;

            case "xregistros":
              xRegistros();
              genEtiquetasRegistro(colObj);
            break;

            default:
              xAtributos();
            break;
        }

    } 
    
     public void xAtributos(){
         
         //CUANDO SE CONTRUYE LA MATRIZ DE PROXIMIDAD CXC(ATRIBUTOS POR ATRIBUTOS), SIMILAR A LA DE EURODIST
        columnsNameConCnom = new String[columns+1]; 
        columnsName = new String[columns]; 
        colsClas = new Class[columns+1];
        dataOutConCnom = new Object[columns][columns+1];
        dataOut = new double[columns][columns];

        columnsNameConCnom[0] = "***";
        colsClas[0] = String.class;
        for(int i = 0; i < columns; i++ ){
            columnsName[i] = columnsNameConCnom[i+1] = dataIn.getColumnName(colsSel[i]);
            dataOutConCnom[i][0] = columnsNameConCnom[i+1]; //pone los mismos nobres de las variables en la primera columna
            colsClas[i+1] = Double.class;
        }
        
        datosEntrada = Utils.MachineLearning.math.math.Math.transpose(datosEntrada);
        for (int i = 0; i < columns; i++) { 
            for (int j = i; j < columns; j++) { 
                if(i==j){
                     dataOutConCnom[i][j+1] = dataOut[i][j] = 0.0;
                }else{
                    dataOutConCnom[i][j+1] = dataOut[i][j] = Utils.MachineLearning.math.math.Math.distance(datosEntrada[i], datosEntrada[j]);
                    dataOutConCnom[j][i+1] = dataOut[j][i] = dataOut[i][j];
                }
            }
        } 
     }
    
    public void xRegistros(){
        
        // CUANDO SE CONTRUYE LA MATRIZ DE PROXIMIDAD FXF(REGISTRO POR REGISTRO)
        
        columnsNameConCnom = new String[rows+1]; 
        columnsName = new String[rows];
        colsClas = new Class[rows+1];
        dataOutConCnom = new Object[rows][rows+1]; // el mas 1 es por que se agraga una columna mas que es la de nombre de variables
        dataOut = new double[rows][rows];
        
        columnsNameConCnom[0] = "X";
        colsClas[0] = String.class;
        for(int j = 0; j < rows; j++){
            columnsName[j] = columnsNameConCnom[j+1] = "C"+(j+1);
            dataOutConCnom[j][0] = "C"+(j+1);
            colsClas[j+1] = Double.class;
        }

        for (int i = 0; i < rows; i++) { 
            for (int j = i; j < rows; j++) { 
                if(i==j){
                    dataOutConCnom[i][j+1] =  dataOut[i][j] = 0.0;
                }else{
                    dataOutConCnom[i][j+1] = dataOut[i][j] = Utils.MachineLearning.math.math.Math.distance(datosEntrada[i], datosEntrada[j]);
                    dataOutConCnom[j][i+1] =  dataOut[j][i] = dataOut[i][j];
                }
            }
        }  
    }
    
    public void genEtiquetasAtributo(){

         etiquetas = new String[columns];
         etiquetasDif = new ArrayList(1);
         
         for(int i = 0; i<columns; i++){
             etiquetas[i] = columnsNameConCnom[i+1];
             etiquetasDif.add(etiquetas[i]);
         }
    }
    
    public void genEtiquetasRegistro(int colEt){
        
         etiquetas = new String[rows];
         etiquetasDif = new ArrayList(1);
         for(int i = 0; i<rows; i++){
             etiquetas[i] = dataIn.getValueAt(i ,colEt).toString();
             
             if(!etiquetasDif.contains(etiquetas[i])){
                etiquetasDif.add(etiquetas[i]);
             }
         }
    }
    
    public String[] getEtiquetas(){
        return etiquetas;
    }
    
    public ArrayList getEtiquetasDiferentes(){
        return etiquetasDif;
    }
    
    public double[][] getMatrixData(){
        return  dataOut;
    }
    
    public String[] getMatrixAtributes(){
        return columnsName;
    }
    
    
    /**
     *  Returns the number of columns of the table. 
     */
    public int getColumnCount() {
        return dataOutConCnom[0].length;
    }
    
    /**
     *  Returns the number of rows of the table 
     */
    public int getRowCount() {
        return dataOutConCnom.length;
    }
    
     /**
     *  Returns a default name for the column 
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int col) {
        return columnsNameConCnom[col];
    }
    
     /**
     *  Returns the value of a cell queried, in a row and column of the table.
     *
     *  @param  row  the row being queried
     *  @param  col the column being queried
     *  @return datos value of a cell queried
     */
    public Object getValueAt(int row, int col) {
        return dataOutConCnom[row][col];
    }
    
     /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param c  the column being queried
     *  @return the Object.class
     */
    public Class getColumnClass(int c) {
//        return getValueAt(0, c).getClass();
         return colsClas[c]; 
    }
    
     /**
     *  Returns if the cell is editable.  This is the default implementation for all cells.
     *
     *  @param  row  the row being queried
     *  @param  col the column being queried
     *  @return boolean value that depends if it is editable
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }
 
}
