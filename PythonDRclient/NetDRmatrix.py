#                                                                   NETDR


# ENSAMBLE DIRECTO DE INCRUSTAMIENTOS  =============================================================================================


# NetDR DISCRIMINANTE  _____________________________________________________________________________________________________________


# PARA PUNTO DOS (Con metodos DR) PARA DISCRIMINANTE
# La versión discriminante utiliza aprendizaje supervisado, utilizando una función de costo compuesta a partir de dos procesos:

# 1. El primper proceso aprende las etiquetas de manera supervisada para mantener la similitud intraCluster. Para esto se utiliza un decoder particular en el forward, definiendo una
# red fully conectecd ( self.fcNets = nn.Linear(d, len(self.clases)) )  que toma el incrustamiento generado en el encoder d dimensional y lo expande
# a las dimensiones del numero  de clases, generando una matriz de etiquetas E de b(tamaño del minilote) X e(numero de clases).

# En el entrenamiento, se compara la  matriz de etiquetas generadas E con una matriz O(OneHot) de bxe creada a partir del vector de etiquetas del minilote, en donde 1 representa la
# etiqueta asignada y 0 las demás etiquetas que no correponden. El objetivo es que la matriz de etiquetas generadas coicida la matriz O(OneHot) para apreder la asignación de etiquetas.

# De este proceso se genera la primer función de costo, utilizando entropia cruzada, basada en la similitud intra cluster Ls = C(E, O)

# 2. El segundo proceso, intenta aplicar un alejameniento interclusters para evitar el solapamiento y mejorar la discriminación de clusters. La idea es hacer que la
# distancia entre clusters sea mayor a un umbral de solapamiento. Por tanto, si se genera una matriz de distancias a partir de la matriz de etiquetas generadas en el
# paso anterior, se obtiene una matriz cuadrada de e x e(e es numero de etiquetas), la matriz de distancias de clases ideal es aquella cuya diagonal es 0 ya que
# representa que cada clase es igual a si misma, y los demas clusters deberían tener una distancia de 1 (distancia maxima, por eso debe estar normalizada), que representa
# la maxima distancia con una clase diferente.
# Sin embargo, se prioriza la separación de clusters que tienen mayor solapamiento, es decir, aquellos clusters cuya distancia en menor a un umbral. El proceso para
# generar la matriz de distancias entre clusters es el siguiente: primero se debe calcular la distancia entre clases, como la entrada es la matriz de etiquetas generada
# en el proceso anterior, es necesario transponerla para que la distancia sea entre clases y no entre puntos, posteriormente es necesario normalizar las distancias para
# que la máxima  distancia sea 1.

# Posteriormente se debe construir una matriz dist2 que al multiplicarse con la matriz de distancias dist(D) deje solo aquellos valores que sean menores al ubral y anule
# aquellos que sean mayores, gegerando la matriz M=dist*dist2. Por tanto, la matriz de multiplicación dist2 debe tener valores en uno en las posiciones  relativas a la
# matriz de distancia donde se desee mantener los valores y cero en las posiciones en donde se desee anularlos (dist2 = torch.zeros(dist.shape), dist2[dist < umbral] = 1 ).

# Los valores anulados deben convertirse en unos, ya que uno representa la máxima distancia entre clusters de diferentes clases y como estos valores sobrepasaron el
# umbral, significa que ya se encuentran lo suficientemente separados para evitar el solapamiento y por tanto se les asigna el valor de uno. Esto se hace, creando una
# matriz dist3 llena de ceros en las posiciones relativas a la matriz de distancia original dist(D), donde los valores sean menores al umbral y unos en las demás
# posiciones. Finlamente, la matriz reultante S es la suma de la matriz M y la matriz dist3 S=M+dist3.  ( dist3 = torch.ones(dist.shape), dist3[dist < umbral] = 0,
# dist = dist+dist3 ).

# Ahora se tiene una matriz S con los valores originales de distancia menores al umbral, diagonal ceros que representa los valores de distancia de cada clase con sigo
# misma y valores en uno para las distanca mayores al umbral. La matriz S es generada en forward de la red FFN, en cada iteración del entrenamiento la matriz S es comparada
# mediante entropia cruzada con el objetivo de aprendizaje, la matriz U(matrizUnos), que es una matriz cuadrada  de e x e llena unos y su diagonal de ceros, ya que
# idealmente, todas las clases distintas a si mismas deberían estar  totalmente separadas, en donde el valor de distancia 1, significa que no existe solapamiento
# entre dos clases.

# Del segundo proceso se genera la segunda función de costo, basada en la diferencia inter cluster Ld = C(S, U)

# La función de costo total y la cual se desea optimizar es L que es la suma de Ls y Ld:  L=Ls+Ld
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

