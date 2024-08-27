#!/usr/bin/env python
# coding: utf-8

# In[ ]:

import torch
import numpy as np

from sklearn import decomposition
def pca(X, d):
    pca = decomposition.PCA(d)
    pca.fit(X)
    embedding = pca.transform(X)
    return embedding

from sklearn.manifold import SpectralEmbedding
def le(X, n_neighbors, n_components):
    embedding = SpectralEmbedding(n_components, n_neighbors=n_neighbors)
    rd = embedding.fit_transform(X) 
    return rd

from sklearn.manifold import LocallyLinearEmbedding
def lleSk(X, n_neighbors, n_components):
    embedding = LocallyLinearEmbedding(n_components=n_components, n_neighbors=n_neighbors)
    rd = embedding.fit_transform(X)
    return rd


#_____________________Kernels________________________________________________________________________

from scipy.linalg import eigh

#  kpca ***
def kpca(K, n_components): # K es el kernel de entrada, n_components es la dimension a reducir
    K = K.numpy()
    eigvals, eigvecs = eigh(K)
    eigvals, eigvecs = eigvals[::-1], eigvecs[:, ::-1] # obtiene una lista al revés 
    X_pc = np.column_stack([eigvecs[:, i] for i in range(n_components)]) # Selecciona los eigenvectores asociados a los mayores igen valores   
    return X_pc

def centrar(K,N):    
    K = 0.5*(K + torch.t(K)) 
    kS = torch.sum(K, 0)/N  # suma los elementos de una columna y cada uno lo divide entre N
    kSm = kS.repeat(N, 1)  # construye una matriz repitiendo n veces una linea
    K = K - (kSm+torch.t(kSm)) + (torch.sum(kS)/N) # es como el bsxfun(@plus, kS, kS') en Matlab
    return K

def K_polinomial(X, p, c, s): 
    X=X.float()
    K = torch.mm(X,torch.t(X)) # covarianza
    K = torch.pow((s*K+c), p)
    
    # centrado del kernel
    n,p = K.shape
    K = centrar(K,n)
    return K

def kpcaPoli(X, d, p, c, s, device): 
    X = torch.from_numpy(X).to(device)
    ker_kpca = K_polinomial(X, p, c, s)
    X_pc = kpca(ker_kpca, d)
    return X_pc, ker_kpca

# Kernel de LE ****

from sklearn.neighbors import radius_neighbors_graph
from sklearn.neighbors import kneighbors_graph
from scipy.sparse import csgraph

def K_de_le(X, k, tv, device): # X es la matriz de datos, K es el tamaño del vecindario, tv es el tipo de vecindario (r por radio, n por numero de vecinos)
    
    torch.set_printoptions(sci_mode=False)
    
    #X = X.cpu().data
    
    radius : float
    # Matriz de adyacencia
    if tv=="N" or tv=="n":
        k = int(k)
        A = kneighbors_graph(X, k, mode='connectivity', metric='minkowski', p=2, metric_params=None, include_self=True) #mode='distance'
        
    if tv=="R" or tv=="r":    
        A = radius_neighbors_graph(X,k,mode='connectivity', metric='minkowski', p=2, metric_params=None, include_self=True)
    
    A = (A+np.transpose(A))/2 # lo puese según matlab, afecta a normed en el laplaciano
    A = A.toarray()
    
    # L = A - D
    L = csgraph.laplacian(A, normed=False, return_diag=False) 
    L = torch.from_numpy(L).to(device)
    
    # kernel de LE = matriz pseudo-inversa de L
    k_le = torch.pinverse(L)
    #k_le = np.linalg.pinv(L)
    #k_le = torch.from_numpy(k_le).to(device)

    ## Se debe centrar el kernel
    n,p = k_le.shape
    k_le = centrar(k_le,n)
    
    return k_le

