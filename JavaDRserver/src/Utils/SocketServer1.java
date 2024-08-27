package Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;

public class SocketServer1 {
    
       private int d;
       private double[][] Xin;
       private double[][] Yd; 
       double[][] kernel;
       private String algorithm;
       
       Socket clientSocket;
       ReceiveMatrixYdr reciver;

      public SocketServer1() {
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
      
          // Método para convertir un JsonArray en una matriz de números
      private static double[][] convertJsonArrayToDoubleArray(JsonArray jsonArray) {
            double[][] result = new double[jsonArray.size()][((JsonArray) jsonArray.get(0)).size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                   JsonArray row = (JsonArray) jsonArray.get(i);
                   for (int j = 0; j < row.size(); j++) {
                         result[i][j] = row.get(j).getAsDouble();
                   }
        }
        return result;
    }
      
      public  void fit(double[][] Xin, String[] etiquetas, int d, String algorithm) {
          
             this.Xin = Xin;
             this.d = d;
             this.algorithm = algorithm;
             
             Map<String, Object> data = new HashMap<>();
             data.put("Xin", Xin);
             data.put("etiquetas", etiquetas);
             data.put("d", d);
             data.put("algorithm", algorithm);

            // Crear objeto Gson y empaquetar los datos en JSON
            Gson gson = new Gson();
            String jsonData = gson.toJson(data);
            
             // Crea el hilo de Envío
             Thread sendThread = new Thread(new SendMatrixThread(clientSocket, jsonData));
             sendThread.start();
             
             // Obtener la matriz Ydr de Python
             String receivedData = reciver.recibir();
             
            JsonObject jsonObject = gson.fromJson(receivedData, JsonObject.class);
            // Extraer las matrices según sus identificadores
            JsonArray encodeRdJsonArray = jsonObject.getAsJsonArray("encodeRd");
            JsonArray kernelJsonArray = jsonObject.getAsJsonArray("kernel");
            
            // Convertir los arrays JSON en matrices de números
            Yd = gson.fromJson(encodeRdJsonArray, double[][].class);
            kernel = gson.fromJson(kernelJsonArray, double[][].class);

            // Convertir los arrays JSON en matrices de números
//            Yd = convertJsonArrayToDoubleArray(encodeRdJsonArray);
//            kernel = convertJsonArrayToDoubleArray(kernelJsonArray);
             
//     // Decodificación de Json
//     Yd = gson.fromJson(receivedData, double[][].class);
      }
   
      public double[][] getLowMatrix(){
         return Yd;
     }
      
      public double[][] getKernel(){
         return kernel;
     }
     
}

// Envía datos al cliente Python
class SendMatrixThread1 implements Runnable {
       private Socket clientSocket;
       String jsonXin;

       public SendMatrixThread1(Socket socket, String jsonXin) {
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
class ReceiveMatrixYdr1{
        private Socket clientSocket;

        public ReceiveMatrixYdr1(Socket socket) {
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

