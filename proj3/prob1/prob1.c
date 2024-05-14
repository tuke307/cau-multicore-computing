#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include "prob1_functions.h"

int main(int argc, char *argv[]) {
    int scheduling_type, num_threads, prime_count;
    double execution_time;

    // Check if command line arguments are correct
    if (argc != 3) {
        fprintf(stderr, "Usage: %s scheduling_type# #_of_threads\n", argv[0]);
        return 1;
    }

    scheduling_type = atoi(argv[1]);
    num_threads = atoi(argv[2]);

    printf("Number of threads: %d\n", num_threads);

    switch(scheduling_type) {
        case 1:
            printf("static with default chunk size\n");
            break;
        case 2:
            printf("dynamic with default chunk size\n");
            break;
        case 3:
            printf("static with chunk size 10\n");
            break;
        case 4:
            printf("dynamic with chunk size 10\n");
            break;
        default:
            printf("Invalid scheduling type\n");
            return 1;
    }

    setup(scheduling_type, num_threads, &prime_count, &execution_time);

    printf("Execution time: %.2f milliseconds\n", execution_time * 1000);
    printf("Number of prime numbers: %d\n", prime_count);

    return 0;
}