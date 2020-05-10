#! /bin/sh

# Auteur : @AUTHOR@
# Version initiale : @DATE@

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
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_synt sur $1."
        exit 1
    else
        echo "Succes attendu de test_synt sur $1."
    fi
}

for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
do
    test_synt_invalid "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
    test_synt_valid "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/*.deca
do
    test_synt_valid "$cas_de_test"
done
