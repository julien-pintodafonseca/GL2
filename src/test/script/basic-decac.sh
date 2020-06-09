#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -b a termine avec un status different de zero."
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    echo "ERREUR: decac -b n'a produit aucune sortie"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
    echo "ERREUR: La sortie de decac -b contient erreur ou error"
    exit 1
fi

echo "Pas de probleme detecte avec decac -b."

# ... et ainsi de suite.



var=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`

start=`date +%s%N`
decac_moins_P=$(decac -P $var)
end=`date +%s%N`
runtime=`expr $end - $start`

decac_moins_P=$(decac -P $var)
if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -P a termine avec un status different de zero."
    exit 1
fi

start_1=`date +%s%N`
decac_without_P=$(decac $var)
end_1=`date +%s%N`
runtime_without_P=`expr $end_1 - $start_1`

if [ $runtime -gt $runtime_without_P ]; then
    echo "Maybe Something wrong: decac -P take more time than decac"
    exit 1
fi

echo "The time of execution with p is $runtime"
echo "The time of execution without p is $runtime_without_P"

echo "Pas de probleme detecte avec decac -P."

