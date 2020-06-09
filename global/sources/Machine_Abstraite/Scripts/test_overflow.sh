#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima overflow.ass > tmp-overflow.$$.txt

cat > tmp-expect.$$.txt <<EOF
Last value before overflow (positive): 0x1p+127
Last value before overflow (negative): -0x1p+127
Last value before underflow (positive): 0x1p-149
Last value before underflow (negative): -0x1p-149
EOF

if diff -u tmp-expect.$$.txt tmp-overflow.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-overflow.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-overflow.$$.txt
    exit 1
fi
    
