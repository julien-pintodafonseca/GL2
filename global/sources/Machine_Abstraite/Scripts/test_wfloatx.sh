#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima wfloatx.ass > tmp-wfloatx.$$.txt

cat > tmp-expect.$$.txt <<EOF
decimal : 1.00000e+00
hexadecimal : 0x1p+0
decimal : 1.00000e+00
hexadecimal : 0x1p+0
decimal : 1.00000e+00
hexadecimal : 0x1.000002p+0
decimal : 1.00000e+00
hexadecimal : 0x1.fffffep-1
EOF

if diff -u tmp-expect.$$.txt tmp-wfloatx.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-wfloatx.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-wfloatx.$$.txt
    exit 1
fi
    
