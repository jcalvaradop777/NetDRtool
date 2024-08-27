/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.Icons.Filters.Proximity;

/**
 *
 * @author JuanKDD
 */

import gui.Icons.VisDR.*;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderCelda extends DefaultTableCellRenderer 
{
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) 
    {
        //Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(table.getValueAt(row, 1).equals("String")){
            cell.setBackground(Color.RED);
        }else{
            cell.setBackground(Color.WHITE);
        }
        
        if(table.getValueAt(row, 2).equals("Label")){
//            // esto es para saber que en la columna TARGET solo pueda existir una sola label
//            for(int i=0; i<table.getRowCount(); i++){
//                if(i!=row){
//                  table.setValueAt("Attribute", i, 2);   
//                }  
//            }
//            cell.setText("Label");
            cell.setBackground(Color.GREEN);
        }
        repaint();
        return cell;
    }
}
