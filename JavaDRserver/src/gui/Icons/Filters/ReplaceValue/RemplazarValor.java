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

package gui.Icons.Filters.ReplaceValue;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.table.AbstractTableModel;

/**
 * This class is used for remplace a vaule in the table.<br>  
 * This in particular contains information about AbstractTableModel 
 * whit output data.
 *
 * 
 */
public class RemplazarValor extends AbstractTableModel{
    /** The input data that arrive from a connection. */
    AbstractTableModel datosEntrada;
    
    /** Data after to have applied the filter. */
    Object[][] data;
    
    /** Names of the attributes. */
    String[] columnsName;
    
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
    
    
   /** 
   * This initializes and resizes the variables
   * and constructs the new table.
   *
   * @param dataIn input data that arrive from a connection.
   * @param colsel Selected Column.
   * @param atri Selected Attributes.
   * @param rem Value for remplace.
   */
    public RemplazarValor(AbstractTableModel dataIn, int colsel, ArrayList atri, String rem){
        datosEntrada = dataIn;        
        ColSel = colsel;
        atrisel = atri;
        numatri = atri.size();
        remcon = rem;
        replaceCount = 0;

        nuevaTabla();
    }
    
   /**
   * This function constructs the new table  
   * whit filtered data
   */
    public void nuevaTabla() {
        int rows = datosEntrada.getRowCount();
        int columns = datosEntrada.getColumnCount();
        
        data = new Object[rows][columns];
        columnsName = new String[columns];
        
        colsClas = new Class[columns];
        Class colCla = Object.class;
        Class valorClas = findClass(remcon);
        
        
        for(int i = 0; i < columns; i++){
            if(i != ColSel){
                for(int j = 0; j < rows; j++){
                    data[j][i] = datosEntrada.getValueAt(j ,i);
                }
            } else{
                for(int j = 0; j < rows; j++){
                    if(atrisel.contains(datosEntrada.getValueAt(j,ColSel).toString())){
                        if(valorClas.equals(Integer.class)){
                            data[j][ColSel] = Integer.parseInt(remcon);
                        }else if(valorClas.equals(Double.class)){
                            data[j][ColSel] = Double.parseDouble(remcon);
                        }else if(valorClas.equals(String.class)){
                            data[j][ColSel] = remcon;
                        }
                        replaceCount++;
                    } else {
//                        data[j][ColSel] = datosEntrada.getValueAt(j ,ColSel);
                        
                        // verifico a que tipo de dato pertenece
                        if(esEntero(datosEntrada.getValueAt(j,ColSel).toString())){
                           if(!colCla.equals(String.class) && !colCla.equals(Double.class))  
                               data[j][ColSel] = Integer.parseInt(datosEntrada.getValueAt(j ,ColSel).toString());
                               colCla = Integer.class;  // El string predominta sobre el double y el double sobre el integer 
                        }else if(esDouble(datosEntrada.getValueAt(j,ColSel).toString())){
                           if(!colCla.equals(String.class)) 
                               data[j][ColSel] = Double.parseDouble(datosEntrada.getValueAt(j ,ColSel).toString());
                               colCla = Double.class;  // es decir, si algun elemento fue string, deja a toda la columna como string
                        }else {
                           data[j][ColSel] = datosEntrada.getValueAt(j ,ColSel).toString();
                           colCla = String.class;
                        }   
                    }
                }
            }
            columnsName[i] = datosEntrada.getColumnName(i);
            colsClas[i] = datosEntrada.getColumnClass(i);
        }
        
        if(colCla.equals(valorClas)){ // si tanto la clase del valor a remplazar como la de la columna en general es la misma, le asigna esa clase
            colsClas[ColSel] = colCla; // asigna la clase encontrada a la columana seleccionada
        }else if(colCla.equals(String.class) || valorClas.equals(String.class)){
            colsClas[ColSel] = String.class;
        }else if(colCla.equals(Double.class) || valorClas.equals(Double.class)){
            colsClas[ColSel] = Double.class;
        }else if(colCla.equals(Integer.class) || valorClas.equals(Integer.class)){
            colsClas[ColSel] = Integer.class;
        }
    }
  
    
    private boolean esEntero(String cad){
        boolean bd = false;
        int valNum = 0;
                
        try{  // si lo puede transformar a numero lo transforma a numero
          valNum = Integer.parseInt(cad);
          bd = true;
        } catch (Exception e) {
          bd = false;
        }
        return bd;
    }
    
    private boolean esDouble(String cad){
        boolean bd = false;
        double valNum = 1.0;
                
        try{  // si lo puede transformar a numero lo transforma a numero
          valNum = Double.parseDouble(cad);
          bd = true;
        } catch (Exception e) {
          bd = false;
        }
        return bd;
    }
    
    private Class findClass(String cad){
        Class cl = Object.class;
        if(esEntero(cad)){
           cl = Integer.class;  // El string predominta sobre el double y el double sobre el integer 
        }else if(esDouble(cad)){
           cl = Double.class;  // es decir, si algun elemento fue string, deja a toda la columna como string
        }else {
           cl = String.class;
        }   
        return cl;
    }
    
    
    /**
     *  Returns the number of columns of the table. 
     */
    public int getColumnCount() {
        return columnsName.length;
    }
    
    /**
     *  Returns the number of rows of the table 
     */
    public int getRowCount() {
        return data.length;
    }
    
     /**
     *  Returns a default name for the column 
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int col) {
        return columnsName[col];
    }
    
     /**
     *  Returns the value of a cell queried, in a row and column of the table.
     *
     *  @param  row  the row being queried
     *  @param  col the column being queried
     *  @return datos value of a cell queried
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
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
