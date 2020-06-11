#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de l'interface en ligne de commande de decac.

PATH=./src/main/bin:"$PATH"

# ----------------- Option -b -----------------

decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then
    echo "[ERROR] decac -b a termine avec un status different de zero."
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    echo "[ERROR] decac -b n'a produit aucune sortie"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error" ; then
    echo "[ERROR] La sortie de decac -b contient erreur ou error"
    exit 1
fi

echo "Pas de probleme detecte avec decac -b."

# ----------------- Option -P -----------------

var=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

decac_moins_P=$(decac -P $var)
if [ "$?" -ne 0 ]; then
    echo "[ERROR] decac -P a termine avec un status different de zero."
    exit 1
fi

if [ "$decac_moins_P" != "" ]; then
        echo "[ERROR] decac -P a produit une sortie"
        exit 1
fi

echo "Pas de probleme detecte avec decac -P."

# ----------------- Option -p -----------------

#var0=`ls ./src/test/deca/codegen/valid/provided/*.deca`
var1=`ls ./src/test/deca/codegen/valid/renduInitial/*.deca`
var2=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`
var3=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

for var in ${var1} ${var2} ${var3}
do
    file=$(basename $var)
    if [ "$file" -ne "40empty_main.deca" ]; then
        decac_moins_p=$(decac -p $var)
        if [ "$?" -ne 0 ]; then
            echo "[ERROR] decac -p a termine avec un status different de zero."
            exit 1
        fi

        if [ "$decac_moins_p" = "" ]; then
            echo "[ERROR] decac -p n'a produit aucune sortie"
            exit 1
        fi
    fi
done

var_invalid_0=`ls src/test/deca/syntax/invalid/renduInter01/*.deca`
var_invalid_1=`ls src/test/deca/syntax/invalid/renduInter02/*.deca`

for var_1 in ${var_invalid_0} ${var_invalid_1}
do
    decac_moins_p=$(decac -p var1 >> /dev/null 2>&1)
    if [ "$?" -eq 0 ]; then
        echo "[ERROR] decac -p a termine avec un status de zero (avec les fichiers invalides)."
        exit 1
    fi
done

echo "Pas de probleme detecte avec decac -p."

# ----------------- Option -v -----------------

#var0=`ls ./src/test/deca/codegen/valid/provided/*.deca`
var1=`ls ./src/test/deca/codegen/valid/renduInitial/*.deca`
var2=`ls ./src/test/deca/codegen/valid/renduInter01/*.deca`
var3=`ls ./src/test/deca/codegen/valid/renduInter02/*.deca`

for var in ${var1} ${var2} ${var3}
do
    decac_moins_v=$(decac -v $var)
    if [ "$?" -ne 0 ]; then
        echo "[ERROR] decac -v a termine avec un status different de zero."
        exit 1
    fi

    if [ "$decac_moins_v" != "" ]; then
        echo "[ERROR] decac -v a produit une sortie"
        exit 1
    fi
done

echo "Pas de probleme detecte avec decac -v."

# --------------- Option -r X -----------------

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
            echo "[ERROR] decac -r X a termine avec un status different de zero."
            exit 1
        fi

        if [ "$decac_moins_r_X" != "" ]; then
            echo "[ERROR] decac -r X a produit une sortie"
            exit 1
        fi
    done
done

echo "Pas de probleme detecte avec decac -r X."
