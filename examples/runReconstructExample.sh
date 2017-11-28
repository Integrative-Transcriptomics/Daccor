#!/bin/sh

# location of the fastq-dump program from the SRA toolkit(https://www.ncbi.nlm.nih.gov/sra/docs/toolkitsoft/)
fastqdump=/share/home/seitza/software/sratoolkit.2.8.1-ubuntu64/bin/fastq-dump

# download the syphilis sample SRR3584843
echo "downloading fastq files"

if [[ ! -f data/SRR3584843_R1.fastq.gz || ! -f data/SRR3584843_R2.fastq.gz ]]; then
	$fastqdump --gzip -O data --split-3 SRR3584843
	mv data/SRR3584843_1.fastq.gz data/SRR3584843_R1.fastq.gz
	mv data/SRR3584843_2.fastq.gz data/SRR3584843_R2.fastq.gz
fi

# setting the variables
genome=data/GCF_000008605.1_ASM860v1_genomic.fna
fqConfig=data/exampleFQConfig.txt
outPrefix=SRR3584843
repeatConfig=data/exampleRepeatConfig.txt

echo "starting reconstruction"
java -jar ../build/libs/Daccor-0.0.1.jar reconstruct -f $fqConfig -i $genome -o $outPrefix -r $repeatConfig
