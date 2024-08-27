/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.Icons.VisDR;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author JuanKDD
 */
public class dataLegend extends AbstractTableModel {

    Object[][] data;
    String columnsName[] = {"Color","Atribute"};
    
    public dataLegend(Object[][] data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
//        throw new UnsupportedOperationException("Not supported yet.");
        return data.length;
    }

    @Override
    public int getColumnCount() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return data[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.      
        return data[rowIndex][columnIndex];
    }
    
    public String getColumnName(int col) {
        return columnsName[col];
    }
}