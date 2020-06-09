#! /bin/sh

exp2=2
TIME=/usr/bin/time
cd "$(dirname "$0")"/../L_Interp/Test/

# date -R 010100002001
# date -d 'Mon, 01 Jan 2001 00:00:00' +%s
init=$(date +%s)
act1=$(( $($TIME -f "%e" -o tmp-$$.txt ima clk.ass) / 10))
exp1=$(( ($init - 978303600) / 10))

act2=$(awk -F '.' '{print $1}' tmp-$$.txt)

rm -f tmp-$$.txt

if [ $act1 -ne $exp1 ]; then
    echo "KO => SCLK seems incorrect"
    echo "FOUND $act1 vs $exp1 expected !"
    exit 1
fi

if [ $act2 -ne $exp2 ]; then
    echo "KO => CLK seems incorrect"
    echo "FOUND $act2 vs $exp2 expected !"
    exit 1
fi
    
echo "=> OK"
