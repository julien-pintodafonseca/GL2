#! /bin/sh

# Auteur : Equipe GL2
# Version initiale : 2020

# Test minimaliste de la syntaxe.
# On lance test_synt sur un fichier valide, et les tests invalides.

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".
#
# Il faudrait aussi lancer ces tests sur tous les fichiers deca
# automatiquement. Un exemple d'automatisation est donné avec une
# boucle for sur les tests invalides, il faut aller encore plus loin.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_synt_invalid () {
    # fonction à modifier plus tard
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1."
    else
        echo "Succes inattendu de test_synt sur $1."
        exit 1
    fi
}

test_synt_valid () {
    # $1 = premier argument.
    # ex : src/test/deca/syntax/valid/01brackets.deca

    path=$(echo $1 | tr '\\' '/')
    #echo $1
    #result=$(test_synt "$1" 2>&1 | tr '\\' '/')
    #echo $result

    cmd=$(test_synt "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    #on exécute notre test en filtrant le message d'erreur, err vaut 1 s'il y a une erreur, 0 sinon
    err=$(test_synt "$path" 2>&1 | tr '\\' '/' | grep -c -e "$path:[0-9][0-9]*:")

    if [ $code -eq 0 ] && [ $err -eq 0 ]
    then
        echo "Succes attendu de test_synt sur $1."
    else
        echo "Echec inattendu pour test_synt sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
    test_synt_valid "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/renduInitial/*.deca
do
    test_synt_valid "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/renduInter01/*.deca
do
    test_synt_valid "$cas_de_test"
done

#for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
#do
#    test_synt_invalid "$cas_de_test"
#done
