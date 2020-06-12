#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Only generate a cobertura report
# $@ : MVN_OPTS

echo "cleaning files..."
mvn cobertura:clean $@
rm cobertura.ser 2> /dev/null
echo "generating cobertura report..."
if [ $# -eq 1 ] && [ $1 == "skip" ]; then
  mvn cobertura:cobertura -Dmaven.exec.skip=true
else
  mvn cobertura:cobertura $@
fi
