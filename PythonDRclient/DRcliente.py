import socket
import json
import numpy as np
import torch
import io
import base64

from metodosDR import lda, t_sne, factor, isomap, graphEnc, umap, trimap, slisemap, densemap, parametricumap, spca, pca, kpcaPoli, lleSk, le, kLeP, kLleP, kCmdsP, fica, srp, grp, lpp
from AE import autoencoder
from NetDRmatrix import NetDR, loadNetDR
from rdEncoder import enc, loadRdEnc, XinTotrainloader
from modeloAE import EncoderFC, weights_init
from cluster import Vscore, Rnx

def receive_matrix(client_socket):
    Xin_concatenated = ""  # Cadena para concatenar las matrices recibidas

    while True:
        Xin_fragment = client_socket.recv(1024).decode('utf-8')
        # Concatenar el fragmento a la cadena existente
        Xin_concatenated += Xin_fragment

        if '\n' in Xin_fragment: # Verificar si el fragmento contiene el marcador de fin de línea
            cliente(client_socket, Xin_concatenated)
            Xin_concatenated = ""
            
def dimensionalityReduction(Xin, etiquetas, d, algorithm, metodosC, incrustamientoO, saveName, epocas, learningRate, ruta):
     Xin = np.array(Xin)
     kernel = np.zeros((2, 2))
     encodeRd = np.zeros((2, 2))
     model = EncoderFC(Din=2, dout=2).apply(weights_init)  
     score = 0 # Solo lo retorna el evaluation
     labelsPredict = np.array([0, 0])
     imagebytes = bytes()
     
     if algorithm == "PCA":
        encodeRd = pca(Xin, d)
     elif algorithm == "UMAP":
        encodeRd = umap(Xin, d)
     elif algorithm == "TriMap":
        encodeRd = trimap(Xin, d)
     elif algorithm == "SliseMap":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = slisemap(Xin, labelsIn, d)
     elif algorithm == "DensMap":
        encodeRd = densemap(Xin, d)
     elif algorithm == "PUMAP":
        encodeRd = parametricumap(Xin, d)
     elif algorithm == "IsoMap":
        encodeRd = isomap(Xin, d)
     elif algorithm == "AutoEncoder":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = autoencoder(Xin, labelsIn, d, device)
     elif algorithm == "SPCA":
        encodeRd = spca(Xin, d)
     elif algorithm == "LDA":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = lda(Xin, labelsIn, d)
     elif algorithm == "FA":
        encodeRd = factor(Xin, d)  
     elif algorithm == "GraphEncoder":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = graphEnc(100, Xin, labelsIn, device)
     elif algorithm == "LE":
        tkle = 20
        encodeRd = le(Xin, tkle, d)
     elif algorithm == "LLE":
        tk = 20
        encodeRd = lleSk(Xin, tk, d) 
     elif algorithm == "KPCA":
        gp = 2
        cp = 0
        sp = 1
        encodeRd, kernel = kpcaPoli(Xin, d, gp, cp, sp, device)
     elif algorithm == "KLE":
        tk = 20
        gp = 2
        cp = 0
        sp = 1
        encodeRd, kernel = kLeP(Xin, tk, d, "n", gp, cp, sp, device)
     elif algorithm == "KLLE":
        tk = 20
        gp = 2
        cp = 0
        sp = 1
        encodeRd, kernel = kLleP(Xin, tk, d, gp, cp, sp, device)
     elif algorithm == "KMDS":
        tk = 20
        gp = 2
        cp = 0
        sp = 1
        encodeRd, kernel = kCmdsP(Xin, d, gp, cp, sp, device)
     elif algorithm == "FICA":
        encodeRd = fica(Xin, d) 
     elif algorithm == "SRP":
        encodeRd = srp(Xin, d) 
     elif algorithm == "GRP":
        encodeRd = grp(Xin, d) 
     elif algorithm == "LPP":
        encodeRd = lpp(Xin, d) 
     elif algorithm == "NetDRd":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = NetDR(Xin, labelsIn, d, metodosC, "1", device, saveName, epocas, learningRate)   # 1 discriminante, 2 topológico
     elif algorithm == "NetDRt":
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        encodeRd = NetDR(Xin, labelsIn, d, metodosC, "2", device, saveName, epocas, learningRate)  #ESTOY MIRANDO COMO SE PUEDE GUARDAR EL MODELO DE NETDR
     elif algorithm == "Learning":
        incrustamientoO = np.array(incrustamientoO)
        incrustamientoO = torch.from_numpy(incrustamientoO).to(device)
        opcData = 2 # opcData = 1 para tensor, 2 para tabular
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        trainloader = XinTotrainloader(Xin, labelsIn, device)
        filimg, dimImg = Xin.shape
        encodeRd, model = enc(incrustamientoO, trainloader, dimImg, algorithm, d, opcData, saveName, epocas, learningRate)
     elif algorithm == "Model":
        partes = ruta.split('.')
        extension = partes[-1] # averigua si la extension de modelo es .model (para primera fase) o .netdr (para t o d)
        opcData = 2 # opcData = 1 para tensor, 2 para tabular
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        trainloader = XinTotrainloader(Xin, labelsIn, device)
        filimg, dimImg = Xin.shape
        if extension == "model":
           encodeRd, model = loadRdEnc(trainloader, dimImg, ruta, d, opcData)
        elif extension == "netdr":
           encodeRd, model = loadNetDR(Xin, labelsIn, trainloader, dimImg, ruta, d, opcData, device)
     elif algorithm =="Vscore":
        incrustamientoO = np.array(incrustamientoO)
        incrustamientoO = torch.from_numpy(incrustamientoO).to(device)
        encodeRd = incrustamientoO
        numeros_flotantes = [float(num) for num in etiquetas]
        labelsIn = np.array(numeros_flotantes)
        unicos = np.unique(labelsIn)
        nc = len(unicos)
        score, labelsPredict = Vscore(incrustamientoO, nc, labelsIn)
        print("Vscore: ", score)
     elif algorithm =="Rnx":
        incrustamientoO = np.array(incrustamientoO)
        incrustamientoO = torch.from_numpy(incrustamientoO).to(device)
        encodeRd = incrustamientoO
        score, imagebytes = Rnx(Xin, "score", incrustamientoO)
        print("Rnx: ", score)

     encodeRd = encodeRd.tolist()
     kernel = kernel.tolist()
     modelo_bytes = io.BytesIO()
     torch.save(model.state_dict(), modelo_bytes)
     modelo_bytes.seek(0)
     # Codifica los bytes a base64
     modelo_base64 = base64.b64encode(modelo_bytes.read()).decode('utf-8')
     labelsPredict = labelsPredict.tolist()
     image_base64 = base64.b64encode(imagebytes).decode('utf-8')

     data = {"encodeRd": encodeRd, "kernel": kernel, "modelo": modelo_base64, "score": score, "labelsPredict": labelsPredict, "imagebytes": image_base64}
     json_data = json.dumps(data) + "stop" + '\n'
     return json_data

