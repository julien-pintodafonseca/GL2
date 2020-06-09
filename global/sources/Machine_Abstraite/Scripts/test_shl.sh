#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima shl.ass > tmp-shl.$$.txt

cat > tmp-expect.$$.txt <<EOF
2
4
8
16
32
64
128
256
512
1024
2048
4096
8192
16384
32768
65536
131072
262144
524288
1048576
2097152
4194304
8388608
16777216
33554432
67108864
134217728
268435456
536870912
1073741824
0
OK
-2
OK
OK
  ** IMA ** ERREUR ** Ligne 62 : 
    SHL avec operande 1 : flottant
EOF

if diff -u tmp-expect.$$.txt tmp-shl.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-shl.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-shl.$$.txt
    exit 1
fi
    
