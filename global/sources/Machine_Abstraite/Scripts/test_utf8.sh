#! /bin/sh

cd "$(dirname "$0")"/../L_Interp/Test/

cat > tmp-$$-expect.txt <<EOF
abcdef$&'=
ijkl
éàä
¢π
€⡷
语
𐍈𐍆
EOF

# cat tmp-$$-expect.txt

ima utf8_io.ass < tmp-$$-expect.txt > tmp-$$.txt
echo >> tmp-$$-expect.txt  # BUG ? ima ajoute une fin de ligne bizarrement !

if diff -u tmp-$$-expect.txt tmp-$$.txt ; then
    rm -f tmp-$$-expect.txt tmp-$$.txt
    echo "=> OK"
else
    rm -f tmp-$$-expect.txt tmp-$$.txt
    echo "=> FAILED"
    exit 1
fi
    
