/**
 * @file prob1.c
 * @brief This program calculates the number of prime numbers between 1 and 200000 using OpenMP for parallelization.
 *
 * The program takes two command-line arguments: the scheduling type and the number of threads.
 * The scheduling types are as follows:
 * 1: Static scheduling with default chunk size
 * 2: Dynamic scheduling with default chunk size
 * 3: Static scheduling with chunk size 10
 * 4: Dynamic scheduling with chunk size 10
 *
 * The number of threads can be any integer value, but typically it should be a value that matches the number of cores available on the machine.
 *
 * The program prints the number of prime numbers found and the execution time.
 *
 * Usage: prob1.out scheduling_type _of_thread
 */


#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

// Function to check if a number is prime
int is_prime(int n) {
    if (n <= 1) return 0;
    if (n % 2 == 0 && n > 2) return 0;
    for (int i = 3; i * i <= n; i += 2) {
        if (n % i == 0) return 0;
    }
    return 1;
}

int main(int argc, char *argv[]) {
    // Check if command line arguments are correct
    if (argc != 3) {
        fprintf(stderr, "Usage: %s scheduling_type# #_of_threads\n", argv[0]);
        return 1;
    }

    int scheduling_type = atoi(argv[1]);
    int num_threads = atoi(argv[2]);
    int count = 0;
    double start_time, end_time;

    printf("Number of threads: %d\n", num_threads);

    // Set the number of threads
    omp_set_num_threads(num_threads);

    // Choose the scheduling type based on the command line argument
    switch(scheduling_type) {
        case 1:
            printf("static with default chunk size\n");
            omp_set_schedule(omp_sched_static, 0);
            break;
        case 2:
            printf("dynamic with default chunk size\n");
            omp_set_schedule(omp_sched_dynamic, 0);
            break;
        case 3:
            printf("static with chunk size 10\n");
            omp_set_schedule(omp_sched_static, 10);
            break;
        case 4:
            printf("dynamic with chunk size 10\n");
            omp_set_schedule(omp_sched_dynamic, 10);
            break;
        default:
            printf("Invalid scheduling type\n");
            return 1;
    }

    start_time = omp_get_wtime();
    
    // Parallelize the loop
    #pragma omp parallel for schedule(runtime) reduction(+:count)
    for (int i = 1; i <= 200000; i++) {
        if (is_prime(i)) {
            count++;
        }
    }

    end_time = omp_get_wtime();

    printf("Number of primes: %d\n", count);
    printf("Execution time: %.2f seconds\n", end_time - start_time);

    return 0;
}
