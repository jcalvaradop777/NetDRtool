#!/usr/bin/env python
# coding: utf-8

# In[1]:


########################################################################
# Aplicación de RD
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

from KPCA import K_rbf, K_polinomial, kpca, kpcaRBF,  kpcaPoli, pca, pca2
from LLE import klleGpu, K_de_lle, lle, lleSk, kLleP, ller, kLleP2
from MDS import kcmdsGpu, K_de_cmds, mds, kCmdsP, cmdsr, mdsSK
from LE import kleGpu, K_de_le, kleGpu2, K_de_le2, le, kLeP, ler
from OtrosDR import lda, t_sne, factor, isomap, graphEnc, umap, trimap, slisemap, densemap, parametricumap, spca
import torch
import os
from time import time
import matplotlib.pyplot as plt
from MKLpy.algorithms import EasyMKL
from MKLpy.preprocessing import kernel_normalization
from sklearn.svm import SVC
from MKLpy.algorithms import AverageMKL
from MKLpy.algorithms import GRAM
from MKLpy.scheduler  import ReduceOnWorsening
from MKLpy.callbacks  import EarlyStopping, Monitor
from sklearn.metrics import accuracy_score
from MKLpy.model_selection import cross_val_score

def menu():
    os.system('cls') 
    print ("MENU DE COMBINACION RD")
    print ("\t0 - Aplicación de un método clásico RD individual")
    print ("\t1 - Aplicación de un método kernel RD individual")
    print ("\t2 - Combinación lineal de métodos kernel RD (MKL)")
    print ("\t3 - Deep MKL")
    print ("\t4 - Otros métodos")
    print ("\t9 - Salir")

def menuRdKernel():
    os.system('cls') 
    print ("MENU DE METODOS Kernel RD")
    print ("\tA - Kernel Clasical Multidimensional Scaling (KCMDS)")
    print ("\tB - Kernel Locally Linear Embedding (KLLE)")
    print ("\tC - Kernel Laplacian Eigenmaps (KLE)")
    print ("\tD - Kernel Principal Componet Analisis (KPCA polinomial)")
    print ("\tE - KCMDS Polinomial")
    print ("\tF - KLLE Polinomial")
    print ("\tG - KLE Polinomial")
    print ("\tH - KernelRBF Principal Componet Analisis (KPCArbf)")
    print ("\tZ - Salir")
    
def menuRdClasico():
    os.system('cls') 
    print ("MENU DE METODOS RD CLASICOS")
    print ("\tA - Clasical Multidimensional Scaling (CMDS)")
    print ("\tB - Locally Linear Embedding (LLE)")
    print ("\tC - Laplacian Eigenmaps (LE)")
    print ("\tD - Principal Componet Analysis (PCA)")
    print ("\tE - CMDSr")
    print ("\tF - LLEr")
    print ("\tG - LEr")
    print ("\tH - MDSsk")
    print ("\tZ - Salir")
    
def menuMKL():
    os.system('cls') 
    print ("MENU MKL")
    print ("\tA - Ponderación")
    print ("\tB - EasyMKL")
    print ("\tC - AverageMKL")
    print ("\tD - GRAM")
    print ("\tZ - Salir")
    
def menuRdOtros():
    os.system('cls') 
    print ("MENU DE OTROS METODOS RD")
    print ("\tA - Linear Discriminant Analysis Componet Analisis (LDA)")
    print ("\tB - T-SNE")
    print ("\tC - FactorAnalysis")
    print ("\tD - Isomap")
    print ("\tE - GraphEncoder")
    print ("\tF - Umap")
    print ("\tG - Trimap")
    print ("\tH - Slisemap")
    print ("\tI - DenseMap")
    print ("\tJ - parametricUMAP")
    print ("\tK - ScaledPCA")
    print ("\tZ - Salir")        
    
    
