/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.VisDR;

import Utils.AvlTree;
import Utils.DataSet;
import Utils.MachineLearning.graph.Graph;
import Utils.MachineLearning.plot.plot.Palette;
import Utils.MachineLearning.plot.plot.PlotCanvas;
import Utils.MachineLearning.plot.plot.ScatterPlot;
import Utils.SocketServer;
import algorithm.reduction.Regularization.Behaviour;
import algorithm.reduction.Regularization.Homotopic;
import algorithm.reduction.Regularization.LGNX;
import algorithm.reduction.kernel.DDR;
import algorithm.reduction.kernel.DDRmix;
import algorithm.reduction.kernel.Kmix;
import algorithm.reduction.kernel.KMDS;
import algorithm.reduction.kernel.KLLE;
import algorithm.reduction.kernel.KLE;
import algorithm.reduction.manifold.LLE;
import algorithm.reduction.manifold.LaplacianEigenmap;
import algorithm.reduction.mds.MDS;
import algorithm.reduction.projection.KPCA;
import algorithm.reduction.projection.PCA;
import gui.Icons.DBConnection.DBConnectionIcon;
import gui.Icons.Rules.RulesIcon;
import gui.KnowledgeFlow.ChooserEscritorio;
import gui.KnowledgeFlow.Icon;
import gui.KnowledgeFlow.JackAnimation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 */
public class ScatterIcon extends Icon{
    
    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    private JPanel pane;
    private String title;
     
    public String algorithm;
    
    public MDS mds;
    public PCA pca;
    public LLE lle;
    public LaplacianEigenmap le;
    public KPCA<double[]> kpca;
    public KMDS kmds;
    public KLLE klle;
    public KLE kle;
    public Kmix kmix;
    public DDR ddr;
    public DDRmix ddrmix;
    public LGNX lgnx;
    public Behaviour behaviour;
    public Homotopic homotopic;
    
    public SocketServer mySocket; // Para todos los métodos DR de Python
    
    public double[][] dataIn;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif = new ArrayList(1);
    
    public Integer d = 2;
    public String selLabel = "Nominal";
    public char  pointLegend = '*';
    public boolean viewGraph = false;
    
    public static configureVisConD cVisConD; // con configuración de dimensión
    public static configureVisConGraph cVisConGraph; // con grafico de grafo
    public static configureVisSinD cVisSinD; // sin configuración de dimensión

    /** Creates a new instance of DBConnectionIcon */
    public ScatterIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
        super.constrainsTo.add("RulesIcon");

        setInfo("Scatter in process");
        System.out.println("Scatter in process");
        
        mnuConfigure = new javax.swing.JMenuItem();
        mnuConfigure.setText("Configure...");
        mnuConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigureActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuConfigure);
        mnuConfigure.setVisible(false);
        mnuConfigure.setEnabled(false);
        
        cVisConD = new configureVisConD(this);
        cVisConD.setVisible(false);

        cVisConGraph = new configureVisConGraph(this);
        cVisConGraph.setVisible(false);
        
        cVisSinD = new configureVisSinD(this);
        cVisSinD.setVisible(false);
        
        
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
    
    public JMenuItem getMnuConfigure() {
        return mnuConfigure;
    }

    public JMenuItem getMnuView() {
        return mnuView;
    }
    
    public JMenuItem getMnuRun() {
        return mnuRun;
    }
     
     private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
        final ScatterIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if(algorithm.equals("PCA")){                 
                    cVisConD.updateIcon(icon);
                    cVisConD.setVisible(true);
                    
                if(etiquetas==null){
                       cVisConD.getCombo().setVisible(false);
                    }
                    
                }
