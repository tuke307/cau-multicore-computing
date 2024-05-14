#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    long num_steps = 10000000;
    double step, pi, sum = 0.0;
    double start_time, end_time;

    // Check if command line arguments are correct
    if (argc != 4) {
        fprintf(stderr, "Usage: %s scheduling_type# chunk_size #_of_threads\n", argv[0]);
        return 1;
    }

    // Parse command line arguments
    int sched_type = atoi(argv[1]);
    int chunk_size = atoi(argv[2]);
    int num_threads = atoi(argv[3]);

	printf("Number of threads: %d\n", num_threads);

    // Set the number of threads
    omp_set_num_threads(num_threads);

    // Determine the scheduling strategy
    omp_sched_t sched;
    switch (sched_type) {
        case 1:
			printf("static with chunk size %d\n", chunk_size);
            sched = omp_sched_static;
            break;
        case 2:
			printf("dynamic with chunk size %d\n", chunk_size);
            sched = omp_sched_dynamic;
            break;
        case 3:
			printf("guided with chunk size %d\n", chunk_size);
            sched = omp_sched_guided;
            break;
        default:
            fprintf(stderr, "Invalid scheduling type. Use 1 for static, 2 for dynamic, or 3 for guided.\n");
            return 1;
    }
    omp_set_schedule(sched, chunk_size);

    step = 1.0 / (double) num_steps;
    start_time = omp_get_wtime();

    // Parallel loop
    #pragma omp parallel for reduction(+:sum)
    for (long i = 0; i < num_steps; i++) {
        double x = (i + 0.5) * step;
        sum = sum + 4.0 / (1.0 + x * x);
    }

    pi = step * sum;
    end_time = omp_get_wtime();

    double timeDiff = end_time - start_time;
    printf("Execution Time : %lfms\n", timeDiff * 1000); // Multiply by 1000 to convert seconds to milliseconds
    printf("pi=%.24lf\n", pi);

    return 0;
}
