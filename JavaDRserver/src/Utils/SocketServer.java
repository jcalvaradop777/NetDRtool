package Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import java.util.List;

public class SocketServer {
    
       private int d;
       private double[][] Xin;
       private double[][] Yd; 
       double[][] kernel;
       byte[] modelo;
       private String algorithm;
       double[] labelsPredict;
       double score;
       byte[] imageBytes;
       
       Socket clientSocket;
       ReceiveMatrixYdr reciver;

      public SocketServer() {
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
      
      public  void fit(double[][] Xin, String[] etiquetas, int d, String algorithm,  List<Map<String, Object>> vectorMetodos, double[][] incrustamientoO, String saveName, Integer epocas, double learningRate, String ruta) {
          
             this.Xin = Xin;
             this.d = d;
             this.algorithm = algorithm;
             
             Map<String, Object> data = new HashMap<>();
             data.put("Xin", Xin);
             data.put("etiquetas", etiquetas);
             data.put("d", d);
             data.put("algorithm", algorithm);
             data.put("metodos", vectorMetodos); // El vector de métodos tiene el incrustamiento y el nombre de método DR para ser combinados
             data.put("incrustamientoO", incrustamientoO); // tiene el incrustamiento generado por un método DR, el cuál será aprendido de manera neuroanal como la primera fase de NetDR.
             data.put("saveName", saveName);
             data.put("epocas", epocas);
             data.put("learningRate", learningRate);
             data.put("ruta", ruta);
             
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
            String modeloBase64 = jsonObject.get("modelo").getAsString();
//            JsonArray labelsPredictJsonArray = jsonObject.getAsJsonArray("labelsPredict");
            //JsonArray scoreJsonArray = jsonObject.getAsJsonArray("score");

            // Convertir los arrays JSON en matrices de números
            Yd = gson.fromJson(encodeRdJsonArray, double[][].class);
            kernel = gson.fromJson(kernelJsonArray, double[][].class);
            modelo = java.util.Base64.getDecoder().decode(modeloBase64);
            labelsPredict = gson.fromJson(jsonObject.get("labelsPredict"), double[].class);
//            System.out.println("labelsPredict: " + labelsPredict);
            score = jsonObject.get("score").getAsDouble();       
            
            // Extraer los bytes de la imagen del campo "imagebytes"
            String imageBytesBase64 = jsonObject.get("imagebytes").getAsString();
            // Decodificar los bytes Base64 a un arreglo de bytes
            imageBytes = java.util.Base64.getDecoder().decode(imageBytesBase64);    
      }
   
      public double[][] getLowMatrix(){
         return Yd;
     }
      
      public double[][] getKernel(){
         return kernel;
     }
      
      public byte[] getModelo(){
         return modelo;
     }
      
      public String[] getLabelsPredict(){
        String[] labelsPredictStrings = new String[labelsPredict.length];
        for (int i = 0; i < labelsPredict.length; i++) {
            labelsPredictStrings[i] = Double.toString(labelsPredict[i]);
        }
         return labelsPredictStrings;
     }
      
      public double getScore(){
         return score;
     }
      
      public byte[] getImageRnx(){
         return imageBytes;
     }
     
}

// Envía datos al cliente Python
class SendMatrixThread implements Runnable {
       private Socket clientSocket;
       String jsonXin;

       public SendMatrixThread(Socket socket, String jsonXin) {
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
class ReceiveMatrixYdr{
        private Socket clientSocket;

        public ReceiveMatrixYdr(Socket socket) {
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