import tensorflow as tf
from pynndescent import NNDescent
from torch.autograd import Variable
import torch.nn as nn
import torch.nn.functional as F
import torch
import numpy as np
import torch.optim as optim
from tqdm import tqdm
import io
# from modeloAE import Decoder2820, Decoder28, Decoder32, weights_init
from umap.umap_ import find_ab_params
from modeloAE import Encoder2820, Encoder28, Encoder32, weights_init, EncoderFC
import base64

# Xin2 = np.column_stack((Xin, labelsIn))
# umap_graph, sigmas, rhos = getGrafoHD(Xin2, n_neighbors=15)

# dh = umap_graph.toarray()
# dh = torch.tensor(dh)

# min_dist = 0.1 # controla la intensidad con la que UMAP puede empaquetar los puntos (0 es más)
# _a, _b = find_ab_params(1.0, min_dist)


class MyEnsembleRD(nn.Module):

    def __init__(self, metodos, d, labelsIn):
        super().__init__()
        self.d = d
        self.metodos = metodos
        _, clm = self.metodos[0].rd.shape
        self.dimMetodos = len(self.metodos)*clm
        #print("dimensiones metodos", self.dimMetodos)

        self.clases = torch.unique(labelsIn)
        self.fcNets = nn.Linear(d, len(self.clases))
        self.softmax = nn.Softmax(dim=1)

        # Encoder specification
        # de forma escalonada

        self.combinationDiscriminante = nn.Sequential(  # con 3 metodos
            nn.Linear(self.dimMetodos, 100),
            nn.ReLU(),
            # nn.BatchNorm1d(20),
            # nn.Linear(20, 40),
            # nn.ReLU(),
            # #nn.BatchNorm1d(40),
            # nn.Linear(40, 60),
            # nn.ReLU(),
            # #nn.BatchNorm1d(60),
            # nn.Linear(60, 80),
            # nn.ReLU(),
            # #nn.BatchNorm1d(80),
            # nn.Linear(80, 100),
            # nn.ReLU(),
            # #nn.BatchNorm1d(100),
            # nn.Linear(100, 100),
            # nn.ReLU(),
            nn.BatchNorm1d(100),
            nn.Linear(100, 80),
            nn.ReLU(),
            nn.BatchNorm1d(80),
            nn.Linear(80, 60),
            nn.ReLU(),
            nn.BatchNorm1d(60),
            nn.Linear(60, 40),
            nn.ReLU(),
            nn.BatchNorm1d(40),
            nn.Linear(40, 20),
            nn.ReLU(),
            nn.BatchNorm1d(20),
            nn.Linear(20, self.d)
        )

        # self.combination2 = nn.Linear(4, 2) # con 2 metodos

        # de forma directa
        # self.combinationTopologica = nn.Linear(self.dimMetodos, self.d)

        # print("\t1 - Reducción lineal escalonada")
        # print("\t2 - Reducción lineal directa")
        # self.opcionrd = input("Selecciona una opción >> ")

    def forward(self, x):
        code = self.encode(x)
        # out = self.decoder(code)
        # CREO QUE HAY QUE DEJARLO PARA TOPOLOGICO
        # # Euclidean distances between samples (and negative samples)
        # distancesLD = torch.cdist(code, code, p=2)  # <----------------------------
        # dl = convert_distance_to_probability(distancesLD, _a, _b)  #umap
        matrizEtiquetasGen = self.acercamientoIntraCluster(code)
        # solapamiento = self.solapamiento(code)
        # distance = self.alejamientoExtraCluster(matrizEtiquetasGen)
        return code, matrizEtiquetasGen  # , distance  # 1 out,

    def encode(self, x):
        rdComb = self.combinationDiscriminante(x)
        # if self.opcionrd == "1":  # con reducción escalonada
        #     rdComb = self.combinationDiscriminante(x)
        # elif self.opcionrd == "2":  # con esta capa se aplica la rd directamente, sin reducciones intermedias
        #     # <---- aplica la capa fullyconect (nn.Linear)
        #     rdComb = self.combinationTopologica(x)
        return rdComb

    def acercamientoIntraCluster(self, x):
        x = self.fcNets(x)  # de d a numero de clases
        # x = self.softmax(x) # en probabilidades
        return x

    def alejamientoExtraCluster(self, matrix):
        umbral = 0.3
        # Hay que transponerla ya que se quiere hacer la distancia de clase X clase, no de n x n, ya que la idea es encontrar la diferencia entre clases.
        matrix = matrix.t()

        # Calcula la matriz de distancias euclidianas entre los puntos de una sola matriz
        dist = torch.cdist(matrix, matrix, p=2)
        # print("dist",dist)

        dist = F.normalize(dist, p=2, dim=1)
        # print("dist normalizada",dist)

        # poner 0 donde se quiere anular los valores, es decir en los valores mayores al umbral, y 1 donde se quiere dejar los valores
        dist2 = torch.zeros(dist.shape)
        dist2[dist < umbral] = 1
        # print("dist2 umbral",dist2)

        M = dist*dist2  # se quitan valores mayores al umbral
        # print("dist quitando distancias mayores del umbral",dist)

        # para convertir los 0 en 1 y viceversa, con la intención de aumentar la distancia entre clusters, Ya que anular los valores mayores al umbral estos valores
        # quedaron en cero, y teniendo en cuenta que en realidad 1 representa la maxima distancia, entonces e se necesario convertir los ceros en unos
        dist3 = torch.ones(dist.shape)
        dist3[dist < umbral] = 0
        # print("dist conversion 0 a 1",dist3)

        S = M+dist3  # se cambian 0 por 1
        # print("cambiando 0 por 1 y viceversa",dist)

        # Posiblemente la llenada de 1nos en la diaganoal deba hacerse directamente despues de multiplicar dist*dist2 ya que posteriormente se
        S.fill_diagonal_(1)
        # suma con dist3 y la giagonal ya estaría llena de unos. Sin embargo parace que no es necesario hacer este llenado de diagonal de unos ya que posteriormente se
        # compara con la matriz de unos y dagonal ceros del entrenamiento, y como la diagonal ya esta llena de ceros, y la diagonal de la matriz distancias también es
        # ceros, entonces ya habrá llegado a su objetivo de aprendizaje. Sin embargo puede ser que al llenarla de unos y comparar con la diagonal de distancias que es
        # cero, el efecto sea algo así como una optimización intraclusters, ya que intenta hacer mas compacto los elementos de un clusters, al aproximar los unoa a ceros
        # pero esto solo es una hiótesis.

        # dist = umbral - dist
        # print("dist resta umbral",dist)
        # dist.fill_diagonal_(0)

        return S


