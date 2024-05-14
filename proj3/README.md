# project 3

## problem 1
the program use “schedule(static)” using 8 threads
```bash
gcc-14 -v -fopenmp prob1.c -o prob1.out
./prob1.out 1 8
```

evaluation script with 1, 2, 4, 6, 8, 10, 12, 14, 16 threads
```bash
gcc-14 -v -fopenmp evaluation.c prob1.c -o evaluation.out
./evaluation.out
```


## problem 2
dynamic scheduling (chunk size = 4) using 8 threads
```bash
gcc-14 -v -fopenmp prob2.c -o prob2.out
./prob2.out 2 4 8
```