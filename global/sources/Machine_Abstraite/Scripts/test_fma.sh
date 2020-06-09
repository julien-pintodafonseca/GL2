#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima fma.ass > tmp-horner.$$.txt

cat > tmp-expect.$$.txt <<EOF
Resultat :-5.00821e+06 -0x1.31ad4p+22
Resultat FMA :-5.00821e+06 -0x1.31ad44p+22
Différence : 1.00000e+00
EOF

if diff -u tmp-expect.$$.txt tmp-horner.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-horner.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-horner.$$.txt
    exit 1
fi
    