//                else if(algorithm.equals("LLE") || algorithm.equals("LE")){
//                    cVisConGraph.updateIcon(icon);
//                    cVisConGraph.setVisible(true);
//                }
                else if( algorithm.equals("DDRmix") || algorithm.equals("DDR")|| algorithm.equals("KCMDS") || algorithm.equals("KLE") || algorithm.equals("KLLE")|| algorithm.equals("Kmix") || algorithm.equals("conEtiquetas") || algorithm.equals("MDS") || algorithm.equals("LGNX") || algorithm.equals("Behaviour") || algorithm.equals("Homotopic")){ 
                    cVisSinD.updateIcon(icon);
                    cVisSinD.setVisible(true);
                }else{ // Metodos en los que se puede configurar la dimiensión a la que se quiere reducir  (CREO QUE VAN A SER LA MAYORIA SIN D)
                    cVisSinD.updateIcon(icon);
                    cVisSinD.setVisible(true);
                }
            }
        });
    }
     
    private void mnuRunActionPerformed(java.awt.event.ActionEvent evt) {
        
        String mensaje = "";
        double[][] y = null;
        Graph graph = null;
        PlotCanvas plot = null;
        
        if(d < 2 || d > 3){
                JOptionPane.showMessageDialog(this, "It is not possible to plot the assigned dimension", "NetDRtool", JOptionPane.ERROR_MESSAGE);
        }else{
    
        pane = new JPanel(new GridLayout(1, 2));
        
        JackAnimation jack = new JackAnimation();
        this.add(jack);
        this.setComponentZOrder(jack, 0);
        jack.setBounds(this.animation.getX(), this.animation.getY(), 36, 36);
        this.setAnimation(jack);
        this.startAnimation();
        
        if(algorithm.equals("sinEtiquetas")){
            y = dataIn;
            mensaje = ("Plain Text");
            title = "Original Data";

        // conEtiqueteas se utiliza cuando se conecta directamente el scater a 
        //la fuente de datos, es decir, no se aplico ningun metodo, pero si se 
        //selecciono un atributo label
        }else if(algorithm.equals("conEtiquetas")){ 
            y = dataIn;
            mensaje = ("Filters");
            title = "Original Data";
            
        }else if(algorithm.equals("MDS")){
            y = mds.getCoordinates();
            plot = ScatterPlot.plot(y, etiquetas);
            
            mensaje = ("MDS (d = "+d+")");
            title = "Classical Multi-Dimensional Scaling";
        
        } 
//        else if(algorithm.equals("PCA")){
//            pca.setProjection(d);
//            y = pca.project(dataIn);
//            mensaje = ("PCA (d = "+d+")");
//            title = "Principal Component Analysis";
//        }
//        else if(algorithm.equals("LLE")){
//             y = lle.getCoordinates();
//             graph = lle.getNearestNeighborGraph();
//             mensaje = ("LLE (d = "+d+")");
//             title = "Locally Linear Embedding";
////             etiquetas=null;       
//        }else if(algorithm.equals("LE")){
//            
//            y = le.getCoordinates();
//             graph = le.getNearestNeighborGraph();
//             mensaje = ("LE (d = "+d+")");
//             title = "Laplacian Eigenmap";
////             etiquetas=null; // para que lo grafique en blnco y negro, revisar... si se quire visualizar en color igual en lle
//        }
//        else if(algorithm.equals("KPCA")){
//            y = kpca.getCoordinates();
//            mensaje = ("KPCA (d = "+d+")");   
//            title = "Kernel Principal Component Analysis";
//            
//        }
//        else if(algorithm.equals("KMDS")){
//            try {
//                y = kmds.getMDSRD(d);
//            } catch (Exception ex) {
//                Logger.getLogger(ScatterIcon.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            mensaje = ("KMDS (d = "+d+")");   
//            title = "Kernel PMulti-Dimensional Scaling";
//            
//        }
//        else if(algorithm.equals("KLE")){
//            try {
//                y = kle.getLERD(d);
//            } catch (Exception ex) {
//                Logger.getLogger(ScatterIcon.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            mensaje = ("KLE (d = "+d+")");   
//            title = "Kernel Laplacian Eigenmaps";
//            
//        }
//        else if(algorithm.equals("KLLE")){
//            try {
//                y = klle.getLLERD(d);
//            } catch (Exception ex) {
//                Logger.getLogger(ScatterIcon.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            mensaje = ("KLLE (d = "+d+")");   
////            title = "Kernel Locally Linear Embeding";
//            
//        }
        else if(algorithm.equals("Kmix")){
            y = kmix.getLowMatrix();
            mensaje = ("Kmix (d = "+d+")");   
            title = "Kernel Mixture Dimensionality Reduction";
            
        }else if(algorithm.equals("MKL")){
            y = kmix.getLowMatrix();
            mensaje = ("MKL (d = "+d+")");   
            title = "Kernel Mixture Dimensionality Reduction";
            
        }else if(algorithm.equals("DDR")){
            y = ddr.getLowMatrix();
            mensaje = ("DDR (d = "+d+")");   
            title = "Data Driven Dimensionality Reduction";
            
        }else if(algorithm.equals("DDRmix")){
            y = ddrmix.getLowMatrix();
            mensaje = ("DDRmix (d = "+d+")");   
            title = "Data Driven Mix Dimensionality Reduction";
        }else if(algorithm.equals("LGNX")){
            y = lgnx.getNewCoordinates();
            mensaje = ("LGNX (d = "+d+")");   
            title = "Regularization for LGNX Dimensionality Reduction";
        }else if(algorithm.equals("Behaviour")){
            y = behaviour.getNewCoordinates();
            mensaje = ("Behaviour (d = "+d+")");   
            title = "Regularization for Behaviour Dimensionality Reduction";
        }else if(algorithm.equals("Homotopic")){
            y = homotopic.getNewCoordinates();
            mensaje = ("Homotopic (d = "+d+")");   
            title = "Regularization for Homotopic Dimensionality Reduction";
        
        }else // PARA EL RESTO DE MÉTODOS DR PROCESADOS EN PYTHON
            try {
                y = mySocket.getLowMatrix();
            } catch (Exception ex) {
                Logger.getLogger(ScatterIcon.class.getName()).log(Level.SEVERE, null, ex);
            }
            mensaje = (algorithm + "(d = "+d+")");   
            title = algorithm;
        /////// PLOT
        
        plot = new PlotCanvas(Utils.MachineLearning.math.math.Math.colMin(y), Utils.MachineLearning.math.math.Math.colMax(y));
        
        if(etiquetas==null){
            plot.points(y, pointLegend); 
        }else{
            if(selLabel=="Nominal"){
                // forma 1: con los nombres de las etiquetas
                plot.points(y, etiquetas);
            }else if(selLabel=="Colour"){              
                // forma 2: con codigo de colores y puntos
                
                if(etiquetasDif.size()<40){ 
                    // para codificar las etiquetas y ponerles colores
                    int[] codEtiquetas = new int[etiquetas.length];
                    for(int i = 0; i < etiquetas.length; i++) {
                        codEtiquetas[i] = etiquetasDif.indexOf(etiquetas[i]);
                    }
                    
                    // pone en la cordenada y[i] un punto(determinado por pointLegend) con un color depediendo del codigo de etiqueta asignado
                    for (int i = 0; i < y.length; i++) {
                        plot.point(pointLegend, Palette.COLORS[codEtiquetas[i]], y[i]);
                   }

                    //se crea una talba con el codigo del color y su respectiva etiqueta
                    Object [][]data = new Object[etiquetasDif.size()][2];
                    for(int i=0; i<etiquetasDif.size(); i++){
                        data[i][0] = i;
                        data[i][1] = etiquetasDif.get(i);
                    }
                    dataLegend dataLegend = new dataLegend(data);
                    plot.setLegend(dataLegend);                   
                     
                }else{
                    JOptionPane.showMessageDialog(this, "The number of labels is greater than the color palette", "NetDRtool", JOptionPane.ERROR_MESSAGE);
                }
            }else if(selLabel=="Black"){
                // forma 3: en blanco y negro
                plot.points(y, pointLegend); 
            }else if(selLabel=="RGB"){ // la definicion de colores RGB tiene que ser explicita en la tabla de datos
                // forma 3: en blanco y negro
               for (int i = 0; i < y.length; i++) { 
                    
                    String strDatos= etiquetas[i];
                    StringTokenizer tokens = new StringTokenizer(strDatos, "*");
                    
                    //Es neceario saber si la cadena se parte en 3 partes(RGB) utilizando el token *
                    // si la cadena no esta dividida en 3 partes significa que no tiene el formato 100*100*100
                    if(tokens.countTokens()==3){
                        Integer[] datos = new Integer[3];
                        int c=0;
                        while(tokens.hasMoreTokens()){
                            String str = tokens.nextToken();
                            datos[c] = Integer.valueOf(str);
                            c++;
                        }
                        plot.point(pointLegend, new Color(datos[0], datos[1], datos[2]), y[i]);
                    }else{
                        JOptionPane.showMessageDialog(this, "The data format is not compatible with the RGB format of VisMineDR: 100 * 100 * 100", "NetDRtool", JOptionPane.ERROR_MESSAGE);
                        break;
                    } 
                }
            }
        }
        
         //para dibujar los grafos en el caso de le y lle
         if(viewGraph){
             plot.points(y, pointLegend, Color.RED);
             int n = y.length;

             for (int i = 0; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (graph.hasEdge(i, j)) {
                        plot.line(y[i], y[j]);
                    }
                }
            }
         }
        
        plot.setTitle(mensaje);
        putAxisLabels(algorithm, plot);
        pane.add(plot); 
        this.stopAnimation();
        mnuView.setEnabled(true);
        
       /////// 
        
      } // else de d>3     
    }
    
    private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
        final ScatterIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame(title);
                f.setSize(new Dimension(700, 700));
                f.setLocationRelativeTo(null);
        //                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.getContentPane().add(pane);
                f.setVisible(true);
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
    
   private void putAxisLabels(String algorithm, PlotCanvas plot){
       if(algorithm.equals("sinEtiquetas") || algorithm.equals("conEtiquetas")){ 
           for(int i = 0; i < atributos.length; i++) {
              plot.setAxisLabel(i, plot.getAxisLabel(i)+" :" + atributos[i]);
           }
       }else{ // caso contrario es si los datos son generados por métodos 
           for(int i=0; i<d; i++){
               plot.setAxisLabel(i, plot.getAxisLabel(i)+" :" + "d"+(i+1));
           }
       }
   }    
}