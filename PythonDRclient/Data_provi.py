#!/usr/bin/env python
# coding: utf-8

# In[ ]:


######################################################################## 
# DATOS TENSORIALES
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


# In[ ]:


import os

def T_menuData():
    os.system('cls') 
    print ("MENU DE DATOS TENSORIALES")
    print ("\t1 - FashionMNIST")
    print ("\t2 - MNIST")
    print ("\t3 - CIFAR")
    print ("\t4 - Coil20")
    print ("\t5 - Faces")
    print ("\t6 - Tufts")
    print ("\t7 - naturalImages")
    print ("\t8 - Signs")
    print ("\t9 - FER")
    print ("\t10 - Chess")
    print ("\t11 - Lego")
    print ("\t12 - Brain")
    print ("\t13 - Chest_xray")
    print ("\t14 - SenosGT")
    print ("\t15 - Senos")
    print ("\t16 - Pulmon")
    print ("\t17 - PulmonCNPT")
    print ("\t18 - Glaucoma")
    print ("\t19 - Brain17")
    print ("\t20 - Brain44")
    print ("\t21 - Osteoartritis")
    print ("\t22 - Brain300x300")
    print ("\t24 - USPS")
    print ("\t25 - Imagenette")
    print ("\t26 - Intel")
    print ("\t27 - Vegetable")
    print ("\t28 - MedicalMnist")
    print ("\t29 - IndiansignLanguage")
    print ("\t23 - Salir")


# In[ ]:


######################################################################## 
# DATOS FashionMINIST
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
# Importación y transformación de datos (imagenes Fashion)
# The output of torchvision datasets are PILImage images of range [0, 1].
# We transform them to Tensors of normalized range [-1, 1]

# La salida de los conjuntos de datos de Torchvision son imágenes PILImage del rango [0, 1]. (PIL (Python Imagin Library) )
# Las transformamos en Tensores de rango normalizado [-1, 1]

# Para poder usar el dataset con PyTorch, se debe transformar a tensor. Para ello, definimos una transformación T que usaremos en el proceso de carga. Definimos también un DataLoader, un objeto generador de Python cuyo cometido es proporcionar las imágenes en grupos de batch_size imágenes a la vez.

# n: asignacion del tamaño (en registros)
# bsize: tamaño del minilote

from torch.utils.data.dataset import random_split
import torchvision
import torchvision.transforms as transforms
import torch
import matplotlib.pyplot as plt

def T_FashionMNIST(n,bsize):

    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,))])

    trainset = torchvision.datasets.FashionMNIST(root='./data', train=True, download=True, transform=transform)
    # len(trainset) longitud del conjuto; trainset[i] un elemento en particular

    trainset_reduced, trainset_reduced2 = random_split(trainset, [n, 60000-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4) # Para poder partir el conjunto en minilotes y que sea iterable

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,784)
    Xin = Xin.numpy()
    #__________
    
    classes = ('t-shirt', 'trouser', 'pullover', 'dress', 'coat', 'sandal', 'shirt', 'sneaker', 'bag', 'boot')

    #import matplotlib.pyplot as plt
    # functions to show an image

    #def imshow(img):
    #    img = img / 2 + 0.5     # unnormalize
    #    npimg = img.numpy()
    #    plt.imshow(np.transpose(npimg, (1, 2, 0)))

    # get some random training images
    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)
    #images.to(device)
    #labels.to(device)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')

    # show images
    #imshow(torchvision.utils.make_grid(images))
    # print labels
    print(' '.join('%5s' % classes[labels[j]] for j in range(4)))
    #print(images[1])

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS MNIST
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

from torch.utils.data.dataset import random_split
import torchvision
import torchvision.transforms as transforms
import torch
import matplotlib.pyplot as plt

def T_MNIST(n,bsize):

    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,))])

    trainset = torchvision.datasets.MNIST(root='./data', train=True, download=True, transform=transform)
    # len(trainset) longitud del conjuto; trainset[i] un elemento en particular

    trainset_reduced, trainset_reduced2 = random_split(trainset, [n, 60000-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4) # Para poder partir el conjunto en minilotes y que sea iterable

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  
    Xin = Xin.reshape(-1,784)
    Xin = Xin.numpy()
    #__________
    
    classes = ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)
    #images.to(device)
    #labels.to(device)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')

    # show images
    #imshow(torchvision.utils.make_grid(images))
    # print labels
    print(' '.join('%5s' % classes[labels[j]] for j in range(4)))
    #print(images[1])

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS CIFAR
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torchvision.transforms as transforms
import torch
import matplotlib.pyplot as plt

