/*
 * FileTableModel.java
 */

package gui.Icons.File;

import Utils.FileManager;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Juan Carlos Alvarado
 */
public class FileTableModel extends AbstractTableModel {
    /** La ruta del archivo de acceso aleatorio de tipo .arff. */
    private String filePath;
    
    /** Los nombres de las columnas. */
    Object[] columnNames;
    
//    /** Los nombres de las columnas en formato string. */
    String[] attributes;
    
    /** Los datos provenientes de un archivo de acceso aleatorio .arff. */
    Object[][] data;
    
//    /** son los mismos datos pero en formato matriz de datos double*/
    double[][] dataValues; 
    
    /** tiene la clase de las columnas*/
    Class colClas[] = null; //nnn
    
    int rows;
    int cols;
    
    /** Creates a new instance of FileTableModel */
    public FileTableModel(String file, char delimiter) {
        filePath = file;
        
        FileManager fileMngt = new FileManager(filePath);
//        fileMngt.readCsvByFile(); // este tambien construye el dictionary
        //fileMngt.dataAndAttributes(true);
        fileMngt.readCsv(delimiter);
        
        int size = fileMngt.getAttributes().length;
        columnNames = new Object[size];
        columnNames = fileMngt.getAttributes();
        
        rows = fileMngt.getData().length;
        cols = fileMngt.getData()[0].length;
        data = new Object[rows][cols];
        data = fileMngt.getData();
        
        colClas = fileMngt.getColsClass();
        
//        genDataValues();
    }
    
    /** Creates a new instance of FileTableModel */
    public FileTableModel(String file, String ext, char delimiter) {
        filePath = file;
        
        FileManager fileMngt = new FileManager(filePath);
        if(ext.compareTo("arff")==0){
            fileMngt.dataAndAttributes(true);
        }else{
//            fileMngt.readCsvByFile();   // este tambien construye el dictionary
            fileMngt.readCsv(delimiter);
        } 
        
        int size = fileMngt.getAttributes().length;
        columnNames = new Object[size];
        columnNames = fileMngt.getAttributes();
        
        rows = fileMngt.getData().length;
        cols = fileMngt.getData()[0].length;
        
        data = new Object[rows][cols];
        data = fileMngt.getData();
        
        colClas = fileMngt.getColsClass(); 
        
//        genDataValues();
    }
    
    @Override
    public Class getColumnClass(int column) {
         return colClas[column];
    }
    
    public String getColumnName(int column) {
        return (String) columnNames[column];
    }
    
    //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    //||||||||||||||| AbstractTableModel implemented methods |||||||||||||||||
    //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
    public int getRowCount() {
        return data.length;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public boolean isNumeric(){ // para saber si la matriz esta llena de valores numericos
        
        // para validar que solo ingresen datos cuantitativos
        boolean bdCuanti = true;
        for(int j = 0; j < this.getColumnCount(); j++){
           if(this.getColumnClass(j).toString().equals("class java.lang.String")){
               bdCuanti = false;
           }
        }
        return bdCuanti;
    }
    
    public double[][] getDataValues(){ // genera una matriz de datos cuantitativos
        
       if(isNumeric()){
            dataValues = new double[rows][cols];
            attributes = new String[cols];

            // datos ***** realiza una transformación del dataset para que quede en terminos de matriz como lo requiere RD                                                    
            for(int j = 0; j < cols; j++){
                for(int i = 0; i < rows; i++){
                      dataValues[i][j] = Double.parseDouble(this.getValueAt(i, j).toString());                  
                }
                 attributes[j] = (String)columnNames[j];
            }       
        }
       return  dataValues;
    }
    
    public String[] getColumnsNameString(){
        return attributes;
    }
    
}