def kLeP(X, k, d, tve, p, c, s, device):

    # Kernel de LE
    ker_le = K_de_le(X, k, tv=tve, device=device) 
    
    # LO NORMALIZO
    ker_le = (ker_le - torch.min(ker_le))/(torch.max(ker_le) - torch.min(ker_le))
    
    # Aplicación de func polinómica
    ker_poli_le = torch.pow((s*ker_le+c), p)
    #ker_poli_le = (ker_le*0.1)  + (torch.pow((s*ker_le+c), p)*0.9)
    
    # centrado del kernel
    n,p = ker_poli_le.shape
    ker_poli_le = centrar(ker_poli_le,n)
    
    # Aplicacion de kpca al kernel de CMDS 
    kle = kpca(ker_poli_le, d) 
    print("LE Polinomial", kle)
    
    return kle, ker_poli_le


#  Kernel de LLE ***

from scipy import linalg
def get_weights(X, nbors, reg, K):
    n,p = X.shape
    Weights = np.zeros((n,n))
    for i in range(n):
        X_bors = X[nbors[i],:] - X[i]
        cov_nbors = np.dot(X_bors, X_bors.T)
        #regularization tems
        trace = np.trace(cov_nbors)
        if trace >0 :
            R = reg*trace
        else:
            R = reg
        cov_nbors.flat[::K+1] += R
        weights = linalg.solve(cov_nbors, np.ones(K-1).T)  #, sym_pos=True
        weights = weights/weights.sum()
        Weights[i, nbors[i]] = weights
    return(Weights)

from sklearn.neighbors import NearestNeighbors 
from sklearn import neighbors
def Knbor_Mat(X, K, t = 2.0, dist_metric = "euclidean", algorithm = "ball_tree"):
    n,p = X.shape
    #knn = neighbors.NearestNeighbors(K+1, metric = dist_metric, algorithm=algorithm).fit(X)
    knn = neighbors.NearestNeighbors(n_neighbors=K).fit(X)
    distances, nbors = knn.kneighbors(X)
    return(nbors[:,1:])

from scipy.linalg import eigh
def K_de_lle(X, K, device): # X es la matriz de datos, K es el tamaño del vecindario
    reg =0.001
    nbors = Knbor_Mat(X,K)
    Weights = get_weights(X, nbors, reg, K)
    W = torch.from_numpy(Weights).to(device)
    # Calculo de la matriz M = (I-W)' (I-W) 
    n,p = W.shape
    #I = np.eye(n)
    I = torch.eye(n).to(device)
    m = (I-W)
    M = torch.mm(torch.t(m),m)
    # Calculo del kernel de lle : klle = lmax In - M
    #lamb, _ = torch.eig(M)
    lamb, _ = torch.linalg.eig(M)
    lamb = torch.abs(lamb)
    lamb = torch.max(lamb)
    k_lle = lamb*I - M  # lmax-1n - M
    #k_lle = torch.from_numpy(k_lle).to(device)
    ## Se debe centrar el kernel
    k_lle = centrar(k_lle,n)
    return k_lle

def kLleP(X, K, d, p, c, s, device):

    # Kernel de LLE
    ker_lle = K_de_lle(X, K, device)
    
    # LO NORMALIZO
    ker_lle = (ker_lle - torch.min(ker_lle))/(torch.max(ker_lle) - torch.min(ker_lle))
    
    # Aplicación de func polinómica
    #ker_poli_lle = torch.pow((s*ker_lle+c), p)
    ker_poli_lle = (ker_lle*0.9997)  + (torch.pow((s*ker_lle+c), p)*0.0003)
    
    # centrado del kernel polinomial
    n,p = ker_poli_lle.shape
    ker_poli_lle = centrar(ker_poli_lle, n)
    
    # Aplicacion de kpca al kernel de klle 
    klle = kpca(ker_poli_lle, d) 
    #print("LLE Polinomial", klle)
    ker_poli_lle = ker_poli_lle.numpy()
    
    return klle, ker_poli_lle

# KCMDS ***

