# DACCOR - Detection charACterization and reConstruction of Repetitive regions in genomes

## Dependencies:

- jdk7
- gradle
- EAGER for the <code>reconstruct</code> subprogram (DOI: 10.1186/s13059-016-0918-z)

## generating the jar file
A precompiled jar file can be found in the folder "Releases"  
This program can be built with gradle (https://gradle.org). for that just type

`gradle build`

The jar-files are then contained in the build/libs folder. Additionally, the jar file in the "Releases" folder will be replaced with the newly built jar.

## Possile errors

If you get an empty output and no error messages, the problem might lie in an out-of memory exception that is caught and hidden within the java environment.

If so, please first try to run it with more memory like

<code>java -Xmx8G -jar ...</code>

## examples
examples are provided in the examples folder

## Tools
The following tools are available:

### identify
identify the repetitive regions in a given reference genome
#### Parameters:
- -g,--gff <arg>           Path of GFF file
- -h,--help                Prints options
- -i,--input <arg>         input filename, REQUIRED
- -k,--kmersize <arg>      size of initial kmer [readlength/2 OR 17]
- -m,--mismatches <arg>    number of mismatches allowed [0]
- -M,--margin <arg>        Margin around repeat to extract [0]
- -o,--output <arg>        output filename, REQUIRED
- -p,--processes <arg>     Number of threads per genome [1]
- -rl,--readlength <arg>   readlength [17]
- -S,--separately          Analyze each sequence separately [false]
- -t,--threshold <arg>     Min length of displayed results [readlength OR 51]
- -ws,--writeSeparately    write entries into separate files [false]

### reconstruct
reconstruct the genome and the given regions for each given sample with EAGER automatically
#### Parameters:
- -e,--eager <arg>     config file for reconstructed samples already reconstructed with EAGER
- -f,--fastqs <arg>    config file for fastq files, REQUIRED
- -h,--help            Prints options
- -i,--input <arg>     input filename (reference), REQUIRED
- -o,--output <arg>    output folder, REQUIRED
- -r,--repeats <arg>   config file for repeat input files, REQUIRED

### combine
combine reconstructed regions with the reconstructeg genome
#### Parameters:
- -g,--genomes <arg>   config file for reconstructed genomes, REQUIRED
- -h,--help            Prints options
- -o,--output <arg>    output folder, REQUIRED
- -r,--repeats <arg>   config file for reconstructed repeats, REQUIRED

### pipeline
run the full pipeline from the identification of repetitive regions until the generation of the combined reconstructed genomes
#### Parameters:
- -e,--eager <arg>         eager folder for reference file
- -f,--fastqs <arg>        config file for fastq files, REQUIRED
- -g,--gff <arg>           Path of GFF file
- -h,--help                Prints options
- -i,--input <arg>         input filename, REQUIRED
- -k,--kmersize <arg>      size of initial kmer [readlength/2 OR 17]
- -m,--mismatches <arg>    number of mismatches allowed [0]
- -M,--margin <arg>        Margin around repeat to extract [0]
- -o,--output <arg>        output filename, REQUIRED
- -p,--processes <arg>     Number of threads per genome [1]
- -rl,--readlength <arg>   readlength [17]
- -S,--separately          Analyze each sequence separately [false]
- -t,--threshold <arg>     Min length of displayed results [readlength OR 51]

