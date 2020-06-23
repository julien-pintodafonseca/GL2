#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de compilation et d'exécution de fichier .deca.

# On compile un fichier .deca, on lance ima sur le fichier .ass généré,
# et on compare le résultat avec la valeur attendue dans le fichier .expected.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
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

# Fonction vérifiant les tests invalides à l'exécution
test_codegen_invalid () {
    # $1 = fichier .deca
    # $2 = fichier .expected
    # $3 = 1 (optionnel : uniquement si le test est lancé sur un fichier isolé)
    ass_file="${1%.deca}.ass"
    rm -f $ass_file 2>/dev/null
    decac $1 # On génère le fichier assembleur .ass

    # init progress bar
    progress_bar $ass_file 0 $3

    # test
    if [ $? -ne 0 ]; then
        echo
        echo "[FAILED] $1 : KO (pour la génération de code)"
    elif [ ! -f $ass_file ]; then
        echo
        echo "[FAILED] $1 : Fichier .ass non généré."
    else
        resultat=$(ima $ass_file) # On exécute le fichier assembleur

        if [ $? -eq 0 ]; then
            echo
            echo "[FAILED] $1 : KO"
        elif [ ! -f $2 ]; then
            echo
            echo "[FAILED] $1 : Fichier .expected inexistant"
        else
            # si le test s'est exécuté avec une erreur, on regarde si le résultat obtenu correspond à celui attendu
            if [ "$resultat" = "$(cat $2)" ]; then
                # echo "$1 : PASSED"
                nbpassed=$((nbpassed+1))
            else
                echo
                echo "[FAILED] $1 : FAILED"
                echo "Résultat inattendu de ima:"
                echo "$resultat"
            fi
        fi
        rm -f $ass_file
    fi

    # progress bar
    progress_bar $ass_file 1 $3
}

# Fonction vérifiant les tests valides à l'exécution
test_codegen_valid () {
    # $1 = fichier .deca
    # $2 = fichier .expected
    # $3 = 1 (optionnel : uniquement si le test est lancé sur un fichier isolé)
    ass_file="${1%.deca}.ass"
    rm -f $ass_file 2>/dev/null
    decac $1 # On génère le fichier assembleur .ass

    # init progress bar
    progress_bar $ass_file 0 $3

    # test
    if [ $? -ne 0 ]; then
        echo
        echo "[FAILED] $1 : KO (pour la génération de code)"
    elif [ ! -f $ass_file ]; then
        echo
        echo "[FAILED] $1 : Fichier .ass non généré."
    else
        resultat=$(ima $ass_file) # On exécute le fichier assembleur

        if [ $? -ne 0 ]; then
            echo
            echo "[FAILED] $1 : KO (fichier assembleur inexécutable)"
        elif [ ! -f $2 ]; then
            echo
            echo "[FAILED] $1 : Fichier .expected inexistant"
        else
            # On regarde si le résultat obtenu correspond à celui attendu
            if [ "$resultat" = "$(cat $2)" ]; then
                # echo "$1 : PASSED"
                nbpassed=$((nbpassed+1))
            else
                echo
                echo "[FAILED] $1 : FAILED"
                echo "Résultat inattendu de ima:"
                echo "$resultat"
            fi
        fi
        rm -f $ass_file
    fi

    # progress bar
    progress_bar $ass_file 1 $3
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "provided"
test_step_provided() {
  echo "=== STEP: PROVIDED ==="

  # no src/test/deca/context/interactive/provided/

  # echo "--- TEST: src/test/deca/codegen/valid/provided/ ---"
  for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  # echo

  # no src/test/deca/codegen/invalid/provided/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInitial"
test_step_renduInitial() {
  echo "=== STEP: RENDU_INITIAL ==="

  # no src/test/deca/context/interactive/renduInitial/

  # echo "--- TEST: src/test/deca/codegen/valid/renduInitial/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInitial/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  # echo

  # no src/test/deca/codegen/invalid/renduInitial/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter01"
test_step_renduInter01() {
  echo "=== STEP: RENDU_INTER01 ==="

  # Le contenu de src/test/deca/context/interactive/renduInter01/ doit être testé manuellement

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter01/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  #echo

  # no src/test/deca/codegen/invalid/renduInter01/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduInter02"
test_step_renduInter02() {
  echo "=== STEP: RENDU_INTER02 ==="

  # Le contenu de src/test/deca/context/interactive/renduInter02/ doit être testé manuellement

  # echo "--- TEST: src/test/deca/codegen/valid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduInter02/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduInter02/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_invalid "$cas_de_test" "$expected.expected"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "renduFinal"
test_step_renduFinal() {
  echo "=== STEP: RENDU_FINAL ==="

  # Le contenu de src/test/deca/context/interactive/renduFinal/ doit être testé manuellement

  # echo "--- TEST: src/test/deca/codegen/valid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/valid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/renduFinal/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/renduFinal/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_invalid "$cas_de_test" "$expected.expected"
  done
  # echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant d'exécuter tous les tests de l'étape "bibliothequeStandard"
test_step_bibliothequeStandard() {
  echo "=== STEP: BIBLIOTHEQUE_STANDARD ==="

  # no src/test/deca/context/interactive/bibliothequeStandard/

  # echo "--- TEST: src/test/deca/codegen/valid/bibliothequeStandard/ ---"
  for cas_de_test in src/test/deca/codegen/valid/bibliothequeStandard/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_valid "$cas_de_test" "$expected.expected"
  done
  # echo

  # echo "--- TEST: src/test/deca/codegen/invalid/bibliothequeStandard/ ---"
  for cas_de_test in src/test/deca/codegen/invalid/bibliothequeStandard/*.deca
  do
      nbtests=$((nbtests+1))
      expected=${cas_de_test%.deca}
      test_codegen_invalid "$cas_de_test" "$expected.expected"
  done
  # echo
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
