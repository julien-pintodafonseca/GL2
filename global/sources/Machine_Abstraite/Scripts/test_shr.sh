#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

ima shr.ass > tmp-shr.$$.txt

cat > tmp-expect.$$.txt <<EOF
536870912
268435456
134217728
67108864
33554432
16777216
8388608
4194304
2097152
1048576
524288
262144
131072
65536
32768
16384
8192
4096
2048
1024
512
256
128
64
32
16
8
4
2
1
0
-1
OK
OK
  ** IMA ** ERREUR ** Ligne 49 : 
    SHR avec operande 1 : flottant
EOF

if diff -u tmp-expect.$$.txt tmp-shr.$$.txt; then
    echo "OK"
    rm -f tmp-expect.$$.txt tmp-shr.$$.txt
else
    echo "Difference found"
    rm -f tmp-expect.$$.txt tmp-shr.$$.txt
    exit 1
fi
    
