#!/usr/bin/env bash

# See http://yann.lecun.com/exdb/mnist/

mkdir -p build/3rd-party/MNIST
cd build/3rd-party/MNIST

wget -nv -N http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz
wget -nv -N http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz
wget -nv -N http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz
wget -nv -N http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz

gunzip -fk *.gz
