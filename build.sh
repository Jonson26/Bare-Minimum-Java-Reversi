#!/bin/bash

arg="$1"

if [ $arg = "clean" ]; then
    clean=1
    build=0
    run=0
    package=0
fi

if [ $arg = "build" ]; then
    clean=0
    build=1
    run=0
    package=0
fi

if [ $arg = "buildrun" ]; then
    clean=0
    build=1
    run=1
    package=0
fi

if [ $arg = "run" ]; then
    clean=0
    build=0
    run=1
    package=0
fi

if [ $arg = "package" ]; then
    clean=0
    build=0
    run=0
    package=1
fi

if [ $arg = "all" ]; then
    clean=1
    build=1
    run=0
    package=1
fi

if [ $clean = 1 ]; then
    echo "Cleaning"
    cd build
    rm -v *.class
    rm -v *.jar
    cd ..
fi

if [ $build = 1 ]; then
    echo "Compiling"
    javac -d ./build ./src/*.java
fi

if [ $run = 1 ]; then
    echo "Running"
    cd build
    java Reversi
fi

if [ $package = 1 ]; then
    echo "Packaging"
    cd build
    jar cvfm Reversi.jar manifest.txt *.class img
fi