def K_de_cmds(X, device):
    #X = X.float() 
    #X = X.double() 
    X = torch.from_numpy(X).to(device)
    DX = torch.cdist(X, X, 2).double() # matriz de distancias pares
    # Calculo del kernel de CMDS : -1/2(I-1n 1nT)D(I-1n 1nT)
    n,p = DX.shape
    I = torch.eye(n).to(device)
    O = torch.ones([n,n]).to(device) 
    O = O/n
    IO = I-O.double()
    print("IO", IO)
    #ker_cmds  = -0.5 * (I - O) * (DX**2) * (I - O) # es como esto pero con dot product (el * se utiliza distinto en matlab)
    ker_cmds = -0.5 * torch.mm(IO, torch.mm((DX**2),IO))
    ## Se debe centrar el kernel
    ker_cmds = centrar(ker_cmds,n)   
    #print("ker_cmds", ker_cmds)
    return ker_cmds

def kCmdsP(X, d, p, c, s, device):
    # Kernel de CMDS
    ker_cmds = K_de_cmds(X, device)
    # Aplicación de func polinómica
    ker_poli_cmds = torch.pow((s*ker_cmds+c), p)
    # centrado del kernel
    n,p = ker_poli_cmds.shape
    ker_poli_cmds = centrar(ker_poli_cmds,n)
    # Aplicacion de kpca al kernel de CMDS 
    kmds = kpca(ker_poli_cmds, d) 
    #print("KCMDS Polinomial", kmds)
    ker_poli_cmds = ker_poli_cmds.numpy()
    return kmds, ker_poli_cmds

#________________________________

class LDA:

    def __init__(self, n_components):
        self.n_components = n_components
        self.linear_discriminants = None

    def fit(self, X, y):
        n_features = X.shape[1]
        class_labels = np.unique(y)

        # Within class scatter matrix:
        # SW = sum((X_c - mean_X_c)^2 )

        # Between class scatter:
        # SB = sum( n_c * (mean_X_c - mean_overall)^2 )

        mean_overall = np.mean(X, axis=0)
        SW = np.zeros((n_features, n_features))
        SB = np.zeros((n_features, n_features))
        for c in class_labels:
            X_c = X[y == c]
            mean_c = np.mean(X_c, axis=0)
            # (4, n_c) * (n_c, 4) = (4,4) -> transpose
            SW += (X_c - mean_c).T.dot((X_c - mean_c))

            # (4, 1) * (1, 4) = (4,4) -> reshape
            n_c = X_c.shape[0]
            mean_diff = (mean_c - mean_overall).reshape(n_features, 1)
            SB += n_c * (mean_diff).dot(mean_diff.T)

        # Determine SW^-1 * SB
        A = np.linalg.inv(SW).dot(SB)
        # Get eigenvalues and eigenvectors of SW^-1 * SB
        eigenvalues, eigenvectors = np.linalg.eig(A)
        # -> eigenvector v = [:,i] column vector, transpose for easier calculations
        # sort eigenvalues high to low
        eigenvectors = eigenvectors.T
        idxs = np.argsort(abs(eigenvalues))[::-1]
        eigenvalues = eigenvalues[idxs]
        eigenvectors = eigenvectors[idxs]
        # store first n eigenvectors
        self.linear_discriminants = eigenvectors[0:self.n_components]

    def transform(self, X):
        # project data
        return np.dot(X, self.linear_discriminants.T)


# In[ ]:

from sklearn.discriminant_analysis import LinearDiscriminantAnalysis

def lda(Xin, labelsIn, n_components):
    #Xin = Xin.cpu().detach().numpy()
    #Xin = Xin.cpu().data
    clf = LinearDiscriminantAnalysis()
    result = clf.fit_transform(Xin, labelsIn) 
    embedding = result[:,:n_components]
    return embedding


# In[ ]:


from sklearn.manifold import TSNE
import numpy as np
import torch

def t_sne(Xin, n_components, device):
    embedding = TSNE(n_components)
    # encodeRd = embedding.fit_transform(Xin.cpu().detach().numpy())
    encodeRd = embedding.fit_transform(Xin)
    encodeRd = torch.from_numpy(encodeRd).to(device)
    return encodeRd


# In[ ]:


from sklearn.decomposition import FactorAnalysis
import numpy as np
import torch

