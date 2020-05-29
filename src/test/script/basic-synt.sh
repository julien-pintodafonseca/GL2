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
test_synt_invalid_advanced () {
    # $1 = premier argument.
    # $2 = deuxième argument

    path=$(echo $1 | tr '\\' '/')

    cmd=$(test_synt "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    #on exécute notre test en filtrant le message d'erreur, err vaut 1 s'il y a une erreur, 0 sinon
    err=$(test_synt "$path" 2>&1 | tr '\\' '/' | grep -c -e "$path:[0-9][0-9]*:")
    file=${2%*.expected}
    if [ $code -eq 0 ] && [ $err -eq 0 ]
    then
        echo "$1 : KO"
    else
        test_synt "$path" 2>&1 | grep ".deca" > "$file.res"

        if grep "$(cat $2)" "$file.res" > /dev/null ; then
            echo "$1 : PASSED."
            ((nbpassed++))
        else
            echo "$1 : FAILED."
        fi
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

test_synt_valid_advanced () {
    # $1 = premier argument.
    # $2 = deuxième argument

    path=$(echo $1 | tr '\\' '/')

    cmd=$(test_synt "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    #on exécute notre test en filtrant le message d'erreur, err vaut 1 s'il y a une erreur, 0 sinon
    err=$(test_synt "$path" 2>&1 | tr '\\' '/' | grep -c -e "$path:[0-9][0-9]*:")
    file=${2%*.expected}
    if [ $code -eq 0 ] && [ $err -eq 0 ]
    then
        test_synt "$path" > "$file.res"

        if diff -q "$file.res" "$2" > /dev/null ; then
            echo "$1 : PASSED."
            ((nbpassed++))
        else
            echo "$1 : FAILED."
        fi
    else
        echo "$1 : KO"
    fi
}

#for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
#do
#    test_synt_valid "$cas_de_test"
#done

#for cas_de_test in src/test/deca/syntax/valid/renduInitial/*.deca
#do
#    test_synt_valid "$cas_de_test"
#done

nbtests=0
nbpassed=0
echo "### TEST: src/test/deca/codegen/valid/renduInter01/ ###"
for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
do
    ((nbtests++))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid_advanced "$cas_de_test" "$file"
done

echo "### TEST: src/test/deca/codegen/interactive/renduInter01/ ###"
for cas_de_test in src/test/deca/codegen/interactive/renduInter01/*.deca
do
    ((nbtests++))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid_advanced "$cas_de_test" "$file"
done

echo "### TEST: src/test/deca/context/invalid/renduInter01/ ###"
for cas_de_test in src/test/deca/context/invalid/renduInter01/*.deca
do
    ((nbtests++))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid_advanced "$cas_de_test" "$file"
done

echo "### TEST: src/test/deca/syntax/invalid/renduInter01/ ###"
for cas_de_test in src/test/deca/syntax/invalid/renduInter01/*.deca
do
    ((nbtests++))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/invalid/renduInter01/$expected"
    test_synt_invalid_advanced "$cas_de_test"  "$file.expected"
done
echo "$nbpassed/$nbtests"
