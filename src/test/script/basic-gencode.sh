#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Test de compilation et d'exécution de fichier .deca.

# On compile un fichier .deca, on lance ima sur le fichier .ass généré,
# et on compare le résultat avec la valeur attendue dans le fichier .expected.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

# Fonction vérifiant les tests invalides à l'exécution
test_codegen_invalid () {
    # $1 = fichier .deca
    # $2 = fichier .expected

    ass_file="${1%.deca}.ass"

    rm -f $ass_file 2>/dev/null

    decac $1 # On génère le fichier assembleur .ass

    if [ $? -ne 0 ]; then
        echo "[FAILED] $1 : KO (pour la génération de code)"
    elif [ ! -f $ass_file ]; then
        echo "[FAILED] $1 : Fichier .ass non généré."
    else
        resultat=$(ima $ass_file) # On exécute le fichier assembleur

        if [ $? -eq 0 ]; then
            echo "[FAILED] $1 : KO"
        elif [ ! -f $2 ]; then
            echo "[FAILED] $1 : Fichier .expected inexistant"
        else
            # si le test s'est exécuté avec une erreur, on regarde si le résultat obtenu correspond à celui attendu
            if [ "$resultat" = "$(cat $2)" ]; then
                # echo "$1 : PASSED"
                nbpassed=$((nbpassed+1))
            else
                echo "[FAILED] $1 : FAILED"
                echo "Résultat inattendu de ima:"
                echo "$resultat"
            fi
        fi
        rm -f $ass_file
    fi
}

# Fonction vérifiant les tests valides à l'exécution
test_codegen_valid () {
    # $1 = fichier .deca
    # $2 = fichier .expected

    ass_file="${1%.deca}.ass"

    rm -f $ass_file 2>/dev/null

    decac $1 # On génère le fichier assembleur .ass

    if [ $? -ne 0 ]; then
        echo "[FAILED] $1 : KO (pour la génération de code)"
    elif [ ! -f $ass_file ]; then
        echo "[FAILED] $1 : Fichier .ass non généré."
    else
        resultat=$(ima $ass_file) # On exécute le fichier assembleur

        if [ $? -ne 0 ]; then
            echo "[FAILED] $1 : KO (fichier assembleur inexécutable)"
        elif [ ! -f $2 ]; then
            echo "[FAILED] $1 : Fichier .expected inexistant"
        else
            # On regarde si le résultat obtenu correspond à celui attendu
            if [ "$resultat" = "$(cat $2)" ]; then
                # echo "$1 : PASSED"
                nbpassed=$((nbpassed+1))
            else
                echo "[FAILED] $1 : FAILED"
                echo "Résultat inattendu de ima:"
                echo "$resultat"
            fi
        fi
        rm -f $ass_file
    fi
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
# Main

nbtests=0
nbpassed=0

test_step_provided
test_step_renduInitial
test_step_renduInter01
test_step_renduInter02
test_step_renduFinal

echo "##### RESULTS: $nbpassed/$nbtests #####"
