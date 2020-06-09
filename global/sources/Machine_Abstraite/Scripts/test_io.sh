#! /bin/sh

fail=no

fail () {
    echo "$*: test failed"
    fail=yes
    continue
}

cd "$(dirname "$0")"/../L_Interp/Test/

for t in int-data*.in; do
    ima read_int.ass < "$t" > "${t%%.in}".actual || fail "$t"
    diff -u "${t%%.in}".actual "${t%%.in}".res || fail "$t"
    rm -f "${t%%.in}".actual
    echo "$t": OK
done

for t in float-data*.in; do
    ima read_float.ass < "$t" > "${t%%.in}".actual || fail "$t"
    diff -u "${t%%.in}".actual "${t%%.in}".res || fail "$t"
    rm -f "${t%%.in}".actual
    echo "$t": OK
done

for t in int float; do
    ima read_${t}2.ass < $t-data-plain.in > read_${t}2.actual && fail "read_${t}2"
    diff -u read_${t}2.actual read_${t}2.res || fail "read_${t}2"
    rm -f read_${t}2.actual
    echo "read_${t}2": OK
done

# simulate a user entering a value, and pressing enter once. ima
# should not try to read after this. If it does, it will be blocked by
# "sleep 1", and the ima process will end _after_ the end of this
# sleep 1. Normally, it should end almost immediately, hence before
# the end of this sleep 1. The order of echo reader and echo writter
# tells us if everything went fine.
printf 'check that ima does not try to read when it shouldn'\''t: '
rm -f actual
(echo 'before writer' >> actual; printf '42\n'; sleep 1; echo writer >> actual) | (ima read_one_int.ass >/dev/null && echo reader >> actual)

echo "%%%%%" >> actual

# same idea, but this time ima should emit an error message (instead of
# just terminating the execution)
(echo 'before writer' >> actual; printf '42\n'; sleep 1; echo writer >> actual) | (ima read_int2.ass >> actual && echo reader >> actual)

echo "%%%%%" >> actual

# same idea, but reading a float
(echo 'before writer' >> actual; printf '42.0\n'; sleep 1; echo writer >> actual) | (ima read_float2.ass >> actual && echo reader >> actual)

echo "before writer
reader
writer
%%%%%
before writer
  ** IMA ** ERREUR ** Ligne 3 : 
    WINT avec R1 flottant
writer
%%%%%
before writer
  ** IMA ** ERREUR ** Ligne 2 : 
    WINT avec R1 flottant
writer" > expected

if diff -u expected actual ; then
    echo "OK"
else
    fail=yes
    echo "ERROR (ima continued reading after RINT or RFLOAT)"
fi

rm -f expected actual

if [ "$fail" = yes ]; then
    exit 1
fi

