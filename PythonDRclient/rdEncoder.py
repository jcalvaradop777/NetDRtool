#!/usr/bin/env python
# coding: utf-8

# In[ ]:


# RD_ENC
# Genera un encoderAE a partir de un método RD


# In[ ]:


########################################################################
# Aplicación de RD
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

from time import time
import matplotlib.pyplot as plt
    
def rd(Xint, labelsIn, opcionMenuMet, d, clases, device): # labelsIn solo para LDA
    
    if opcionMenuMet=="CMDS" or opcionMenuMet=="cmds":
        print ("")
        print("Has seleccionado CMDS")

        t1 = time()
        encodeRd = mds(Xint, d, device)
        print("tiempo:", time() - t1)
        
    if opcionMenuMet=="CMDSsk" or opcionMenuMet=="cmdssk":
        print ("")
        print("Has seleccionado CMDSsk")

        t1 = time()
        encodeRd = mdsSK(Xint, d, device)
        print("tiempo:", time() - t1)
                
    if opcionMenuMet=="LLE" or opcionMenuMet=="lle":
        print ("")
        print("Has seleccionado LLE")
        tk = int(input("Suministre el tamaño del vecindario (k) >> "))

        t1 = time()
#         encodeRd = lle(Xint, tk, d) # k = 100
        encodeRd = lleSk(Xint, tk, d)
        print("tiempo:", time() - t1)
                
    if opcionMenuMet=="LE" or opcionMenuMet=="le":
        print ("")
        print("Has seleccionado LE")
            
        tkle = input("Suministre el tamaño del vecindario (k) para LE >> ")
            
        t1 = time()
        encodeRd = le(Xint, int(tkle), d, device) # k = 15
        print("tiempo:", time() - t1)
            
    if opcionMenuMet=="PCA" or opcionMenuMet=="pca":
        print ("")
        print("Has seleccionado PCA")

        t1 = time()
        encodeRd = pca(Xint, d, device) 
        #encodeRd, vtrunc = pca2(Xint, d) 
        print("tiempo:", time() - t1)
        
    if opcionMenuMet=="LDA" or opcionMenuMet=="lda":
        print ("")
        print("Has seleccionado LDA")

        t1 = time()
        encodeRd = lda(Xint, labelsIn, d, device)
        print("tiempo:", time() - t1)
  
    if opcionMenuMet=="KCMDS" or opcionMenuMet=="kcmds":
        print ("")
        print("Has seleccionado KCMDS")

        t1 = time()
        encodeRd, k_cmds = kcmdsGpu(Xint, d, device)
        print("tiempo:", time() - t1)
                
    if opcionMenuMet=="KLLE" or opcionMenuMet=="klle":
        print ("")
        print("Has seleccionado KLLE")
        tk = int(input("Suministre el tamaño del vecindario (k) >> "))

        t1 = time()
        encodeRd, k_lle = klleGpu(Xint, tk, d, device) # k = 100
        print("tiempo:", time() - t1)
                
    if opcionMenuMet=="KLE" or opcionMenuMet=="kle":
        print ("")
        print("Has seleccionado KLE")
        tk = input("Suministre el tamaño del vecindario (k) >> ")

        t1 = time()
        encodeRd, k_le = kleGpu(Xint, tk, d, device) # k = 15
        print("tiempo:", time() - t1)
            
    if opcionMenuMet=="KPCA" or opcionMenuMet=="kpca":
        print ("")
        print("Has seleccionado KPCA polinomial")
            
        gp = int(input("Suministre el grado del polinomio >> "))
        cp = int(input("Suministre el offset >> "))
        sp = int(input("Suministre el factor de escala >> "))

        t1 = time()
        encodeRd, k_pca = kpcaPoli(Xint, d, gp, cp, sp, device) # Xint=matriz de entrada, d=dimensión, p=grado del polinómio, c=offset o bias, s=factor de escala  :  #K(x,x')=(s*x⋅x'+c)^p
        print("tiempo:", time() - t1)
        bd2=False
            
    if opcionMenuMet=="KPCArbf" or opcionMenuMet=="kpcarbf":
        print ("")
        print("Has seleccionado KPCArbf")
        
        gama = float(input("Suministre el factor Gama >> ")) # ejemplo gama=0.5

        t1 = time()
        encodeRd, k_pca_rbf = kpcaRBF(Xint, d, gama)  # matriz de datos, dimension, gama de rbf
        print("tiempo:", time() - t1)
        bd2=False
        
    if opcionMenuMet=="TSNE" or opcionMenuMet=="tsne":
        print ("")
        print("Has seleccionado TSNE")

        t1 = time()
        encodeRd = t_sne(Xint, d, device) 
        print("tiempo:", time() - t1)
              
    if opcionMenuMet=="FACTOR" or opcionMenuMet=="factor":
        print ("")
        print("Has seleccionado Factor Analysis")

        t1 = time()
        encodeRd = factor(Xint, d, device) 
        print("tiempo:", time() - t1)
        
    if opcionMenuMet=="ISOMAP" or opcionMenuMet=="isomap":
        print ("")
        print("Has seleccionado Isomap")

        t1 = time()
        encodeRd = isomap(Xint, d, device) 
        print("tiempo:", time() - t1)
        
    # Nuevos metodos RD después del 2019___________________ 
    
    if opcionMenuMet=="UMAP" or opcionMenuMet=="umap":
        print ("")
        print("Has seleccionado Umap")

        t1 = time()
        encodeRd = umap(Xint, d, device) 
        print("tiempo:", time() - t1)
        
    if opcionMenuMet=="SLISEMAP" or opcionMenuMet=="slisemap":
        print ("")
        print("Has seleccionado Slisemap")

        t1 = time()
        encodeRd = slisemap(Xint, labelsIn, d, device) 
        print("tiempo:", time() - t1)
        
    if opcionMenuMet=="TRIMAP" or opcionMenuMet=="trimap":
        print ("")
        print("Has seleccionado Trimap")

        t1 = time()
        encodeRd = trimap(Xint, d, device) 
        print("tiempo:", time() - t1)
                
    else:
        print ("")
        #print("No has pulsado ninguna opción RD correcta...")
            
        bd1=False

    # Normaliza
    encodeRd = (encodeRd - torch.min(encodeRd))/(torch.max(encodeRd) - torch.min(encodeRd))
    
    # Grafica la RD
    rd = encodeRd.cpu().detach().numpy()
    x=rd[:,0]
    y=rd[:,1] 
    numEt = len(clases)
    plt.scatter(x, y, c=labelsIn, label=clases, cmap=plt.cm.get_cmap('jet', numEt))
    plt.show()
    
    return encodeRd