def send_matrix(client_socket, Ydr):
     client_socket.sendall(Ydr.encode('utf-8'))
     
def cliente(client_socket, dataJson):
    # Desempaquete el Json
    data = json.loads(dataJson)
    Xin = data['Xin']
    etiquetas = data['etiquetas']
    d = data['d']
    algorithm = data['algorithm']
    metodosC = data['metodos']
    incrustamientoO = data['incrustamientoO']
    saveName = data['saveName']
    epocas = data['epocas']
    learningRate = data['learningRate']
    ruta = data['ruta']
    #print("Xin JSON:", Xin)
    #print("etiquetas_Json:", etiquetas)
    #print("d_Json:", d)
    #print("algorithm_Json:", algorithm)
    #print("ruta", ruta)
    
    Ydr = dimensionalityReduction(Xin, etiquetas, d, algorithm, metodosC, incrustamientoO, saveName, epocas, learningRate, ruta)
    #print("Matriz DR:", Ydr)
    send_matrix(client_socket, Ydr) 
    
    # Guardar en archivo
   #  with open("YdrJson.txt", 'w') as archivo:
   #     archivo.write(Ydr)
    
# Configuración del cliente *******
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('127.0.0.1', 12345))   

# Verifica si CUDA (GPU) está disponible
device = torch.device("cpu")
if torch.cuda.is_available():
    device = torch.device("cuda")
    print("Se ha encontrado una GPU CUDA. Se utilizará CUDA.")
    torch.cuda.get_device_name(0)

receive_matrix(client_socket)