def ensambleRNRD(trainloader, labelsIn, listaMetodos, bz, numepochs, d, device, saveName, learningRate, modelosTransferidos):

    labelsIn = torch.from_numpy(labelsIn).to(device)
    # nc = len(np.unique(labelsIn))
    nc = len(torch.unique(labelsIn))
    # matrizUnos = torch.ones(nc,nc)
    # matrizUnos = matrizUnos.fill_diagonal_(0)

    # concatenacion de métodos RD y conversión a dataloader
    bdC = True
    for met in listaMetodos:
        if bdC:  # en principio unionC esta vacia y portanto por primvera ocasión recibe el rd
            # concatenación por columnas, se quita la columna referente a las etiquetas
            unionC = met.rd  # [:, :d]
            bdC = False
        else:  # como unionC ya no esta vacia ya puede concatenarla con lo siguiente
            # concatenacion por Columnas
            unionC = np.concatenate((unionC, met.rd), axis=1)  # [:, :d]
    unionC = torch.from_numpy(unionC).float().to(device)
    dataset = torch.utils.data.TensorDataset(unionC, labelsIn)
    RDloader = torch.utils.data.DataLoader(dataset, batch_size=bz, shuffle=False)
    # RDiter = iter(RDloader) # Las iteraciones del dataloadoer respectivo a la concatenación de métodos RD

    encEnsembleRD = MyEnsembleRD(listaMetodos, d, labelsIn)  # .apply(weights_init)
    encEnsembleRD.to(device)

    # 3. Define a Loss function and optimizer
    # criterion = nn.SmoothL1Loss().to(device)
    criterionMse = nn.MSELoss()  # error
    criterionCross = nn.CrossEntropyLoss()

    optimizer = optim.Adam(encEnsembleRD.parameters(), lr=learningRate)  # lr=0.0001

    # 4. Train the network
    print('Start Training AE Assembler')
    codes = np.random.rand(0, d)

    # loop over the dataset multiple times: cada entrenamiento se da en (epocas * minilotes)
    for epoch in tqdm(range(numepochs)):
        # tqdm pone una barra de tiempo

        running_loss = 0.0
        for RDiter, data in zip(RDloader, trainloader):
            # for i, data in enumerate(trainloader, 0): # recorre las imagenes por minilotes. El tamaño del minilote se estableció en 64 (batch_size=64) por lo que el trainloader carga minilotes de 64 imagenes (inputs torch.Size([64, 1, 28, 28] es decir [tamaño minilote, 1, 28, 28] en otras palabras: 64 imagens monocromaticas (1 canal rgb) de 28x28 pixeles))
            # get the inputs
            inputs, labels = data
            inputs = inputs.to(device)
            labels = labels.to(device)
            # wrap them in Variable
            # la función Variable (obsoleta creo) creo que es similar a poner requires_grad=True
            inputs, labels = Variable(inputs), Variable(labels)

            # para los incrustamientos RD concatenados
            RDcocatenacion, RDlabels = RDiter  # .next()
            RDlabels = RDlabels.to(torch.long)
            RDcocatenacion = RDcocatenacion.to(device)
            RDlabels = RDlabels.to(device)
            RDcocatenacion, RDlabels = Variable(RDcocatenacion), Variable(RDlabels)

            matriz_onehot = torch.eye(nc)[RDlabels]

            # zero the parameter gradients
            optimizer.zero_grad()

            # *** forward + backward + optimize
            # forward, out es como el decode, el tamaño del out y code en el ultimo minilote es el excedente, ejemplo si son 100 tuplas con un tamño de batch_size=64, solo se pueden formar dos grupos de minilotes uno de 64 y otro de 32 por tal razon el minilote final es de 32 inputs (imagenes)
            # 1 out, # Se quito matrizDistanciaClusters
            code, matrizEtiquetasGen = encEnsembleRD(RDcocatenacion)
            if epoch == numepochs-1:  # solo guardo en la ultima epoca, donde los parametros ya han sido optimizados
                # unifica los code respectivos de cada minilote
                codes = np.concatenate((codes, code.cpu().data), axis=0)
                # decodes = np.concatenate((decodes, out.cpu().data), axis=0)
                # decodes.append(out.cpu().data) # unificacion de los outs (decodes), sirve para guardar la reconstruccion de las imagenes por minilotes
                # unifico todas las tuplas de cada minilote para que coincidan con el tamaño de los datos de entrada
                # labelsCodes = np.concatenate((labelsCodes, labels.cpu().data), axis=0)
                # labelsCodes.append(labels.cpu().data)

            loss = criterionCross(matrizEtiquetasGen, matriz_onehot)  # backward
            # loss2 = criterionCross(matrizDistanciaClusters, matrizUnos)  # backward
            # loss = loss1 + loss2
            loss.backward()  # backward
            optimizer.step()  # optimize

            # print statistics
            # running_loss += loss.item()
            # if i % 100 == 99:    # print every 100 mini-batches
            # print('[%d, %5d] loss: %.3f' %(epoch + 1, i + 1, running_loss / 100))
            # running_loss = 0.0
            print("lossssss", loss.item(), end='\r')

        # if epoch % int(0.1*numepochs) == 0:
            # print(f'epoch {epoch} \t Loss: {loss.item():.4g}')

    print('Finished Training AE Clasico')
    
    # GUARDAR EL PAQUETE DE MODELOS PROGENITORES JUNTO AL GENERADO POR NETDR 
    #ruta = "./model/NetDRd.model"
    modelosTransferidos['NetDRd'] = encEnsembleRD.state_dict()
    torch.save(modelosTransferidos, saveName)  

    return codes

 # NetDR TOPOLOGICO  _____________________________________________________________________________________________________________


