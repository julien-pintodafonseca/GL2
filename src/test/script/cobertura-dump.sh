#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Shows cobertura code coverage directly in terminal

echo "cleaning files..."
oui=$(mvn cobertura:clean)
echo "generating cobertura report..."
oui=$(mvn cobertura:cobertura)
echo "retrieving cobertura results..."
mvn cobertura:dump-datafile
