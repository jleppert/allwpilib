#!/bin/bash

SCRIPT_DIR=$( cd ${0%/*} && pwd -P )

mkdir -p $SCRIPT_DIR/build
cd $SCRIPT_DIR/build

cmake .. -DWITH_JAVA=OFF -DWITH_TESTS=OFF -DWITH_CSCORE=OFF -DWITH_SIMULATION_MODULES=OFF -DWITH_GUI=OFF

make -j $(nproc --all)

make install
