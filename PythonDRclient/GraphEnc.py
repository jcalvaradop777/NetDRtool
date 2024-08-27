# https://github.com/zepx/graphencoder/blob/master/graphencoder.py

import numpy as np
import argparse
from tqdm import tqdm
from sklearn.datasets import load_wine
from sklearn import preprocessing
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics.cluster import normalized_mutual_info_score
from torch import nn, optim
import torch

from model import GraphEncoder


def trainGraphEncoder(ne, Xin, labelsIn, device):

    X = Xin
    Y = labelsIn
    k = len(np.unique(Y))

    min_max_scaler = preprocessing.MinMaxScaler()
    X = min_max_scaler.fit_transform(X)
    
    # Obtain Similarity matrix
    S = cosine_similarity(X, X)

    D = np.diag(1.0 / np.sqrt(S.sum(axis=1)))
    X_train = torch.tensor(D.dot(S).dot(D)).float().to(device)

    layers = [len(X_train)] + [128, 2, 128] + [len(X_train)]

    model = GraphEncoder(layers, k).to(device)
    optimizer = optim.Adam(model.parameters(), lr=0.01)

    epcs = ne #10
    with tqdm(total=epcs) as tq:
        for epoch in range(1, epcs + 1):
            optimizer.zero_grad()
            X_hat, code = model(X_train)
            loss = model.loss(X_hat, X_train, 0.01, 0.5)
            nmi = normalized_mutual_info_score(model.get_cluster(), Y, average_method='arithmetic')

            loss.backward()
            optimizer.step()

            tq.set_postfix(loss='{:.3f}'.format(loss), nmi='{:.3f}'.format(nmi))
            tq.update()
        print(model.get_cluster())
    print("code", code.shape)
    #code = torch.from_numpy(code).to(device)
    return code