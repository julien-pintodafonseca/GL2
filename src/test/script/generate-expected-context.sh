#! /bin/sh

# Auteur : Equipe GL2
# Date : 2020

# Script utilitaire spécifique
# => Pour utilisateurs avertis

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Generation des fichiers .expected pour les programmes .deca préalablement testés
generate_expected () {
    test_context "$1" > "$2" || nbgenerated=$((nbgenerated-1))
    nbgenerated=$((nbgenerated+1))
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant de générer tous les .expected de l'étape "provided"
generate_step_provided() {
  echo "=== STEP: PROVIDED ==="

  # no src/test/deca/codegen/interactive/provided/

  # no src/test/deca/codegen/invalid/provided/

  echo "### TEST: src/test/deca/codegen/valid/provided/ ###"
  for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
  do
      nbtogenerate=$((nbtogenerate+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/provided/$expected.expected"
      generate_expected "$cas_de_test" "$file"
  done
  echo

  echo "### TEST: src/test/deca/context/valid/provided/ ###"
  for cas_de_test in src/test/deca/context/valid/provided/*.deca
  do
      nbtogenerate=$((nbtogenerate+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/provided/$expected.expected"
      generate_expected "$cas_de_test" "$file"
  done
  echo
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant de générer tous les .expected de l'étape "renduInitial"
generate_step_renduInitial() {
  echo "=== STEP: RENDU_INITIAL ==="

  # no src/test/deca/codegen/interactive/renduInitial/

  # no src/test/deca/codegen/invalid/renduInitial/

  echo "### TEST: src/test/deca/codegen/valid/renduInitial/ ###"
  for cas_de_test in src/test/deca/codegen/valid/renduInitial/*.deca
  do
      nbtogenerate=$((nbtogenerate+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInitial/$expected.expected"
      generate_expected "$cas_de_test" "$file"
  done
  echo

  # no src/test/deca/context/valid/renduInitial/
}

# ----------------------------------------------------------------------------------------------------
# Fonction permettant de générer tous les .expected de l'étape "renduInter01"
generate_step_renduInter01() {
  echo "=== STEP: RENDU_INTER01 ==="

  echo "### TEST: src/test/deca/codegen/interactive/renduInter01/ ###"
  for cas_de_test in src/test/deca/codegen/interactive/renduInter01/*.deca
  do
      nbtogenerate=$((nbtogenerate+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter01/$expected.expected"
      generate_expected "$cas_de_test" "$file"
  done
  echo

  # no src/test/deca/codegen/invalid/renduInter01/

  echo "### TEST: src/test/deca/codegen/valid/renduInter01/ ###"
  for cas_de_test in src/test/deca/codegen/valid/renduInter01/*.deca
  do
      nbtogenerate=$((nbtogenerate+1))
      expected=$(basename $cas_de_test .${cas_de_test##*.})
      file="src/test/deca/context/valid/renduInter01/$expected.expected"
      generate_expected "$cas_de_test" "$file"
  done
  echo

  # no src/test/deca/context/valid/renduInter01/
}

# ----------------------------------------------------------------------------------------------------
# Main

nbtogenerate=0
nbgenerated=0

generate_step_provided
generate_step_renduInitial
generate_step_renduInter01

echo "### RESULTS: $nbgenerated/$nbtogenerate"
