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

package gui.Icons.Filters.Selection;

import gui.Icons.Filters.TariyTableModel;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * This class is used for create a new Table whit selected data.<br>  
 * This in particular contains information about AbstractTableModel 
 * whit output data.
 *
 * 
 */
public class Seleccion extends AbstractTableModel{
    /** The input data that arrive from a connection. */
    AbstractTableModel datosEntrada;
    
     /** Data after to have applied the filter. */
    final Object[][] datos;
    double[][] dataValues; // son los mismos datos pero en formato matriz de datos double
    
    /** Names of the attributes. */
    final String[] nomcol;
    
    /** Number of Columns. */
    int columns;
    
    /** Number of Rows. */
    int rows;
    
    /** Target Column. */
    int colObje;
    
    /** Selected Columns. */
    int colsel[];
    
    /** Number of Selected Columns. */
    int numcols;
    
    String[] etiquetas;
    ArrayList etiquetasDif;
    
   /** 
   * This initializes and resizes the variables
   * and constructs the new table.
   *
   * @param dataIn input data that arrive from a connection.
   * @param nc Number of Columns.
   * @param cs Selected Column.
   * @param o Target Column.
   */
    public Seleccion(AbstractTableModel dataIn, int nc, int[] cs, int o){
        datosEntrada = dataIn;
        numcols = nc + 1;
        colsel = cs;
        colObje = o;
        columns = dataIn.getColumnCount();
        rows = dataIn.getRowCount();
        datos = new Object[rows][numcols]; // son los datos totales abastracttable
        dataValues = new double[rows][numcols]; // datos cuantitativos matriz
        nomcol = new String[numcols];
        nuevaTabla();
    }
   
   /**
   * This function constructs the new table  
   * whit filtered data
   */
    public void nuevaTabla() {
        etiquetas = new String[rows];
        etiquetasDif = new ArrayList(1);
        
        for(int f = 0; f < rows; f++ ){
            for(int c = 0; c < numcols; c++ ){
                if(c == numcols-1){ // pone la col objetivo de ultima 
                    datos[f][c] = datosEntrada.getValueAt(f,colObje);
                    
                    // para generar el vector de etiquetas y las etiquetas diferentes
                    etiquetas[f] = datosEntrada.getValueAt(f ,colObje).toString();
                    
                    if(!etiquetasDif.contains(etiquetas[f])){
                        etiquetasDif.add(etiquetas[f]);
                    }
                }
                else datos[f][c] = datosEntrada.getValueAt(f,colsel[c]);
            }
        }
        // Para insertar el nombre de las columnas
        for(int i = 0; i < numcols; i++ ){
            if(i == numcols-1) nomcol[i] = datosEntrada.getColumnName(colObje);
            else nomcol[i] = datosEntrada.getColumnName(colsel[i]);
        }
    }
    
    public String[] getEtiquetas(){
        return etiquetas;
    }
    
    public ArrayList getEtiquetasDiferentes(){
        return etiquetasDif;
    }
    
    public double[][] getDataValues(){ // genera una matriz de datos cuantitativos
        
        // para validar que solo ingresen datos cuantitativos
        boolean bdCuanti = true;
        for(int j = 0; j < this.getColumnCount()-1; j++){
           if(this.getColumnClass(j).toString().equals("class java.lang.String")){
               bdCuanti = false;
           }
        }

       if(bdCuanti){
            dataValues = new double[this.getRowCount()][this.getColumnCount()-1];

            // datos ***** realiza una transformaci�n del dataset para que quede en terminos de matriz como lo requiere RD                                                    
            for(int i = 0; i < this.getRowCount(); i++){
                for(int j = 0; j < this.getColumnCount()-1; j++){
                    if(this.getValueAt(i, j) != null){                 
                        dataValues[i][j] = Double.parseDouble(this.getValueAt(i, j).toString());
                    } 
                }
            }
 
                
           }
        return  dataValues;
    }
    
    public String[] getMatrixAtributes(){
        return nomcol;
    }
   
    /**
     *  Returns the number of columns of the table. 
     */
    public int getColumnCount() {
        return nomcol.length;
    }
    
    /**
     *  Returns the number of rows of the table 
     */
    public int getRowCount() {
        return datos.length;
    }
    
    /**
     *  Returns a default name for the column 
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int col) {
        return nomcol[col];
    }
    
    /**
     *  Returns the value of a cell queried, in a row and column of the table.
     *
     *  @param  row  the row being queried
     *  @param  col the column being queried
     *  @return datos value of a cell queried
     */
    public Object getValueAt(int row, int col) {
        return datos[row][col];
    }
    
    /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param c  the column being queried
     *  @return the Object.class
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
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
    
     /**
     *  this method assigns a value to a cell.
     *
     *  @param  value   value to assign to cell
     *  @param  row   row of cell
     *  @param  col  column of cell
     */
    public void setValueAt(Object value, int row, int col) {
        datos[row][col] = value;
    }
}