# In[ ]:


########################################################################
#  Neural Network ENCODER
# ^^^^^^^^^^^^^^^^^^^^
from modeloAE import Encoder2820, Encoder28, Encoder32, weights_init, EncoderFC
import torch.optim as optim
import torch.nn as nn
from torch.autograd import Variable
from tqdm import tqdm # barra de tiempo
import csv

def enc(encodeRd, trainloader, dimImg, metRD, d, opcData, saveName, numepochs, learning_rate):

# 2. Definition Convolution Neural Network (ENCODER)
    if opcData=="1": # Tratamiento para tensor
        if dimImg==28: # para imagenes como mnist que son de 28*28
            encoder = Encoder28(d).apply(weights_init)
        elif dimImg==32: # para imagenes como Coil20 o CIFAR que son de 32*32 con diferente numero de canales
            encoder = Encoder32(d,nChanelsIn=1).apply(weights_init)
        elif dimImg==20: # para imagenes como face que son de 28*20
            encoder = Encoder2820(d).apply(weights_init)
    else: # Para datos tabulares
        encoder = EncoderFC(dimImg, d).apply(weights_init)
        
    encoder.to(device)
        
    #print(encoder)

# 3. Define a Loss function and optimizer
    #criterion = nn.SmoothL1Loss().to(device)
    criterion = nn.MSELoss() # error
    #learning_rate = 0.001  #0.0001 0.001 #1e-3
    optimizer = optim.Adam(encoder.parameters(), lr=learning_rate, weight_decay=1e-5) # 0.01  1e-5

# 4. Train the network (ENCODER)
    #codes = np.array([[0,0]]) # unificacion de codes de cada minilote. Estas dimensiones deberían ser dinamicas en función de la dimensión a reducir (codesize=2)  
    codes = np.random.rand(0, d) # unificacion de codes de cada minilote. Estas dimensiones deberían ser dinamicas en función de la dimensión a reducir (codesize=2)  
    #decodes = [] # lista: unificacion de outs (decodes)

    #labelsCodes = np.array([0]) # unificacion de etiquetas
    labelsCodes = np.random.rand(0)

    print('Start Training')
    for epoch in tqdm(range(numepochs)):  # loop over the dataset multiple times: cada entrenamiento se da en (epocas * minilotes)
    # tqdm pone una barra de tiempo

        running_loss = 0.0
        for i, data  in enumerate(trainloader, 0): # recorre las imagenes por minilotes. El tamaño del minilote se estableció en 64 (batch_size=64) por lo que el trainloader carga minilotes de 64 imagenes (inputs torch.Size([64, 1, 28, 28] es decir [tamaño minilote, 1, 28, 28] en otras palabras: 64 imagens monocromaticas (1 canal rgb) de 28x28 pixeles))
            # get the inputs
            inputs, labels = data
            inputs = inputs.to(device)
            inputs = inputs.to(torch.float32)
            labels = labels.to(device)

            # wrap them in Variable
            inputs, labels = Variable(inputs), Variable(labels) # la función Variable (obsoleta creo) creo que es similar a poner requires_grad=True
        
            # zero the parameter gradients
            optimizer.zero_grad()

            # *** forward + backward + optimize
            code = encoder(inputs) 
            # normaliza el code
            #code = (code - torch.min(code)) / (torch.max(code) - torch.min(code))
        
            # =============== Para conformar la matriz de codes (reducción de dimensión realizada por el AE)
            if epoch==numepochs-1: # solo guarda en la ultima epoca, donde los parametros ya han sido optimizados
                codes=np.concatenate((codes, code.cpu().data), axis=0) # unifica los code respectivos de cada minilote
                #labelsCodes=np.concatenate((labelsCodes, labels.cpu().data), axis=0) # unifico todas las tuplas de cada minilote para que coincidan con el tamaño de los datos de entrada
        
            # ===============Adapta el tamaño del encodeRd al minilote (para poder comparar el code con el subcojunto mini de RD)
            encodeRdMini = encodeRd[list(trainloader._index_sampler)[i]]
            encodeRdMini = encodeRdMini.float()
            #==========================================================
        
            #loss = criterion(inputs, out) # backward
            loss = criterion(code, encodeRdMini)
        
            loss.backward() # backward
            optimizer.step() # optimize
            
            print("losssssssssssss", loss.item())

            # print statistics
            #running_loss += loss.item()
            #if i % 10 == 99:    # print every 100 mini-batches
            #    print('[%d, %5d] loss: %.3f' %(epoch + 1, i + 1, running_loss / 100))
            #    running_loss = 0.0
            
        if epoch % int(0.1*numepochs) == 0:
            print(f'epoch {epoch} \t Loss: {loss.item():.4g}')

    #codesM = np.column_stack((codes, labelsCodes))            
    print('Finished Training')

    torch.save(encoder.state_dict(), saveName)
    
    # ########### Guarda los 2d de los métodos ###########
    ruta2d = "./model/2d_Enc_" + metRD + ".csv"
    with open(ruta2d, 'w', newline='', encoding='utf-8') as csvfile2d:
        writer = csv.writer(csvfile2d)
        writer.writerows(codes)
    
    return codes, encoder


