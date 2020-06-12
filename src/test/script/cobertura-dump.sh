#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Generate a cobertura report
# Shows code coverage results directly in terminal
# $@ : MVN_OPTS

echo "cleaning files..."
mvn cobertura:clean
echo "generating cobertura report..."
if [ $# -eq 1 ] && [ $1 == "skip" ]; then
  mvn cobertura:cobertura -Pcobertura
else
  mvn cobertura:cobertura $@
fi
echo "retrieving cobertura results..."
mvn cobertura:dump-datafile