def T_CIFAR(n,bsize):

    #transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1)])
    
    resize_transform = transforms.Resize((32, 32))
    transform = transforms.Compose([resize_transform, transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,))])

    trainset = torchvision.datasets.CIFAR10(root='./data', train=True, download=True, transform=transform)
    # len(trainset) longitud del conjuto; trainset[i] un elemento en particular

    trainset_reduced, trainset_reduced2 = random_split(trainset, [n, 50000-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4) # Para poder partir el conjunto en minilotes y que sea iterable

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    #Xin, labelsIn = dataiterXin.next()  
    Xin, labelsIn = next(dataiterXin)
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    #__________
    
    classes = ('airplane', 'automobile', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck')

    dataiter = iter(trainloader)
    #images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    images, labels = next(dataiter)
    print("images ",images.shape)
    print("labels ",labels.shape)
    #images.to(device)
    #labels.to(device)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0])

    # show images
    #imshow(torchvision.utils.make_grid(images))
    # print labels
    print(' '.join('%5s' % classes[labels[j]] for j in range(4)))
    #print(images[1])

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


class miDataset(torch.utils.data.Dataset):
    
  def __init__(self, numpy_data, etiquetas, transformaciones = None):
    self.data = numpy_data
    self.transformaciones = transformaciones
    self.etiquetas = etiquetas
  
  def __len__(self):
    return len(self.data)

  def __getitem__(self, idx):
    
    idx_numpy = self.data[idx]
    idx_etiqueta = self.etiquetas[idx]

    if self.transformaciones:
      idx_numpy = self.transformaciones(idx_numpy)
    
    idx_numpy = torch.from_numpy(idx_numpy).double()
  
    return idx_numpy, idx_etiqueta


# In[ ]:


import scipy.io
#import torchvision.transforms as transforms
import torch

def T_Coil20(n, bsize):
    
    mat = scipy.io.loadmat("./data/COIL20.mat") # cargamos datos en formato de matlab

    Xin=X=mat['X'] # incialmente los datos estan en estructura matriz de 1024
    X=X.reshape(-1,1,32,32) # Se lo transforma a estructura tensor

    y=mat['Y']
    y=y-1 # se le resta 1 poruqe las clases se parten desde la posicion 0, y en matlab las trae desde la pos 1, es decir, los nombres de las imagenes estan corridas un lugar
    y=y[:, 0] 

    ###transform = transforms.Compose([transforms.ToTensor()]) # conjunto de transformaciones que se aplica a cada imagen en la clase "miDataset"
    ###dataset = miDataset(X, y, transform) # se genera un dataSet con la clase abstracta creada denominada "miDataset"
    ###trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=True) # Para poder partir el conjunto en minilotes y que sea iterable

    X = torch.from_numpy(X).float()
    y = torch.from_numpy(y)
    
    Xin = Xin[:n, :]
    X = X[:n, :]
    y = y[:n]

    dataset = torch.utils.data.TensorDataset(X, y)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=64, shuffle=False)
    
    classes = ('pato', 'flecha', 'carro', 'gato', 'anacin', 'carro2', 'lego', 'talco', 'tylenol', 'vaselina', 'ficha', 'vaso', 'alcancia', 'plafon', 'tarro', 'frasco', 'vasija', 'taza', 'carro3', 'arequipe')

    #batch = next(iter(trainloader))   
    #dataiter = iter(trainloader)
    images, labels = next(iter(trainloader))
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print(' '.join('%5s' % classes[int(labels[j])] for j in range(4)))
    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    print("Elemento:",classes[int(labels[0])],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, y 


# In[ ]:


import numpy as np
import matplotlib.pyplot as plt
import scipy.io

def T_Faces(batch_size):
    
    img_rows=28
    img_cols=20
    mat = scipy.io.loadmat('./data/frey_rawface.mat')
    X = mat["ff"].T.reshape((-1, 1, img_rows, img_cols))
    X = X.astype('float32')/255.

    size = len(X)

    X = X[:int(size/batch_size)*batch_size]
    X = torch.from_numpy(X).float()
    Xin = X.reshape(-1,560)
    Xin = Xin.numpy()

    # ___etiquetas y
    print ("ETIQUETAS FACES")
    print ("\t1 - Etiquetas continuas")
    print ("\t2 - Etiquetas discretas")
    opcCoD = input("Seleccione la opción: >> ")
    
    if opcCoD == "1":
        #y = np.asarray(range(1920))
        #y = torch.from_numpy(y)
        y = torch.range(0,1920,1)
        clases = y
    elif opcCoD == "2":
        ncfaces = int(input("Número de clases: >> ")) # numero de clases faces
        y = torch.zeros(1920) # labels discretos
        inc = 0 # inicio
        lim = int(1920/ncfaces) # limite superior
        incre = lim # constante, incremento
        for i in range(ncfaces):
            y[inc:lim] = i+1
            #print(y[inc])  
            #print("lim", lim)
            inc = lim
            lim = lim + incre
            clases = torch.range(0,ncfaces,1) # clases discretas
    # ___

    dataset = torch.utils.data.TensorDataset(X, y)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size, shuffle=False)

    images, labels = next(iter(trainloader))
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')

    #print(' '.join('%5s' % classes[int(labels[j])] for j in range(4)))
    #print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    #print("tamaño del mini lote", trainloader.batch_size) 
    #print("numero de minilotes ", len(trainloader)) 
    #print("Elemento:",labels[0])

    return trainloader, clases, Xin, y 


# In[ ]:


######################################################################## 
# DATOS de personas Tufts
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch

def T_Tufts(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Tufts/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=bsize, num_workers=4, shuffle=False)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    #__________    
    inc = 0 # inicio
    lim = inc+9
    labelsIn = torch.zeros(998) # labels discretos
    for i in range(111): #111 clases, 2 participantes renunciaron
        labelsIn[inc:lim] = i+1
        inc = lim
        lim = lim + 9
    
    classes = torch.range(1,111,1)
    #__________

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS naturalImages
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split

def T_naturalImages(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/naturalImages/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6899-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    #__________    
    #inc = 0 # inicio
    #lim = inc+9
    #labelsIn = torch.zeros(n) # labels discretos
    #for i in range(111): #111 clases, 2 participantes renunciaron
        #labelsIn[inc:lim] = i+1
        #inc = lim
        #lim = lim + 9
    
    classes = ('airplane', 'car', 'cat', 'dog', 'flower', 'fruit', 'motorbike', 'person')
    #__________

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS Signs
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import cv2
import numpy as np
import matplotlib.pyplot as plt

def T_Signs(bsize):
    
    Xload = np.load('./data/Signs/X.npy')
    yload = np.load('./data/Signs/Y.npy')
    
    Xin = np.empty((len(Xload), 1024))
    labelsIn = yload.argmax(-1)
    
    for i in range(len(Xload)):
        img = Xload[i].reshape(64, 64)
        res = cv2.resize(img  , (32 , 32))
        Xin[i] = res.reshape(-1,1024)
    
    #__Grafica una señal
    plt.figure(figsize=(5,3))
    img = Xin[0].reshape(32, 32)
    plt.imshow(img)
    plt.axis('off')
    #title = "Sign " + labelsIn[0] 
    #plt.title(title)
    plt.show()
    
    classes = torch.range(0,9,1)
    
    Xt = torch.from_numpy(Xin).float()
    yt = torch.from_numpy(labelsIn)
    
    dataset = torch.utils.data.TensorDataset(Xt, yt)
    trainloader = torch.utils.data.DataLoader(dataset, bsize, shuffle=False)

    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de FacesEmotion
# The task is to categorize each face based on the emotion shown in the facial expression into one of seven categories 
# (0=Angry, 1=Disgust, 2=Fear, 3=Happy, 4=Sad, 5=Surprise, 6=Neutral).
# The training set consists of 28,709 examples and the public test set consists of 3,589 examples.

# https://www.kaggle.com/msambare/fer2013
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_FacesEmotion(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/FacesEmotion/test'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 7178-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Angry', 'Disgust', 'Fear', 'Happy', 'Sad', 'Surprise', 'Neutral')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Chess

# https://www.kaggle.com/niteshfre/chessman-image-dataset
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Chess(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Chess/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 552-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin) # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Bishop', 'King', 'Knight', 'Pawn', 'Queen', 'Rook')

    dataiter = iter(trainloader)
    images, labels = next(dataiter) # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Chess

# https://www.kaggle.com/niteshfre/chessman-image-dataset
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Lego(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Lego/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = torch.range(0,16,1)

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de BRAIN

# https://www.kaggle.com/datasets/masoudnickparvar/brain-tumor-mri-dataset
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Brain():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Brain/Training/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=5713, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=5713, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('glioma', 'meningioma', 'notumor', 'pituitary')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de chest_xray

# https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_ChestXray():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/chest_xray/train/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=5216, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=5216, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Normal', 'Pneumonia')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de chest_xray

# https://www.kaggle.com/datasets/paultimothymooney/chest-xray-pneumonia
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_SenosGT():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Dataset_BUSI_with_GT/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=1578, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=1578, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('benign', 'malignant', 'normal')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de chest_xray

# https://www.kaggle.com/datasets/rahelsarif/mammography-images-converted-to-jpeg-512512px
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Senos():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/BreastCancer/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=54706, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=54706, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Cancer_negative', 'Cancer_positive', 'implant_cancer_negative', 'implant_cancer_positive')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, covid, neumonia, 

# https://www.kaggle.com/datasets/tawsifurrahman/covid19-radiography-database
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Pulmon():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/pulmon/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=15153, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=15153, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Covid', 'Normal', 'Pneumonia')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, covid, neumonia, 

# https://www.kaggle.com/datasets/jtiptj/chest-xray-pneumoniacovid19tuberculosis
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_PulmonCNPT():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/pulmonCNPT/train/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=6326, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=6326, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Covid', 'Normal', 'Pneumonia', 'Tuberculosis')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, Glaucoma

# https://www.kaggle.com/datasets/sshikamaru/glaucoma-detection
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Glaucoma():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Glaucoma/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=1355, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=1355, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Acrina', 'Origa')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, Brain Tumor 17

# https://www.kaggle.com/datasets/fernando2rad/brain-tumor-mri-images-17-classes
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Brain17():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Brain17/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=4448, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=4448, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('GliomaT1', 'GliomaT1C+', 'GliomaT2', 'MeningiomaT1', 'MeningiomaT1C+', 'MeningiomaT2', 'NeurocitomaT1', 'NeurocitomaT1C+', 'NeurocitomaT2', 'NormalT1', 'NormalT2', 'OtrosT1', 'OtrosT1C+', 'OtrosT2', 'SchwannomaT1', 'SchwannomaT1C+', 'SchwannomaT2')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, Brain Tumor 44

# https://www.kaggle.com/datasets/fernando2rad/brain-tumor-mri-images-44c
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Brain44():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Brain44/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=4479, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=4479, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('NormalT1', 'NormalT2', 'AstrocitomaT1', 'AstrocitomaT1C+', 'AstrocitomaT2', 'CarcinomaT1', 'CarcinomaT1C+', 'CarcinomaT2', 'EpendimomaT1', 'EpendimomaT1C+', 'EpendimomaT2', 'GangliogliomaT1', 'GangliogliomaT1C+', 'GangliogliomaT2', 'GerminomaT1', 'GerminomaT1C+', 'GerminomaT2', 'GlioblastomaT1', 'GlioblastomaT1C+', 'GlioblastomaT2', 'GranulomaT1', 'GranulomaT1C+', 'GranulomaT2', 'MeduloblastomaT1', 'MeduloblastomaT1C+', 'MeduloblastomaT2', 'MeningiomaT1', 'MeningiomaT1C+', 'MeningiomaT2', 'NeurocitomaT1', 'NeurocitomaT1C+', 'NeurocitomaT2', 'OligodendrogliomaT1', 'OligodendrogliomaT1C+', 'OligodendrogliomaT2', 'PapilomaT1', 'PapilomaT1C+', 'PapilomaT2', 'SchwannomaT1', 'SchwannomaT1C+', 'SchwannomaT2', 'TuberculomaT1', 'TuberculomaT1C+', 'TuberculomaT2')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


# In[ ]:


######################################################################## 
# DATOS de Pulmon, Osteoartritis

# https://www.kaggle.com/datasets/farjanakabirsamanta/osteoarthritis-prediction
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import torchvision
import torch
import torchvision.transforms as transforms
from torch.utils.data.dataset import random_split
import matplotlib.pyplot as plt

def T_Osteoartritis():
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Osteoartritis/' # al dejar la carpeta con las subcarpetas y utilizando imageFoalder, se carga todas las imagenes con su respectiva clase que corresponde (subcarpeta)
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    #trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 6379-n])

    trainloader = torch.utils.data.DataLoader(train_dataset, batch_size=4479, shuffle=False, num_workers=0)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(train_dataset, batch_size=4479, shuffle=False, num_workers=0)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = dataiterXin.next()  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Normal', 'Osteoartrits')

    dataiter = iter(trainloader)
    images, labels = dataiter.next()  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn

######################################################################## 
# DATOS de USPS
# #################
def T_USPS(n,bsize):
    
    resize_transform = transforms.Resize((32, 32))

    transform = transforms.Compose([resize_transform, transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,))])

    trainset = torchvision.datasets.USPS(root='./data', train=True, download=True, transform=transform)
    # len(trainset) longitud del conjuto; trainset[i] un elemento en particular

    trainset_reduced, trainset_reduced2 = random_split(trainset, [n, 7291-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4) # Para poder partir el conjunto en minilotes y que sea iterable

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    #__________
    
    classes = ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)
    #images.to(device)
    #labels.to(device)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')

    # show images
    #imshow(torchvision.utils.make_grid(images))
    # print labels
    print(' '.join('%5s' % classes[labels[j]] for j in range(4)))
    #print(images[1])

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn

######################################################################## 
# DATOS de imagenette
# #################
def T_Imagenette(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/imagenette/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 9469-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('pez', 'perro', 'sonido', 'motosierra', 'iglesia', 'trompeta', 'camion', 'gasolinera', 'pelotagolf', 'paracaidas')

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn

######################################################################## 
# DATOS de Intel
# #################
def T_Intel(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Intel/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 14034-n])  

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=12)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=12)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('buildings', 'forest', 'glacier', 'mountain', 'sea', 'street')

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn

