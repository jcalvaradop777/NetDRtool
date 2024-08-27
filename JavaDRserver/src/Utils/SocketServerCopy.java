package Utils;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class SocketServerCopy {
    
       private int d;
       private double[][] Xin;
       private double[][] Yd; 
       
       Socket clientSocket;
       ReceiveMatrixYdr reciver;

      public SocketServerCopy() {
            try {
                   ServerSocket serverSocket = new ServerSocket(12345);
                   System.out.println("Servidor escuchando en el puerto 12345");

                   clientSocket = serverSocket.accept();
                   System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                   reciver = new ReceiveMatrixYdr(clientSocket);
                   
            } catch (IOException e) {
                    e.printStackTrace();
            }
      }
      
      public  void fit(double[][] Xin, int d) {
          
             this.Xin = Xin;
             this.d = d;
            
             Gson gson = new Gson(); 
             String jsonXin = gson.toJson(Xin);
            
             // Crea el hilo de Envío
             Thread sendThread = new Thread(new SendMatrixThread(clientSocket, jsonXin));
             sendThread.start();
             
             // Obtener la matriz Ydr de Python
             String receivedData = reciver.recibir();
             System.out.println("Matriz Ydr recibida: " + receivedData);
             
             // Decodificación de Json
            Yd = gson.fromJson(receivedData, double[][].class);
      }
   
      public double[][] getLowMatrix(){
         return Yd;
     }
     
}

// Envía datos al cliente Python
class SendMatrixThreadCopy implements Runnable {
       private Socket clientSocket;
       String jsonXin;

       public SendMatrixThreadCopy(Socket socket, String jsonXin) {
            this.clientSocket = socket;
            this.jsonXin = jsonXin;
      }

       @Override
       public void run() {
            try {
                   PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);  
                   writer.println(jsonXin);
                   
            } catch (IOException e) {
                   e.printStackTrace();
            }
        }
}


// Recibe datos del cliente Python
class ReceiveMatrixYdrCopy{
        private Socket clientSocket;

        public ReceiveMatrixYdrCopy(Socket socket) {
            this.clientSocket = socket;
        }
        
        public String recibir() {
            String matrixData = "";
            try {
                   BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    while (true) {
                         // Lee la matriz enviada por el cliente
                         matrixData = reader.readLine();
                         // Rompe el ciclo si se encuentra el carácter '\n'
                         if (matrixData.contains("stop")) {
                               matrixData = matrixData.replace("stop", "");
                               break;
                         }
                   }
            }catch (IOException e) {
                   e.printStackTrace();
            }
            return matrixData;
        }
}

