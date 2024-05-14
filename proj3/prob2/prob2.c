#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include "prob2_functions.h"

int main(int argc, char *argv[]) {
    int scheduling_type, chunk_size, num_threads;
    double execution_time, pi;

    // Check if command line arguments are correct
    if (argc != 4) {
        fprintf(stderr, "Usage: %s scheduling_type# chunk_size #_of_thread\n", argv[0]);
        return 1;
    }

    scheduling_type = atoi(argv[1]);
    chunk_size = atoi(argv[2]);
    num_threads = atoi(argv[3]);

    printf("Number of threads: %d\n", num_threads);

    switch(scheduling_type) {
        case 1:
            printf("static with chunk size %d\n", chunk_size);
            break;
        case 2:
            printf("dynamic with chunk size %d\n", chunk_size);
            break;
        case 3:
            printf("guided with chunk size %d\n", chunk_size);
            break;
        default:
            printf("Invalid scheduling type\n");
            return 1;
    }

    calculate_pi(scheduling_type, chunk_size, num_threads, &pi, &execution_time);

    printf("Execution time: %.2f milliseconds\n", execution_time * 1000);
    printf("pi=%.24lf\n", pi);

    return 0;
}