######################################################################## 
# DATOS de Vegetable
# #################
def T_Vegetable(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
        
    data_path = './data/Vegetable/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)
    
    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 21000-n])  

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)
    
    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()
    
    classes = ('Bean', 'Bitter', 'Bottle', 'Brinjal', 'Broccoli', 'Cabbage', 'Capsicum', 'Carrot', 'Cauliflower', 'Cucumber', 'Papaya', 'Potato', 'Pumpkin', 'Radish', 'Tomato')

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  # images=torch.Size([64, 1, 28, 28]),  labels = torch.Size([64])
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn

######################################################################## 
# DATOS de Medical Mnist
# https://www.kaggle.com/datasets/andrewmvd/medical-mnist?select=HeadCT
# #################
def T_medicalMnist(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
            
    data_path = './data/medicalMnist/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)

    # Obtiene las clases (etiquetas) del conjunto de datos
    classes = train_dataset.classes

    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 58954 -n])    

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular

    print("Elemento:",classes[labels[0]],", Etiqueta:",labels[0])
    
    return trainloader, classes, Xin, labelsIn


######################################################################## 
# DATOS de Sign Language
# https://www.kaggle.com/datasets/prekshapalva/indian-sign-language
# #################
def T_signLanguage(n,bsize):
    
    transform = transforms.Compose([transforms.ToTensor(), transforms.Normalize((0.5,), (0.5,)), transforms.Grayscale(num_output_channels=1), transforms.Resize((32,32))])
            
    data_path = './data/signLanguage/'
    train_dataset = torchvision.datasets.ImageFolder(root=data_path, transform=transform)

    # Obtiene las clases (etiquetas) del conjunto de datos
    classes = train_dataset.classes

    # # Crea un vector de etiquetas para todo el conjunto de datos
    # labels2 = train_dataset.targets

    trainset_reduced, trainset_reduced2 = random_split(train_dataset, [n, 53512-n])

    trainloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=bsize, shuffle=False, num_workers=4)

    # la matriz Xin para comparación con curvas RNX__________
    Xinloader = torch.utils.data.DataLoader(trainset_reduced, batch_size=n, shuffle=False, num_workers=4)
    dataiterXin = iter(Xinloader)
    Xin, labelsIn = next(dataiterXin)  # son los datos totales de entrada en formato matricial, labelsIn son las etiquetas totales
    Xin = Xin.reshape(-1,1024)
    Xin = Xin.numpy()

    dataiter = iter(trainloader)
    images, labels = next(dataiter)  
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    #trainloader.dataset[1] para acceder a un elemento en particular
    
    return trainloader, classes, Xin, labelsIn
# In[ ]:


######################################################################## 
# 1. DATOS
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import os

