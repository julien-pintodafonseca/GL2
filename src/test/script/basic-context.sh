#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de la vérification contextuelle.
# On lance test_context sur les fichier .deca valides ou invalides
#
# Dans le cas d'un fichier valide, on teste qu'il n'y a pas d'erreur.
# Ensuite, on compare l'arbre généré avec le résultat attendu.
#
# Dans le cas d'un fichier invalide, on teste que la commande test_context retourne bien une erreur (code de retour != 0).
# Ensuite, on vérifie que l'erreur générée correspond bien à l'erreur attendue.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
PROGRESS=-1

DEFAULT='\033[0m'
RED='\033[0;31m'
BROWN='\033[0;33m'
GREEN='\033[0;32m'

# Barre de progression
progress_bar () {
    path=$1
    state=$2
    unique=$3

    # shopt -s nullglob
    folder="${path%/*}/*.deca"

    TOTAL=$(find ${path%/*} -type f -name "*.deca" | wc -l)
    if [ -n "$3" ] && [ "$unique" -eq "1" ]; then
        TOTAL=1
    fi

    TEXT=$folder
    if [ "$TOTAL" -eq "1" ]; then
        TEXT=${path}
    fi

    if [ "$2" -eq "1" ]; then
        if [ "$PROGRESS" -eq "-1" ]; then
            PROGRESS=1
        else
            PROGRESS=$(( $PROGRESS + 1 ))
        fi

        pd=$(( $PROGRESS * 73 / $TOTAL ))
        d1=$(( $PROGRESS * 100 / $TOTAL ))
        d2=$(( ($PROGRESS * 1000 / $TOTAL) % 10 ))
        if [ "$d1" -lt "30" ]; then
            printf "${RED}\r%3d.%1d%% %.${pd}s${DEFAULT} - $TEXT" $d1 $d2
        elif [ "$d1" -lt "100" ]; then
            printf "${BROWN}\r%3d.%1d%% %.${pd}s${DEFAULT} - $TEXT" $d1 $d2
        else
            printf "${GREEN}\r%3d.%1d%% %.${pd}s${DEFAULT} - $TEXT" $d1 $d2
        fi
        if [ "$PROGRESS" -eq "$TOTAL" ]; then
            echo
            PROGRESS=0
        fi
    else
        if [ "$PROGRESS" -eq "-1" ] || [ "$PROGRESS" -eq "0" ]; then
            printf "${RED}\r%3d.%1d%% %.${pd}s${DEFAULT} - $TEXT" $(( 0 * 100 / $TOTAL )) $(( (0 * 1000 / $TOTAL) % 10 ))
        fi
    fi
}

# Fonction vérifiant les tests invalides contextuellement
test_context_invalid () {
    # $1 = fichier .deca
    # $2 = fichier .expected
    # $3 = 1 (optionnel : uniquement si le test est lancé sur un fichier isolé)
    path=$(echo $1 | tr '\\' '/')
    cmd=$(test_context "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    # init progress bar
    progress_bar $path 0 $3

    # test
    if [ $code -eq 0 ]; then
        echo
        echo "[FAILED] $1 : KO"
    else
        # si le test s'est exécuté avec une erreur, on regarde si l'erreur générée correspond à celle attendue
        file=${2%*.expected}
        test_context "$path" 2> "$file.res" > /dev/null

        if [ -f $2 ]; then
            if grep "$(cat $2)" "$file.res" > /dev/null ; then
                # echo "$1 : PASSED."
                nbpassed=$((nbpassed+1))
            else
                echo
                echo "[FAILED] $1 : FAILED."
                echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
            fi
        else
            echo
            echo "[FAILED] $1 : Fichier .expected inexistant"
        fi
    fi

    # progress bar
    progress_bar $path 1 $3
}

# Fonction vérifiant les tests valides contextuellement
test_context_valid () {
    # $1 = fichier .deca
    # $2 = fichier .expected
    # $3 = 1 (optionnel : uniquement si le test est lancé sur un fichier isolé)
    path=$(echo $1 | tr '\\' '/')
    cmd=$(test_context "$path" 2>&1) # on exécute notre test
    code=$? # si code vaut 0 alors succès, sinon échec

    # init progress bar
    progress_bar $path 0 $3

    # test
    if [ $code -eq 0 ]; then
        # si le test s'est exécuté sans erreur, on regarde si le résultat généré correspond à celui attendu
        file=${2%*.expected}
        test_context "$path" > "$file.res"

        if [ -f $2 ]; then
            if grep "$(cat $2)" "$file.res" > /dev/null ; then
                # echo "$1 : PASSED."
                nbpassed=$((nbpassed+1))
            else
                echo
                echo "[FAILED] $1 : FAILED."
                diff "$2" "${file}.res"
            fi
        else
            echo
            echo "[FAILED] $1 : Fichier .expected inexistant"
        fi
    else
        echo
        echo "[FAILED] $1 : KO"
    fi

    # progress bar
    progress_bar $path 1 $3
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "provided"
test_step_provided() {
  echo "=== STEP: PROVIDED ==="

  # no src/test/deca/codegen/interactive/provided/

  # no src/test/deca/codegen/invalid/provided/

  # echo "--- TEST: src/test/deca/codegen/valid/provided/ ---"
  for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/provided/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/valid/provided/ ---"
  for cas_de_test in src/test/deca/context/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/provided/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/provided/ ---"
  for cas_de_test in src/test/deca/context/invalid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/invalid/provided/$expected.expected"
      test_context_invalid "$cas_de_test" "$file"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInitial"
test_step_renduInitial() {
  echo "=== STEP: RENDU_INITIAL ==="

  # no src/test/deca/codegen/interactive/renduInitial/

  # no src/test/deca/codegen/invalid/renduInitial/

  # echo "--- TEST: src/test/deca/codegen/valid/renduInitial/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInitial/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # no src/test/deca/context/valid/renduInitial/

  # echo "--- TEST: src/test/deca/context/invalid/renduInitial/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/invalid/renduInitial/$expected.expected"
      test_context_invalid "$cas_de_test" "$file"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter01"
test_step_renduInter01() {
  echo "=== STEP: RENDU_INTER01 ==="

  # echo "--- TEST: src/test/deca/codegen/interactive/renduInter01/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter01/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # no src/test/deca/codegen/invalid/renduInter01/

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter01/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter01/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # no src/test/deca/context/valid/renduInter01/

  # echo "--- TEST: src/test/deca/context/invalid/renduInter01/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/invalid/renduInter01/$expected.expected"
      test_context_invalid "$cas_de_test" "$file"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter02"
test_step_renduInter02() {
  echo "=== STEP: RENDU_INTER02 ==="

  # echo "--- TEST: src/test/deca/codegen/interactive/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter02/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter02/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter02/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # no src/test/deca/context/valid/renduInter02/

  # echo "--- TEST: src/test/deca/context/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/invalid/renduInter02/$expected.expected"
      test_context_invalid "$cas_de_test" "$file"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduFinal"
test_step_renduFinal() {
  echo "=== STEP: RENDU_FINAL ==="

  # echo "--- TEST: src/test/deca/codegen/interactive/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduFinal/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduFinal/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/valid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduFinal/$expected.expected"
      test_context_valid "$cas_de_test" "$file"
  done
  # echo

  # no src/test/deca/context/valid/renduFinal/

  # echo "--- TEST: src/test/deca/context/invalid/renduFinal/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/invalid/renduFinal/$expected.expected"
      test_context_invalid "$cas_de_test" "$file"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "bibliothequeStandard"
test_step_bibliothequeStandard() {
  echo "=== STEP: BIBLIOTHEQUE_STANDARD ==="
  nbtests=$((nbtests+1))
  deca="src/test/deca/codegen/valid/bibliothequeStandard/00helloWorld.deca"
  expected="src/test/deca/context/valid/bibliothequeStandard/00helloWorld.expected"
  test_context_valid "$deca" "$expected" 1
}

# ----------------------------------------------------------------------------------------------------
# Main

nbtests=0
nbpassed=0

test_step_provided
test_step_renduInitial
test_step_renduInter01
test_step_renduInter02
test_step_renduFinal
test_step_bibliothequeStandard

echo "##### RESULTS: $nbpassed/$nbtests #####"
