#! /bin/sh

if ! test_affichage > tmp-actual.$$.txt; then
    echo "test_affichage failed"
    cat tmp-actual.$$.txt
    rm -f tmp-actual.$$.txt
    exit 1
fi

sed -e 's/^[ \t]*//' -e 's/[ \t]*$//' -e 's/[ \t][ \t]*/ /' \
    < tmp-actual.$$.txt > tmp-actual-filtered.$$.txt

rm -f tmp-actual.$$.txt

cat > tmp-expect.$$.txt <<EOF
; Debut du programme.
WINT
SETROUND_TONEAREST
SETROUND_DOWNWARD
SETROUND_UPWARD
SETROUND_TOWARDZERO
FMA R2, R3
WFLOATX
; Fin du programme
EOF

if diff -u tmp-expect.$$.txt tmp-actual-filtered.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-actual-filtered.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-actual-filtered.$$.txt
    exit 1
fi
    
