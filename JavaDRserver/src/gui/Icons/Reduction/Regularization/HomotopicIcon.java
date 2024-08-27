/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.Reduction.Regularization;

import algorithm.reduction.Regularization.Behaviour;
import algorithm.reduction.Regularization.Homotopic;
import algorithm.reduction.Regularization.LGNX;
import algorithm.reduction.kernel.DDR;
import algorithm.reduction.kernel.DDRmix;
import algorithm.reduction.kernel.KLE;
import algorithm.reduction.kernel.KLLE;
import algorithm.reduction.kernel.KMDS;
import gui.Icons.Reduction.*;
import algorithm.reduction.kernel.Kmix;
import algorithm.reduction.manifold.LLE;
import algorithm.reduction.manifold.LaplacianEigenmap;
import algorithm.reduction.mds.MDS;
import algorithm.reduction.projection.KPCA;
import algorithm.reduction.projection.PCA;
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
public class HomotopicIcon extends Icon{
    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public String algorithm;
    public String metodoDR;
    
    // Parametros
    public float mu;
    public int d;
       
    // Datos
    public AbstractTableModel dataIn = null;
    public double[][] dataValues;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif = new ArrayList(1);
    
    // kernelRDlocal
    public double[][] kernelRDlocal;

    public Homotopic homotopic;
    double[][] dataOut = null;
    
     // metodos RD con los que se conecta
    public KLLE klle;
    public KLE kle;

    
    public static configureHomotopic confHomotopic;
    
    public static verDatosReg verHomotopic; //cambiarlo
    
    /** Creates a new instance of DBConnectionIcon */
    public HomotopicIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
        super.constrainsTo.add("ScatterIcon");
        
        algorithm = s.getText();

        setInfo("Regularization in process");
        System.out.println("Regularization in process");
        
        mnuConfigure = new javax.swing.JMenuItem();
        mnuConfigure.setText("Configure...");
        mnuConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigureActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuConfigure);
        mnuConfigure.setEnabled(false);
        confHomotopic = new configureHomotopic(this);
        confHomotopic.setVisible(false);
         
        mnuRun = new javax.swing.JMenuItem();
        mnuRun.setText("Run...");
        mnuRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    mnuRunActionPerformed(evt);
                } catch (Exception ex) {
                    Logger.getLogger(HomotopicIcon.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        verHomotopic = new verDatosReg(); 
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
        final HomotopicIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                confHomotopic.updateIcon(icon);
                confHomotopic.setVisible(true);
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
    
    private void mnuRunActionPerformed(java.awt.event.ActionEvent evt) throws Exception {

        JackAnimation jack = new JackAnimation();
        this.add(jack);
        this.setComponentZOrder(jack, 0);
        jack.setBounds(this.animation.getX(), this.animation.getY(), 36, 36);
        this.setAnimation(jack);

        this.startAnimation();
             
        if(metodoDR.equals("KLE")){
            kernelRDlocal = kle.getKernelLE();
        }else if(metodoDR.equals("KLLE")){
            kernelRDlocal = klle.getKernelLLE();
        }
        
        long clock = System.currentTimeMillis();

        try {
            if(dataValues == null){
                JOptionPane.showMessageDialog(this, "Link input data", "VisMineDR", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(kernelRDlocal == null){
                JOptionPane.showMessageDialog(this, "Link DR Local Kernel matrix data (KLE, KLLE)", "VisMineDR", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(dataValues.length != kernelRDlocal.length){
                JOptionPane.showMessageDialog(this, "Matrix are not supported", "VisMineDR", JOptionPane.ERROR_MESSAGE);
            }else{
               homotopic = new Homotopic(d,mu, dataValues, kernelRDlocal); // datavaluesk no es aqui toca meter los datos reducidos
            }
        } catch (Exception ex) {
            Logger.getLogger(HomotopicIcon.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.format("Learn DDR from %d samples in %dms\n", System.currentTimeMillis()-clock);
        ChooserEscritorio.setStatus("Learn Behaviour in " + (System.currentTimeMillis()-clock));
        this.stopAnimation();    

        mnuView.setEnabled(true);
    }
    
    private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
         java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {

            //Datos de salida
            String[] dr = new String[d];
            for(int i=0; i<d; i++){
                dr[i] = "d"+(i+1); 
            } 
            dataOut = homotopic.getNewCoordinates();
            DRTableModel dataO = new DRTableModel(dataOut, dr);  
            
            // RD   
            DRTableModel Ydr = new DRTableModel(kernelRDlocal, dr);

            //tablas
            verHomotopic.setDatas(dataIn, Ydr, dataO); 
            verHomotopic.setVisible(true);

            }
        });
    }
         
}
