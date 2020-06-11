#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Generate a cobertura report
# Shows code coverage results directly in terminal

echo "cleaning files..."
oui=$(mvn cobertura:clean)
oui=$(rm cobertura.ser 2> /dev/null)
echo "generating cobertura report..."
oui=$(mvn cobertura:cobertura)
echo "retrieving cobertura results..."
mvn cobertura:dump-datafile
