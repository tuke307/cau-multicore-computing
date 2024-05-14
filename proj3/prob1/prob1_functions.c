#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include "prob1_functions.h"

// Function to check if a number is prime
int is_prime(int n) {
    if (n <= 1) return 0;
    if (n % 2 == 0 && n > 2) return 0;
    for (int i = 3; i * i <= n; i += 2) {
        if (n % i == 0) return 0;
    }
    return 1;
}

void setup(int scheduling_type, int num_threads, int *prime_count, double *execution_time) {
    // Set the number of threads
    omp_set_num_threads(num_threads);

    // Choose the scheduling type based on the command line argument
    switch(scheduling_type) {
        case 1:
            omp_set_schedule(omp_sched_static, 0);
            break;
        case 2:
            omp_set_schedule(omp_sched_dynamic, 0);
            break;
        case 3:
            omp_set_schedule(omp_sched_static, 10);
            break;
        case 4:
            omp_set_schedule(omp_sched_dynamic, 10);
            break;
        default:
            *prime_count = -1;
            *execution_time = -1.0;
            return;
    }

    int count = 0;
    double start_time, end_time;

    start_time = omp_get_wtime();
    
    // Parallelize the loop
    #pragma omp parallel for schedule(runtime) reduction(+:count)
    for (int i = 1; i <= 200000; i++) {
        if (is_prime(i)) {
            count++;
        }
    }

    end_time = omp_get_wtime();

    // Store the results
    *prime_count = count;
    *execution_time = end_time - start_time;
}
