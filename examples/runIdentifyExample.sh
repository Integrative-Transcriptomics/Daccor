#!/bin/sh

# seting the variables

genome=data/identify/GCF_000008605.1_ASM860v1_genomic.fna
kMerSize=31
mismatches=5
threads=5
minLength=101
outPrefix=Nichols_repeats

# running the program

java -jar ../build/libs/Daccor-0.0.1.jar identify -i $genome -k $kMerSize -m $mismatches -o $outPrefix -p $threads -t $minLength
