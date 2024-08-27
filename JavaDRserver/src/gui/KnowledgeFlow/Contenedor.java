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

/*
 * Contenedor.java
 *
 */

package gui.KnowledgeFlow;

import gui.Icons.Association.AssociationIcon;
import gui.Icons.DBConnection.DBConnectionIcon;
import gui.Icons.File.FileIcon;
import gui.Icons.Filters.FilterIcon;
import gui.Icons.Prediction.PredictionIcon;
import gui.Icons.Rules.RulesIcon;
import gui.Icons.Tree.HierarchicalTreeIcon;
import gui.Icons.Tree.TextTreeIcon;
import gui.Icons.Tree.WekaTreeIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Contain a <code>JSplitPanne</code> to add the selector panels and Work Area
 * (Class <code>MyCanvas</code>)
 *
 * @author  Juan Carlos Alvarado
 */
public class Contenedor extends JPanel {
    /** Selector panel to Connection */
    pnlPreprocesamiento pre;
   /** Selector panel to Algorithm */
    pnlAlgoritmos alg;
    /** Selector panel to Viewer */
    pnlVisores vis;
    /** Selector panel to Pre-processing */
    pnlFilters fil;
    /** Work area class */
    MyCanvas canvas;
    // used for save the JLabel selected
    private JLabel dragged;
    // used in the hide left component in the JSplitPanne Contenedor
    private int backup_select;
    
    /** Creates new form Contenedor */
    public Contenedor() {
        initComponents();
        
        canvas = new MyCanvas(this);
        
        pre = new pnlPreprocesamiento(this);
        alg = new pnlAlgoritmos(this);
        vis = new pnlVisores(this);
        fil = new pnlFilters(this);
        
        scrollPanel.setViewportView(canvas);
        scrollSelector.setViewportView(pre);
        contenedor.setLeftComponent(scrollSelector);
        contenedor.setRightComponent(scrollPanel);
        
        contenedor.setTopComponent(scrollSelector);
        contenedor.setBottomComponent(scrollPanel);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contenedor = new javax.swing.JSplitPane();
        scrollPanel = new javax.swing.JScrollPane();
        scrollSelector = new javax.swing.JScrollPane();

        contenedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        contenedor.setDividerLocation(100);
        contenedor.setDividerSize(2);
        contenedor.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        scrollPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Work Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), javax.swing.UIManager.getDefaults().getColor("Button.focus"))); // NOI18N
        contenedor.setRightComponent(scrollPanel);
        contenedor.setLeftComponent(scrollSelector);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contenedor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contenedor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Return the JLabel associated to icon selected.
     */
    public JLabel getDragged() {
        return dragged;
    }
    
    /**
     * Set the JLabel which was selected.
     *
     * @param dragged JLabel selected.
     */
    public void setDragged(JLabel dragged) {
        this.dragged = dragged;
    }
    
    /**
     * Get the Container
     */
    public JSplitPane getContenedor(){
        return contenedor;
    }
    
    /**
     * Change or hide the left panel
     *
     * @param select The selector panel
     */
    public void changeDividerLocation(int divLoc){
       contenedor.setDividerLocation(divLoc);
    }
    
    public void changeLeftPanel(int select){
        JPanel selector = null;
        switch(select){
            case 0:
                if(backup_select == select){
                    backup_select = -1;
                } else {
                    selector = pre;
                    backup_select = select;
                }
                break;
            case 1:
                if(backup_select == select){
                    backup_select = -1;
                } else {
                    selector = fil;
                    backup_select = select;
                }
                break;
            case 2:
                if(backup_select == select){
                    backup_select = -1;
                } else {
                    selector = alg;
                    backup_select = select;
                }
                break;
            case 3:
                if(backup_select == select){
                    backup_select = -1;
                } else {
                    selector = vis;
                    backup_select = select;
                }
                break;
        }
        if(selector == null){
            contenedor.setDividerLocation(0);
        } else {
            contenedor.setDividerLocation(100);
        }
        scrollSelector.setViewportView(selector);
    }
    /**
     * Get the JScrollPane associated to Work Area.
     */
    public JScrollPane getScrollPanel() {
        return scrollPanel;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane contenedor;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JScrollPane scrollSelector;
    // End of variables declaration//GEN-END:variables
    
}
