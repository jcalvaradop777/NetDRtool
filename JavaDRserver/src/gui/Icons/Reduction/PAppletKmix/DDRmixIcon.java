/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.Reduction.PAppletKmix;

import gui.Icons.Reduction.*;

import Utils.MachineLearning.math.math.kernel.GaussianKernel;
import algorithm.reduction.kernel.DDRmix;
import algorithm.reduction.kernel.Kmix;
import algorithm.reduction.kernel.KMDS;
import algorithm.reduction.manifold.LaplacianEigenmap;
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
public class DDRmixIcon extends Icon{
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
       
    // Datos K1
    public double[][] dataValuesK1;
    public String[] atributosK1;
    public String[] etiquetasK1;
    public ArrayList etiquetasDifK1 = new ArrayList(1);
    
    // Datos K2
    public double[][] dataValuesK2;
    public String[] atributosK2;
    public String[] etiquetasK2;
    public ArrayList etiquetasDifK2 = new ArrayList(1);
    
    // Datos K3
    public double[][] dataValuesK3;
    public String[] atributosK3;
    public String[] etiquetasK3;
    public ArrayList etiquetasDifK3 = new ArrayList(1);
    
    float R = 0, G = 0, B = 0;
    
    public int d;

    public ArrayList nameKernels = new ArrayList(1); 
    
    double[][] datosReducidos = null;
    
    public DDRmix ddrmix;

    public static configureMix ckmix;
    
    public static verDatosRedDDRmix verYK;
    
    public boolean tieneDataMatrix = false; // para saber si la matriz que llega de fileicon es kernel o de datos
    
