/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.Reduction.DDR;

import algorithm.reduction.kernel.DDR;
import gui.Icons.Reduction.PAppletKmix.*;
import gui.Icons.Reduction.*;
import algorithm.reduction.kernel.Kmix;
import gui.KnowledgeFlow.ChooserEscritorio;
import gui.KnowledgeFlow.Icon;
import gui.KnowledgeFlow.JackAnimation;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 */
public class DDRIcon extends Icon{
    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public String algorithm;
       
    // Datos
    public AbstractTableModel dataIn = null;
    public double[][] dataValues;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif = new ArrayList(1);
    
    // Kernel
    public double[][] dataValuesK;
    
    public int d;

    public ArrayList nameKernels = new ArrayList(1); 
    
    double[][] dataOut = null;
    
    public DDR ddr;

    public static configureDDR cddr;
    
    public static verDatosDDR verYK;
    
    public int typeMatrix = 0;
    
    /** Creates a new instance of DBConnectionIcon */
    public DDRIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
        super.constrainsTo.add("ScatterIcon");
        super.constrainsTo.add("LGNXIcon");
        super.constrainsTo.add("BehaviourIcon");
        
        algorithm = s.getText();

        setInfo("Dimensionality Reduction in process");
        System.out.println("Dimensionality Reduction in process");
        
        mnuConfigure = new javax.swing.JMenuItem();
        mnuConfigure.setText("Configure...");
        mnuConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigureActionPerformed(evt);
            }
        });
        
        super.pupMenu.add(mnuConfigure);
        mnuConfigure.setEnabled(false);
         
        cddr = new configureDDR(this);
        cddr.setVisible(false);
         
        mnuRun = new javax.swing.JMenuItem();
        mnuRun.setText("Run...");
        mnuRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRunActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuRun);
        mnuRun.setEnabled(false);
        
        mnuView = new javax.swing.JMenuItem();
        mnuView.setText("View...");
        mnuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuViewActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuView);

        verYK = new verDatosDDR();

        mnuView.setEnabled(false);
               
        mnuHelp = new javax.swing.JMenuItem();
        mnuHelp.setText("Help...");
        mnuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHelpActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuHelp);
    }

    public JMenuItem getMnuRun() {
        return mnuRun;
    }
    
    public JMenuItem getMnuConfigure() {
        return mnuConfigure;
    }

    private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
        final DDRIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cddr.updateIcon(icon);
                cddr.setVisible(true);
            }
        });
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
    
    private void mnuRunActionPerformed(java.awt.event.ActionEvent evt) {

        JackAnimation jack = new JackAnimation();
        this.add(jack);
        this.setComponentZOrder(jack, 0);
        jack.setBounds(this.animation.getX(), this.animation.getY(), 36, 36);
        this.setAnimation(jack);

        this.startAnimation();
        long clock = System.currentTimeMillis();

        try {
          
            if(dataValues == null){
                JOptionPane.showMessageDialog(this, "Link input data", "VisMineDR", JOptionPane.INFORMATION_MESSAGE);
            }else if(dataValuesK == null){
                JOptionPane.showMessageDialog(this, "Link kernel matrix data", "VisMineDR", JOptionPane.INFORMATION_MESSAGE);
            }else if(dataValues.length != dataValuesK.length){
                JOptionPane.showMessageDialog(this, "Matrix are not supported", "VisMineDR", JOptionPane.ERROR_MESSAGE);
            }else{
                ddr = new DDR(dataValues, dataValuesK, d);
            }
           
          
        } catch (Exception ex) {
            Logger.getLogger(DDRIcon.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.format("Learn DDR from %d samples in %dms\n", System.currentTimeMillis()-clock);
        ChooserEscritorio.setStatus("Learn DDR in " + (System.currentTimeMillis()-clock));
        this.stopAnimation();    

        mnuView.setEnabled(true);
    }
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
         java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {

            // Kernel
            String[] ck = new String[dataValuesK[0].length];
            for(int i=0; i<dataValuesK[0].length; i++){
                ck[i] = "c"+(i+1); 
            }
            DRTableModel dtk = null;
            dtk = new DRTableModel(dataValuesK, ck);

             //Eigens
             String[] ds = new String[ddr.getEigVectores().length];
             for(int i=0; i<ddr.getEigVectores().length; i++){
                ds[i] = "d"+(i+1); 
             }

            DRTableModel eigenVectores = new DRTableModel(ddr.getEigVectores(), ds);
            DRTableModel eigenValores = new DRTableModel(ddr.getEigValores(), ds);

            //Datos de salida
            String[] dr = new String[d];
            for(int i=0; i<d; i++){
                dr[i] = "d"+(i+1); 
            } 
            dataOut = ddr.getLowMatrix();
            DRTableModel dataO = new DRTableModel(dataOut, dr);  

            //tablas
            verYK.setDatas(dataIn, dtk, dataO, eigenVectores, eigenValores); 
            verYK.setVisible(true);

            }
        });
    }
         
}