# In[ ]:


import torch
import numpy as np
import math
# from KPCA import K_rbf, K_polinomial, kpca, kpcaRBF,  kpcaPoli, pca, pca2
# from LLE import klleGpu, K_de_lle, lle, lleSk
# from MDS import kcmdsGpu, K_de_cmds, mds, mdsSK
# from LE import kleGpu, K_de_le, kleGpu2, K_de_le2, le
# from OtrosDR import lda, t_sne, factor, isomap
# from OtrosDRnuevos import umap, slisemap, trimap

def rdEnc(Xint, labelsIn, trainloader, dimImg, metRD, d, clases, opcData, device, nepocas): # labelsIn solo para LDA
    incrustamiento = rd(Xint, labelsIn, metRD, d, clases, device)
    rdenc, encoder = enc(incrustamiento, trainloader, dimImg, metRD, d, opcData, nepocas)
    return rdenc, encoder


# In[ ]:


#____________________________________________________
#  CARGA MODELOS ANTERIORES
#____________________________________________________


# In[ ]:


########################################################################
# Carga el modelo
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

from modeloAE import Encoder2820, Encoder28, Encoder32, weights_init, EncoderFC
import torch
import numpy as np

import torch
# Verifica si CUDA (GPU) está disponible
if torch.cuda.is_available():
    device = torch.device("cuda")
    torch.cuda.get_device_name(0)
else:
    device = torch.device("cpu")
    
def loadRdEnc(trainloader, dimImg, ruta, d, opcData): # Carga un solo modelo NetDR
    # Definition Convolution Neural Network (ENCODER)
    if opcData=="1": # Tratamiento para tensor
        if dimImg==28: # para imagenes como mnist que son de 28*28
            rdenc = Encoder28(d).apply(weights_init)
        elif dimImg==32: # para imagenes como Coil20 o CIFAR que son de 32*32 con diferente numero de canales
            rdenc = Encoder32(d,nChanelsIn=1).apply(weights_init)
        elif dimImg==20: # para imagenes como face que son de 28*20
            rdenc = Encoder2820(d).apply(weights_init)
    else: # para matrices
        rdenc = EncoderFC(Din=dimImg, dout=d).apply(weights_init)  
        
    rdenc.to(device)
    #ruta = "./model/encoder"+metRD+".model"
    rdenc.load_state_dict(torch.load(ruta, map_location=device))
    print("modelo: ", rdenc)

    # Construye la matriz RD

    codes = np.random.rand(0, d)
    #labelsCodes = np.random.rand(0)

    for data in trainloader:
        inputs, labels = data
        inputs = inputs.to(torch.float32)
        inputs = inputs.to(device)

        with torch.no_grad():
            encoded = rdenc.encode(inputs)
            enc = encoded.cpu().detach().numpy()
    
        codes = np.concatenate((codes, enc), axis=0) # unifica los code respectivos de cada minilote
        #labelsCodes = np.concatenate((labelsCodes, labels.cpu().data), axis=0)
    
    #codesM = np.column_stack((codes, labelsCodes)) 
    print("Finished Loading")
    return codes, rdenc

def XinTotrainloader(Xin, labelsIn, device):
    fl, cl = Xin.shape
    bz = fl
    
    Xint = torch.from_numpy(Xin).to(device)
    yt = torch.from_numpy(labelsIn).to(device)
    dataset = torch.utils.data.TensorDataset(Xint, yt)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=bz, shuffle=False)
    return trainloader