def loadDataOrigin(device): #trainloader, etiquetas, Xin, labelsIn, Xint, dimImg, rowImg, nCh
    
    bd=True
    dimImg=0
    batchSize=64

    while bd==True:
    
        T_menuData()
 
        opcionMenu = input("Selecciona una opción >> ")
 
        if opcionMenu=="1": #(1,28,28)    
            print ("")
            print("Has selecionado el data set FashionMNIST")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_FashionMNIST(n, batchSize) # etiquetas realmente son clases       
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=28
            rowImg=28
            nCh=1# No aplica
            bd=False
            nombreDB = "FashionMnist"

        elif opcionMenu=="2": #(1,28,28)     
            print ("")
            print("Has selecionado el data set MNIST")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_MNIST(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=28
            rowImg=28
            nCh=1# No aplica
            bd=False
            nombreDB = "Mnist"
        
        elif opcionMenu=="3": #(3,32,32)
            print ("")
            print("Has selecionado el data set CIFAR")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_CIFAR(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32
            rowImg=32
            nCh=3
            bd=False
            nombreDB = "Cifar"
        
        elif opcionMenu=="4": #(1,32,32)
            print ("")
            print("Has selecionado el data set Coil20")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Coil20(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32
            rowImg=32
            nCh=1
            bd=False
            nombreDB = "Coil20"
        
        elif opcionMenu=="5": #(1,28,20)
            print ("")
            print("Has selecionado el data set Faces")
            trainloader, etiquetas, Xin, labelsIn = T_Faces(batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=28
            rowImg=20
            nCh=1# No aplica
            bd=False
            nombreDB = "Faces"
        
        elif opcionMenu=="6":
            print ("")
            print("Has selecionado el data set Tufts")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Tufts(n, batchSize) # etiquetas realmente son clases       
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Tufts"
            
        elif opcionMenu=="7":
            print ("")
            print("Has selecionado el data set naturalImages")
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_naturalImages(n, batchSize) # etiquetas realmente son clases       
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "naturalImages"
            
        elif opcionMenu=="8":
            print ("")
            print("Has selecionado el data set Sings")  
            trainloader, etiquetas, Xin, labelsIn = T_Signs(batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Singns"
            
        elif opcionMenu=="9":
            print ("")
            print("Has selecionado el data set FER")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_FacesEmotion(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "FER"
            
        elif opcionMenu=="10":
            print ("")
            print("Has selecionado el data set Chess")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Chess(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Chess"
            
        elif opcionMenu=="11":
            print ("")
            print("Has selecionado el data set Lego")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Lego(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Lego"
            
        elif opcionMenu=="12":
            print ("")
            print("Has selecionado el data set Brain")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Brain()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Brain"
            
        elif opcionMenu=="13":
            print ("")
            print("Has selecionado el data set ChestXray")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_ChestXray()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Chest"   
            
        elif opcionMenu=="14":
            print ("")
            print("Has selecionado el data set SenosGT")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_SenosGT()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "SenosGT"   
            
        elif opcionMenu=="15":
            print ("")
            print("Has selecionado el data set Senos")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Senos()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Senos"   
            
        elif opcionMenu=="16":
            print ("")
            print("Has selecionado el data set Pulmon")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Pulmon()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Pulmon"   
            
        elif opcionMenu=="17":
            print ("")
            print("Has selecionado el data set PulmonCNPT")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_PulmonCNPT()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "PulmonCNPT"   
            
        elif opcionMenu=="18":
            print ("")
            print("Has selecionado el data set Glaucoma")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Glaucoma()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Glaucoma"   
            
        elif opcionMenu=="19":
            print ("")
            print("Has selecionado el data set Brain17")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Brain17()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Brain17"   
            
        elif opcionMenu=="20":
            print ("")
            print("Has selecionado el data set Brain44")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Brain44()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Brain44"   
            
        elif opcionMenu=="21":
            print ("")
            print("Has selecionado el data set Osteoartritis")  
#             n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Osteoartritis()
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Osteoartritis"   
            
        elif opcionMenu=="24":
            print ("")
            print("Has selecionado el data set USPS")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_USPS(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "USPS"  
            
        elif opcionMenu=="25":
            print ("")
            print("Has selecionado el data set Imagenette")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Imagenette(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Imagenette"  
            
        elif opcionMenu=="26":
            print ("")
            print("Has selecionado el data set Intel")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Intel(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Intel"  
            
        elif opcionMenu=="27":
            print ("")
            print("Has selecionado el data set Vegetable")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_Vegetable(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "Vegetable"     
            
        elif opcionMenu=="28":
            print ("")
            print("Has selecionado el data set Medical Mnist")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_medicalMnist(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "medicalMnist"
            
        elif opcionMenu=="29":
            print ("")
            print("Has selecionado el data set signLanguage")  
            n = int(input("Tamaño del conjunto de datos: >> "))
            trainloader, etiquetas, Xin, labelsIn = T_signLanguage(n, batchSize)
            Xint = torch.from_numpy(Xin).to(device)
            dimImg=32 #256
            rowImg=32 #336
            nCh=1# No aplica
            bd=False
            nombreDB = "signLanguage"       
             
        elif opcionMenu=="30":
            bd=False
            break
        else:
            print ("")
            input("No has pulsado ninguna opción correcta...\npulsa una tecla para continuar")
            
        return trainloader, etiquetas, Xin, labelsIn, Xint, dimImg, rowImg, nCh, nombreDB


# In[ ]:


########################################################################
# Carga el Xin y labelsIn asociadas al modelo neuronal
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
def loadXinLTensorCsv(rutaX, rutaY, bsize):
    
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    
    print("Cargando Trainloader, Xin y LabelsIn asociados al modelo...")
    import pandas as pd
    
    T_menuData()
    opcionMenu = input("Selecciona una opción >> ")
 
    if opcionMenu=="1": #(1,28,28)    
        print ("")
        print("Carga de FashionMNIST")
        dimImg=28
        rowImg=28
        nCh=1
        nombreDB = "FashionMnist"
        classes = ('t-shirt', 'trouser', 'pullover', 'dress', 'coat', 'sandal', 'shirt', 'sneaker', 'bag', 'boot')

    elif opcionMenu=="2": #(1,28,28)     
        print ("")
        print("Carga de MNIST")
        dimImg=28
        rowImg=28
        nCh=1# No aplica
        nombreDB = "Mnist"
        classes = ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        
    elif opcionMenu=="3": #(3,32,32)
        print ("")
        print("Has selecionado el data set CIFAR")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "Cifar"
        classes = ('airplane', 'automobile', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck')
        
    elif opcionMenu=="4": #(1,32,32)
        print ("")
        print("Has selecionado el data set Coil20")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Coil20"
        classes = ('pato', 'flecha', 'carro', 'gato', 'anacin', 'carro2', 'lego', 'talco', 'tylenol', 'vaselina', 'ficha', 'vaso', 'alcancia', 'plafon', 'tarro', 'frasco', 'vasija', 'taza', 'carro3', 'arequipe')
        
    elif opcionMenu=="5": #(1,28,20)
        print ("")
        print("Has selecionado el data set Faces")
        dimImg=20
        rowImg=28 # CAMBIE AQUI LA RESOLU
        nCh=1# No aplica
        bd=False
        nombreDB = "Faces"
        # ___etiquetas y
        print ("ETIQUETAS FACES")
        print ("\t1 - Etiquetas continuas")
        print ("\t2 - Etiquetas discretas")
        opcCoD = input("Seleccione la opción: >> ")
    
        if opcCoD == "1":
            #y = np.asarray(range(1920))
            #y = torch.from_numpy(y)
            y = torch.range(0,1920,1)
            classes = y
        elif opcCoD == "2":
            ncfaces = int(input("Número de clases: >> ")) # numero de clases faces
            y = torch.zeros(1920) # labels discretos
            inc = 0 # inicio
            lim = int(1920/ncfaces) # limite superior
            incre = lim # constante, incremento
            for i in range(ncfaces):
                y[inc:lim] = i+1
                #print(y[inc])  
                #print("lim", lim)
                inc = lim
                lim = lim + incre
                classes = torch.range(0,ncfaces,1) # clases discretas
        # ___
    elif opcionMenu=="11": #(1,32,32)
        print ("")
        print("Has selecionado el data set Legos")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Lego"
        classes = torch.range(0,16,1)
        
    elif opcionMenu=="12": #(1,32,32)
        print ("")
        print("Has selecionado el data set Brain")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Brain"
        classes = ('glioma', 'meningioma', 'notumor', 'pituitary')
        
    elif opcionMenu=="13": #(1,32,32)
        print ("")
        print("Has selecionado el data set Chest Xray")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Chest Xray"
        classes = ('Normal', 'Pneumonia')
        
    elif opcionMenu=="14": #(1,32,32)
        print ("")
        print("Has selecionado el data set SenosGT")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "SenosGT"
        classes = ('benign', 'malignant', 'normal')
        
    elif opcionMenu=="15": #(1,32,32)
        print ("")
        print("Has selecionado el data set Senos")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Senos"
        classes = ('Cancer_negative', 'Cancer_positive', 'implant_cancer_negative', 'implant_cancer_positive')
        
    elif opcionMenu=="16": #(1,32,32)
        print ("")
        print("Has selecionado el data set Pulmon")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Pulmon"
        classes = ('Covid', 'Normal', 'Pneumonia')
        
    elif opcionMenu=="17": #(1,32,32)
        print ("")
        print("Has selecionado el data set PulmonCNPT")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "PulmonCNPT"
        classes = ('Covid', 'Normal', 'Pneumonia', 'Tuberculosis')
        
    elif opcionMenu=="18": #(1,32,32)
        print ("")
        print("Has selecionado el data set Glaucoma")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Glaucoma"
        classes = ('Acrina', 'Origa')
        
    elif opcionMenu=="19": #(1,32,32)
        print ("")
        print("Has selecionado el data set Brain17")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Brain17"
        classes = ('GliomaT1', 'GliomaT1C+', 'GliomaT2', 'MeningiomaT1', 'MeningiomaT1C+', 'MeningiomaT2', 'NeurocitomaT1', 'NeurocitomaT1C+', 'NeurocitomaT2', 'NormalT1', 'NormalT2', 'OtrosT1', 'OtrosT1C+', 'OtrosT2', 'SchwannomaT1', 'SchwannomaT1C+', 'SchwannomaT2')
        
    elif opcionMenu=="20": #(1,32,32)
        print ("")
        print("Has selecionado el data set Brain44")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Brain44"
        classes = ('NormalT1', 'NormalT2', 'AstrocitomaT1', 'AstrocitomaT1C+', 'AstrocitomaT2', 'CarcinomaT1', 'CarcinomaT1C+', 'CarcinomaT2', 'EpendimomaT1', 'EpendimomaT1C+', 'EpendimomaT2', 'GangliogliomaT1', 'GangliogliomaT1C+', 'GangliogliomaT2', 'GerminomaT1', 'GerminomaT1C+', 'GerminomaT2', 'GlioblastomaT1', 'GlioblastomaT1C+', 'GlioblastomaT2', 'GranulomaT1', 'GranulomaT1C+', 'GranulomaT2', 'MeduloblastomaT1', 'MeduloblastomaT1C+', 'MeduloblastomaT2', 'MeningiomaT1', 'MeningiomaT1C+', 'MeningiomaT2', 'NeurocitomaT1', 'NeurocitomaT1C+', 'NeurocitomaT2', 'OligodendrogliomaT1', 'OligodendrogliomaT1C+', 'OligodendrogliomaT2', 'PapilomaT1', 'PapilomaT1C+', 'PapilomaT2', 'SchwannomaT1', 'SchwannomaT1C+', 'SchwannomaT2', 'TuberculomaT1', 'TuberculomaT1C+', 'TuberculomaT2')
     
    elif opcionMenu=="21": #(1,32,32)
        print ("")
        print("Has selecionado el data set Osteoartritis")
        dimImg=32
        rowImg=32
        nCh=1
        nombreDB = "Osteoartritis"
        classes = ('Normal', 'Osteoartritis')
        
    elif opcionMenu=="22": #(1,32,32)
        print ("")
        print("Has selecionado el data set Brain300x300")
        dimImg=300
        rowImg=300
        nCh=1
        nombreDB = "Brain300x300"
        classes = ('glioma', 'meningioma', 'notumor', 'pituitary')
        
    elif opcionMenu=="24": 
        print ("")
        print("Has selecionado el data set USPS")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "USPS"
        classes = ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        
    elif opcionMenu=="25": 
        print ("")
        print("Has selecionado el data set Imagenette")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "Imagenette"
        classes = ('pez', 'perro', 'sonido', 'motosierra', 'iglesia', 'trompeta', 'camion', 'gasolinera', 'pelotagolf', 'paracaidas')
        
    elif opcionMenu=="26": 
        print ("")
        print("Has selecionado el data set Intel")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "Intel"
        classes = ('buildings', 'forest', 'glacier', 'mountain', 'sea', 'street')
        
    elif opcionMenu=="27": 
        print ("")
        print("Has selecionado el data set Vegetable")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "Vegetable"
        classes = ('Bean', 'Bitter', 'Bottle', 'Brinjal', 'Broccoli', 'Cabbage', 'Capsicum', 'Carrot', 'Cauliflower', 'Cucumber', 'Papaya', 'Potato', 'Pumpkin', 'Radish', 'Tomato')

    elif opcionMenu=="28": 
        print ("")
        print("Has selecionado el data set Medical Mnist")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "medicalMnist"
        classes = ('AbdomenCT', 'BreastMRI', 'CXR', 'ChestCT', 'Hand', 'HeadCT')

    elif opcionMenu=="29": 
        print ("")
        print("Has selecionado el data set Indian signLanguage")
        dimImg=32
        rowImg=32
        #nCh=3
        nCh=1
        nombreDB = "signLanguage"
        classes = ('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
        
    elif opcionMenu=="30":
        print ("Salida")
         
    #df = pd.read_csv('./RNXmat/Xin'+nombreDB+'.dat', header=None)
    df = pd.read_csv(rutaX+nombreDB+'.dat', header=None)
    Xi = X = np.asarray(df)
    #X = np.loadtxt("./model/Xin.csv", delimiter=',')
    X = X.reshape(-1,nCh,rowImg,dimImg) # Se lo transforma a estructura tensor
    #X = np.array(X)
    
    if opcionMenu != "5": # diferente de faces
        #y = np.transpose(np.loadtxt("./RNXmat/labelsIn"+nombreDB+".csv", delimiter=',', unpack=True))
        y = np.transpose(np.loadtxt(rutaY+nombreDB+".csv", delimiter=',', unpack=True))
        y = torch.from_numpy(y)

    X = torch.from_numpy(X).float()
    
    dataset = torch.utils.data.TensorDataset(X, y)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False)
    
    #batch = next(iter(trainloader))   
    #dataiter = iter(trainloader)
    images, labels = next(iter(trainloader))
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    print("X ", X.shape)
    print("y", y.shape) 
    
    Xt = torch.from_numpy(Xi).float().device
    
    return trainloader, classes, Xi, y, Xt, dimImg, rowImg, nCh, nombreDB


# In[ ]:


######################################################################## 
# 1. DATOS Tensor
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

def loadDataTensor(rutaX, rutaY, bsize, device):
    print ("DataSets")
    print ("\t1 - Generación de datos de la fuente")
    print ("\t2 - Carga de datos desde archivo generado")
    print ("\tZ - Salir")
    opcionDS = input("Seleccione la opción: >> ")

    if opcionDS == "1":
        trainloader, clases, Xin, labelsIn, Xint, dimImg, rowImg, nCh, nombreDB = loadDataOrigin(device) # aqui las etiquetas las estoy tomando como clases
        guardarXL(nombreDB+"_Generated", Xin, labelsIn)
    elif opcionDS == "2":
        trainloader, clases, Xin, labelsIn, Xint, dimImg, rowImg, nCh, nombreDB = loadXinLTensorCsv(rutaX, rutaY, bsize) # aqui las etiquetas las estoy tomando como clases
        
    return trainloader, clases, Xin, labelsIn, Xint, dimImg, rowImg, nCh, nombreDB


# In[ ]:


######################################################################## 
# PARA EXPANDIR DATOS TENSORIALES CON KDTREE
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


# In[ ]:


# con collate_fn se puede hacer minilotes transformados, menores o iguales al batch de entrada. 
# En este caso la idea es generar un lote mayor al batch de entrada conformado por los k vecinos de cada punto, por tanto llamo esta función, no como parametro del dataloader, sino antes de establecerlo. 

from sklearn.neighbors import KDTree

def Mycollate_fn(device,X,y,v): # v es el tamaño del vecindario para cada punto
    
    # Este bloque comentado, era necesario para el collate_fn
    # X, y = zip(*batch)
    # X  = torch.stack(list(X), dim=0)
    # n,ch,f,l = X.shape
    # X2 = X.view(n, ch*f*l) # aplano x para hacer las distancias de vecindarios

    kdt = KDTree(X, leaf_size=30, metric='euclidean')
    vecinos = kdt.query(X, k=v, return_distance=False)
    print("vecinos", vecinos)
   
    y_list, X_list, indices = [], [], []

    for v in vecinos:
        for i in v:
            #print("i", i)
            if i not in indices:
                indices.append(i)
                y_list.append(y[i])
                X_list.append(X[i])
   
    y_list  = torch.stack(y_list, dim=0)
    # print("y_list", y_list.shape)
    
    X_list  = torch.stack(X_list, dim=0)
    # print("X_list", X_list.shape)
    
    return X_list.to(device), y_list.to(device)


# In[ ]:


########################################################################
# Expansión KDTREE y Collate_fn
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
def loadKDTreeCsv(device, X, y, nCh, rowImg, dimImg, bsize, vsize): # bsize es el tamaño del minilote. vsize es el tamaño del vecindario para cada punto pasado a la función Mycollate_fn
    print("Expandiendo con KDTREE y Collate_fn ...")
    
    X = torch.from_numpy(X).float()

    X, y = Mycollate_fn(device,X, y,vsize)
     
    Xt = X.reshape(-1,nCh,rowImg,dimImg) # Se lo transforma a estructura tensor

    dataset = torch.utils.data.TensorDataset(Xt, y)

    # trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False, collate_fn=collate_fn)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False)
    
    #batch = next(iter(trainloader))   
    #dataiter = iter(trainloader)
    images, labels = next(iter(trainloader))
    print("images ",images.shape)
    print("labels ",labels.shape)

    # Dibujamos la imagen
    plt.figure(figsize=(1,1))
    images = images.cpu().detach().numpy()
    plt.imshow(images[0][0], cmap='gray')
    plt.show()

    print("tamaño del cojunto de datos o lote", len(trainloader.dataset)) 
    print("tamaño del mini lote", trainloader.batch_size) 
    print("numero de minilotes ", len(trainloader)) 
    print("Xtensor ", Xt.shape)
    print("X ", X.shape)
    print("y", y.shape) 
    
    #Xt = torch.from_numpy(Xi).float().to(device)
    
    return trainloader, X, y, Xt


# In[ ]:


######################################################################## 
# DATOS MATRICIALES
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


# In[ ]:


import os

def M_menuData():
    os.system('cls') 
    print ("MENU DE DATOS MATRICIALES")
    print ("\t1 - Swiss Roll")
    print ("\t2 - Digits")
    print ("\t3 - Iris")
    print ("\t4 - Moons")
    print ("\t5 - Scurve")
    print ("\t6 - Shpere")
    print ("\t7 - Manuales")
    print ("\t8 - CarinomaHepatoCeluar")
    print ("\t9 - Alzaimer")
    print ("\t10 - CerebroVascular")
    print ("\t11 - Heart")
    print ("\t12 - Diabetes")
    print ("\t13 - Higado")
    print ("\t14 - Mamografia")
    print ("\t15 - Diabetic2")
    print ("\t16 - Ataque Cardiaco")
    print ("\t17 - Framing Heart")
    print ("\t18 - Cancer Cervical")
    print ("\t19 - Survival")
    print ("\t20 - Wisconsin")
    print ("\t21 - HeartMatriz")
    print ("\t22 - Derrame")
    print ("\t23 - Hcc")
    print ("\t24 - Parkinsons")
    print ("\t25 - HeartOtro")
    print ("\t26 - Arritmia")
    print ("\t27 - Shpere2")
    
    print ("\t30 - Salir")


# In[ ]:


######################################################################## 
# 1. DATOS Matriz
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import os

def loadDataMatriz(device):

        M_menuData()
 
        opcionMenu = input("Selecciona una opción >> ")
 
        if opcionMenu=="1":   
            print ("")
            print("Has selecionado el data set Swiss Roll")
            clases, Xin, etiquetas, Xint, nombreDB = M_SwissRoll(device)           

        elif opcionMenu=="2":    
            print ("")
            print("Has selecionado el data set Digits")
            clases, Xin, etiquetas, Xint, nombreDB = M_Digits(device)
            Xint = torch.from_numpy(Xin).to(device)
        
        elif opcionMenu=="3": 
            print ("")
            print("Has selecionado el data set Iris")
            clases, Xin, etiquetas, Xint, nombreDB = M_Iris(device)  
        
        elif opcionMenu=="4": 
            print ("")
            print("Has selecionado el data set Moons")
            clases, Xin, etiquetas, Xint, nombreDB = M_Moons(device)
        
        elif opcionMenu=="5":
            print ("")
            print("Has selecionado el data set Scurve")
            clases, Xin, etiquetas, Xint, nombreDB = M_Scurve(device) 
            
        elif opcionMenu=="6": 
            print ("")
            print("Has selecionado el data set Shpere")
            clases, Xin, etiquetas, Xint, nombreDB = M_Sphere(device)
            
        elif opcionMenu=="7": 
            print ("")
            print("Has selecionado el data set Manuales")
            clases, Xin, etiquetas, Xint, nombreDB = Manuales(device)
            
        elif opcionMenu=="8": 
            print ("")
            print("Has selecionado el data set Carcinoma")
            clases, Xin, etiquetas, Xint, nombreDB = M_Carcionoma(device)
            
        elif opcionMenu=="9": 
            print ("")
            print("Has selecionado el data set Alzaimer")
            clases, Xin, etiquetas, Xint, nombreDB = M_Alzaimer(device)
            
        elif opcionMenu=="10": 
            print ("")
            print("Has selecionado el data set Cerebro Vascular")
            clases, Xin, etiquetas, Xint, nombreDB = M_CerebroVascular(device)
            
        elif opcionMenu=="11": 
            print ("")
            print("Has selecionado el data set Heart")
            clases, Xin, etiquetas, Xint, nombreDB = M_Heart(device)
            
        elif opcionMenu=="12": 
            print ("")
            print("Has selecionado el data set Diabetes")
            clases, Xin, etiquetas, Xint, nombreDB = M_Diabetes(device)
            
        elif opcionMenu=="13": 
            print ("")
            print("Has selecionado el data set Higado")
            clases, Xin, etiquetas, Xint, nombreDB = M_Higado(device)
            
        elif opcionMenu=="14": 
            print ("")
            print("Has selecionado el data set Mamografia")
            clases, Xin, etiquetas, Xint, nombreDB = M_Mamografia(device)
            
        elif opcionMenu=="15": 
            print ("")
            print("Has selecionado el data set Diabetic2")
            clases, Xin, etiquetas, Xint, nombreDB = M_Diabetic2(device)
            
        elif opcionMenu=="16": 
            print ("")
            print("Has selecionado el data set Ataque Cardiaco")
            clases, Xin, etiquetas, Xint, nombreDB = M_AtaqueCar(device)
            
        elif opcionMenu=="17": 
            print ("")
            print("Has selecionado el data set Framing Heart")
            clases, Xin, etiquetas, Xint, nombreDB = M_FramingHeart(device)
            
        elif opcionMenu=="18": 
            print ("")
            print("Has selecionado el data set Cancer Cervical")
            clases, Xin, etiquetas, Xint, nombreDB = M_Cervical(device)
            
        elif opcionMenu=="19": 
            print ("")
            print("Has selecionado el data set Survival")
            clases, Xin, etiquetas, Xint, nombreDB = M_Survival(device)
            
        elif opcionMenu=="20": 
            print ("")
            print("Has selecionado el data set Wisconsin")
            clases, Xin, etiquetas, Xint, nombreDB = M_Wisconsin(device)
            
        elif opcionMenu=="21": 
            print ("")
            print("Has selecionado el data set Heart Matriz")
            clases, Xin, etiquetas, Xint, nombreDB = M_HeartMatriz(device)
            
        elif opcionMenu=="22": 
            print ("")
            print("Has selecionado el data set Derrame")
            clases, Xin, etiquetas, Xint, nombreDB = M_Derrame(device)   
            
        elif opcionMenu=="23": 
            print ("")
            print("Has selecionado el data set HCC")
            clases, Xin, etiquetas, Xint, nombreDB = M_Hcc(device)    
            
        elif opcionMenu=="24": 
            print ("")
            print("Has selecionado el data set Parkinsons")
            clases, Xin, etiquetas, Xint, nombreDB = M_Parkinsons(device) 
            
        elif opcionMenu=="25": 
            print ("")
            print("Has selecionado el data set HeartOtro")
            clases, Xin, etiquetas, Xint, nombreDB = M_HeartOtro(device)  
            
        elif opcionMenu=="26": 
            print ("")
            print("Has selecionado el data set Arritmia")
            clases, Xin, etiquetas, Xint, nombreDB =  M_Arritmia(device)
            
        elif opcionMenu=="27": 
            print ("")
            print("Has selecionado el data set Shpere2")
            clases, Xin, etiquetas, Xint, nombreDB = M_Sphere2(device)
            
        else:
            print ("")
            input("No has pulsado ninguna opción correcta...\npulsa una tecla para continuar")
            
        return clases, Xin, etiquetas, Xint, nombreDB


# In[ ]:


from sklearn.datasets import make_swiss_roll
from sklearn.preprocessing import MinMaxScaler
from matplotlib import pyplot as plt
import mpl_toolkits.mplot3d.axes3d as p3

import scipy.io
#import torchvision.transforms as transforms
import torch

def M_SwissRoll(device):
    
    optSwiss = input("(p) para cargar los datos generados previamente en phyton, (c) para cargar los datos de matlab, (g) para genear los datos: ")
    
    if optSwiss=="p" or optSwiss=="P":
        print ("")
        print("Has selecionado: Cargar el rollo suizo gereado en Phyton previamente")
        
        df = pd.read_csv('./data/XinSwissRoll.dat', header=None)
        X = np.asarray(df)

        colors = np.transpose(np.loadtxt("./data/labelsInSwissRoll.csv", delimiter=',', unpack=True))
        # Se discretiza en 15 clases
        limites = np.linspace(colors.min(), colors.max(), num=15)
        colors = np.digitize(colors, bins=limites, right=True)
        
    if optSwiss=="c" or optSwiss=="C":
        print ("")
        print("Has selecionado: Cargar el rollo suizo")
        
        mat = scipy.io.loadmat("./data/data_swissroll.mat") # cargamos datos en formato de matlab

        X=mat['Y'] # incialmente los datos estan en estructura matriz de 1024
        colors=mat['L']
        colors=colors-1 # se le resta 1 poruqe las clases se parten desde la posicion 0, y en matlab las trae desde la pos 1, es decir, los nombres de las imagenes estan corridas un lugar
        colors=colors[:, 0] 
        
    if optSwiss=="g" or optSwiss=="G":
        print ("")
        print("Has selecionado: Generar el rollo suizo")
          
        n_samples = int(input("Número de puntos: "))
        noise = 0.05
        X, colors = make_swiss_roll(n_samples=n_samples, noise=noise)
        X = MinMaxScaler().fit_transform(X)
        guardarXL('SwissRoll', X, colors)
    
    clases = ('azul', 'celeste', 'cyan', 'magenta', 'verdeoscuro', 'verde', 'verdeclaro', 'marilloverdoso', 'amarilloclaro', 'amarillo', 'amarillooscuro', 'naranja', 'rojoclaro', 'rojo', 'rojooscuro')         
    Xt = torch.from_numpy(X).float().to(device)
    nombreDB = "SwissRoll"
    
    fig = plt.figure()
    ax = p3.Axes3D(fig)
    ax.scatter(X[:,0], X[:,1], X[:,2], c=colors, cmap=plt.cm.jet)
    plt.title('SwissRoll')
    plt.show()
    
    return clases, X, colors, Xt, nombreDB


# In[ ]:


## Datos Digits

from sklearn.datasets import load_digits

def M_Digits(device):

    inputs, labels = load_digits(return_X_y=True)
    Xt = torch.from_numpy(inputs).float().to(device)
    print(inputs.shape)
    
    clases = ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')   
    nombreDB = "Digits"
    
    plt.scatter(inputs[:, 0], inputs[:, 1], c=labels, cmap=plt.cm.jet)
    plt.title('Digits')
    plt.show()
    
    return clases, inputs, labels, Xt, nombreDB


# In[ ]:


from sklearn import datasets

def M_Iris(device):
    
    iris = datasets.load_iris()
    
    X = iris.data
    y = iris.target
    target_names = iris.target_names
    
    Xt = torch.from_numpy(X).to(device)
    print(X.shape)
    nombreDB = "Iris"
    
    plt.scatter(X[:, 0], X[:, 1], c=y, cmap=plt.cm.jet)
    plt.title('Iris')
    plt.show()
    
    return target_names, X, y, Xt, nombreDB


# In[ ]:


from sklearn.datasets import make_moons 

def M_Moons(device):

    inputs, labels = make_moons(n_samples = 500, noise = 0.02, random_state = 417)
    Xt = torch.from_numpy(inputs).float().to(device)
    
    plt.scatter(inputs[:, 0], inputs[:, 1], c=labels, cmap=plt.cm.jet)
    plt.title('Moons')
    plt.show()
    
    inputs = torch.from_numpy(inputs).float()
    print(inputs.shape)
    
    clases = ('azul', 'rojo')     
    nombreDB = "Moons"
    
    return clases, inputs, labels, Xt, nombreDB


# In[ ]:


import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from sklearn import manifold, datasets

# Next line to silence pyflakes. This import is needed.
Axes3D

def M_Scurve(device):

    n_points = 1000
    inputs, labels = datasets.make_s_curve(n_points, random_state=0)
    n_neighbors = 10
    n_components = 2

    # Create figure
    fig = plt.figure(figsize=(15, 8))
    fig.suptitle("Manifold Learning with %i points, %i neighbors" % (1000, n_neighbors), fontsize=14)

    # Add 3d scatter plot
    ax = fig.add_subplot(251, projection='3d')
    ax.scatter(inputs[:, 0], inputs[:, 1], inputs[:, 2], c=labels, cmap=plt.cm.Spectral)
    ax.view_init(4, -72)
    plt.show()
    
    Xt = torch.from_numpy(inputs).float().to(device)
    print(inputs.shape)
    
    clases = ('azul', 'celeste', 'cyan', 'magenta', 'verdeoscuro', 'verde', 'verdeclaro', 'marilloverdoso', 'amarilloclaro', 'amarillo', 'amarillooscuro', 'naranja', 'rojoclaro', 'rojo', 'rojooscuro') # son las clases         
    nombreDB = "Scurve"
    
    return clases, inputs, labels, Xt, nombreDB


# In[ ]:


import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.ticker import NullFormatter

from sklearn import manifold
from sklearn.utils import check_random_state

# Next line to silence pyflakes.
Axes3D

def M_Sphere(device):
    
    optSph = input("(p) para cargar los datos generados previamente en phyton, (g) para genear los datos: ")
    
    if optSph=="p" or optSph=="P":
        print ("")
        print("Has selecionado: Cargar el la esfera gereada en Phyton previamente")
        
        df = pd.read_csv('./data/XinShpere.dat', header=None)
        sphere_data = np.asarray(df)

        colors = np.transpose(np.loadtxt("./data/labelsInShpere.csv", delimiter=',', unpack=True))
        #colors = torch.from_numpy(colors)
        n_samples = len(sphere_data )

    if optSph=="g" or optSph=="G":
        print ("")
        print("Has selecionado: Generar la esfera")
        # Variables for manifold learning.
        n_neighbors = int(input("neighbors (15): "))
        n_samples = int(input("Número de puntos: "))

        # Create our sphere.
        random_state = check_random_state(0)
        p = random_state.rand(n_samples) * (2 * np.pi - 0.55)
        t = random_state.rand(n_samples) * np.pi

        # Sever the poles from the sphere.
        indices = ((t < (np.pi - (np.pi / 8))) & (t > ((np.pi / 8))))
        colors = p[indices]
    
        x, y, z = np.sin(t[indices]) * np.cos(p[indices]),             np.sin(t[indices]) * np.sin(p[indices]),             np.cos(t[indices])

        sphere_data = np.array([x, y, z]).T
        guardarXL('Shpere', sphere_data, colors)

    # Plot our dataset.
    fig = plt.figure(figsize=(10,10))
    plt.suptitle("Esfera %i points, 3 dimensions" % (n_samples), fontsize=14)

    ax = fig.add_subplot(251, projection='3d')
    ax.scatter(sphere_data[:, 0], sphere_data[:, 1], sphere_data[:, 2], c=colors, cmap=plt.cm.rainbow)
    ax.view_init(40, -10)
    plt.show()
    
    Xt = torch.from_numpy(sphere_data).float().to(device)
    
    clases = ('azul', 'celeste', 'cyan', 'magenta', 'verdeoscuro', 'verde', 'verdeclaro', 'marilloverdoso', 'amarilloclaro', 'amarillo', 'amarillooscuro', 'naranja', 'rojoclaro', 'rojo', 'rojooscuro') # son las clases         
    nombreDB = "Shpere"
    
    return clases, sphere_data, colors, Xt, nombreDB


# In[ ]:


import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

def M_Sphere2(device):
    
    optSph = input("(p) para cargar los datos generados previamente en phyton, (g) para genear los datos: ")
    
    if optSph=="p" or optSph=="P":
        print ("")
        print("Has selecionado: Cargar el la esfera gereada en Phyton previamente")
        
        df = pd.read_csv('./data/XinShpere2.dat', header=None)
        sphere_data = np.asarray(df)

        colors = np.transpose(np.loadtxt("./data/labelsInShpere2.csv", delimiter=',', unpack=True))
        #colors = torch.from_numpy(colors)
        n_samples = len(sphere_data )

    if optSph=="g" or optSph=="G":
        
        num_points = int(input("Por favor, ingrese el número de puntos en la esfera: "))

        # Generar coordenadas aleatorias en la superficie de la esfera
        u = np.random.rand(num_points)
        v = np.random.rand(num_points)
        theta = 2 * np.pi * u
        phi = np.arccos(2 * v - 1)
        x = np.sin(phi) * np.cos(theta)
        y = np.sin(phi) * np.sin(theta)
        z = np.cos(phi)

        # Crear etiquetas asignadas a cada punto en función de su posición en la esfera
        colors = np.round(theta / (2 * np.pi) * 15)
        
        sphere_data = np.array([x, y, z]).T
        guardarXL('Shpere2', sphere_data, colors)

    clases = ('azul', 'celeste', 'cyan', 'magenta', 'verdeoscuro', 'verde', 'verdeclaro', 'marilloverdoso', 'amarilloclaro', 'amarillo', 'amarillooscuro', 'naranja', 'rojoclaro', 'rojo', 'rojooscuro') # son las clases         
    nombreDB = "Shpere2"

    # Crear una figura 3D
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    # Graficar los puntos en la esfera con colores basados en las etiquetas
    ax.scatter(sphere_data[:,0], sphere_data[:,1], sphere_data[:,2], c=colors, cmap=plt.cm.get_cmap('jet', 15))

    # Configurar los ejes y el título
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    ax.set_title('Dataset de una Esfera 2')

    # Mostrar la figura
    plt.show()
    
    Xt = torch.from_numpy(sphere_data).float().to(device)
    
    return clases, sphere_data, colors, Xt, nombreDB


# In[ ]:


# Datos de prueba

def Manuales(device):
    Xin = np.array([[1,2,3,4],[5,6,7,8],[9,1,6,3],[7,9,8,2],[5,5,6,4],[3,7,9,4]])
    Xt = torch.tensor([[1,2,3,4],[5,6,7,8],[9,1,6,3],[7,9,8,2],[5,5,6,4],[3,7,9,4]])
    Xt = Xt.to(device)
    etiquetas = torch.tensor([1,2,3,4,5,6])
    nombreDB = "Manuales"
    
    plt.scatter(Xin[:, 0], Xin[:, 1], c=etiquetas, cmap=plt.cm.jet)
    plt.title('Manuales')
    plt.show()
    
    return etiquetas, Xin, etiquetas, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Carcionoma(device):
    
    X = pd.read_csv('./data/hcc.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/ClasesHcc.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Carcinoma"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from sklearn.preprocessing import LabelEncoder

def M_Alzaimer(device):
    
    X = pd.read_csv('./data/alzaimer.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Yalzaimer.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel() # Pone de una dimensión el arreglo
    codificador = LabelEncoder() # cambia categorías cualitativas a numéricas
    y = codificador.fit_transform(y)

    Xt = torch.from_numpy(X).to(device)
    nombreDB = "Alzaimer"
    clases = ('Demented', 'Nondemented', 'Converted') 

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_CerebroVascular(device):
    
    X = pd.read_csv('./data/cerebroVascular.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/YcerebroVascular.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "CerebroVascular"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Heart(device):
    
    X = pd.read_csv('./data/heart.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Yheart.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Heart"
    clases = torch.tensor([0,1,2,3,4])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_FramingHeart(device):
    
    X = pd.read_csv('./data/framingham.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Yframingham.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "FramingHeart"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Diabetes(device):
    
    X = pd.read_csv('./data/diabetes.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Ydiabetes.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Diabetes"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Higado(device):
    
    X = pd.read_csv('./data/higado.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Yhigado.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Higado"
    clases = torch.tensor([1,2])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Mamografia(device):
    
    X = pd.read_csv('./data/mamografia.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Ymamografia.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Mamografia"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_Diabetic2(device):
    
    X = pd.read_csv('./data/diabetic.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Ydiabetic.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Diabetic2"
    clases = torch.tensor([0,1,2,3,4,5])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

def M_AtaqueCar(device):
    
    X = pd.read_csv('./data/attackHeart.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/YattackHeart.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Ataque"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Cervical(device):
    
    X = pd.read_csv('./data/cervical.csv')
    X = X.replace("?", 0)
    X = np.asarray(X)
    X = X.astype(float)
    y = pd.read_csv('./data/Ycervical.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Cervical"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Survival(device):
    
    X = pd.read_csv('./data/survival.csv')
    X = X.replace("", 0)
    X = X.fillna(0)
    
    X['ethnicity'] = pd.factorize(X['ethnicity'])[0]
    X['gender'] = pd.factorize(X['gender'])[0]
    X['icu_admit_source'] = pd.factorize(X['icu_admit_source'])[0]
    X['icu_stay_type'] = pd.factorize(X['icu_stay_type'])[0]
    X['icu_type'] = pd.factorize(X['icu_type'])[0]
    X['pre_icu_los_days'] = pd.factorize(X['pre_icu_los_days'])[0]
    X['apache_3j_bodysystem'] = pd.factorize(X['apache_3j_bodysystem'])[0]
    X['apache_2_bodysystem'] = pd.factorize(X['apache_2_bodysystem'])[0]
    
#     print("X",X)
    
    X = np.asarray(X)
    
    X = X.astype(float)
    y = pd.read_csv('./data/Ysurvival.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Cervical"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from sklearn.preprocessing import LabelEncoder
import numpy as np

def M_Wisconsin(device):
    
    X = pd.read_csv('./data/wisconsin.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/Ywisconsin.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel() # Pone de una dimensión el arreglo
    codificador = LabelEncoder() # cambia categorías cualitativas a numéricas
    y = codificador.fit_transform(y)

    Xt = torch.from_numpy(X).to(device)
    nombreDB = "Wisconsin"
    clases = ('M', 'B') 

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
from sklearn.preprocessing import LabelEncoder
import numpy as np

def M_HeartMatriz(device):
    
    X = pd.read_csv('./data/heartMatriz.csv')
    X = np.asarray(X)
#     X = X.astype(float)
    y = pd.read_csv('./data/YheartMatriz.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel() # Pone de una dimensión el arreglo
   
    Xt = torch.from_numpy(X).to(device)
    nombreDB = "heartMatriz"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Derrame(device):
    
    X = pd.read_csv('./data/derrameMatriz.csv')
    X = X.replace("", 0)
    X = X.fillna(0)
    
    X['gender'] = pd.factorize(X['gender'])[0]
    X['ever_married'] = pd.factorize(X['ever_married'])[0]
    X['work_type'] = pd.factorize(X['work_type'])[0]
    X['Residence_type'] = pd.factorize(X['Residence_type'])[0]
    X['smoking_status'] = pd.factorize(X['smoking_status'])[0]
    
#     print("X",X)
    
    X = np.asarray(X)
    
    X = X.astype(float)
    y = pd.read_csv('./data/YderrameMatriz.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Derrame"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Hcc(device):
    
    X = pd.read_csv('./data/hccMatriz.csv')
    X = X.replace("?", 0)
    X = np.asarray(X)
    X = X.astype(float)
    y = pd.read_csv('./data/YhccMatriz.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Hcc"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Parkinsons(device):
    
    X = pd.read_csv('./data/parkinsons.csv')
    X = X.replace("?", 0)
    X = np.asarray(X)
    X = X.astype(float)
    y = pd.read_csv('./data/Yparkinsons.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Parkinsons"
    clases = torch.tensor([0,1])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_HeartOtro(device):
    
    X = pd.read_csv('./data/heartOtro.csv')
    X = X.replace("?", 0)
    X = np.asarray(X)
    X = X.astype(float)
    y = pd.read_csv('./data/YheartOtro.csv')
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "HeartOtro"
    clases = torch.tensor([1,2,3])

    return clases, X, y, Xt, nombreDB


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader
import numpy as np

def M_Arritmia(device):
    
    X = pd.read_csv('./data/arritmia.csv')
    X = X.replace("?", 0)
    X = np.asarray(X)
    X = X.astype(float)
    y = pd.read_csv('./data/Yarritmia.csv')
    valores_unicos = y['type'].unique()
    print(valores_unicos)
    y = np.asarray(y)
#     y = y.astype(float)
    y = y.ravel()
    codificador = LabelEncoder() # cambia categorías cualitativas a numéricas
    y = codificador.fit_transform(y)
    Xt = torch.from_numpy(X).to(device)
    #dataset = torch.utils.data.TensorDataset(Xt, y)
    #trainloader = torch.utils.data.DataLoader(dataset, batch_size=bsize, shuffle=False) 
    nombreDB = "Arritmia"
    clases = ('F', 'N', 'VEB', 'SVEB')  #('F', 'N', 'Q', 'VEB', 'SVEB') 

    return clases, X, y, Xt, nombreDB


# OTRAS FUNCIONES DE DATOS TABULARES

# In[ ]:


# así se exporta un archivo a .dat que puede ser leido en matlab
import csv

def guardarData(Xin,L):
    miData = np.column_stack((Xin, L)) 

    with open('./miData.dat', 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(miData)
    
# para cargarlo en matlab
#file = 'miData.dat';
#dt = csvread(file)
#X = dt(:,1:3)
#L = dt(:,4)


# In[ ]:


########################################################################
# Función para guardar los datos de entrada Xin
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
import csv

def guardarXL(nombreDB, Xin, labelsIn):
    print("Guardando Xin y el labelsIn...")        
    with open('./data/Xin'+nombreDB+'.dat', 'w', newline='', encoding='utf-8') as csvfile2:
        writer = csv.writer(csvfile2)
        writer.writerows(Xin)
        
    output_array = np.array(labelsIn)
    np.savetxt("./data/labelsIn"+nombreDB+".csv", output_array, delimiter=",")


# In[ ]:


import pandas as pd
import torch
from torch.utils.data import Dataset, DataLoader

class CSVDataset(Dataset):
    def __init__(self, csv_file):
        self.data = pd.read_csv(csv_file)
        
    def __len__(self):
        return len(self.data)
        
    def __getitem__(self, idx):
        # Convertir las filas del csv en tensores PyTorch
        x = torch.tensor(self.data.iloc[idx, :-1].values, dtype=torch.float32)
        y = torch.tensor(self.data.iloc[idx, -1], dtype=torch.float32)
        return x, y

