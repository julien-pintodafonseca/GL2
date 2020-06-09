#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima floatx.ass > tmp-floatx.$$.txt

cat > tmp-expect.$$.txt <<EOF
Un: #0x1.0p0
decimal : 1.00000e+00
hexadecimal : 0x1p+0
Plus grand normalise: #0x1.FFFFFEp127
decimal : 3.40282e+38
hexadecimal : 0x1.fffffep+127
Plus imposant normalise negatif: #-0x1.FFFFFFF3p127
decimal : -3.40282e+38
hexadecimal : -0x1.fffffep+127
Plus petit normalise: #0x1.0p-126
decimal : 1.17549e-38
hexadecimal : 0x1p-126
Arrondi inférieur en dessous du petit normalise: #0x1.FFFFFEp-127
decimal : 1.17549e-38
hexadecimal : 0x1.fffffcp-127
Plus grand denormalise: #0x1.FFFFFCp-127
decimal : 1.17549e-38
hexadecimal : 0x1.fffffcp-127
Arrondi inferieur au plus petit normalise: #0x1.FFFFFDp-127
decimal : 1.17549e-38
hexadecimal : 0x1.fffffcp-127
Plus petit denormalise: #0x1.0p-149
decimal : 1.40130e-45
hexadecimal : 0x1p-149
Denormalise (9 bits significatifs dans la mantisse): #0x1.34FFFFp-140
decimal : 8.64601e-43
hexadecimal : 0x1.348p-140
arrondi à 0 (troncature des bits et non arrondi): #0x1.FFFFFEp-150
decimal : 0.00000e+00
hexadecimal : 0x0p+0
Arrondi a 0: #0x1.7FFFFFp-150
decimal : 0.00000e+00
hexadecimal : 0x0p+0
Pas d'arrondi a 0 sans 1 en tete: #0x0.8000001p-148
decimal : 1.40130e-45
hexadecimal : 0x1p-149
Pas d'arrondi a 0 sans 1 en tete: #0x0.000002p-126
decimal : 1.40130e-45
hexadecimal : 0x1p-149
Pas d'arrondi a 0 sans 1 en tete: #0x0.8000001p-149
decimal : 0.00000e+00
hexadecimal : 0x0p+0
Pas d'arrondi a 0 sans 1 en tete: #0x0.000002p-127
decimal : 0.00000e+00
hexadecimal : 0x0p+0
Troncature de denormalise: #0x0.00002AFFFp-126
decimal : 2.94273e-44
hexadecimal : 0x1.5p-145
decimal : 1.00000e+00
hexadecimal : 0x1.000002p+0
decimal : -0.00000e+00
hexadecimal : -0x0p+0
EOF

if diff -u tmp-expect.$$.txt tmp-floatx.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-floatx.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-floatx.$$.txt
    exit 1
fi
