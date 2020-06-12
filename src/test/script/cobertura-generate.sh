#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Only generate a cobertura report
# $@ : MVN_OPTS

echo "cleaning files..."
mvn cobertura:clean
echo "generating cobertura report..."
if [ $# -eq 1 ] && [ $1 == "skip" ]; then
  mvn cobertura:cobertura -Pcobertura
else
  mvn cobertura:cobertura $@
fi
