# project 3

## problem 1
the program use “schedule(static)” using 8 threads
```bash
gcc-14 -v -fopenmp -o prob1.out prob1.c
./prob1.out 1 8
```

## problem 2
dynamic scheduling (chunk size = 4) using 8 threads
```bash
gcc-14 -v -fopenmp -o prob2.out prob2.c
./prob2.out 2 4 8
```