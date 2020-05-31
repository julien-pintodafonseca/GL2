#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de compilation et d'exécution de fichier .deca.

# On compile un fichier .deca, on lance ima sur le fichier .ass généré,
# et on compare le résultat avec la valeur attendue dans le fichier .expected.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

test_codegen () {
    # $1 = fichier .deca
    # $2 = fichier .expected

    ass_file="${1%.deca}.ass"

    rm -f $ass_file 2>/dev/null

    decac $1 # On génère le fichier assembleur .ass

    if [ $? -ne 0 ] ; then
        echo "$1 : KO (pour la génération de code)"
    elif [ ! -f $ass_file ]; then
        echo "Fichier cond0.ass non généré."
    else
        resultat=$(ima $ass_file) # On exécute le fichier assembleur

        if [ $? -ne 0 ] ; then
            echo "$1 : KO (fichier assembleur inexécutable)"
        elif [ ! -f $2 ]; then
            echo "$1 : Fichier .expected inexistant"
        else
            # On regarde si le résultat obtenu correspond à celui attendu
            if [ "$resultat" = "$(cat $2)" ]; then
                nbpassed=$((nbpassed+1))
                echo "$1 : PASSED"
            else
                echo "$1 : FAILED"
                echo "Résultat inattendu de ima:"
                echo "$resultat"
            fi
        fi
        rm -f $ass_file
    fi
}


nbtests=0
nbpassed=0
echo "=== STEP: PROVIDED ==="

# no src/test/deca/context/interactive/renduInter01/

echo "### TEST: src/test/deca/codegen/valid/provided/ ###"
for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
    nbtests=$((nbtests+1))
    expected=${cas_de_test%.deca}
    test_codegen "$cas_de_test" "$expected.expected"
done
echo

# no src/test/deca/codegen/invalid/provided/

# ----------------------------------------------------------------------------------------------------
echo "=== STEP: RENDU_INITIAL ==="

# no src/test/deca/context/interactive/renduInitial/

echo "### TEST: src/test/deca/codegen/valid/renduInitial/ ###"
for cas_de_test in src/test/deca/codegen/valid/renduInitial/*.deca
do
    nbtests=$((nbtests+1))
    expected=${cas_de_test%.deca}
    test_codegen "$cas_de_test" "$expected.expected"
done
echo

# no src/test/deca/codegen/invalid/renduInitial/

# ----------------------------------------------------------------------------------------------------
echo "=== STEP: RENDU_INTER01 ==="

# Le contenu de src/test/deca/context/interactive/renduInitial/ doit être testé manuellement

echo "### TEST: src/test/deca/codegen/valid/renduInter01/ ###"
for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
do
    nbtests=$((nbtests+1))
    expected=${cas_de_test%.deca}
    test_codegen "$cas_de_test" "$expected.expected"
done
echo

# no src/test/deca/codegen/invalid/renduInter01/

echo "### RESULTS: $nbpassed/$nbtests"