    /** Creates a new instance of DBConnectionIcon */
    public DDRmixIcon(JLabel s, int x, int y, int indiceIcon) {
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
         
        ckmix = new configureMix(this);
         
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

        verYK = new verDatosRedDDRmix();

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
    
    // CON etiquetas
    public boolean setKernels(String nameKernel, double[][] dataValuesK, String[] atributes, String[] etiquetasK, ArrayList etiquetasDifK){
        
        boolean bd = false; //para saber si se puede establecer la conexion
        
        switch (nameKernels.size()) {

            case 0:
                dataValuesK1 = dataValuesK;
                atributosK1 = atributes;
                etiquetasK1 = etiquetasK;
                etiquetasDifK1 = etiquetasDifK;
                nameKernels.add(nameKernel);
                bd = true;
            break;

            case 1:
                if(dataValuesK1.length == dataValuesK.length && dataValuesK1[0].length == dataValuesK[0].length && etiquetasK1.length == etiquetasK.length && etiquetasDifK1.size() == etiquetasDifK.size()){
                    dataValuesK2 = dataValuesK;
                    atributosK2 = atributes;
                    etiquetasK2 = etiquetasK;
                    etiquetasDifK2 = etiquetasDifK;
                    nameKernels.add(nameKernel);
                    bd = true;
                }else{
                    JOptionPane.showMessageDialog(null, "Incompatible data table", "VisMineDR", JOptionPane.ERROR_MESSAGE);
                }
            break;

            case 2:
                if(dataValuesK2.length == dataValuesK.length && dataValuesK2[0].length == dataValuesK[0].length && etiquetasK2.length == etiquetasK.length && etiquetasDifK2.size() == etiquetasDifK.size()){
                    dataValuesK3 = dataValuesK;
                    atributosK3 = atributes;
                    etiquetasK3 = etiquetasK;
                    etiquetasDifK3 = etiquetasDifK;
                    nameKernels.add(nameKernel);
                    bd = true;
                }else{
                    JOptionPane.showMessageDialog(null, "Incompatible data table", "VisMineDR", JOptionPane.ERROR_MESSAGE);
                }    
            break;

            default:
            JOptionPane.showMessageDialog(null, "Can merge 1 to 3 kernel methods", "VisMineDR", JOptionPane.ERROR_MESSAGE);
            break;
        }
        return bd;
    }
    
    // SIN etiquetas
    public boolean setKernels(String nameKernel, double[][] dataValuesK, String[] atributes){

        boolean bd = false; //para saber si se puede establecer la conexion
         
        switch (nameKernels.size()) {
 
            case 0:
                dataValuesK1 = dataValuesK;
                atributosK1 = atributes;
                etiquetasK1 = null;
                etiquetasDifK1 = null;
                nameKernels.add(nameKernel);
                bd = true;
            break;

            case 1:
                if(dataValuesK1.length == dataValuesK.length && dataValuesK1[0].length == dataValuesK[0].length ){
                    dataValuesK2 = dataValuesK;
                    atributosK2 = atributes;
                    etiquetasK2 = null;
                    etiquetasDifK2 = null;
                    nameKernels.add(nameKernel);
                    bd = true;
                }else{
                    JOptionPane.showMessageDialog(null, "Incompatible data table", "VisMineDR", JOptionPane.ERROR_MESSAGE);
                }
            break;

            case 2:
                if(dataValuesK2.length == dataValuesK.length && dataValuesK2[0].length == dataValuesK[0].length ){
                    dataValuesK3 = dataValuesK;
                    atributosK3 = atributes;
                    etiquetasK3 = null;
                    etiquetasDifK3 = null;
                    nameKernels.add(nameKernel);
                    bd = true;
                }else{
                    JOptionPane.showMessageDialog(null, "Incompatible data table", "VisMineDR", JOptionPane.ERROR_MESSAGE);
                }
            break;

            default:
            JOptionPane.showMessageDialog(null, "Can merge 1 to 3 kernel methods", "VisMineDR", JOptionPane.ERROR_MESSAGE);
            break;
       }
        
       return bd;
    }
    
    
    private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
        final DDRmixIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                if(algorithm.equals("DDR")){
                    
                    if(nameKernels.size()==2){
                         ckmix.buildPapplet2K(nameKernels);
                         ckmix.setVisible(true);
                    }else if(nameKernels.size()==3){
                         ckmix.buildPapplet3K(nameKernels);
                         ckmix.setVisible(true);
                    }else{
                         JOptionPane.showMessageDialog(null, "Can merge 1 to 3 kernel methods", "VisMineDR", JOptionPane.ERROR_MESSAGE);
                    }
//                }
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
            if(nameKernels.size()==2){
                ddrmix = new DDRmix(dataValues,dataValuesK1, dataValuesK2, R, B, d);
            }
            if(nameKernels.size()==3){
                ddrmix = new DDRmix(dataValues, dataValuesK1, dataValuesK2, dataValuesK3, R, G, B, d);
            }
        } catch (Exception ex) {
            Logger.getLogger(DDRmixIcon.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.format("Learn Kmix from %d samples in %dms\n", dataValuesK1.length, System.currentTimeMillis()-clock);
        ChooserEscritorio.setStatus("Learn Kmix in " + (System.currentTimeMillis()-clock));
        this.stopAnimation();    

        mnuView.setEnabled(true);
    }
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
         java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {

              // Kernels
              DRTableModel dtk1 = null;
              DRTableModel dtk2 = null;
              DRTableModel dtk3 = null;
              
              if(nameKernels.size()==2){
                dtk1 = new DRTableModel(dataValuesK1, atributosK1);
                dtk2 = new DRTableModel(dataValuesK2, atributosK2);
              }else if(nameKernels.size()==3){
                dtk1 = new DRTableModel(dataValuesK1, atributosK1);
                dtk2 = new DRTableModel(dataValuesK2, atributosK2);
                dtk3 = new DRTableModel(dataValuesK3, atributosK3);
              }
              
              ///////////

              //Eigens
              String[] ds = new String[ddrmix.getEigVectores().length];
              for(int i=0; i<ddrmix.getEigVectores().length; i++){
                  ds[i] = "d"+(i+1); 
              }

              DRTableModel eigenVectores = new DRTableModel(ddrmix.getEigVectores(), ds);
              DRTableModel eigenValores = new DRTableModel(ddrmix.getEigValores(), ds);
              //////

              //Datos de salida
              String[] dr = new String[d];
              for(int i=0; i<d; i++){
                  dr[i] = "d"+(i+1); 
              } 
              datosReducidos = ddrmix.getLowMatrix();
              DRTableModel dataO = new DRTableModel(datosReducidos, dr);  
              /////

              //tablas
              if(nameKernels.size()==2){
                  verYK.setDatas(dataIn, dtk1, dtk2, dataO, eigenVectores, eigenValores); 
                  verYK.setVisible(true);
              }else if(nameKernels.size()==3){
                  verYK.setDatas(dataIn, dtk1, dtk2, dtk3, dataO, eigenVectores, eigenValores); 
                  verYK.setVisible(true);
              }
            }
        });
    }
         
}
