#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

void calculate_pi(int scheduling_type, int chunk_size, int num_threads, double *pi, double *execution_time) {
    double step, pi, sum = 0.0;

    // Set the number of threads
    omp_set_num_threads(num_threads);

    // Choose the scheduling type based on the command line argument
    switch(scheduling_type) {
        case 1:
            omp_set_schedule(omp_sched_static, chunk_size);
            break;
        case 2:
            omp_set_schedule(omp_sched_dynamic, chunk_size);
            break;
        case 3:
            omp_set_schedule(omp_sched_guided, chunk_size);
            break;
        default:
            *execution_time = -1.0;
            return -1.0;
    }

    step = 1.0 / (double) chunk_size;
    double start_time = omp_get_wtime();

    // Parallel loop
    #pragma omp parallel for reduction(+:sum)
    for (long i = 0; i < chunk_size; i++) {
        double x = (i + 0.5) * step;
        sum = sum + 4.0 / (1.0 + x * x);
    }

    *pi = step * sum;
    double end_time = omp_get_wtime();

    // Store the results
    *execution_time = end_time - start_time;
}

int main(int argc, char *argv[]) {
    int scheduling_type, chunk_size, num_threads;
    double execution_time, pi;

    // Check if command line arguments are correct
    if (argc != 3) {
        fprintf(stderr, "Usage: %s scheduling_type# #_of_threads\n", argv[0]);
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