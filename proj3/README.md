# project 3

## problem 1

the program use “schedule(static)” using 8 threads
```bash
gcc-14 -fopenmp -c prob1_functions.c -o prob1_functions.o
gcc-14 -fopenmp prob1.c prob1_functions.o -o prob1.out
./prob1.out 1 8
```

evaluation script with 1, 2, 4, 6, 8, 10, 12, 14, 16 threads
```bash
gcc-14 -fopenmp -c prob1_functions.c -o prob1_functions.o
gcc-14 -fopenmp evaluation.c prob1_functions.o -o evaluation.out
./evaluation.out
```

## problem 2
dynamic scheduling (chunk size = 4) using 8 threads
```bash
gcc-14 -fopenmp -c prob2_functions.c -o prob2_functions.o
gcc-14 -fopenmp prob2.c prob2_functions.o -o prob2.out
./prob2.out 2 4 8
```

evaluation script with 1, 2, 4, 6, 8, 10, 12, 14, 16 threads and chunk size of 1, 5, 10, 100
```bash
gcc-14 -fopenmp -c prob2_functions.c -o prob2_functions.o
gcc-14 -fopenmp evaluation.c prob2_functions.o -o evaluation.out
./evaluation.out
```