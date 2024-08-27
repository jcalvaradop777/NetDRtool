/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.Reduction;

import Utils.MachineLearning.math.math.kernel.GaussianKernel;
import Utils.SocketServer;
import algorithm.reduction.kernel.KMDS;
import algorithm.reduction.kernel.KLE;
import algorithm.reduction.kernel.KLLE;
import algorithm.reduction.manifold.LLE;
import algorithm.reduction.manifold.LaplacianEigenmap;
import algorithm.reduction.mds.MDS;
import algorithm.reduction.projection.KPCA;
import algorithm.reduction.projection.PCA;
import gui.KnowledgeFlow.ChooserEscritorio;
import gui.KnowledgeFlow.Icon;
import gui.KnowledgeFlow.JackAnimation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
  
/**
 *
 */
public class NetDRmodelIcon extends Icon{
    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public String ruta;
    public double[][] datosReducidos;
    
    public AbstractTableModel dataIn = null;
    public double[][] dataValues;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif = new ArrayList(1);
    public String algorithm = "Model";
    
    public Integer d = 2;

    // menus de configuración
    public static configureNetDRModel cmodel;
    //    public static configureDDR cddr;
    
    public static verDatosReducidos verY;
    
    public SocketServer mySocket; // Para todos los métodos DR que están en Python
    
    public byte[] modelo;
    
    /** Creates a new instance of DBConnectionIcon */
    public NetDRmodelIcon(JLabel s, int x, int y, int indiceIcon, SocketServer mySocket) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
        super.constrainsTo.add("NetDRIcon");
        super.constrainsTo.add("ScatterIcon");
        super.constrainsTo.add("EvaluationIcon");

        this.mySocket = mySocket;
        
        setInfo("Load neural model in process");
        System.out.println("Load neural model in process");
        
        // MENÚ DE CONFIGURACIÓN
        mnuConfigure = new javax.swing.JMenuItem();
        mnuConfigure.setText("Configure...");
        mnuConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigureActionPerformed(evt);
            }
        });
        
        super.pupMenu.add(mnuConfigure);
        mnuConfigure.setEnabled(false);
        cmodel = new configureNetDRModel(this);
        cmodel.setVisible(false);

        
        // MENÚ DE EJECUCIÓN
        mnuRun = new javax.swing.JMenuItem();
        mnuRun.setText("Run...");
        mnuRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRunActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuRun);
        mnuRun.setEnabled(false);
        
        // MENÚ DE VISUALIZACIÓN
        mnuView = new javax.swing.JMenuItem();
        mnuView.setText("View...");
        mnuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuViewActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuView);
       verY = new verDatosReducidos();
        
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

    public JMenuItem getMnuRun() {
        return mnuRun;
    }
    
    public JMenuItem getMnuConfigure() {
        return mnuConfigure;
    }
    
    private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
        final NetDRmodelIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cmodel.updateIcon(icon);
                cmodel.setVisible(true);
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

        double[][] incrustamientoO = new double[1][1]; 
        incrustamientoO[0][0] = 0;
        String saveName = "";
        List<Map<String, Object>> vectorMetodos = new ArrayList<>();
        Integer epocas = 0;
        double learningRate = 0;
                    
        mySocket.fit(dataValues, etiquetas, d, algorithm, vectorMetodos, incrustamientoO, saveName, epocas, learningRate, ruta);
        datosReducidos = mySocket.getLowMatrix();
        modelo = mySocket.getModelo();

        ChooserEscritorio.setStatus("Load model in " +(System.currentTimeMillis()-clock)+ "ms");
        System.out.format("Load model in " +(System.currentTimeMillis()-clock)+ "ms");      

        this.stopAnimation();

        mnuView.setEnabled(true);
    }
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                llenarDatosTablas();
            }
        });
    }
        
        public void llenarDatosTablas(){
          String[] ds = new String[d];
          for(int i=0; i<d; i++){
              ds[i] = "d"+(i+1); 
          }
          DRTableModel dataO = new DRTableModel(datosReducidos, ds);    

          verY.setDatas(dataIn, dataO); 
          verY.setVisible(true);
    }
    
}
