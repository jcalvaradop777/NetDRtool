/*
 * FileIcon.java
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gui.Icons.File;

import Utils.DataSet;
import Utils.FileManager;
import gui.Icons.Association.AssociationIcon;
import gui.Icons.VisDR.ImageTensorIcon;
import gui.KnowledgeFlow.ChooserEscritorio;
import gui.KnowledgeFlow.Icon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.table.AbstractTableModel;

public class FileIcon extends Icon implements Serializable{
    private static final long serialVersionUID = -8747548706885792656L;
    
//    private static final long serialVersionUID = 1; 
    
    private JMenuItem mnuOpen;
    private JMenuItem mnuLoad;
    private JMenuItem mnuHelp;
    
    public String filePath;
    public String nameFile;
    public DataSet dataset;
    public boolean xcon;
    public boolean isMarketBasket; 
    public FileTableModel data; // le cambie este, era abastractTableModel

    public static miOpenFile of;  // la pongo estatica para que solo sea reconocida en la clase y no se pase de obj en obj, de lo contrario me pone problemas en la serializacion
    public static miExampleFile ef;
    
    public String fileName;
    
    //public FileTableModel fileTable;
   
    
    /** Creates a new instance of FileIcon */
    public FileIcon(JLabel s, int x, int y, int indiceIcon) {
        super(s, x, y, indiceIcon); // falta ponerle el indiceicon

        this.fileName = s.getName();
        
        super.constrainsTo = new ArrayList();
        super.constrainsTo.add("FilterIcon");
        super.constrainsTo.add("AssociationIcon");
        super.constrainsTo.add("PredictionIcon");
        super.constrainsTo.add("StandarizeIcon");
        super.constrainsTo.add("DRIcon");
        super.constrainsTo.add("DDRIcon");
        super.constrainsTo.add("KmixIcon");
        super.constrainsTo.add("DDRmixIcon");
        super.constrainsTo.add("ScatterIcon");
        super.constrainsTo.add("LGNXIcon");
        super.constrainsTo.add("BehaviourIcon");
        super.constrainsTo.add("HomotopicIcon");
        super.constrainsTo.add("ImageTensorIcon");
        
        filePath = "";
        dataset = null;
        xcon = false;
        isMarketBasket= false;
        
        if(fileName.equals("plaintext")){
            of = new miOpenFile(this);
            of.setVisible(false);
        } else if(fileName.equals("Example")){
            ef = new miExampleFile(this);
            ef.setVisible(false);
        }
        
        mnuOpen = new JMenuItem();
        mnuOpen.setText("Open...");
        mnuOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mnuOpenActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuOpen);
        

        mnuLoad = new JMenuItem();
        mnuLoad.setText("Load...");
        mnuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mnuLoadActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuLoad);
        mnuLoad.setEnabled(false);
        
        mnuHelp = new JMenuItem();
        mnuHelp.setText("Help...");
        mnuHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mnuHelpActionPerformed(evt);
            }
        });
        super.pupMenu.add(mnuHelp);
    }
    
    public JMenuItem getMnuOpen() {
        return mnuOpen;
    }
    
    public JMenuItem getMnuLoad() {
        return mnuLoad;
    }
     
    private void mnuOpenActionPerformed(ActionEvent evt) {
      final FileIcon icon = this;
      java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
                   
            if(fileName.equals("plaintext")){
                of.updateIcon(icon);
                of.setVisible(true);
            } else if(fileName.equals("Example")){
                ef.updateIcon(icon);
                ef.setVisible(true);
            }
        }
      });
    }
    
    private void mnuHelpActionPerformed(ActionEvent evt) {
        final Icon icon = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Utils.Help(icon.getName().trim()).setVisible(true);
                new Utils.miHelp("plaintext.htm").setVisible(true);
            }
        });
    }
    
    private void mnuLoadActionPerformed(ActionEvent evt) {
        final FileManager fm = new FileManager(filePath);
        final FileIcon ficon = this;
        Thread load = new Thread(new Runnable() {
            public void run() {
                Iterator it = tos.iterator();
                while (it.hasNext()) {
                    Icon icon = (Icon) it.next();
                    if(icon instanceof AssociationIcon) {
                        System.out.println("assoc");
                        if (isMarketBasket) {
                            System.out.println("market");
                            dataset = fm.buildUnivaluedDataSet();
                            ((AssociationIcon) icon).dataset = dataset;
                            dataset.showNTree();
                        } else {
                            dataset = buildDataSet();
                            ((AssociationIcon) icon).dataset = dataset;
                        }
                        xcon = true;
                        
                    }
                    stopAnimation();
                    // Construir TableModel cuando un filtro esta conectado con
                    // un archivo plano.
                    /*else if (icon instanceof FiltersIcon) {
                       ((FiltersIcon) icon).matrix = fm.buildDataMatrix();
                        xcon = true;
                    }*/
                }
                // Si nadie esta conectado con el archivo plano crear dataset
                // por defecto.
                if (!xcon) {
                    if (isMarketBasket) {
                        dataset = fm.buildUnivaluedDataSet();
                        //dataset.showNTree();
                    } else {
                        dataset = buildDataSet();
                        //dataset.showNTree();
                    }
                    stopAnimation();
                }
                ChooserEscritorio.setStatus("Load " + dataset.getNtransactions() + " instances.");
                ficon.setInfo("Load " + dataset.getNtransactions() + " instances.");
            }
        });
        this.startAnimation();
        load.start();
        //fileTable = of.getTableModel();
        //Chooser.setStatus("Load " + fileTable.getRowCount() + " instances.");
        //this.setInfo("Load " + fileTable.getRowCount() + " instances.");
    }
    
    
//     public FileTableModel getTableModel(){
//        FileTableModel fileTable = new FileTableModel(filePath);
//        return fileTable;
//    }
    
    public DataSet buildDataSet(){
        dataset = new DataSet("");
        ArrayList dictionary;
        
        dictionary = this.getDictionaryFromTableModel();
        dataset.buildMultiValuedDictionary(dictionary);
//        data = of.getModel();
        
        for(int i = 0; i < data.getRowCount(); i++){
            for(int j = 0; j < data.getColumnCount(); j++){
                if(data.getValueAt(i, j) != null){                 
                    String value = data.getColumnName(j) + "=" + data.getValueAt(i, j).toString();
                    short item = dataset.codeAttribute(value);
                    int id = (j == data.getColumnCount() - 1) ? -1 : j;
                    dataset.buildNTree(item, id);
                }
            }
        }
        
        return dataset;
    }
    
    private ArrayList getDictionaryFromTableModel() {
        ArrayList dictionary = new ArrayList();
        int rows = data.getRowCount();
        int columns = data.getColumnCount();
        for(int column = 0; column < columns; column++){
            String columnName = data.getColumnName(column);
            ArrayList attributes = this.getAttributes(rows, column);
            Iterator it = attributes.iterator();
            while(it.hasNext()){
//                String attribute = (String)it.next();
                dictionary.add(columnName + "=" + it.next());
            }
        }
        
        return dictionary;
    }
    
    private ArrayList getAttributes(int rows, int column){
        ArrayList values = new ArrayList();
        for(int row = 0; row < rows; row++){
            Object key = data.getValueAt(row, column);
            int index = Collections.binarySearch(values, key.toString());
            if(index < 0){
                values.add((-(index) - 1), key.toString());
            }
        }
        return values;
    }
    
    public void updtateIconOpenFile(){
        this.of.updateIcon(this); // aqui le paso los valores a visualizador
    }
}
