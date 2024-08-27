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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
  

public class NetDRIcon extends Icon{
    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public String algorithm;
 
    // Datos Metodo 0
    public double[][] Ydr0; // Incrustamiento de cada método DR progenitor
    public String algorithm0; // nombre del algortimo progenitor
    public String  modelo0; // puede ser que el método DR de llegada (fase1) sea el incrustamiento directo (DRicon) o por aprendizaje (LearningIcon) o por carga de modelo aprendido (ModelIcon)
    public String tipo0;
    
    // Datos Metodo 1
    public double[][] Ydr1;
    public String algorithm1;
    public String modelo1;
    public String tipo1;
    
    // Datos Metodo 2
    public double[][] Ydr2;
    public String algorithm2;
    public String modelo2;
    public String tipo2;
    
    // Datos Metodo 3
    public double[][] Ydr3;
    public String algorithm3;
    public String modelo3;
    public String tipo3;
    
    // Datos Metodo 4
    public double[][] Ydr4;
    public String algorithm4;
    public String modelo4;
    public String tipo4;
    
    int contadorMetodos;
    List<Map<String, Object>> vectorMetodos;
   
    public Integer d = 2;
    public Integer epocas = 10;
    public double learningRate = 0.1;
    
    public AbstractTableModel dataIn = null;
    public double[][] dataValues;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif;
    public double[][] datosReducidos = null;  // incrustamiento de NETDR
    public String saveName;

    public static configureNetDR cNetDR;
    public static verDatosReducidos verY;
    public SocketServer mySocket; // Procesamiento en Python
    
    /** Creates a new instance of DBConnectionIcon */
    public NetDRIcon(JLabel s, int x, int y, int indiceIcon, SocketServer mySocket) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
        super.constrainsTo.add("ScatterIcon");
        super.constrainsTo.add("EvaluationIcon");
        
        this.mySocket = mySocket;
        
        algorithm = s.getText();
        
        contadorMetodos = 0;
        vectorMetodos = new ArrayList<>();

        setInfo("Dimensionality Reduction in process");
        System.out.println("Dimensionality Reduction in process");
        
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
        cNetDR = new configureNetDR(this);
        cNetDR.setVisible(false);
         
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
        final NetDRIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cNetDR.updateIcon(icon);
                cNetDR.setVisible(true);
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
        
        if(dataValues != null){
            
            JackAnimation jack = new JackAnimation();
            this.add(jack);
            this.setComponentZOrder(jack, 0);
            jack.setBounds(this.animation.getX(), this.animation.getY(), 36, 36);
            this.setAnimation(jack);

            this.startAnimation();
            long clock = System.currentTimeMillis();

            if(d > dataValues[0].length){
                JOptionPane.showMessageDialog(this, "The reduction dimension can not be greater than the original dimension", "NetDRtool", JOptionPane.ERROR_MESSAGE);
            }else{
                
                   double[][] incrustamientoO = new double[1][1]; // esto es solo para para la fase 1 de aprendizaje neuroanal de incrustamiento, en NetDR
                   
                   String ruta = "";
                   
                   mySocket.fit(dataValues, etiquetas, d, algorithm, vectorMetodos, incrustamientoO, saveName, epocas, learningRate, ruta);
                   datosReducidos = mySocket.getLowMatrix();


             ChooserEscritorio.setStatus("Learn NetDR in " +(System.currentTimeMillis()-clock)+ "ms");
             System.out.format("Learn NetDR from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);      
            }
            this.stopAnimation();
            

            mnuView.setEnabled(true);
        
        }else{
            JOptionPane.showMessageDialog(this, "NULL Data", "NetDRtool", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                   datosReducidos = mySocket.getLowMatrix();
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
    
    public void updateIconConfigureNetDR(){
        this.cNetDR.updateIcon(this);
    }
    
    
    public boolean setMetodos(String tipo, byte[] modelo, AbstractTableModel dataIn, String nameMetodo, double[][] dataValues, String[] atributos, String[] etiquetas, ArrayList etiquetasDif, double[][] Ydr){
        
        boolean bd = false; //para saber si se puede establecer la conexion
        
        this.dataIn = dataIn;
        this.dataValues = dataValues;
        this.atributos = atributos;
        this.etiquetas = etiquetas;
        this.etiquetasDif = etiquetasDif;
        
        String modeloBase64 = Base64.getEncoder().encodeToString(modelo);
        
        switch (contadorMetodos) {
 
            case 0: 
                Ydr0 = Ydr;
                algorithm0 = nameMetodo; // En el caso de la carga de los modelos, el algorithm realmente es la ruta al modelo
                modelo0 = modeloBase64;
                tipo0 = tipo;
                
                Map<String, Object> data0 = new HashMap<>();
                data0.put("tipo", tipo0);
                data0.put("Ydr", Ydr0);
                data0.put("metDR", algorithm0);
                data0.put("modelo", modelo0);
                vectorMetodos.add(data0);
                
                bd = true;
                break;

            case 1:
                Ydr1 = Ydr;
                algorithm1 = nameMetodo;
                modelo1 = modeloBase64;
                tipo1 = tipo;
                
                Map<String, Object> data1 = new HashMap<>();
                data1.put("tipo", tipo1);
                data1.put("Ydr", Ydr1);
                data1.put("metDR", algorithm1);
                data1.put("modelo", modelo1);
                vectorMetodos.add(data1);
                bd = true;
                break;

            case 2:
                Ydr2 = Ydr;
                algorithm2 = nameMetodo;
                modelo2 = modeloBase64;
                tipo2 = tipo;
                
                Map<String, Object> data2 = new HashMap<>();
                data2.put("tipo", tipo2);
                data2.put("Ydr", Ydr2);
                data2.put("metDR", algorithm2);
                data2.put("modelo", modelo2);
                vectorMetodos.add(data2);
                
                bd = true;
                break;
                
            case 3:
                Ydr3 = Ydr;
                algorithm3 = nameMetodo;
                modelo3 = modeloBase64;
                tipo3 = tipo;
                
                Map<String, Object> data3 = new HashMap<>();
                data3.put("tipo", tipo3);
                data3.put("Ydr", Ydr3);
                data3.put("metDR", algorithm3);
                data3.put("modelo", modelo3);
                vectorMetodos.add(data3);
                
                bd = true;
                break;
                
             case 4:
                Ydr4 = Ydr;
                algorithm4 = nameMetodo;
                modelo4 = modeloBase64;
                tipo4 = tipo;
                
                Map<String, Object> data4 = new HashMap<>();
                data4.put("tipo", tipo4);
                data4.put("Ydr", Ydr4);
                data4.put("metDR", algorithm4);
                data4.put("modelo", modelo4);
                vectorMetodos.add(data4);
                
                bd = true;
                break;

            default:
                JOptionPane.showMessageDialog(null, "Can merge 1 to 5 kernel methods", "NetDRtool", JOptionPane.ERROR_MESSAGE);
                break;
       }
        
       contadorMetodos = contadorMetodos+1;
       return bd;
    }
    
}