# PARA PUNTO DOS (Con metodos DR) TOPOLOGICO
# Tiene una función de costo compuesta. La primer parte intenta conservar la topología local, elaborando una matriz de probabilidades emergente de un
# grafo de vecindarios el cual es comparado con una matriz de probabilidades de la matriz de distancias de baja dimensión.
# La otra parte de la función de costo intenta preservar la topologia global, comparando las matrices de distancia de alta y baja dimensión.
# La función de costo total es la suma de las dos funciones de costo anteriors, es es ponderada permitiendo elegir que se desea conservar, si mas lo local o lo global.
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
# ________________________ 2da Forma de obtener el Grafo UMAP UMAP


def getGrafoHD(Xin, n_neighbors):

    # number of trees in random projection forest
    n_trees = 5 + int(round((Xin.shape[0]) ** 0.5 / 20.0))
    # max number of nearest neighbor iters to perform
    n_iters = max(5, int(round(np.log2(Xin.shape[0]))))
    # distance metric
    metric = "euclidean"
    # number of neighbors for computing k-neighbor graph
    # n_neighbors = 10

    # get nearest neighbors
    nnd = NNDescent(Xin.reshape((len(Xin), np.product(np.shape(Xin)[1:]))), n_neighbors=n_neighbors, metric=metric, n_trees=n_trees, n_iters=n_iters, max_candidates=60, verbose=True)
    # get indices and distances
    knn_indices, knn_dists = nnd.neighbor_graph

    # Construir complejo simplicial difuso
    # La función fuzzy_simplicial_set toma el grafo del vecino más próximo y computa un grafo de las probabilidades de que exista una arista entre puntos.
    # Las probabilidades locales, unidireccionales (P UMAP i|j), se calculan entre un punto y sus vecinos para determinar la probabilidad de que exista un borde (o simplex), basándose en la suposición de que
    # los datos se distribuyen uniformemente a través de un múltiple en un espacio de datos deformado.
    # Bajo este supuesto, se establece una noción local de distancia mediante la distancia al késimo vecino más cercano y la probabilidad local se escala por esa noción local de distancia.
    # p UMAPj∣i = exp(−(d(xi,xj)−ρi)/σi)
    # Donde ρi es un parámetro de conectividad local ajustado a la distancia de xi a su vecino más cercano, y σi es un parámetro de conectividad local ajustado a la distancia local alrededor de xi a sus
    # k vecinos más cercanos (donde k es un hiperparámetro).
    # En el paquete UMAP, estos parámetros se calculan usando smooth_knn_dist.
    # Después de calcular las probabilidades de borde unidireccional para cada punto de datos, calculamos una probabilidad global como la probabilidad de que ocurra cualquiera de las dos probabilidades
    # locales unidireccionales:
    # pij=(pj∣i+pi∣j)−pj∣ipi∣j

    # ///video
    # p UMAPj∣i = exp(−(d(xi,xj)−ρi)/σi)  ecuación para las puntuaciones de similitud:  se eleva la distancia bruta negativa menos la distancia al vecino mas cercano dividido por sigma
    # pij=(pj∣i+pi∣j)−pj∣ipi∣j  o  SymetricalScore=(S1+S2)-S1S2   Hace que las puntuaciones sean simetricas

    from sklearn.utils import check_random_state
    from umap.umap_ import fuzzy_simplicial_set
    random_state = check_random_state(None)
    # build fuzzy_simplicial_set
    umap_graph, sigmas, rhos = fuzzy_simplicial_set(X=Xin, n_neighbors=n_neighbors, metric=metric, random_state=random_state, knn_indices=knn_indices, knn_dists=knn_dists,)
    return umap_graph, sigmas, rhos

