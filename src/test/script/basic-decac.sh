#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

decac_moins_b=$(decac -b)

# ----------- Option -b -------------
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


# ----------- Option -P -------------
var=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

#start=`date +%s%N`
#decac_moins_P=$(decac -P $var)
#end=`date +%s%N`
#runtime=`expr $end - $start`

decac_moins_P=$(decac -P $var)
if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -P a termine avec un status different de zero."
    exit 1
fi

#start_1=`date +%s%N`
#decac_without_P=$(decac $var)
#end_1=`date +%s%N`
#runtime_without_P=`expr $end_1 - $start_1`

#if [ $runtime -gt $runtime_without_P ]; then
#    echo "Maybe Something wrong: decac -P take more time than decac"
#    exit 1
#fi

#echo "The time of execution with p is $runtime"
#echo "The time of execution without p is $runtime_without_P"

echo "Pas de probleme detecte avec decac -P."



# ----------- Option -p -------------
#var0=`ls ./src/test/deca/codegen/valid/provided/*.deca`
var1=`ls ./src/test/deca/codegen/valid/renduInitial/*.deca`
var2=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`
var3=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`
#var="${var1} ${var2} ${var3}"
#decac_moins_p=$(decac -p $var)
#echo $var

for var in ${var1} ${var2} ${var3}
do
    decac_moins_p=$(decac -p $var)
    if [ "$?" -ne 0 ]; then
        echo "ERREUR: decac -p a termine avec un status different de zero."
        exit 1
    fi

    if [ "$decac_moins_p" = "" ]; then
        echo "ERREUR: decac -p n'a produit aucune sortie"
        exit 1
    fi
done

var_invalid_0=`ls src/test/deca/syntax/invalid/renduInter01/*.deca`
var_invalid_1=`ls src/test/deca/syntax/invalid/renduInter02/*.deca`

for var_1 in ${var_invalid_0} ${var_invalid_1}
do
    decac_moins_p=$(decac -p var1 >> /dev/null 2>&1)
    if [ "$?" -eq 0 ]; then
        echo "ERREUR: decac -p a termine avec un status de zero (avec les fichiers invalids)."
        exit 1
    fi
done



echo "Pas de probleme detecte avec decac -p."

# ----------- Option -v -------------

#var0=`ls ./src/test/deca/codegen/valid/provided/*.deca`
var1=`ls ./src/test/deca/codegen/valid/renduInitial/*.deca`
var2=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`
var3=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

for var in ${var1} ${var2} ${var3}
do
    decac_moins_v=$(decac -v $var)
    if [ "$?" -ne 0 ]; then
        echo "ERREUR: decac -v a termine avec un status different de zero."
        exit 1
    fi

    if [ "$decac_moins_v" != "" ]; then
        echo "ERREUR: decac -v a produit une sortie"
        exit 1
    fi
done

echo "Pas de probleme detecte avec decac -v."

# ----------- Option -r X -------------

#var0=`ls ./src/test/deca/codegen/valid/provided/*.deca`
var1=`ls ./src/test/deca/codegen/valid/renduInitial/*.deca`
var2=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`
var3=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

for num in 5 6 7 8 9 10 11 12 13 14 15 16
do
    for var in ${var1} ${var2} ${var3}
    do
        decac_moins_r_X=$(decac -r $num $var)
        if [ "$?" -ne 0 ]; then
            echo "ERREUR: decac -r X a termine avec un status different de zero."
            exit 1
        fi

        if [ "$decac_moins_r_X" != "" ]; then
            echo "ERREUR: decac -r X a produit une sortie"
            exit 1
        fi
    done
done

echo "Pas de probleme detecte avec decac -r X."