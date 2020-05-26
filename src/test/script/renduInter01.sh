#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

echo "### INFO: recompiling java files for $decac ###" 
cd "$(dirname "${decac}")/../../.."
mvn compile || exit 1

nbtests=0
nbpassed=0

source="${root}/codegen/valid/renduInter01"
resultat="${root}/syntax/valid/renduInter01"
echo "### TEST: $(pwd) ###"
#rm -fR *.res *.ass || exit 1
for f in ${source}/*.deca ; do
    file="${f%.deca}"
    ((nbtests++))
    test_synt "${source}/${f}.deca" > "${resultat}/${file}.res"
    if [ -f "${resultat}/${file}.res" ]; then
      if diff -q "${resultat}/${f}.expected" "${resultat}/${f}.res" > /dev/null ; then
    	    echo "--- ${file}: PASSED ---"
    	    ((nbpassed++))
    	else
  	    cat ${file}.res
  	    echo "--- ${file}: FAILED ---"
  	    diff "${file}.expected" "${file}.res"
      fi
    else
    	echo "--- ${file}: KO ---"
    fi
    echo
done

cd "${root}/context/invalid/renduInter01"
echo "### TEST: $(pwd) ###"
rm -f *.res *.ass || exit 1
for f in *.deca ; do
    file="${f%.deca}"
    ((nbtests++))
    if decac "${f}" 2> "${file}.res" ; then
	    echo "--- ${file}: KO ---"
	    string=`cat "${file}.expected" > /dev/null`;
    elif grep string "${file}.res" > /dev/null ; then
	    echo "--- ${file}: PASSED ---"
	    ((nbpassed++))
    else
	    echo "--- ${file}: FAILED ---"
	    echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
	    echo "IN \"$(cat ${file}.res)\""
    fi
    echo
done

cd "${root}/codegen/valid/renduInter01"
echo "### TEST: $(pwd) ###"
rm -f *.res *.ass || exit 1
for f in *.deca ; do
    file="${f%.deca}"
    ((nbtests++))
    decac "${f}" && (ima "${file}.ass" > "${file}.res")
    if [ -f "${file}.res" ]; then 
	    if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
  	    echo "--- ${file}: PASSED ---"
  	    ((nbpassed++))
    	else
  	    cat ${file}.res
  	    echo "--- ${file}: FAILED ---"
  	    diff "${file}.expected" "${file}.res"
    	fi
    else
    	echo "--- ${file}: KO ---"
    fi
    echo
done


echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"
