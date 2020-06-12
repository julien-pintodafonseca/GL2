#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Only generate a cobertura report
# $@ : MVN_OPTS

if [ $# -eq 1 ] && [ $1 == "skip" ]; then
  echo "cleaning files..."
  mvn cobertura:clean
  echo "generating cobertura report..."
  mvn cobertura:cobertura -Pcobertura
else
  echo "cleaning files..."
  mvn cobertura:clean $@
  echo "generating cobertura report..."
  mvn cobertura:cobertura $@
fi
