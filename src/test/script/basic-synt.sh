#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de la syntaxe.
# On lance test_synt sur les fichier .deca valides ou invalides
#
# Dans le cas d'un fichier valide, on teste qu'il n'y a pas d'erreur.
# Ensuite, on compare l'arbre généré avec le résultat attendu.
#
# Dans le cas d'un fichier invalide, on teste que la commande test_synt retourne bien une erreur (code de retour != 0).
# Ensuite, on vérifie que l'erreur générée correspond bien à l'erreur attendue.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Fonction vérifiant les tests invalides syntaxiquement
test_synt_invalid () {
    # $1 = fichier .deca
    # $2 = fichier .expected

    path=$(echo $1 | tr '\\' '/')

    cmd=$(test_synt "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    if [ $code -eq 0 ]
    then
        echo "$1 : KO"
    else
        if [ ! -f $2 ]; then
            echo "$1 : Fichier .expected inexistant"
        else
            # On regarde si le résultat obtenu correspond à celui attendu
            file=${2%*.expected}
            test_synt "$path" 2>&1 | grep ".deca" > "$file.res"

            if grep "$(cat $2)" "$file.res" > /dev/null ; then
                echo "$1 : PASSED."
                nbpassed=$((nbpassed+1))
            else
                echo "$1 : FAILED."
            fi
        fi
    fi
}

# Fonction vérifiant les tests valides syntaxiquement
test_synt_valid () {
    # $1 = fichier .deca
    # $2 = fichier .expected

    path=$(echo $1 | tr '\\' '/')

    cmd=$(test_synt "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    if [ $code -eq 0 ]
    then
        if [ ! -f $2 ]; then
            echo "$1 : Fichier .expected inexistant"
        else
            # On regarde si le résultat obtenu correspond à celui attendu
             file=${2%*.expected}
            test_synt "$path" > "$file.res"

            if diff -q "$file.res" "$2" > /dev/null ; then
                echo "$1 : PASSED."
                nbpassed=$((nbpassed+1))
            else
                echo "$1 : FAILED."
            fi
        fi
    else
        echo "$1 : KO"
    fi
}

#for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
#do
#    nbtests=$((nbtests+1))
#    expected=$(basename $cas_de_test .${cas_de_test##*.})
#    file="src/test/deca/syntax/valid/provided/$expected.expected"
#    test_synt_valid "$cas_de_test"
#done

#for cas_de_test in src/test/deca/syntax/valid/renduInitial/*.deca
#do
#    nbtests=$((nbtests+1))
#    expected=$(basename $cas_de_test .${cas_de_test##*.})
#    file="src/test/deca/syntax/valid/renduInitial/$expected.expected"
#    test_synt_valid "$cas_de_test"
#done
#
# ...

nbtests=0
nbpassed=0
echo "### TEST: src/test/deca/codegen/valid/renduInter01/ ###"
for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
do
    nbtests=$((nbtests+1))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid "$cas_de_test" "$file"
done
echo

echo "### TEST: src/test/deca/codegen/interactive/renduInter01/ ###"
for cas_de_test in src/test/deca/codegen/interactive/renduInter01/*.deca
do
    nbtests=$((nbtests+1))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid "$cas_de_test" "$file"
done
echo

echo "### TEST: src/test/deca/context/invalid/renduInter01/ ###"
for cas_de_test in src/test/deca/context/invalid/renduInter01/*.deca
do
    nbtests=$((nbtests+1))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/valid/renduInter01/$expected.expected"
    test_synt_valid "$cas_de_test" "$file"
done
echo

echo "### TEST: src/test/deca/syntax/invalid/renduInter01/ ###"
for cas_de_test in src/test/deca/syntax/invalid/renduInter01/*.deca
do
    nbtests=$((nbtests+1))
    expected=$(basename $cas_de_test .${cas_de_test##*.})
    file="src/test/deca/syntax/invalid/renduInter01/$expected"
    test_synt_invalid "$cas_de_test"  "$file.expected"
done
echo

echo "$nbpassed/$nbtests"