# convert probabilities to distances
# Low-d Scores = 1 / 1 + ad**(2b) calcula puntuaciones de similitud en baja dimensión, donde d es la distancia en baja, y a y b estan ligados con la agrupacion y dispersión
# de forma predeterminada alfa es igual a 1.577 y beta es igual a  0.8951. Si alfa y beta son igual a 1, las puntuaciones serían igual a las de T-SNE


def convert_distance_to_probability(distances, a=1.0, b=1.0):
    return -torch.log1p(a * distances ** (2 * b))


# from modeloAE import Decoder2820, Decoder28, Decoder32, weights_init


# controla la intensidad con la que UMAP puede empaquetar los puntos (0 es más)
min_dist = 0.1
_a, _b = find_ab_params(1.0, min_dist)


class MyEnsembleRT(nn.Module):

    def __init__(self, metodos, d):
        super().__init__()
        self.d = d
        self.metodos = metodos
        _, clsmtds = self.metodos[0].rd.shape
        self.dimMetodos = len(self.metodos)*clsmtds
        #print("dimensiones metodos", self.dimMetodos)

        # Encoder specification
        # de forma escalonada

        # self.combinationDiscriminante = nn.Sequential(  # con 3 metodos
        #     nn.Linear(self.dimMetodos, 20),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(20),
        #     nn.Linear(20, 40),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(40),
        #     nn.Linear(40, 60),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(60),
        #     nn.Linear(60, 80),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(80),
        #     nn.Linear(80, 100),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(100),
        #     nn.Linear(100, 100),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(100),
        #     nn.Linear(100, 80),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(80),
        #     nn.Linear(80, 60),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(60),
        #     nn.Linear(60, 40),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(40),
        #     nn.Linear(40, 20),
        #     nn.ReLU(),
        #     nn.BatchNorm1d(20),
        #     nn.Linear(20, self.d)
        # )

        # self.combination2 = nn.Linear(4, 2) # con 2 metodos

        # de forma directa
        self.combinationTopologica = nn.Linear(self.dimMetodos, self.d)

        # print("\t1 - Reducción lineal escalonada")
        # print("\t2 - Reducción lineal directa")
        # self.opcionrd = input("Selecciona una opción >> ")

    def forward(self, x):
        code = self.encode(x)
        # out = self.decoder(code)

        # Euclidean distances between samples (and negative samples)
        distancesLD = torch.cdist(code, code, p=2)
        # probabilidad distancias en baja para compararse con el grafo de hd
        gpdl = convert_distance_to_probability(distancesLD, _a, _b)

        distancesLD = (distancesLD - distancesLD.min()) / \
            (distancesLD.max() - distancesLD.min())  # distancias ld normalizadas

        return code, gpdl, distancesLD   # 1 out,

    def encode(self, x):
        rdComb = self.combinationTopologica(x)
        # if self.opcionrd == "1":  # con reducción escalonada
        #     rdComb = self.combinationDiscriminante(x)
        # elif self.opcionrd == "2":  # con esta capa se aplica la rd directamente, sin reducciones intermedias
        #     # <---- aplica la capa fullyconect (nn.Linear)
        #     rdComb = self.combinationTopologica(x)
        return rdComb


