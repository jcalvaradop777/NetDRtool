import torch
from torch import nn
from torch.autograd import Variable
from torch.utils.data import DataLoader
from torchvision.utils import save_image
import torch.optim as optim
from tqdm import tqdm
import numpy as np

########################################################################
# 2. Convolution Neural Network
# ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

class AutoEncoderMat(nn.Module):
    def __init__(self,Din,dout):
        super(AutoEncoderMat, self).__init__()
        
        self.encoder = nn.Sequential(
            nn.Linear(Din, 128),
            nn.ReLU(True),
            #nn.Dropout(0.2), ###
            nn.Linear(128, 64),
            nn.ReLU(True), 
            #nn.Dropout(0.2),
            nn.Linear(64, 12), 
            nn.ReLU(True), 
            #nn.Dropout(0.2),
            nn.Linear(12, dout))
        
        self.decoder = nn.Sequential(
            nn.BatchNorm1d(dout), ####
            nn.Linear(dout, 12), 
            nn.ReLU(True),
            #nn.Dropout(0.2),
            nn.Linear(12, 64),
            nn.ReLU(True),
            #nn.Dropout(0.2),
            nn.Linear(64, 128),
            nn.ReLU(True), 
            #nn.Dropout(0.2),
            nn.Linear(128, Din), 
            nn.Tanh())

    def forward(self, x):
        code = self.encoder(x)
        out = self.decoder(code)
        return out, code

def autoencoder(Xint, labelsIn, d, device):
    
    fl, dim = Xint.shape
    print("---------------dim", dim)
    Xint = torch.from_numpy(Xint)
    labelsIn = torch.from_numpy(labelsIn)
    dataset = torch.utils.data.TensorDataset(Xint, labelsIn)
    trainloader = torch.utils.data.DataLoader(dataset, batch_size=1000, shuffle=False) 
    
    autoenc = AutoEncoderMat(Din=dim, dout=d).double().to(device)

    # criterion = nn.SmoothL1Loss().to(device)
    criterion = nn.MSELoss()  # error
    optimizer = optim.Adam(autoenc.parameters(), lr=0.0001, weight_decay=1e-5)  # lr=0.0001
    #optimizer = optim.SGD(autoenc.parameters(), lr=0.001, momentum=0.9, weight_decay=0.001)
    
    # Train the network
    codes = np.random.rand(0, 2)
    labelsCodes = np.random.rand(0)
    
    print('Start Training Auto-Encoder')
    numepochs = 100
    for epoch in tqdm(range(numepochs)):

        running_loss = 0.0
        for i, data in enumerate(trainloader, 0):
            # get the inputs
            inputs, labels = data
            inputs = inputs.to(device)
            labels = labels.to(device)

            # wrap them in Variable
            # la función Variable (obsoleta creo) creo que es similar a poner requires_grad=True
            inputs, labels = Variable(inputs), Variable(labels)

            # zero the parameter gradients
            optimizer.zero_grad()

            # *** forward + backward + optimize
            # forward, out es como el decode, el tamaño del out y code en el ultimo minilote es el excedente, ejemplo si son 100 tuplas con un tamño de batch_size=64, solo se pueden formar dos grupos de minilotes uno de 64 y otro de 32 por tal razon el minilote final es de 32 inputs (imagenes)
            output, code = autoenc(inputs)

            if epoch == numepochs-1:  # solo guardo en la ultima epoca, donde los parametros ya han sido optimizados
                # unifica los code respectivos de cada minilote
                codes = np.concatenate((codes, code.cpu().data), axis=0)

            loss = criterion(inputs, output)
            loss.backward()  # backward
            optimizer.step()  # optimize

    print('Finished Training AE Clasico')

    # ruta = "./model/encoderAssembler.model"
    # torch.save(encEnsemble.state_dict(), ruta)

    return codes