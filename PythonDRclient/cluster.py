#!/usr/bin/env python
# coding: utf-8

# In[ ]:


# Aplicación Kmeans

from sklearn.cluster import KMeans
import seaborn as sns; sns.set()
import matplotlib.pyplot as plt
from score_rnx import ScoreRnx

def kmeans(X, nc):
    
    model = KMeans(n_clusters = nc, random_state = 0)
    clusters = model.fit_predict(X)
    
    #sns.scatterplot(X[:, 0], X[:, 1], hue = clusters, palette=cmap);
    plt.scatter(X[:, 0], X[:, 1], c=clusters, cmap=plt.cm.get_cmap('jet', nc))
    plt.clim(-0.5, nc-0.5);  
    plt.show()
    
    return clusters, nc


# In[ ]:


# DBSCAN

from sklearn.cluster import DBSCAN
import seaborn as sns; sns.set()
import numpy as np

def dbscan(X):

    # Lo instanciamos indicando una distancia máxima entre puntos de 0.2:
    epstxt = input("Suministre la distancia entre puntos eps >> ")
    epsIn = float(epstxt)

    model = DBSCAN(eps = epsIn)
    clusters = model.fit_predict(X)
    
    #sns.scatterplot(X[:, 0], X[:, 1], hue = clusters);
    nc = len(np.unique(clusters)) # es el numero de elementos del cluster
    plt.scatter(X[:, 0], X[:, 1], c=clusters, cmap=plt.cm.get_cmap('jet', nc))
    plt.clim(-0.5, nc-0.5);  
    plt.show()
    
    return clusters, nc


# In[ ]:


# Spectral Cluster de SKLEARN

from sklearn.cluster import SpectralClustering

def spectral(X,nc):

    model = SpectralClustering(n_clusters=nc, affinity='nearest_neighbors', assign_labels='kmeans')
    clusters = model.fit_predict(X)
    fig, ax = plt.subplots(figsize=(9,7))
    #ax.set_title('kernal transform to higher dimension\nlinear separation is possible', fontsize=18, fontweight='demi')
    plt.scatter(X[:, 0], X[:, 1], c=clusters, cmap=plt.cm.get_cmap('jet', nc))
    plt.clim(-0.5, nc-0.5);
    plt.show()
    
    return clusters, nc


# In[ ]:


# Spectral Cluster paso a paso
# https://medium.com/@tomernahshon/spectral-clustering-from-scratch-38c68968eae0

from sklearn.neighbors import radius_neighbors_graph
from sklearn.neighbors import kneighbors_graph
from scipy.sparse import csgraph 

def spectral1(X,y):

    A = radius_neighbors_graph(X,0.4,mode='distance', metric='minkowski', p=2, metric_params=None, include_self=False)
    # A = kneighbors_graph(X_mn, 2, mode='connectivity', metric='minkowski', p=2, metric_params=None, include_self=False)
    A = A.toarray()

    L = csgraph.laplacian (A, normed = False) 

    eigval, eigvec = np.linalg.eig(L)
    np.where(eigval == np.partition(eigval, 1)[1])# the second smallest eigenvalue

    y_spec = eigvec[:,1].copy()
    y_spec[y_spec < 0] = 0
    y_spec[y_spec > 0] = 1
    type(y_spec),y.shape,y_spec.shape

    dot_size=50
    nc = len(np.unique(y_spec)) # es el numero de elementos del cluster

    fig, ax = plt.subplots(figsize=(9,7))
    ax.set_title('Data with ground truth labels - linear separation not possible', fontsize=18, fontweight='demi')
    ax.scatter(X[:, 0], X[:, 1],c=y_spec ,s=dot_size, cmap=plt.cm.get_cmap('jet', nc))
    plt.clim(-0.5, nc-0.5); 
    plt.show()
    
    return y_spec


# In[ ]:


from sklearn.metrics.cluster import adjusted_mutual_info_score
from sklearn.metrics import silhouette_score
from sklearn.metrics.cluster import adjusted_rand_score
from sklearn.metrics.cluster import fowlkes_mallows_score
from sklearn.metrics.cluster import normalized_mutual_info_score
from sklearn.metrics.cluster import v_measure_score
from sklearn.metrics.cluster import homogeneity_score
from sklearn.metrics.cluster import completeness_score
from sklearn.metrics.cluster import v_measure_score
from sklearn.metrics.cluster import adjusted_rand_score
from sklearn.metrics.cluster import adjusted_mutual_info_score