def ensambleRNRT(trainloader, Xin, labelsIn, listaMetodos, bz, numepochs, d, device, saveName, learningRate, modelosTransferidos):

    # Con grafo (preservación de topologia local)
    umap_graph, sigmas, rhos = getGrafoHD(Xin, n_neighbors=10)
    Gdh = umap_graph.toarray()
    Gdh = torch.tensor(Gdh)

    Xint = torch.from_numpy(Xin).to(device)
    # Con matrices de distancias (preservación de topología global)
    distancesHD = torch.cdist(Xint, Xint, p=3)
    distancesHD = (distancesHD - distancesHD.min()) / \
        (distancesHD.max() - distancesHD.min())

    # concatenacion de métodos RD y conversión a dataloader
    bdC = True
    for met in listaMetodos:
        if bdC:  # en principio unionC esta vacia y portanto por primvera ocasión recibe el rd
            # concatenación por columnas, se quita la columna referente a las etiquetas
            unionC = met.rd[:, :d]
            bdC = False
        else:  # como unionC ya no esta vacia ya puede concatenarla con lo siguiente
            # concatenacion por Columnas
            unionC = np.concatenate((unionC, met.rd[:, :d]), axis=1)
    unionC = torch.from_numpy(unionC).float().to(device)
    labelsInT = torch.from_numpy(labelsIn).to(device)
    dataset = torch.utils.data.TensorDataset(unionC, labelsInT)
    RDloader = torch.utils.data.DataLoader(dataset, batch_size=bz, shuffle=False)
    # RDiter = iter(RDloader) # Las iteraciones del dataloadoer respectivo a la concatenación de métodos RD
    # -----------------------------------------------------------------------------------------------------

    encEnsembleRT = MyEnsembleRT(listaMetodos, d)  # .apply(weights_init)
    encEnsembleRT.to(device)

    # 3. Define a Loss function and optimizer
    # criterion = nn.SmoothL1Loss().to(device)
    # criterion = nn.MSELoss()  # error
    criterion = nn.CrossEntropyLoss()

    optimizer = optim.Adam(encEnsembleRT.parameters(), lr=learningRate, weight_decay=1e-5)  # lr=0.0001

    # 4. Train the network
    print('Start Training AE Assembler')
    codes = np.random.rand(0, d)
    labelsCodes = np.random.rand(0)

    # loop over the dataset multiple times: cada entrenamiento se da en (epocas * minilotes)
    for epoch in tqdm(range(numepochs)):
        # tqdm pone una barra de tiempo

        running_loss = 0.0
        for RDiter, data in zip(RDloader, trainloader):
            # for i, data in enumerate(trainloader, 0): # recorre las imagenes por minilotes. El tamaño del minilote se estableció en 64 (batch_size=64) por lo que el trainloader carga minilotes de 64 imagenes (inputs torch.Size([64, 1, 28, 28] es decir [tamaño minilote, 1, 28, 28] en otras palabras: 64 imagens monocromaticas (1 canal rgb) de 28x28 pixeles))
            # get the inputs
            inputs, labels = data
            inputs = inputs.to(device)
            labels = labels.to(device)
            # wrap them in Variable
            # la función Variable (obsoleta creo) creo que es similar a poner requires_grad=True
            inputs, labels = Variable(inputs), Variable(labels)

            # para los incrustamientos RD concatenados
            RDcocatenacion, RDlabels = RDiter  # .next()
            RDcocatenacion = RDcocatenacion.to(device)
            RDlabels = RDlabels.to(device)
            RDcocatenacion, RDlabels = Variable(RDcocatenacion), Variable(RDlabels)

            # zero the parameter gradients
            optimizer.zero_grad()

            # *** forward + backward + optimize
            # forward, out es como el decode, el tamaño del out y code en el ultimo minilote es el excedente, ejemplo si son 100 tuplas con un tamño de batch_size=64, solo se pueden formar dos grupos de minilotes uno de 64 y otro de 32 por tal razon el minilote final es de 32 inputs (imagenes)
            code, gpdl, distancesld = encEnsembleRT(RDcocatenacion)  # 1 out,
            if epoch == numepochs-1:  # solo guardo en la ultima epoca, donde los parametros ya han sido optimizados
                # unifica los code respectivos de cada minilote
                codes = np.concatenate((codes, code.cpu().data), axis=0)
                # decodes = np.concatenate((decodes, out.cpu().data), axis=0)
                # decodes.append(out.cpu().data) # unificacion de los outs (decodes), sirve para guardar la reconstruccion de las imagenes por minilotes
                # unifico todas las tuplas de cada minilote para que coincidan con el tamaño de los datos de entrada
                labelsCodes = np.concatenate(
                    (labelsCodes, labels.cpu().data), axis=0)
                # labelsCodes.append(labels.cpu().data)

            loss1 = criterion(Gdh, gpdl)  # backward
            loss2 = criterion(distancesHD, distancesld)
            loss = loss1 + loss2
            loss.backward()  # backward
            optimizer.step()  # optimize

            # print statistics
            # running_loss += loss.item()
            # if i % 100 == 99:    # print every 100 mini-batches
            # print('[%d, %5d] loss: %.3f' %(epoch + 1, i + 1, running_loss / 100))
            # running_loss = 0.0

            # print("lossssss", loss.item(), end='\r')

        # if epoch % int(0.1*numepochs) == 0:
            # print(f'epoch {epoch} \t Loss: {loss.item():.4g}')

    print('Finished Training AE Clasico')

    # GUARDAR EL PAQUETE DE MODELOS PROGENITORES JUNTO AL GENERADO POR NETDR 
    #ruta = "./model/NetDRt.model"
    modelosTransferidos['NetDRt'] = encEnsembleRT.state_dict()
    torch.save(modelosTransferidos, saveName)  
    
    return codes


