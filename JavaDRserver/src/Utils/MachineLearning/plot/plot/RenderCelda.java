/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.MachineLearning.plot.plot;

/**
 *
 * @author JuanKDD
 */

import gui.Icons.VisDR.*;
import Utils.MachineLearning.plot.plot.Palette;
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
        
        if( value instanceof Integer ){
            Integer valori = (Integer) value;
//            cell.setBackground(Palette.COLORS[amount]);
            cell.setBackground(Palette.COLORS[valori]);
//            cell.setForeground(Color.WHITE);
        }else if( value instanceof Long ){
            long valorl = (long) value;
//            cell.setBackground(Palette.COLORS[amount]);
            cell.setBackground(Color.BLUE);
            cell.setForeground(Color.WHITE);
        }
        return cell;
    }
}
