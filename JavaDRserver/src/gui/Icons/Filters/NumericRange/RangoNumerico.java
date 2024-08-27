/*
 * EliminarMissing.java
 *
 */

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


package gui.Icons.Filters.NumericRange;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * This class is used for create a new Table whit filtered data by numeric range.<br>  
 * This in particular contains information about AbstractTableModel 
 * whit output data.
 *
 * @author Tariy
 */
public class RangoNumerico extends AbstractTableModel{
    /** The input data that arrive from a connection. */
    AbstractTableModel datosEntrada;
    
    /** Data after to have applied the filter. */
    ArrayList data;
    
    /** Names of the attributes. */
    String[] columnsName;
    
    /** Selected Colmn. */
    int colSel;
    
    /** Minimum limits. */
    int min;
    
    /** Maximum limits. */
    int max;
    String valRem;
    
    Class colsClas[];
      
   /** 
   * This initializes and resizes the variables
   * and constructs the new table.
   *
   * @param dataIn input data that arrive from a connection.
   * @param cs Selected Column.
   * @param mn Minimum limits.
   * @param mx Maximum limits.
   */
    public RangoNumerico(AbstractTableModel dataIn, int cs, int mn, int mx){
        datosEntrada = dataIn;
        int columns = dataIn.getColumnCount();
        columnsName = new String[columns];
        colsClas = new Class[columns];
        for(int i = 0; i < columns; i++){
            columnsName[i] = dataIn.getColumnName(i);
            colsClas[i] = Object.class;
        }
        data = new ArrayList();
        colSel = cs;
        
        min = mn;
        max = mx;
        nuevaTabla();
    }
   
   /**
   * This function constructs the new table  
   * whit filtered data
   */
    public void nuevaTabla() {
        for(int f = 0; f < datosEntrada.getRowCount(); f++){
            
            if(datosEntrada.getColumnClass(colSel).equals(Integer.class)){ // por si es entero
            
                if((Integer)datosEntrada.getValueAt(f,colSel) >= min && (Integer)datosEntrada.getValueAt(f,colSel) <= max) {
                    Object[] row = new Object[columnsName.length];
                    for(int i = 0; i < row.length; i++){
                        row[i] = datosEntrada.getValueAt(f, i);
                        
                        // verifico a que tipo de dato pertenece
                        if(esEntero(datosEntrada.getValueAt(f,i).toString())){
                           if(!colsClas[i].equals(String.class) && !colsClas[i].equals(Double.class))  
                              colsClas[i] = Integer.class;  // El string predominta sobre el double y el double sobre el integer 
                        }else if(esDouble(datosEntrada.getValueAt(f,i).toString())){
                           if(!colsClas[i].equals(String.class)) 
                              colsClas[i] = Double.class;  // es decir, si algun elemento fue string, deja a toda la columna como string
                        }else {
                           colsClas[i] = String.class;
                        }  
                    }
                    data.add(row);
                    //datosEntrada.setValueAt("",f,colSel);  // si en ves de "" le pongo null se da�a por que?  ********
                    //conm ++;           
                }
            } else if(datosEntrada.getColumnClass(colSel).equals(Double.class)){ // por si es Double
            
                if((Double)datosEntrada.getValueAt(f,colSel) >= min && (Double)datosEntrada.getValueAt(f,colSel) <= max) {
                    Object[] row = new Object[columnsName.length];
                    for(int i = 0; i < row.length; i++){
                        row[i] = datosEntrada.getValueAt(f, i);
                            
                        // verifico a que tipo de dato pertenece
                        if(esEntero(datosEntrada.getValueAt(f,i).toString())){
                           if(!colsClas[i].equals(String.class) && !colsClas[i].equals(Double.class))  
                              colsClas[i] = Integer.class;  // El string predominta sobre el double y el double sobre el integer 
                        }else if(esDouble(datosEntrada.getValueAt(f,i).toString())){
                           if(!colsClas[i].equals(String.class)) 
                              colsClas[i] = Double.class;  // es decir, si algun elemento fue string, deja a toda la columna como string
                        }else {
                           colsClas[i] = String.class;
                        }  
                    }
                    data.add(row);
                    //datosEntrada.setValueAt("",f,colSel);  // si en ves de "" le pongo null se da�a por que?  ********
                    //conm ++;   
                }
            } 
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
        return data.size();
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
        Object[] rows = (Object[])data.get(row);
        return rows[col];
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