def loadRdEncJava(trainloader, dimImg, d, opcData, modeloFromJava, device):
    # print("tipo modeloFromJava", type(modeloFromJava))
    modelo_bytes = base64.b64decode(modeloFromJava)
    #modelo_bytes = bytes(modeloFromJava)
    #modelo_bytes = bytes(byte if 0 <= byte <= 255 else 0 for byte in modeloFromJava)
    #modelo_decodificado = base64.b64decode(modelo_bytes)
    
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
    rdenc.load_state_dict(torch.load(io.BytesIO(modelo_bytes), map_location=device))
    print("modelo cargado: ", rdenc)

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


def metodosAcombinar(Xin, dEnc, metodosC, topoOdis, trainloader, device):

    # Reducción de dimensíon para llenar la listaMetodos con DR DIRECTOS (provenientes de JAVA)
    from metodosDR import umap, parametricumap, densemap, trimap, t_sne
    import json

    # Clase Métodos rd
    class MetodoRd:
        nombre = ""
        rd = object

    listaMetodos = list()
    _, dimImg = Xin.shape
    
    modelosTransferidos = {}

    for met in metodosC:
        metodoDRjava = MetodoRd()
        metodoDRjava.nombre = met['metDR']
        
        if met['tipo'] == "incrustamiento":
            #print("incrustamientooooooooo,", met['Ydr'])
            metodoDRjava.rd = np.array(met['Ydr'])
            #print("metodoDRjava.rd ", metodoDRjava.rd)
        elif met['tipo'] == "modelo":
            #print("modelooooooooo,", met['modelo'])
            codes, rdenc = loadRdEncJava(trainloader, dimImg, dEnc, "2", met['modelo'], device)
            metodoDRjava.rd = codes
            # Guardar los modelos en un diccionario
            #models_dict = { met['metDR']: rdenc}
            modelosTransferidos[met['metDR']] = rdenc.state_dict()
           
        listaMetodos.append(metodoDRjava)

    # # Métodos clasicos optimizados con UMAP ***** PUEDE SER QUE SE OPTIMICE LOS METODOS TRAIDOS DE JAVA CON EL TRIMAP COMO AQUI ABAJO
    # mejora = TRIMAP(n_dims=2).fit_transform(encodeRd)

    if topoOdis == "1":  # Discriminante
        dCmb = dEnc

    elif topoOdis == "2":  # Topologico
        dCmb = dEnc
        tsne = MetodoRd()
        tsne.nombre = "tsne"
        tsne.rd = t_sne(Xin, dCmb, device)
        listaMetodos.append(tsne)

    umapM = MetodoRd()
    umapM.nombre = "umapM"
    umapM.rd = umap(Xin, dCmb)
    listaMetodos.append(umapM)

    dumap = MetodoRd()
    dumap.nombre = "densemap"
    dumap.rd = densemap(Xin, dCmb)
    listaMetodos.append(dumap)

    # pumap = MetodoRd()  # como que genera sólo en dos dimensiones
    # pumap.nombre = "parametricUmap"
    # pumap.tipo = "clasico"
    # pumap.rd = parametricumap(Xin, dCmb)
    # cmds.rnx = funcion que calcula rnx
    # pumap.indexOriginales = range(fl)
    # listaMetodos.append(pumap)

    trimapM = MetodoRd()
    trimapM.nombre = "trimapM"
    # encodeRd = pca(Xint, dCmb, device)
    trimapM.rd = trimap(Xin, dCmb)
    listaMetodos.append(trimapM)

    # pcaM = MetodoRd()
    # pcaM.nombre = "pcaM"
    # pcaM.tipo = "clasico"
    # pcaM.rd = pca(Xint, dCmb, device)
    # pcaM.indexOriginales = range(fl)
    # listaMetodos.append(pcaM)

    print("La longitud de la lista es:", len(listaMetodos))
    #print("lista: ", listaMetodos)

    return listaMetodos, modelosTransferidos


# topoOdis: 1 Discriminante, 2 Topologico
def NetDR(Xin, labelsIn, dEnc, metodosC, topoOdis, device, saveName, nepocas, learningRate):

    fl, cl = Xin.shape
    bz = fl

    Xint = torch.from_numpy(Xin).to(device)
    yt = torch.from_numpy(labelsIn).to(device)
    dataset = torch.utils.data.TensorDataset(Xint, yt)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=bz, shuffle=False)

    listaMetodos, modelosTransferidos = metodosAcombinar(Xin, dEnc, metodosC, topoOdis, trainloader, device)

    # "1" discriminante,  "2" topologico

    if topoOdis == "1":  # Es mejor reducir a 100 (no dcompatible con t-sne)
        # discriminante con 10 iteraciones y selección 1 (escalonada)
        #nepocas = 10
        combinacion = ensambleRNRD(trainloader, labelsIn, listaMetodos, bz, nepocas, dEnc, device, saveName, learningRate, modelosTransferidos)
        # combinacionL = np.column_stack((combinacion, labelsIn))

    elif topoOdis == "2":  # Es mejor reducir a 2 e incluir a t-sne
        # topologico con 5 iteraciones y selección 2 (directa)
        #nepocas = 10
        combinacion = ensambleRNRT(trainloader, Xin, labelsIn, listaMetodos, bz, nepocas, dEnc, device, saveName, learningRate, modelosTransferidos)
        # combinacion = TRIMAP(n_dims=2).fit_transform(combinacion)
        # combinacion = umap(mejora, 2, device)
        # combinacionL = np.column_stack((combinacion, labelsIn))

    return combinacion