def aplicacionRD(Xint, labelsIn, clases, d, device):
        
    bd1=True

    while bd1==True:
    
        menu()
 
        opcionMenu = input("Selecciona una opción >> ")
    
        if opcionMenu=="0":
            print ("")
            print("Has selecionado: Aplicación de un método clásico RD individual")
        
            menuRdClasico()
        
            opcionMenuMet = input("Selecciona una opción >> ")
        
            if opcionMenuMet=="A" or opcionMenuMet=="a":
                print ("")
                print("Has seleccionado CMDS")
                metodo = "CMDS"

                t1 = time()
                encodeRd = mds(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="B" or opcionMenuMet=="b":
                print ("")
                print("Has seleccionado LLE")
                metodo = "LLE"
                tk = int(input("Suministre el tamaño del vecindario (k) >> "))

                t1 = time()
                encodeRd = lleSk(Xint, tk, d, device) # k = 100
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="C" or opcionMenuMet=="c":
                print ("")
                print("Has seleccionado LE")
                metodo = "LE"
            
                tkle = input("Suministre el tamaño del vecindario (k) para LE >> ")
            
                t1 = time()
                encodeRd = le(Xint, int(tkle), d, device) # k = 15
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
            
            if opcionMenuMet=="D" or opcionMenuMet=="d":
                print ("")
                print("Has seleccionado PCA")
                metodo = "PCA"

                t1 = time()
                encodeRd = pca(Xint, d, device) 
                #encodeRd, vtrunc = pca2(Xint, 2) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
            
            if opcionMenuMet=="E" or opcionMenuMet=="e":
                print ("")
                print("Has seleccionado CMDSr")
                metodo = "CMDSr"

                t1 = time()
                encodeRd = cmdsr(Xint, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="F" or opcionMenuMet=="f":
                print ("")
                print("Has seleccionado LLEr")
                metodo = "LLEr"
                tkller = int(input("Suministre el tamaño del vecindario (k) para LLEr >> "))

                t1 = time()
                encodeRd = ller(Xint, d, tkller, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="G" or opcionMenuMet=="g":
                print ("")
                print("Has seleccionado LEr")
                metodo = "LEr"
                tkler = input("Suministre el tamaño del vecindario (k) para LEr >> ")

                t1 = time()
                encodeRd = ler(Xint, d, tkler, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="H" or opcionMenuMet=="h":
                print ("")
                print("Has seleccionado CMDSsk")
                metodo = "CMDSsk"

                t1 = time()
                encodeRd = mdsSK(Xint, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd
                
            if opcionMenuMet=="Z" or opcionMenuMet=="z":
                print ("")
                print("Salida")
                bd2=False
                
            else:
                print ("")
                #print("No has pulsado ninguna opción RD correcta...")
            
                bd1=False
 
        elif opcionMenu=="1":
            print ("")
            print("Has selecionado: Aplicación de un método kernel RD individual")
        
            menuRdKernel()
 
            opcionMenuMet = input("Selecciona una opción >> ")
 
            if opcionMenuMet=="A" or opcionMenuMet=="a":
                print ("")
                print("Has seleccionado KCMDS")
                metodo = "KCMDS"

                t1 = time()
                encodeRd, k_cmds = kcmdsGpu(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_cmds
                
            if opcionMenuMet=="B" or opcionMenuMet=="b":
                print ("")
                print("Has seleccionado KLLE")
                metodo = "KLLE"
                tk = int(input("Suministre el tamaño del vecindario (k) >> "))

                t1 = time()
                encodeRd, k_lle = klleGpu(Xint, tk, d, device) # k = 100
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_lle
                
            if opcionMenuMet=="C" or opcionMenuMet=="c":
                print ("")
                print("Has seleccionado KLE")
                metodo = "KLE"
                tk = input("Suministre el tamaño del vecindario (k) >> ")

                t1 = time()
                encodeRd, k_le = kleGpu(Xint, tk, d, device) # k = 15
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_le
            
            if opcionMenuMet=="D" or opcionMenuMet=="d":
                print ("")
                print("Has seleccionado KPCA polinomial")
                metodo = "KPCApoli"
            
                gp = int(input("Suministre el grado del polinomio >> "))
                cp = int(input("Suministre el offset >> "))
                sp = int(input("Suministre el factor de escala >> "))

                t1 = time()
                encodeRd, k_pca_poli = kpcaPoli(Xint, d, gp, cp, sp, device) # Xint=matriz de entrada, d=dimensión, p=grado del polinómio, c=offset o bias, s=factor de escala  :  #K(x,x')=(s*x⋅x'+c)^p
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_pca_poli
            
            if opcionMenuMet=="E" or opcionMenuMet=="e":
                print ("")
                print("Has seleccionado KCMDS Polinomial")
                metodo = "KCMDSpoli"
            
                gp = int(input("Suministre el grado del polinomio >> "))
                cp = int(input("Suministre el offset >> "))
                sp = int(input("Suministre el factor de escala >> "))

                t1 = time()
                encodeRd, k_cmds_poli = kCmdsP(Xint, d, gp, cp, sp, device)  # matriz de datos, dimension, grado, offset, escala
                print("tiempo:", time() - t1)
                bd2=False
                kernel =  k_cmds_poli
            
            if opcionMenuMet=="F" or opcionMenuMet=="f":
                print ("")
                print("Has seleccionado KLLE Polinomial")
                metodo = "KLLEpoli"
            
                tk = int(input("Suministre el tamaño del vecindario (k) >> "))
                gp = int(input("Suministre el grado del polinomio >> "))
                # cp = int(input("Suministre el offset >> "))
                # sp = int(input("Suministre el factor de escala >> "))

                t1 = time()
                encodeRd, k_lle_poli = kLleP2(Xint, tk, d, gp, device)  # matriz de datos, tamaño vecindario, dimension, tipo de vecindario(e o r), grado, offset, escala
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_lle_poli
            
            if opcionMenuMet=="G" or opcionMenuMet=="g":
                print ("")
                print("Has seleccionado KLE Polinomial")
                metodo = "KLEpoli"
            
                tk = input("Suministre el tamaño del vecindario (k) >> ")
                gp = int(input("Suministre el grado del polinomio >> "))
                cp = int(input("Suministre el offset >> "))
                sp = int(input("Suministre el factor de escala >> "))

                t1 = time()
                encodeRd, k_le_poli = kLeP(Xint, tk, d, "n", gp, cp, sp, device)  # matriz de datos, tamaño vecindario, dimension, tipo de vecindario(e o r), grado, offset, escala
                print("tiempo:", time() - t1)
                bd2=False
                kernel = k_le_poli
            
            if opcionMenuMet=="H" or opcionMenuMet=="h":
                print ("")
                print("Has seleccionado KPCArbf")
                metodo = "KPCArbf"
            
                gama = float(input("Suministre el factor Gama >> ")) # ejemplo gama=0.5

                t1 = time()
                encodeRd, k_pca_rbf = kpcaRBF(Xint, d, gama, device)  # matriz de datos, dimension, gama de rbf
                print("tiempo:", time() - t1)
                bd2=False
                kernel =  k_pca_rbf 
                
            else:
                print ("")
                #print("No has pulsado ninguna opción RD correcta...")
            
                bd1=False

        elif opcionMenu=="2":
            print ("")
            print("Has seleccionado: Combinación lineal de métodos kernel RD (MKL)")
            metodo = "MKLlineal"
            
            print("Generando K_cmds ...")
            k_cmds = K_de_cmds(Xint, device)
            #k_cmds  = (k_cmds - torch.min(k_cmds))/(torch.max(k_cmds) - torch.min(k_cmds))
            #guardarKernel("K_cmds", k_cmds)
            
            # print("Generando K_lle ...")
            # tklle = int(input("Suministre el tamaño del vecindario (k) para KLLE >> ")) # tamaño del vecindario (k) para lle
            # k_lle = K_de_lle(Xint, tklle, device)
            # #k_lle = (k_lle - torch.min(k_lle))/(torch.max(k_lle) - torch.min(k_lle))
            # #guardarKernel("K_lle", k_lle)
            
            print("Generando K_le ...")
            tkle = input("Suministre el tamaño del vecindario (k) para KLE >> ") # tamaño del vecindario (k) para le
            k_le = K_de_le(Xint, tkle, "n", device)
            #k_le = (k_le - torch.min(k_le))/(torch.max(k_le) - torch.min(k_le)
            #guardarKernel("K_le", k_le)
            
            print("Generando K_pca ...")
            gp = int(input("Suministre el grado del polinomio >> "))
            cp = int(input("Suministre el offset >> "))
            sp = int(input("Suministre el factor de escala >> "))
            k_kpca = K_polinomial(Xint, gp, cp, sp)
            #guardarKernel("K_kpca", k_kpca)
            
            # Lista de kernels
            k_cmds = k_cmds.cpu().double()
            # k_lle = k_lle.cpu().double()
            k_le = k_le.cpu().double()
            k_kpca = k_kpca.cpu().double()
            
            KL = [k_cmds, k_le, k_kpca] # Kernel List
            KL = [kernel_normalization(K) for K in KL]
            yb = np.random.randint(0, 2, len(labelsIn), dtype='l') # MKLpy requiere que la y solo sea binaria    
            
            sig = "y"
            while sig == "y" or sig == "Y":
            
                menuMKL() 
                opcionMenuMKL = input("Selecciona una opción >> ")
            
                if opcionMenuMKL=="A" or opcionMenuMKL=="a":
                    print ("")
                    print("Has seleccionado MKL Ponderacion")

                    # Pesos ponderados de los métodos kernel para la combinación lineal
                    wk1 = float(input("Suministre el peso ponderado (entre 0 - 1) para KCMDS >> ")) 
                    wk2 = float(input("Suministre el peso ponderado (entre 0 - 1) para KLE >> ")) 
                    #wk3 = float(input("Suministre el peso ponderado (entre 0 - 1) para KLLE >> ")) 
                    wk4 = float(input("Suministre el peso ponderado (entre 0 - 1) para KPCA >> "))
                
                    t1 = time()
                    kernel = (wk1*k_cmds) + (wk2*k_le)  + (wk4*k_kpca) # + (wk3*k_lle)
                    encodeRd = kpca(kernel, d)
                    print("tiempo:", time() - t1)  
                    #bd1=False
                
                if opcionMenuMKL=="B" or opcionMenuMKL=="b":
                    print ("")
                    print("Has seleccionado MKL por EasyMKL")
                    print ("MENU EasyMKL")
                    print ("\tA - Sin base learner")
                    print ("\tB - Con base learner")
                    print ("\tC - Con Cros validation")
                    print ("\tZ - Salir")
                    
                    cbl = input("Digite su opción >> ")
                    t1 = time()
                    
                    if cbl=="A" or cbl=="a":
                        mkl = EasyMKL()
                        kernel = mkl.combine_kernels(KL, yb)
                        encodeRd = kpca(kernel.ker_matrix, d, device)
                        
                    if cbl=="B" or cbl=="b":
                        base_learner = SVC(C=100) 
                        #mkl = EasyMKL(lam=0.1, learner=base_learner)
                        mkl = EasyMKL(lam=0.1, multiclass_strategy='ovo', learner=base_learner).fit(KL,labelsIn)
                        #mkl = mkl.fit(KL, y)
                        kernel = mkl.combine_kernels(KL, yb)
                        encodeRd = kpca(kernel.ker_matrix, d, device)
                        
                    if cbl=="C" or cbl=="c":
                        print ('tuning lambda for EasyMKL...', end='')
                        base_learner = SVC(C=10000)	#"hard"-margin svm
                        best_results = {}
                        for lam in [0, 0.01, 0.1, 0.2, 0.9, 1]:	#possible lambda values for the EasyMKL algorithm
                            #MKLpy.model_selection.cross_val_score performs the cross validation automatically, it may returns
                            #accuracy, auc, or F1 scores
                            scores = cross_val_score(KL, yb, EasyMKL(learner=base_learner, lam=lam), n_folds=5, scoring='accuracy')
                            acc = np.mean(scores)
                            if not best_results or best_results['score'] < acc:
                                best_results = {'lam' : lam, 'score' : acc}
                        #evaluation on the test set
                        
                        print ('done cross_val')
                        mkl = EasyMKL(learner=base_learner, lam=best_results['lam']).fit(KL,yb)
                        kernel = mkl.combine_kernels(KL, yb)
                        encodeRd = kpca(kernel.ker_matrix, 2, device)
                         
                    print("tiempo:", time() - t1)  
                
                if opcionMenuMKL=="C" or opcionMenuMKL=="c":
                    print ("")
                    print("Has seleccionado MKL por AverageMKL")
                    cbl = input("Con base learner? (Y/N) >> ")
                    t1 = time()
                    
                    if cbl=="Y" or cbl=="y":
                        base_learner = SVC(C=100)
                        mkl = AverageMKL(learner=base_learner).fit(KL, yb)
                        kernel = mkl.combine_kernels(KL, yb)
                        encodeRd = kpca(kernel.ker_matrix, 2)
                    
                    if cbl=="N" or cbl=="n":
                        #AverageMKL simply computes the average of input kernels
                        #It looks bad but it is a really strong baseline in MKL ;)
                        mkl = AverageMKL().fit(KL, yb)
                        kernel = mkl.combine_kernels(KL, yb)
                        encodeRd = kpca(kernel.ker_matrix, d)
                        
                    print("tiempo:", time() - t1)  

                if opcionMenuMKL=="D" or opcionMenuMKL=="d":
                    print ("")
                    print("Has seleccionado MKL por GRAM")
                    t1 = time()  
                    
                    monitor   = Monitor()
                    earlystop = EarlyStopping(
                    KL, yb,      #validation data, KL is a validation kernels list
                    patience=5,     #max number of acceptable negative steps
                    cooldown=1,     #how ofter we run a measurement, 1 means every optimization step
                    metric='roc_auc',#the metric we monitor, roc_auc or accuracy
                    )

                    #ReduceOnWorsening automatically reduces the 
                    #learning rate when a worsening solution occurs
                    scheduler = ReduceOnWorsening()

                    mkl = GRAM(max_iter=1000,learning_rate=.01,callbacks=[earlystop, monitor],scheduler=scheduler).fit(KL, yb)
                    kernel = mkl.combine_kernels(KL, yb)
                    encodeRd = kpca(kernel.ker_matrix, d)

                encVis = encodeRd.cpu().detach().numpy()
                plt.scatter(encVis[:,0], encVis[:,1], c=labelsIn, label=clases, cmap=plt.cm.get_cmap('jet', len(clases)))
                plt.show()  
                
                sig = "n"
                #sig = input("Dsea aplicar otra combinación MKL (Y/N) >> ")
        
        elif opcionMenu=="3":
            print ("")
            print("Has seleccionado: Deep MKL")
            print("Mirar la implementación en matlab deepMkl demo.my")
            
        elif opcionMenu=="4":
            print ("")
            print("Has seleccionado: Otros métodos DR")
            menuRdOtros()
            opcionMenuOt = input("Selecciona una opción >> ")    
            
            if opcionMenuOt=="A" or opcionMenuOt=="a":
                print ("")
                print("Has seleccionado LDA")
                metodo = "LDA"

                t1 = time()
                encodeRd = lda(Xint, labelsIn, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd  
                
            if opcionMenuOt=="B" or opcionMenuOt=="b":
                print ("")
                print("Has seleccionado T-SNE")
                metodo = "TSNE"

                t1 = time()
                encodeRd = t_sne(Xint, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd  
                
            if opcionMenuOt=="C" or opcionMenuOt=="c":
                print ("")
                print("Has seleccionado FactorAnalysis")
                metodo = "FACTOR"

                t1 = time()
                encodeRd = factor(Xint, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd   
                
            if opcionMenuOt=="D" or opcionMenuOt=="d":
                print ("")
                print("Has seleccionado ISOMAP")
                metodo = "ISOMAP"

                t1 = time()
                encodeRd = isomap(Xint, d, device) 
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd  
                
            if opcionMenuOt=="E" or opcionMenuOt=="e":
                print ("")
                print("Has seleccionado GraphEncoder")
                metodo = "GraphEncoder"

                t1 = time()
                encodeRd = graphEnc(100, Xint, labelsIn, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
                
            if opcionMenuOt=="F" or opcionMenuOt=="f":
                print ("")
                print("Has seleccionado UMAP")
                metodo = "UMAP"

                t1 = time()
                encodeRd = umap(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
                
            if opcionMenuOt=="G" or opcionMenuOt=="g":
                print ("")
                print("Has seleccionado TRIMAP")
                metodo = "TRIMAP"

                t1 = time()
                encodeRd = trimap(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
                
            if opcionMenuOt=="H" or opcionMenuOt=="h":
                print ("")
                print("Has seleccionado SLISEMAP")
                metodo = "SLISEMAP"

                t1 = time()
                encodeRd = slisemap(Xint, labelsIn, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
                
            if opcionMenuOt=="I" or opcionMenuOt=="i":
                print ("")
                print("Has seleccionado DENSEMAP")
                metodo = "DENSEMAP"

                t1 = time()
                encodeRd = densemap(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
                
            if opcionMenuOt=="J" or opcionMenuOt=="j":
                print ("")
                print("Has seleccionado PARAMETRIC UMAP")
                metodo = "PUMAP"

                t1 = time()
                encodeRd = parametricumap(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd 
             
            if opcionMenuOt=="K" or opcionMenuOt=="k":
                print ("")
                print("Has seleccionado ScaledPCA")
                metodo = "ScaledPCA"

                t1 = time()
                encodeRd = spca(Xint, d, device)
                print("tiempo:", time() - t1)
                bd2=False
                kernel = encodeRd     
                
                
                
        elif opcionMenu=="9":
            bd1=False
            break
        else:
            print ("")
            #input("No has pulsado ninguna opción correcta...\npulsa una tecla para continuar")

    # normalizacion: normalización=(x–min(x))/(max(x)–min(x))
    encodeRd = (encodeRd - torch.min(encodeRd))/(torch.max(encodeRd) - torch.min(encodeRd))
    
    return encodeRd, kernel, metodo


# In[ ]:


import numpy as np

class MetodoRd:
    nombre = ""
    tipo = "" # clasico o kernel
    rd = object
    cluster = np.random.rand(0) #np.array([[0]])
    subsetsCluster = {}
    etTamanoSubsets = []
    rnx = 0
    metricasCluster = 0
    indexOriginales = []

def getListaRd(metodos, Xint, labelsIn, indexOri, device):

    listaMetodos = list()

    for metrd in metodos:

        if metrd.nombre=="CMDS":
            print("CMDS...")
            cmds = MetodoRd()
            cmds.nombre = "CMDS"
            cmds.tipo = "clasico"
            rd = mds(Xint, d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            cmds.rd = rd
            cmds.indexOriginales = indexOri
            listaMetodos.append(cmds)     
        
        if metrd.nombre=="LLE":
            print("LLE...")
            lle = MetodoRd()
            lle.nombre = "LLE"
            lle.tipo = "clasico"
            tk = int(input("Suministre el tamaño del vecindario (k) para LLE >> "))
            rd = lleSk(Xint, tk, d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            lle.rd = rd
            lle.indexOriginales = indexOri
            listaMetodos.append(lle)  
        
        if metrd.nombre=="LE":
            print("LE...")
            lem = MetodoRd()
            lem.nombre = "LE"
            lem.tipo = "clasico"
            tkle = input("Suministre el tamaño del vecindario (k) para LE >> ")
            rd = le(Xint, int(tkle), d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            lem.rd = rd
            lem.indexOriginales = indexOri
            listaMetodos.append(lem)            
        
        if metrd.nombre=="PCA":
            print("PCA...")
            pca = MetodoRd()
            pca.nombre = "PCA"
            pca.tipo = "clasico"
            rd = pca(Xint, d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            pca.rd = rd
            pca.indexOriginales = indexOri
            listaMetodos.append(pca)           
        
        if metrd.nombre=="KCMDS":
            print("KCMDS...")
            kcmds = MetodoRd()
            kcmds.nombre = "KCMDS"
            kcmds.tipo = "kernel"
            rd, k_cmds = kcmdsGpu(Xint, d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kcmds.rd = rd
            kcmds.indexOriginales = indexOri
            listaMetodos.append(kcmds)
                    
        if metrd.nombre=="KLLE":
            print("KLLE...")
            klle = MetodoRd()
            klle.nombre = "KLLE"
            klle.tipo = "kernel"
            tk = int(input("Suministre el tamaño del vecindario (k) KLLE>> "))
            rd, k_lle = klleGpu(Xint, tk, d, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            klle.rd = rd
            klle.indexOriginales = indexOri
            listaMetodos.append(klle)            

        if metrd.nombre=="KLE":
            print("KLE...")
            kle = MetodoRd()
            kle.nombre = "KLE"
            kle.tipo = "kernel"
            tk = input("Suministre el tamaño del vecindario (k) KLE >> ")
            rd, k_le = kleGpu(Xint, tk, d, device) 
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kle.rd = rd
            kle.indexOriginales = indexOri
            listaMetodos.append(kle)            

        if metrd.nombre=="KPCApoli":
            print("KPCApoli...")
            kpcaPoliM = MetodoRd()
            kpcaPoliM.nombre = "KPCApoli"
            kpcaPoliM.tipo = "kernel"
            gp = int(input("Suministre el grado del polinomio >> "))
            cp = int(input("Suministre el offset >> "))
            sp = int(input("Suministre el factor de escala >> "))
            rd, k_pca_poli = kpcaPoli(Xint, d, gp, cp, sp, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kpcaPoliM.rd = rd
            kpcaPoliM.indexOriginales = indexOri
            listaMetodos.append(kpcaPoliM)            

        if metrd.nombre=="KCMDSpoli":
            print("KCMDSpoli...")
            kcmdsPoli = MetodoRd()
            kcmdsPoli.nombre = "KCMDSpoli"
            kcmdsPoli.tipo = "kernel"
            gp = int(input("Suministre el grado del polinomio >> "))
            cp = int(input("Suministre el offset >> "))
            sp = int(input("Suministre el factor de escala >> "))
            rd, k_cmds_poli = kCmdsP(Xint, d, gp, cp, sp, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kcmdsPoli.rd = rd
            kcmdsPoli.indexOriginales = indexOri
            listaMetodos.append(kcmdsPoli)            

        if metrd.nombre=="KLLEpoli":
            print("KLLEpoli...")
            kllePoli = MetodoRd()
            kllePoli.nombre = "KLLEpoli"
            kllePoli.tipo = "kernel"
            tk = int(input("Suministre el tamaño del vecindario (k) >> "))
            gp = int(input("Suministre el grado del polinomio >> "))
            cp = int(input("Suministre el offset >> "))
            sp = int(input("Suministre el factor de escala >> "))
            rd, k_lle_poli = kLleP(Xint, tk, d, gp, cp, sp, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kllePoli.rd = rd
            kllePoli.indexOriginales = indexOri
            listaMetodos.append(kllePoli)            
 
        if metrd.nombre=="KLEpoli":
            print("KLEpoli...")
            klePoli = MetodoRd()
            klePoli.nombre = "KLEpoli"
            klePoli.tipo = "kernel"
            tk = input("Suministre el tamaño del vecindario (k) >> ")
            gp = int(input("Suministre el grado del polinomio >> "))
            cp = int(input("Suministre el offset >> "))
            sp = int(input("Suministre el factor de escala >> "))
            rd, k_le_poli = kLeP(Xint, tk, d, "n", gp, cp, sp, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            klePoli.rd = rd
            klePoli.indexOriginales = indexOri
            listaMetodos.append(klePoli)            

        if metrd.nombre=="KPCArbf":
            print("KPCArbf...")
            kpcaRbfM = MetodoRd()
            kpcaRbfM.nombre = "KPCArbf"
            kpcaRbfM.tipo = "kernel"
            gama = float(input("Suministre el factor Gama >> ")) # ejemplo gama=0.5
            rd, k_pca_rbf = kpcaRBF(Xint, d, gama, device)
            rd = (rd - torch.min(rd))/(torch.max(rd) - torch.min(rd))
            rd = rd.cpu().detach().numpy()
            rd = np.column_stack((rd, labelsIn))
            kpcaRbfM.rd = rd
            kpcaRbfM.indexOriginales = indexOri
            listaMetodos.append(kpcaRbfM)      
        
    return listaMetodos


# In[ ]:


########################################################################
# Función para guardar la matriz kernel
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
import csv

def guardarKernel(nomMet, K):
    print("Guardando la matriz Kernel ...")        
    with open('./RNXmat/'+nomMet+'.dat', 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(K)

