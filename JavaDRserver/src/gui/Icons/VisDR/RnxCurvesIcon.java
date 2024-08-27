/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.VisDR;

import gui.Icons.Reduction.*;
import Utils.SocketServer;
import gui.KnowledgeFlow.Icon;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
  
public class RnxCurvesIcon extends Icon{

    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public String algorithm;
    public double score;
    public byte[] imageBytes;

    public static verRnx verY;
    
    /** Creates a new instance of DBConnectionIcon */
    public RnxCurvesIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);

        algorithm = s.getText();

        setInfo("Rnx Curves in process");
        System.out.println("Rnx Curves in process");
        
        // MENÚ DE VISUALIZACIÓN
        mnuView = new javax.swing.JMenuItem();
        mnuView.setText("View...");
        mnuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuViewActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuView);
       verY = new verRnx();
        
        mnuView.setEnabled(false);
               
        // MENÚ DE AYUDA
        mnuHelp = new javax.swing.JMenuItem();
        mnuHelp.setText("Help...");
        mnuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHelpActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuHelp);
    }

    private void mnuHelpActionPerformed(java.awt.event.ActionEvent evt) {
        final Icon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Utils.Help(icon.getName().trim()).setVisible(true);
                new Utils.miHelp("asociacion.htm").setVisible(true);
            }
        });
    }
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               verY.setScore(algorithm, score, imageBytes);
               verY.setVisible(true);
            }
        });
    }
        
    public JMenuItem getMnuView() {
        return mnuView;
    }
        
}