# CARGA DE MODELO NETDR

def loadRdEnc2Netdr(trainloader, dimImg, modelCarga, d, opcData, device):  # Carga cada uno de los modelos pasados a NetDR
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
    rdenc.load_state_dict(modelCarga)

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


def loadNetDR(Xin, labelsIn, trainloader, dimImg, ruta, d, opcData, device):
    # Cargar los modelos desde el archivo
    models_dict = torch.load(ruta)
    
    metodosC = []
    topoOdis = "1"
    
    for met, modelCarga in models_dict.items():  # Carga los modelos progenitores
        if met != "NetDRt" and met != "NetDRd":
            codes, rdenc = loadRdEnc2Netdr(trainloader, dimImg, modelCarga, d, opcData, device)
            
            dataLoadMetodo = {
                "tipo": "incrustamiento",
                "Ydr": codes,
                "metDR": met,
                "modelo": rdenc
            }
            # Agregar el diccionario a la lista
            metodosC.append(dataLoadMetodo)
            
        elif met == "NetDRd":
            topoOdis = "1"
        elif met == "NetDRt":
            topoOdis = "2"
    
    # obtiene la lista de metodos a combinar        
    listaMetodos, _ = metodosAcombinar(Xin, d, metodosC, topoOdis, trainloader, device)
    
    # concatenacion de métodos RD y conversión a dataloader
    bdC = True
    for met in listaMetodos:
        if bdC:  # en principio unionC esta vacia y portanto por primvera ocasión recibe el rd
            # concatenación por columnas, se quita la columna referente a las etiquetas
            unionC = met.rd[:, :d]
            bdC = False
        else:  # como unionC ya no esta vacia ya puede concatenarla con lo siguiente
            # concatenacion por Columnas
            unionC = np.concatenate((unionC, met.rd[:, :d]), axis=1)
    unionC = torch.from_numpy(unionC).float().to(device)
    labelsInT = torch.from_numpy(labelsIn).to(device)
    dataset = torch.utils.data.TensorDataset(unionC, labelsInT)
    RDloader = torch.utils.data.DataLoader(dataset, batch_size=60, shuffle=False)
        
    # se debe recorrer la lista de models_dict hasta obtener el modelo de NetDR (t o d)
    for met, modelCargaNetdr in models_dict.items(): 
        print("Cual Net", met)
        
        if met == "NetDRd":
            encEnsembleRD = MyEnsembleRD(listaMetodos, d, labelsInT)  # .apply(weights_init)
            encEnsembleRD.to(device)
            encEnsembleRD.load_state_dict(modelCargaNetdr)
            
            incrustamientoNet = np.random.rand(0, d)
            for RDiter in RDloader:
                RDcocatenacion, RDlabels = RDiter 
                #inputs = inputs.to(torch.float32)
                RDcocatenacion = RDcocatenacion.to(device)
                RDlabels = RDlabels.to(device)

                with torch.no_grad():
                    encoded = encEnsembleRD.encode(RDcocatenacion)
                    #encoded, matrizEtiquetasGen = encEnsembleRD(RDcocatenacion)
                    enc = encoded.cpu().detach().numpy()
            
                incrustamientoNet = np.concatenate((incrustamientoNet, enc), axis=0)
            print("Finished Loading NetDRd")
            break
        
        elif met == "NetDRt":
            encEnsembleRD = MyEnsembleRT(listaMetodos, d)  # .apply(weights_init)
            encEnsembleRD.to(device)
            encEnsembleRD.load_state_dict(modelCargaNetdr)
            
            incrustamientoNet = np.random.rand(0, d)
            for RDiter in RDloader:
                RDcocatenacion, RDlabels = RDiter 
                #inputs = inputs.to(torch.float32)
                RDcocatenacion = RDcocatenacion.to(device)
                RDlabels = RDlabels.to(device)

                with torch.no_grad():
                    encoded = encEnsembleRD.encode(RDcocatenacion)
                    enc = encoded.cpu().detach().numpy()
            
                incrustamientoNet = np.concatenate((incrustamientoNet, enc), axis=0)
            print("Finished Loading NetDRt")
            break

    return incrustamientoNet, encEnsembleRD