def printMetrics(X, nc, labels_true, labels_predict):
    print('Nclu: %d' % nc) 
    print("hom: %0.3f" % homogeneity_score(labels_true, labels_predict)) # Puntuación de homogeneidad un clúster debe contener solo muestras que pertenezcan a una sola clase
    print("Com: %0.3f" % completeness_score(labels_true, labels_predict))
    print("vme: %0.3f" % v_measure_score(labels_true, labels_predict))
    print("ard: %0.3f" % adjusted_rand_score(labels_true, labels_predict))
    print("ami: %0.3f" % adjusted_mutual_info_score(labels_true, labels_predict))
    #print("Silhouette Coefficient: %0.3f" % metrics.silhouette_score(X, labels)) 


# In[ ]:


########################################################################
# MENU
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import os

def menuCluster(X, y, nc):

    os.system('cls') 
    print ("MENU DE CLUSTER")
    print ("\t1 - K-means")
    print ("\t2 - DBSCAN")
    print ("\t3 - Espectral")
    print ("\t9 - Salir")

    opcionMenuClus = input("Selecciona una opción >> ")
 
    if opcionMenuClus=="1":
        print ("")
        print("K-means")
        yclus, ncm = kmeans(X, nc)  # len(etiquetas) será el numero de clusters
            
    if opcionMenuClus=="2":
        print ("")
        print("DBSCAN")
        yclus, ncm = dbscan(X)  # len(etiquetas) será el numero de clusters
        
    if opcionMenuClus=="3":
        print ("")
        print("Espectral")
        yclus, ncm = spectral(X, nc)
            
    else:
        print ("")
        print("No has pulsado ninguna opción RD correcta...")

    printMetrics(X, ncm, y, yclus)
    
def Vscore(X, nc, labelsTrue):
    model = KMeans(n_clusters = nc, random_state = 0)
    labelsPredict = model.fit_predict(X)
    # print("labelsPredict",labelsPredict)
    # print("tipo de labelsPredict",type(labelsPredict))
    vs = v_measure_score(labelsTrue, labelsPredict)
    return vs, labelsPredict

def Rnx(X, nombreMet, incrustamiento):
    # pip install score-rnx==0.3
    score = ScoreRnx()
    score.add_high_data(X) #Se añaden los datos de alta dimensión
    score.add_method(nombreMet, incrustamiento) # Se añaden los datos de los métodos de reducción de dimensión 
    
    # De esta manera __get_rnx()__ tendrá en la posición 0 el resultado de evaluación del 
    # método CMDS y en la posición 1 el resultado de evaluación del metodo LLE
    score.run()
    scores = score.get_rnx()
    puntaje = scores[0].score
    image_bytes = score.generate_graph() 
    return puntaje, image_bytes

# Esto va en la libreria instalada ScoreRnx, por si se reinstala se puede perder la siguiente función
# def generate_graph(self):
        
#         image_bytes = bytes()
        
#         if len(self.methods_objects) > 0:
#             markers = ['.', '>', 'o']
#             bottom, top = 0, 100

#             for data_method in self.methods_objects:
#                 plt.plot(np.arange(len(data_method.rnx)), 100 * data_method.rnx,
#                          label=f"{data_method.name}:{round(data_method.score * 100, 3)}%",
#                          marker=random.choice(markers),
#                          markevery=0.1)

#             plt.xscale('log')
#             plt.ylim(bottom, top)
#             plt.xlabel("K")
#             plt.ylabel("100RNX (K)")

#             plt.legend(loc='upper left', shadow=True, fontsize='x-large')
#             #plt.show()
            
#             buffer = io.BytesIO()
#             plt.savefig(buffer, format='png')
#             buffer.seek(0)

#             # Obtener los bytes del archivo de imagen
#             image_bytes = buffer.read()
#         else:
#             ScoreRnxException("Deben haber métodos en el stack para realizar la visualización")
        
#         return image_bytes