def factor(Xin, n_components):
    fa = FactorAnalysis(n_components, random_state=0)
#     encodeRd = fa .fit_transform(Xin.cpu().detach().numpy())
    embedding = fa .fit_transform(Xin)
    return embedding


from sklearn import datasets
from sklearn.manifold import Isomap
# from sklearn_ext.manifold import Isomap as IsomapExt
import matplotlib.pyplot as plt

# REDUCCION DE DIMENSION NEURONAL
def isomap(Xin, n_components):
    # Utilizar Isomap de scikit-learn
    isomap_sklearn = Isomap(n_components=n_components)
#     X_sklearn = isomap_sklearn.fit_transform(Xin.cpu().detach().numpy())
    embedding = isomap_sklearn.fit_transform(Xin)
    return embedding


from GraphEnc import trainGraphEncoder
def graphEnc(ne, Xin, labelsIn, device):  
    codeGraphEnc = trainGraphEncoder(ne, Xin, labelsIn, device)
    return codeGraphEnc

# NUEVOS DR DE MAPEO

# UMAP
# import umap
# import umap.umap_ as umap
from umap.umap_ import UMAP

def umap(Xin, nc):
    # umap_model = umap.UMAP(n_neighbors=15, min_dist=0.1, n_components=2)
    umap_model = UMAP(n_neighbors=15, min_dist=1, n_components=nc)
    embedding = umap_model.fit_transform(Xin)
    return embedding

from umap.parametric_umap import ParametricUMAP
def parametricumap(Xin, nc):
    embedder = ParametricUMAP()
    embedding = embedder.fit_transform(Xin)
    return embedding


# ****************** HABILITAR DENSEMAP
# https://pypi.org/project/densmap-learn/
#import densmap
def densemap(Xin, nc):
    #embedding, ro, re = densmap.densMAP().fit_transform(Xin)
    dense_model = UMAP(densmap=True, random_state=42, n_components=nc)
    embedding = dense_model.fit_transform(Xin)
    return embedding


# Trimap
# import trimap
from trimap.trimap_ import TRIMAP
def trimap(Xin, nc):
    embedding = TRIMAP(n_dims=nc).fit_transform(Xin)
    return embedding


# SLISEMAP
import numpy as np
from slisemap import Slisemap
def slisemap(Xin, labelsIn, nc):
    sm = Slisemap(Xin, labelsIn, radius=10.5, lasso=0.01)
    sm.optimise()
    codesM = sm.Z.detach().cpu().numpy()
    return codesM


# Scaled PCA
def spca(Xin, nc):
    from sklearn.decomposition import PCA
    from sklearn.preprocessing import StandardScaler

    # Scale the data
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(Xin)

    # Apply PCA
    pca = PCA(n_components=nc)
    embedding = pca.fit_transform(X_scaled)
    return embedding

# Scaled FastICA
def fica(Xin, nc):
    from sklearn.decomposition import FastICA
    transformer = FastICA(n_components=nc,random_state=0)
    X_transformed = transformer.fit_transform(Xin)
    return X_transformed

def srp(Xin, nc): 
    from sklearn.pipeline import make_pipeline
    from sklearn.preprocessing import StandardScaler
    from sklearn.random_projection import (GaussianRandomProjection, SparseRandomProjection)
    rng = np.random.RandomState(42)
    srpmetodo = SparseRandomProjection(n_components=nc, density = 'auto', eps = 0.5, random_state=rng, dense_output = False)
    X_new = srpmetodo.fit_transform(Xin)
    return X_new

def grp(Xin, nc): 
    from sklearn.random_projection import GaussianRandomProjection
    rng = np.random.RandomState(42)
    transformer = GaussianRandomProjection(n_components=nc, random_state=rng)
    X_new = transformer.fit_transform(Xin)
    return X_new

def lpp(Xin, nc): 
    from lpproj import LocalityPreservingProjection
    lpp = LocalityPreservingProjection(n_components=nc)
    encodeRd = lpp.fit_transform(Xin)
    return encodeRd