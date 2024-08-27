/*
 * Weka.java
 *
 * Created on 29 de agosto de 2007, 11:27 PM
 */

package gui.manual;

/**
 *
 * @author  TNRsoft
 */
public class Weka extends javax.swing.JPanel {
    
    /** Creates new form Weka */
    public Weka() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel17 = new javax.swing.JLabel();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jTextArea5 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 18));
        jLabel17.setForeground(new java.awt.Color(58, 123, 252));
        jLabel17.setText("Visualizaci\u00f3n Arbol Weka");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14));
        jTextArea1.setRows(5);
        jTextArea1.setText("permite ver las reglas en forma de \u00e1rbol, como  la herramienta Weka. ");
        jTextArea1.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/arbolWeka.png")));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/iconoWeka.png")));

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14));
        jTextArea2.setRows(5);
        jTextArea2.setText("El \u00e1rbol se lo puede mover dando click sostenido izquierdo, adem\u00e1s cuenta con un submen\u00fa, dando click derecho:");
        jTextArea2.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/menuWeka.png")));

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14));
        jTextArea3.setRows(5);
        jTextArea3.setText("- Ir al Nodo Central: \n  Ubica el \u00e1rbol en el nodo central, sin importar donde se encuentra\n\n- Ajustar a Ventana: Ajusta el \u00e1rbol al ancho de la pantalla, as\u00ed :\n");
        jTextArea3.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/arbolHorizontal.png")));

        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14));
        jTextArea4.setRows(5);
        jTextArea4.setText("-  Desplegar \u00c1rbol: extiende todas las ramas del \u00e1rbol:");
        jTextArea4.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/arbolVertical.png")));

        jTextArea5.setColumns(20);
        jTextArea5.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 14));
        jTextArea5.setRows(5);
        jTextArea5.setText("-  La opcion Tama\u00f1o letra modifica el tama\u00f1o de la letra.\n\nAdem\u00e1s este \u00e1rbol cuenta con la opci\u00f3n de mirar un diagrama de pastel de cada nodo, \nen donde se representa el porcentaje de distribuci\u00f3n de los datos seg\u00fan el atributo clase, \nesto se obtiene dando click sobre el nodo azul,  los nodos  ya vistos se marcan con un color verde.");
        jTextArea5.setCaretColor(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Herramienta/pastelWeka.png")));
        jLabel6.setText("jLabel6");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .add(jLabel17)
                .add(531, 531, 531))
            .add(layout.createSequentialGroup()
                .add(371, 371, 371)
                .add(jLabel2)
                .addContainerGap(340, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextArea2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(37, 37, 37)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextArea4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextArea3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addContainerGap(60, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(66, 66, 66)
                .add(jLabel5)
                .addContainerGap(211, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(160, 160, 160)
                .add(jLabel3)
                .addContainerGap(400, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextArea1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(352, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 783, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jTextArea5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(167, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(130, 130, 130)
                .add(jLabel6)
                .addContainerGap(303, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(jLabel2)
                .add(14, 14, 14)
                .add(jTextArea1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 485, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(61, 61, 61)
                .add(jTextArea2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(21, 21, 21)
                .add(jLabel3)
                .add(94, 94, 94)
                .add(jTextArea3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(66, 66, 66)
                .add(jLabel4)
                .add(53, 53, 53)
                .add(jTextArea4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22)
                .add(jLabel5)
                .add(52, 52, 52)
                .add(jTextArea5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(26, 26, 26)
                .add(jLabel6)
                .addContainerGap(145, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    // End of variables declaration//GEN-END:variables
    
}
