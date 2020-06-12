#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Generate a cobertura report
# Shows code coverage results directly in terminal
# $@ : MVN_OPTS

if [ $# -eq 1 ] && [ $1 == "skip" ]; then
  echo "cleaning files..."
  mvn cobertura:clean
  echo "generating cobertura report..."
  mvn cobertura:cobertura -Pcobertura
  echo "retrieving cobertura results..."
  mvn cobertura:dump-datafile
else
  echo "cleaning files..."
  mvn cobertura:clean $@
  echo "generating cobertura report..."
  mvn cobertura:cobertura $@
  echo "retrieving cobertura results..."
  mvn cobertura:dump-datafile $@
fi
