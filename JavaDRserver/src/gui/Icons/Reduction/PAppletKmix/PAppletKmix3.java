/**
 *
 * @author JuanKDD
 */

package gui.Icons.Reduction.PAppletKmix;

import processing.core.*; 


public class PAppletKmix3 extends PApplet {

PImage modelo; //imagen del triangulo

int Vpon; //se inicializa el vector de ponderacion 
float R,G,B; // se define variables en donde se guardan los valores RGB

int [] colab1 = new int[3]; // se define un vector de colores para los labels 

////////////////
String mk1 = ""; //titulos de los kernel en la interfaz
String mk2 = "";
String mk3 = "";

float xc = 0, yc = 0; // para capturar las cordnadas del circulo de seleccion de color

float cx1 = 0; // cordenada para poner el titulo del kernel 1
// no se requiere calcular la cordenada 2
float cx3 = 0; // cordenada para poner el titulo del kernel 3

@Override
public void setup(){
   size(600, 600); 
   modelo = loadImage("src/gui/Icons/Reduction/PAppletKmix/RGB.jpeg"); // se guarda la imagen en la variable modelo
//   cp5 = new ControlP5(this);// inicializo los objetos de la libreria 
   
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
 // titulos de los kernel   
 text(mk1,cx1,100);
 text(mk2,50,150+(modelo.height));
 text(mk3,cx3,150+(modelo.height));
 // pone la imagen del triangulo
 image(modelo,50,120);
 // fin triangulo

 // dibuja el whighted factor
 stroke(125,125,125);
 strokeWeight(10);
 noFill();
 rect(modelo.width+140,190,140,180);
 fill(0); // color blanco
 textSize(20); // tamano de la letra
 text("weighted factors",modelo.width+130,170);
 
//// se dibuja barras de ponderacion
 noStroke(); // no se dibuja el contorno del rectangulo
 
 // color azul
 fill(colab1[2]);
 rect(modelo.width+160,330,20,-2-(100)*(B)); // se dibuja el rectangulo azul, LE
 fill(0); // color negro
 textSize(10); // tama\u00f1o del texto
 text("K1",modelo.width+165,220);
 text(B,modelo.width+155,355); // se imprime el texto de de blue LE
 
 // color rojo  
 fill(colab1[0]); 
 rect(modelo.width+200,330,20,-2-100*(R)); // se dibuja el rectangulo rojo, LLE
 fill(0); // color negro
 textSize(10); // tama\u00f1o de letra
 text("K2",modelo.width+205,220); // se imprime el text
 text(R,modelo.width+194,355); // se imprime el text
 
 // color verde
 fill(colab1[1]); 
 rect(modelo.width+240,330,20,-2-100*(G)); // se dibuja el rectangulo verde, cmds
 fill(0); // color negro
 textSize(10); // tama\u00f1o de letra
 text("K3",modelo.width+245,220); // se imprime el text
 text(G,modelo.width+233,355); // se imprime el text
 
 // caja de combinacion de color
 fill(0); 
 textSize(20);
 text("Choice",modelo.width+175,50);
 stroke(125,125,125);
 fill(Vpon);
 rect(modelo.width+140,70,140,50); 
 
 stroke(255,255,255);
 ellipse(xc,yc,2,2);
}

@Override
public void mousePressed (){ // si el mouse es presionado dentro de la superficie recoge los valores de RGB

   float tc,diff;
   
   if(mouseX>=50 && mouseX<(50+modelo.width) && mouseY>120 && mouseY<=(120+modelo.height)){

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
    // recive los titulos de los  
   public void setTitlesKernels(String mk1, String mk2, String mk3) {
        
        this.mk1 = mk1;
        cx1 = 170 - (textWidth(mk1)/2);
        
        this.mk2 = mk2;
        
        this.mk3 = mk3;
        cx3 = 300 - textWidth(mk3);
    }
    
    public float getR() {
        return R;
    }
    
}