///*
// * AssociationIcon.java
// *
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package gui.Icons.Reduction;
//
//import Utils.MachineLearning.math.math.kernel.GaussianKernel;
//import Utils.SocketServer;
//import algorithm.reduction.kernel.KMDS;
//import algorithm.reduction.kernel.KLE;
//import algorithm.reduction.kernel.KLLE;
//import algorithm.reduction.manifold.LLE;
//import algorithm.reduction.manifold.LaplacianEigenmap;
//import algorithm.reduction.mds.MDS;
//import algorithm.reduction.projection.KPCA;
//import algorithm.reduction.projection.PCA;
//import gui.KnowledgeFlow.ChooserEscritorio;
//import gui.KnowledgeFlow.Icon;
//import gui.KnowledgeFlow.JackAnimation;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JLabel;
//import javax.swing.JMenuItem;
//import javax.swing.JOptionPane;
//import javax.swing.table.AbstractTableModel;
//  
///**
// *
// */
//public class DRIcon11 extends Icon{
//    private JMenuItem mnuConfigure;
//    private JMenuItem mnuRun;
//    private JMenuItem mnuView;
//    private JMenuItem mnuHelp;
//    
//    public String algorithm;
//    
//    public AbstractTableModel dataIn = null;
//    public double[][] dataValues;
//    public String[] atributos;
//    public String[] etiquetas;
//    public ArrayList etiquetasDif = new ArrayList(1);
//    
//    public Integer d = 2; // MDS y PCA, para seleccionar a que dimension se reduce
//    public boolean cor; //PCA para seleccionar el metodo de scalig, correlacion o covarianza 
//    public Integer k = 0; // Para LLE Y LE
//    public Integer t = -1; // Para LE
//    public double gamma = 0.0; // Para KPCA
//    
//    public double[][] datosReducidos = null;
//    public double[][] kernelout = null; // kernel generado por KLE, KLLE Y KMDS 
//    public String[] nameColsKenerl = null; // nombre de las columnas kernel(c1, c2, c....)
//
//    // metodos
//    public MDS mds;
//    public PCA pca;
//    public LLE lle;
//    public LaplacianEigenmap le;
//    public KPCA<double[]> kpca;
//    
//    public KMDS kmds;
//    public KLE kle;
//    public KLLE klle;
//
//    // menus de configuración
//    public static configureDRbasic cmds;
//    public static configurePCA cpca;
//    public static configureLLE clle;
//    public static configureLE cle;
//    public static configureKPCA ckpca;
//    public static configureKernels ckernels;
//    
//    public static configureDRbasic cumap;
//    public static configureDRbasic ctrimap;
//    public static configureDRbasic cslisemap;
//    public static configureDRbasic cdensmap;
//    public static configureDRbasic cpumap;
//    public static configureDRbasic cisomap;
//    public static configureDRbasic cautoencoder;
//    public static configureDRbasic cspca;
//    public static configureDRbasic clda;
//    public static configureDRbasic cfa;
//    public static configureDRbasic cgraphencoder;
//    public static configureDRbasic cfica;
//    public static configureDRbasic csrp;
//    public static configureDRbasic cgrp;
//    public static configureDRbasic clpp;
//    //    public static configureDDR cddr;
//    
//    public static verDatosReducidos verY;
//    public static verDatosReducidosKernel verYK;
//    
//    public SocketServer mySocket; // Para todos los métodos DR que están en Python
//    
//    /** Creates a new instance of DBConnectionIcon */
//    public DRIcon11(JLabel s, int x, int y, int indiceIcon, SocketServer mySocket) {
//        super(s, x, y, indiceIcon);
//        super.constrainsTo = new ArrayList(1);
//        super.constrainsTo.add("ScatterIcon");
//        super.constrainsTo.add("VarianceIcon");
//        super.constrainsTo.add("DDRIcon");
//        super.constrainsTo.add("KmixIcon");
//        super.constrainsTo.add("DDRmixIcon");
//        super.constrainsTo.add("LGNXIcon");
//        super.constrainsTo.add("BehaviourIcon");
//        super.constrainsTo.add("HomotopicIcon");
//        
//        this.mySocket = mySocket;
//        
//        algorithm = s.getText();
//
//        setInfo("Dimensionality Reduction in process");
//        System.out.println("Dimensionality Reduction in process");
//        
//        // MENÚ DE CONFIGURACIÓN
//        mnuConfigure = new javax.swing.JMenuItem();
//        mnuConfigure.setText("Configure...");
//        mnuConfigure.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                mnuConfigureActionPerformed(evt);
//            }
//        });
//        
//        super.pupMenu.add(mnuConfigure);
//        mnuConfigure.setEnabled(false);
//         if(algorithm.equals("MDS")){
//            cmds = new configureDRbasic(this);
//            cmds.setVisible(false);
//         }else if(algorithm.equals("PCA")){
//            cpca = new configurePCA(this);
//            cpca.setVisible(false);
//         }else if(algorithm.equals("LLE")){
//            clle = new configureLLE(this);
//            clle.setVisible(false);
//         }else if(algorithm.equals("LE")){
//            cle = new configureLE(this);
//            cle.setVisible(false);
//         }else if(algorithm.equals("KPCA")){
//            ckpca = new configureKPCA(this);
//            ckpca.setVisible(false);
//         }else if(algorithm.equals("KMDS") || algorithm.equals("KLE") || algorithm.equals("KLLE")){
//            ckernels = new configureKernels(this);
//            ckernels.setVisible(false);
//         }else if(algorithm.equals("UMAP")){
//             cumap = new configureDRbasic(this);
//             cumap.setVisible(false);
//         }else if(algorithm.equals("TriMap")){
//             ctrimap = new configureDRbasic(this);
//             ctrimap.setVisible(false);
//         }else if(algorithm.equals("SliseMap")){
//             cslisemap = new configureDRbasic(this);
//             cslisemap.setVisible(false);
//         }else if(algorithm.equals("DensMap")){
//             cdensmap = new configureDRbasic(this);
//             cdensmap.setVisible(false);
//         }else if(algorithm.equals("PUMAP")){
//             cpumap = new configureDRbasic(this);
//             cpumap.setVisible(false);
//         }else if(algorithm.equals("IsoMap")){
//             cisomap = new configureDRbasic(this);
//             cisomap.setVisible(false);
//         }else if(algorithm.equals("AutoEncoder")){
//             cautoencoder = new configureDRbasic(this);
//             cautoencoder.setVisible(false);
//         }else if(algorithm.equals("SPCA")){
//             cspca = new configureDRbasic(this);
//             cspca.setVisible(false);
//         }else if(algorithm.equals("LDA")){
//             clda = new configureDRbasic(this);
//             clda.setVisible(false);
//         }else if(algorithm.equals("FA")){
//             cfa = new configureDRbasic(this);
//             cfa.setVisible(false);
//         }else if(algorithm.equals("GraphEncoder")){
//             cgraphencoder = new configureDRbasic(this);
//             cgraphencoder.setVisible(false);
//         }else if(algorithm.equals("FICA")){
//             cfica = new configureDRbasic(this);
//             cfica.setVisible(false);
//         }else if(algorithm.equals("SRP")){
//             csrp = new configureDRbasic(this);
//             csrp.setVisible(false);
//         }else if(algorithm.equals("GRP")){
//             cgrp = new configureDRbasic(this);
//             cgrp.setVisible(false);
//         }else if(algorithm.equals("LPP")){
//             clpp = new configureDRbasic(this);
//             clpp.setVisible(false);
//         }
//
////////         else if(algorithm.equals("DDR")){
////////            cddr = new configureDDR();
////////            cddr.setVisible(false);
////////         }
//         
//        // MENÚ DE EJECUCIÓN
//        mnuRun = new javax.swing.JMenuItem();
//        mnuRun.setText("Run...");
//        mnuRun.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                mnuRunActionPerformed(evt);
//            }
//        });
//        super.pupMenu.add(mnuRun);
//        mnuRun.setEnabled(false);
//        
//        // MENÚ DE VISUALIZACIÓN
//        mnuView = new javax.swing.JMenuItem();
//        mnuView.setText("View...");
//        mnuView.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                mnuViewActionPerformed(evt);
//            }
//        });
//        super.pupMenu.add(mnuView);
//        if(algorithm.equals("KMDS")  || algorithm.equals("KLE") || algorithm.equals("KLLE")|| algorithm.equals("KPCA")){ 
//            verYK = new verDatosReducidosKernel(); //el view de los metodos kernel tiene mas pestañas que corresponden a los datos kernel
//        }else{
//            verY = new verDatosReducidos();
//        }
//        mnuView.setEnabled(false);
//               
//        // MENÚ DE AYUDA
//        mnuHelp = new javax.swing.JMenuItem();
//        mnuHelp.setText("Help...");
//        mnuHelp.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                mnuHelpActionPerformed(evt);
//            }
//        });
//        super.pupMenu.add(mnuHelp);
//    }
//
//    public JMenuItem getMnuRun() {
//        return mnuRun;
//    }
//    
//    public JMenuItem getMnuConfigure() {
//        return mnuConfigure;
//    }
//    
////////    public void setNameKernels(String nameKernel){
//////////        if(!nameKernels.contains(nameKernel))
////////        nameKernels.add(nameKernel);
////////    }
//    
//    private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
//        final DRIcon11 icon = this;
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                if(algorithm.equals("MDS")){
//                    cmds.updateIcon(icon);
//                    cmds.setVisible(true);
//                }else if(algorithm.equals("PCA")){
//                    cpca.updateIcon(icon);
//                    cpca.setVisible(true);
//                }else if(algorithm.equals("LLE")){
//                    clle.updateIcon(icon);
//                    clle.setVisible(true);
//                }else if(algorithm.equals("LE")){
//                    cle.updateIcon(icon);
//                    cle.setVisible(true);
//                }else if(algorithm.equals("KPCA")){
//                    ckpca.updateIcon(icon);
//                    ckpca.setVisible(true);
//                }else if(algorithm.equals("KMDS") || algorithm.equals("KLE") || algorithm.equals("KLLE") ){
//                    ckernels.updateIcon(icon);
//                    ckernels.setVisible(true);
//                }else if(algorithm.equals("UMAP")){
//                    cumap.updateIcon(icon);
//                    cumap.setVisible(true);
//                }else if(algorithm.equals("TriMap")){
//                    ctrimap.updateIcon(icon);
//                    ctrimap.setVisible(true);
//                }else if(algorithm.equals("SliseMap")){
//                    cslisemap.updateIcon(icon);
//                    cslisemap.setVisible(true);
//                }else if(algorithm.equals("DensMap")){
//                    cdensmap.updateIcon(icon);
//                    cdensmap.setVisible(true);
//                }else if(algorithm.equals("PUMAP")){
//                    cpumap.updateIcon(icon);
//                    cpumap.setVisible(true);
//                }else if(algorithm.equals("IsoMap")){
//                    cisomap.updateIcon(icon);
//                    cisomap.setVisible(true);
//                }else if(algorithm.equals("AutoEncoder")){
//                    cautoencoder.updateIcon(icon);
//                    cautoencoder.setVisible(true);
//                }else if(algorithm.equals("SPCA")){
//                    cspca.updateIcon(icon);
//                    cspca.setVisible(true);
//                }else if(algorithm.equals("LDA")){
//                    clda.updateIcon(icon);
//                    clda.setVisible(true);
//                }else if(algorithm.equals("FA")){
//                    cfa.updateIcon(icon);
//                    cfa.setVisible(true);
//                }else if(algorithm.equals("GraphEncoder")){
//                    cgraphencoder.updateIcon(icon);
//                    cgraphencoder.setVisible(true);
//                }else if(algorithm.equals("FICA")){
//                    cfica.updateIcon(icon);
//                    cfica.setVisible(true);
//                }else if(algorithm.equals("SRP")){
//                    csrp.updateIcon(icon);
//                    csrp.setVisible(true);
//                }else if(algorithm.equals("GRP")){
//                    cgrp.updateIcon(icon);
//                    cgrp.setVisible(true);
//                }else if(algorithm.equals("LPP")){
//                    clpp.updateIcon(icon);
//                    clpp.setVisible(true);
//                }           
//                
////////                else if(algorithm.equals("DDR")){
////////                    
////////                    if(nameKernels.size()==2){
////////                         cddr.buildPapplet2K(nameKernels);
////////                         cddr.setVisible(true);
////////                    }else if(nameKernels.size()==3){
////////                         cddr.buildPapplet3K(nameKernels);
////////                         cddr.setVisible(true);
////////                    }else{
////////                         JOptionPane.showMessageDialog(null, "Can merge 2 to 3 kernel methods", "VisMineDR", JOptionPane.ERROR_MESSAGE);
////////                    }
////////                }
//            }
//        });
//    }
//    
//    private void mnuHelpActionPerformed(java.awt.event.ActionEvent evt) {
//        final Icon icon = this;
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
////                new Utils.Help(icon.getName().trim()).setVisible(true);
//                new Utils.miHelp("asociacion.htm").setVisible(true);
//            }
//        });
//    }
//    
//    private void mnuRunActionPerformed(java.awt.event.ActionEvent evt) {
//        
//        if(dataValues != null){
//            
//            JackAnimation jack = new JackAnimation();
//            this.add(jack);
//            this.setComponentZOrder(jack, 0);
//            jack.setBounds(this.animation.getX(), this.animation.getY(), 36, 36);
//            this.setAnimation(jack);
//
//            if(algorithm.equals("MDS")){
//
//                if(dataValues.length==dataValues[0].length){// para saber si la matriz es cuadrada como debe ser una matriz de afinidad
//                    this.startAnimation();
//                    long clock = System.currentTimeMillis(); 
//
//                    mds = new MDS(dataValues, d);
//
//                    ChooserEscritorio.setStatus("Learn MDS in " +(System.currentTimeMillis()-clock)+ "ms");
//                    System.out.format("Learn MDS from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
//                    this.stopAnimation();
//                } else{
//                    JOptionPane.showMessageDialog(this, "Matrix is not square", "NeDRtool", JOptionPane.ERROR_MESSAGE);
//                }  
//
//            }
////            else if(algorithm.equals("PCA")){
////                this.startAnimation();
////                long clock = System.currentTimeMillis();
////
////                if(d > dataValues[0].length){
////                    JOptionPane.showMessageDialog(this, "The reduction dimension can not be greater than the original dimension", "VisMineDR", JOptionPane.ERROR_MESSAGE);
////                }else{
////                    pca = new PCA(dataValues, cor);
////                    ChooserEscritorio.setStatus("Learn PCA in " +(System.currentTimeMillis()-clock)+ "ms");
////                    System.out.format("Learn PCA from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);      
////                }
////                this.stopAnimation();
////
////            }
////            else if(algorithm.equals("LLE")){
////
////                 this.startAnimation();
////
////                 long clock = System.currentTimeMillis();
////                 lle = new LLE(dataValues, d, k);
////
////                 ChooserEscritorio.setStatus("Learn LLE in " +(System.currentTimeMillis()-clock)+ "ms");
////                 System.out.format("Learn LLE from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                 this.stopAnimation();    
////
////            }else if(algorithm.equals("LE")){
////
////                 this.startAnimation();
////
////                 long clock = System.currentTimeMillis();
////                 le = new LaplacianEigenmap(dataValues, d, k, -1); ///sigma - 1
////
////                 ChooserEscritorio.setStatus("Learn LE in " +(System.currentTimeMillis()-clock)+ "ms");
////                 System.out.format("Learn LE from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                 this.stopAnimation();  
////
////            }
////            else if(algorithm.equals("KPCA")){
////
////                if (gamma == 0.0) {
////                    int n = 0;
////                    for (int i = 0; i < dataValues.length; i++) {
////                        for (int j = 0; j < i; j++, n++) {
////                            gamma += Utils.MachineLearning.math.math.Math.squaredDistance(dataValues[i], dataValues[j]);
////                        }
////                    }
////                    gamma = Utils.MachineLearning.math.math.Math.sqrt(gamma / n) / 4;
////                } 
////
////                this.startAnimation();
////                long clock = System.currentTimeMillis();
////                kpca = new KPCA<double[]>(dataValues, new GaussianKernel(gamma), d);
////                ChooserEscritorio.setStatus("Learn KPCA in " +(System.currentTimeMillis()-clock)+ "ms");
////                System.out.format("Learn KPCA from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                this.stopAnimation();  
////
////            }
////            else if(algorithm.equals("KMDS")){
////                this.startAnimation();
////                long clock = System.currentTimeMillis();
////
////                try {
////                    kmds = new KMDS(dataValues); 
////                    kernelout = kmds.getKernelMDS();
////                    
////                    nameColsKenerl = new String[kernelout.length];
////                    for(int i=0; i<kernelout.length; i++){
////                        nameColsKenerl[i] = "c"+(i+1); 
////                    }
////              
////                } catch (Exception ex) {
////                    Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                }
////
////                ChooserEscritorio.setStatus("Learn KMDS in " +(System.currentTimeMillis()-clock)+ "ms");
////                System.out.format("Learn KMDS from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                this.stopAnimation();    
////            
////            }
////            else if(algorithm.equals("KLE")){
////                this.startAnimation();
////                long clock = System.currentTimeMillis();
////
////                try {
////                    kle = new KLE(dataValues);
////                    
////                    kernelout = kle.getKernelLE();
////                    
////                    nameColsKenerl = new String[kernelout.length];
////                    for(int i=0; i<kernelout.length; i++){
////                        nameColsKenerl[i] = "c"+(i+1); 
////                    }
////              
////                } catch (Exception ex) {
////                    Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                }
////
////                ChooserEscritorio.setStatus("Learn KLE in " +(System.currentTimeMillis()-clock)+ "ms");
////                System.out.format("Learn KLE from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                this.stopAnimation();    
////            }
////            else if(algorithm.equals("KLLE")){
////                this.startAnimation();
////                long clock = System.currentTimeMillis();
////
////                try {
////                    klle=new KLLE(dataValues,50);
////                    
////                    kernelout = klle.getKernelLLE();                  
////                    
////                    nameColsKenerl = new String[kernelout.length];
////                    for(int i=0; i<kernelout.length; i++){
////                        nameColsKenerl[i] = "c"+(i+1); 
////                    }
////              
////                } catch (Exception ex) {
////                    Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                }
////
////                ChooserEscritorio.setStatus("Learn KLLE in " +(System.currentTimeMillis()-clock)+ "ms");
////                System.out.format("Learn KLLE from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);
////                this.stopAnimation();  
////            }
//             else { // PARA TODOS LOS METODOS DR QUE SE PROCESAN EN EL CLIENTE PYTHON: UMAP, TRIMAP etc...
//                this.startAnimation();
//                long clock = System.currentTimeMillis();
//
//                if(d > dataValues[0].length){
//                    JOptionPane.showMessageDialog(this, "The reduction dimension can not be greater than the original dimension", "NetDRtool", JOptionPane.ERROR_MESSAGE);
//                }else{
//                    
//                    mySocket.fit(dataValues, etiquetas, d, algorithm);
//                    datosReducidos = mySocket.getLowMatrix();
//                    kernelout = mySocket.getKernel();
//                    nameColsKenerl = new String[kernelout.length];
//                    for(int i=0; i<kernelout.length; i++){
//                        nameColsKenerl[i] = "C"+(i+1); 
//                    }
//    
//                    ChooserEscritorio.setStatus("Learn " + algorithm + " in " +(System.currentTimeMillis()-clock)+ "ms");
//                    System.out.format("Learn " + algorithm + " from %d samples in %dms\n", dataValues.length, System.currentTimeMillis()-clock);      
//                }
//                this.stopAnimation();
//            }
//
//            mnuView.setEnabled(true);
//        
//        }else{
//            JOptionPane.showMessageDialog(this, "NULL Data", "NetDRtool", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
////        final FilterIcon ai = this;
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//
//                if(algorithm.equals("MDS")){
//                    datosReducidos = mds.getCoordinates();
//
//                } 
////                else if(algorithm.equals("PCA")){
////                    pca.setProjection(d);
////                    datosReducidos = pca.project(dataValues);
////
////                }
////                else if(algorithm.equals("LLE")){
////                     datosReducidos = lle.getCoordinates();
////
////                }else if(algorithm.equals("LE")){
////                     datosReducidos = le.getCoordinates();
////
////                }
////                else if(algorithm.equals("KPCA")){
////                    datosReducidos = kpca.getCoordinates();
////
////                }
////                else if(algorithm.equals("KMDS")){
////                    try {
////                        //            datosReducidos = kmds.getKernelMDS();
////                        datosReducidos = kmds.getMDSRD(d); //obtiene la version reducida dependiendo de la dimension
////                    } catch (Exception ex) {
////                        Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                
////                }
////                else if(algorithm.equals("KLE")){
////                    try {                      
////                        datosReducidos = kle.getLERD(d); //obtiene la version reducida dependiendo de la dimension
////                    } catch (Exception ex) {
////                        Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                
////                }
////                else if(algorithm.equals("KLLE")){
////                    try {                      
////                        datosReducidos = klle.getLLERD(d); //obtiene la version reducida dependiendo de la dimension
////                    } catch (Exception ex) {
////                        Logger.getLogger(DRIcon.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                    
////                }
//                else{ // PARA TODOS LOS METODOS DR NUEVOS QUE UTILIZAN EL CLIENTE PYTHON: UMAP, TRIMAP etc...
//                        datosReducidos = mySocket.getLowMatrix();
//                        kernelout = mySocket.getKernel();
//                }
//                
//                llenarDatosTablas();
//        
//            }
//        });
//    }
//        
//    public void llenarDatosTablas(){
//   
//      if(algorithm.equals("KMDS") || algorithm.equals("KLE") || algorithm.equals("KLLE")|| algorithm.equals("KPCA") ){  // Para la pestaña de kernel
//          String[] dr = new String[d];
//          for(int i=0; i<d; i++){
//              dr[i] = "d"+(i+1); 
//          }
//          DRTableModel dataO = new DRTableModel(datosReducidos, dr);  
//          DRTableModel kernel = new DRTableModel(kernelout, nameColsKenerl);
////          DRTableModel dataRD = new DRTableModel(kmds.getMDSRD(d), ds);
//          verYK.setDatas(dataIn, dataO, kernel); 
//          verYK.setVisible(true);
//
//      }
////      else if(algorithm.equals("PCA")){ //El view de los metodos kernel tiene mas pestañas por que se visualizan los eigenvectore y eigenvalores
////          
////          // para llenar la tabla de datos de salida
////          String[] ds = new String[d];
////          for(int i=0; i<d; i++){
////              ds[i] = "d"+(i+1); 
////          }
////          DRTableModel dataO = new DRTableModel(datosReducidos, ds);  
////          
////          //para llenar las tablas eigen
////          String[] cs = new String[pca.getEigVectores().length];
////          for(int i=0; i<pca.getEigVectores().length; i++){
////              cs[i] = "d"+(i+1); 
////          }
////          
////          DRTableModel eigenVectores = new DRTableModel(pca.getEigVectores(), cs);
////          DRTableModel eigenValores = new DRTableModel(pca.getEigValores(), cs);
////
////          verYK.setDatas(dataIn, dataO, eigenVectores, eigenValores); 
////          verYK.setVisible(true);
////
////      }
//      
//      else{  // POR LOS DEMÁS MÉTODOS QUE NO TIENEN KERNEL
//          String[] ds = new String[d];
//          for(int i=0; i<d; i++){
//              ds[i] = "d"+(i+1); 
//          }
//          DRTableModel dataO = new DRTableModel(datosReducidos, ds);    
//
//          verY.setDatas(dataIn, dataO); 
//          verY.setVisible(true);
//      }
//    }
//    
//    public void updateIconConfigureMDS(){
//        this.cmds.updateIcon(this); // aqui le paso los valores a visualizador
//    }
//}
