#! /bin/sh

CLEAN_EXIT () {
    if [ "$1" -eq 1 ] ; then
	cat tmp-result.$$.txt
    fi
    rm -f tmp-result.$$.txt
    exit $1
}

if ima ../Test/round.ass > tmp-result.$$.txt; then
    echo OK
else
    echo "ima a leve une erreur en interpretant ../Test/round.ass"
    CLEAN_EXIT 1
fi

if grep -q OK tmp-result.$$.txt; then
    echo OK
else
    echo "ima n'a pas affiche OK en interpretant ../Test/round.ass"
    CLEAN_EXIT 1
fi

if echo "100002.0" | ima ../Test/conv0.ass > tmp-result.$$.txt; then
    echo OK
else
    echo "ima ne convertit pas pareil les flottants directes et en lecture"
    echo "(utilisation de ../Test/conv0.ass)"
    CLEAN_EXIT 1
fi

if echo "100002.1" | ima ../Test/conv.ass > tmp-result.$$.txt; then
    echo OK
else
    echo "ima ne convertit pas pareil les flottants directes et en lecture"
    echo "(utilisation de ../Test/conv.ass)"
    CLEAN_EXIT 1
fi

CLEAN_EXIT 0

