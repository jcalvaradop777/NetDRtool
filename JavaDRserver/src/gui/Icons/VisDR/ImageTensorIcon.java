/*
 * AssociationIcon.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.VisDR;

import gui.Icons.Reduction.*;
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

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.util.Random;

  
/**
 *
 */
public class ImageTensorIcon extends Icon{
//    private JMenuItem mnuConfigure;
    private JMenuItem mnuRun;
    private JMenuItem mnuView;
    private JMenuItem mnuHelp;
    
    public double[][] dataValues;
    public String[] atributos;
    public String[] etiquetas;
    public ArrayList etiquetasDif = new ArrayList(1);

    // menus de configuración
    //public static configureNetDRLearning clearning;
    //    public static configureDDR cddr;
    
    public static verImagesTensor verY;
    
    public SocketServer mySocket; // Para todos los métodos DR que están en Python
    
    /** Creates a new instance of DBConnectionIcon */
    public ImageTensorIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon);
        super.constrainsTo = new ArrayList(1);
      
        setInfo("Images in process");
        System.out.println("Images in process");
        
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
//        //clearning = new configureNetDRLearning(this);
//        //clearning.setVisible(false);

        
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
       verY = new verImagesTensor();
        
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
    
//    public JMenuItem getMnuConfigure() {
//        return mnuConfigure;
//    }
    
    private void mnuConfigureActionPerformed(java.awt.event.ActionEvent evt) {
        final ImageTensorIcon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //clearning.updateIcon(icon);
                //clearning.setVisible(true);
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

        int longitud = dataValues.length;
        Random rand = new Random();
        int numeroAleatorio = 0;

        ImageIcon[] iconos = new ImageIcon[10];
        for (int i = 0; i < 10; i++) {
            numeroAleatorio = rand.nextInt(longitud);
            ImageIcon icon = convertirAImageIcon(dataValues[numeroAleatorio]);
            iconos[i] = icon;
        }
        
        verY.setVectorImage(iconos);

         this.stopAnimation();

        mnuView.setEnabled(true);
    }
    
    private ImageIcon convertirAImageIcon(double[] imageData) {
        int imageWidth = 32; // Ancho de la imagen
        int imageHeight = 32; // Alto de la imagen

        // Crear un BufferedImage en escala de grises
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                // Normalizar el valor de píxel a un rango entre 0 y 255
                double pixelValue = imageData[y * imageWidth + x] * 255;
                int rgb = (int) pixelValue; // Convertir a valor de píxel entero
                rgb = (rgb << 16) | (rgb << 8) | rgb; // RGB en escala de grises
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        // Convertir BufferedImage a ImageIcon
        return new ImageIcon(bufferedImage);
    }
    
    
    
        private void mnuViewActionPerformed(java.awt.event.ActionEvent evt) {
//        final FilterIcon ai = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               //verY.setScore();
               verY.setVisible(true);
            }
        });
    }
        
}
