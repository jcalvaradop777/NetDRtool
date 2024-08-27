/**
 *
 * @author JuanKDD
 */

package gui.Icons.Reduction.PAppletKmix;

import processing.core.*; 

public class PAppletKmix2 extends PApplet {

PImage modelo; //imagen del triangulo
int Vpon; //se inicializa el vector de ponderacion 
float R,G,B; // se define variables en donde se guardan los valores RGB

int [] colab1 = new int[3]; // se define un vector de colores para los labels 

////////////////
String mk1 = "";
String mk2 = "";

float xc = 0, yc = 0; // para capturar las cordnadas del circulo de seleccion de color

@Override
public void setup(){
   size(600, 600); 
   modelo = loadImage("src/gui/Icons/Reduction/PAppletKmix/RB.jpeg"); // se guarda la imagen en la variable modelo
   
   // colores para las barras de ponderacion
   colab1[0] = color(255,0,0); 
   colab1[1] = color(0,255,0); 
   colab1[2] = color(0,0,255); 
}

@Override
public void draw(){
    
 background(255); // color del fondo de la pantalla 
 
 // Pone el triangulo y sus etiquetas
 fill(0); // color blanco 
 textSize(20); // tamano de la letra
 text("Selected Color",110,50); // se imprime el text   
    
 text(mk1,130,100);
 text(mk2,130,150+(modelo.height));
 
 image(modelo,130,120);
 // fin triangulo

 // dibuja el whighted factor
 stroke(125,125,125);
 strokeWeight(10);
 noFill();
 rect(modelo.width+220,190,140,180);
 fill(0); // color blanco
 textSize(20); // tamano de la letra
 text("weighted factors",modelo.width+210,170);
 
//// se dibuja barras de ponderacion
 noStroke(); // no se dibuja el contorno del rectangulo
 
 // color azul
 fill(colab1[2]);
 rect(modelo.width+300,330,20,-2-(100)*(B)); // se dibuja el rectangulo azul, LE
 fill(0); // color negro
 textSize(10); // tama\u00f1o del texto
 text("K1",modelo.width+265,220);
 text(B,modelo.width+255,355); // se imprime el texto de de blue LE
 
 // color rojo  
 fill(colab1[0]); 
 rect(modelo.width+260,330,20,-2-100*(R)); // se dibuja el rectangulo rojo, LLE
 fill(0); // color negro
 textSize(10); // tama\u00f1o de letra
 text("K2",modelo.width+305,220); // se imprime el text
 text(R,modelo.width+294,355); // se imprime el text
 
 // caja de combinacion de color
 fill(0); 
 textSize(20);
 text("Choice",modelo.width+255,50);
 stroke(125,125,125);
 fill(Vpon);
 rect(modelo.width+220,70,140,50); 
 
 stroke(255,255,255);
 ellipse(xc,yc,2,2);
}

@Override
public void mousePressed (){ // si el mouse es presionado dentro de la superficie recoge los valores de RGB

   float tc,diff;
   
   if(mouseX>=130 && mouseX<(130+modelo.width) && mouseY>120 && mouseY<=(120+modelo.height)){

       Vpon = get(mouseX,mouseY);//obtener valor del color
       R = (float)red(Vpon)/255;//convertir a int son float,
       G = (float)green(Vpon)/255;
       B = (float)blue(Vpon)/255;
       tc=R+G+B;
       diff=1-tc;
       diff=diff/3;
       R=R+diff;
       G=G+diff;
       B=B+diff;

       // para poner un circulo de referencia
       xc = mouseX;
       yc = mouseY;
       stroke(255,255,255);
       ellipse(xc,yc,2,2);
  }
}
    
    public void setTitlesKernels(String mk1, String mk2) {
        this.mk1 = mk1;
        this.mk2 = mk2;
    }
    
    public float getR() {
        return R;
    }

}