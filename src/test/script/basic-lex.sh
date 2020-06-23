#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# On lance test_lex_valid sur les fichier .deca valides ou invalides
#
# Dans le cas d'un fichier valide, on teste qu'il n'y a pas d'erreur.
#
# Dans le cas d'un fichier invalide, on teste que la commande test_synt retourne bien une erreur (code de retour != 0).

# On se place dans le répertoire du projet (quel que soit le répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' #no color

progressBar () {
    # Process data
    _progress=$(((${1}*100/${2}*100)/100))
    _done=$(( (${_progress}*4)/10))
    _left=$((40-$_done))
    
    # Build progressbar strings and print the ProgressBar line
    printf "\rProgress : [${GREEN}$(for i in $(seq ${_done}); do echo -n "#"; done)${RED}$(for i in $(seq ${_left}); do echo -n "-"; done)${NC}]${_progress}%%"
}

# Fonction vérifiant les tests valides lexicalement
test_lex_valid () {
    # $1 = fichier .deca

    path=$(echo $1 | tr '\\' '/')
    file=$(basename $path)

    if test_lex "$path" 2>&1 | tail -n 1 | grep -q "$file:[0-9]" ; then
        echo "[FAILED] $1 : FAILED"
    else
        # echo "$1 : OK"
        nbpassed=$((nbpassed+1))
    fi
    progressBar $nbpassed $TOTAL
}

# Fonction vérifiant les test invalides lexicalement
test_lex_invalid () {
    # $1 = fichier .deca

    path=$(echo $1 | tr '\\' '/')
    file=$(basename $path)

    if test_lex "$path" 2>&1 | tail -n 1 | grep -q "$file:[0-9]" ; then
        # echo "$1 : OK"
        nbpassed=$((nbpassed+1))
    else
        echo "[FAILED] $1 : FAILED"
    fi
    progressBar $nbpassed $TOTAL
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "provided"
test_step_provided() {
  echo "\r=== STEP: PROVIDED ===                                           "
  progressBar $nbpassed $TOTAL
  # no src/test/deca/codegen/interactive/provided/

  # no src/test/deca/codegen/invalid/provided/

  # echo "--- TEST: src/test/deca/codegen/valid/provided/ ---"
  for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/provided/ ---"
  for cas_de_test in src/test/deca/context/invalid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/valid/provided/ ---"
  for cas_de_test in src/test/deca/context/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/syntax/valid/provided/ ---"
  for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/syntax/invalid/provided/ ---"
  nbtests=$((nbtests+1))
  test_lex_valid "src/test/deca/syntax/valid/provided/simple_lex.deca"
  nbtests=$((nbtests+1))
  test_lex_invalid "src/test/deca/syntax/invalid/provided/chaine_incomplete.deca"
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInitial"
test_step_renduInitial() {
  echo "\r=== STEP: RENDU_INITIAL ===                                              "
  progressBar $nbpassed $TOTAL
  # no src/test/deca/codegen/interactive/renduInitial/

  # no src/test/deca/codegen/invalid/renduInitial/

  # echo "--- TEST: src/test/deca/codegen/valid/renduInitial/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/renduInitial/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/context/valid/renduInitial/

  # echo "--- TEST: src/test/deca/syntax/valid/renduInitial/ ---"
  for cas_de_test in src/test/deca/syntax/valid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/syntax/invalid/renduInitial/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter01"
test_step_renduInter01() {
  echo "\r=== STEP: RENDU_INTER01 ===                                        "
  progressBar $nbpassed $TOTAL
  # echo "--- TEST: src/test/deca/codegen/interactive/renduInter01/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/codegen/invalid/renduInter01/

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter01/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/renduInter01/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/context/valid/renduInter01/

  # no src/test/deca/syntax/valid/renduInter01/

  # echo "--- TEST: src/test/deca/syntax/invalid/renduInter01/ ---"
  for cas_de_test in src/test/deca/syntax/invalid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter02"
test_step_renduInter02() {
  echo "\r=== STEP: RENDU_INTER02 ===                                          "
  progressBar $nbpassed $TOTAL
  # echo "--- TEST: src/test/deca/codegen/interactive/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/context/valid/renduInter01/

  # no src/test/deca/syntax/valid/renduInter01/

  # echo "--- TEST: src/test/deca/syntax/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/syntax/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduFinal"
test_step_renduFinal() {
  echo "\r=== STEP: RENDU_FINAL ===                                      "
  progressBar $nbpassed $TOTAL
  # echo "--- TEST: src/test/deca/codegen/interactive/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/interactive/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/valid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # echo "--- TEST: src/test/deca/context/invalid/renduFinal/ ---"
  for cas_de_test in src/test/deca/context/invalid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      test_lex_valid "$cas_de_test"
  done
  # echo

  # no src/test/deca/context/valid/renduFinal/

  # no src/test/deca/syntax/valid/renduFinal/

  # no src/test/deca/syntax/invalid/renduFinal/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduFinal"
test_step_bibliothequeStandard() {
  nbtests=$((nbtests+1))
    deca="src/test/deca/codegen/valid/bibliothequeStandard/00helloWorld.deca"
    test_lex_valid "$deca"
}

# ----------------------------------------------------------------------------------------------------
# Main
TOTAL=$(find ./src/test/deca/ -name "*.deca" -type f | wc -l )
TOTAL=220
nbtests=0
nbpassed=0

test_step_provided
test_step_renduInitial
test_step_renduInter01
test_step_renduInter02
test_step_renduFinal
test_step_bibliothequeStandard

echo " "
echo "##### RESULTS: $nbpassed/$nbtests #####"
