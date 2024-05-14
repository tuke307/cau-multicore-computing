#include <stdio.h>
#include "prob2.h"

int main(int argc, char *argv[]) {
    int scheduling_types[] = {1, 2, 3}; // static, dynamic, guided
    int num_threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int chunk_sizes[] = {1, 5, 10, 100};

    for (int i = 0; i < sizeof(scheduling_types)/sizeof(scheduling_types[0]); i++) {
        for (int j = 0; j < sizeof(chunk_sizes)/sizeof(chunk_sizes[0]); j++) {
            for (int k = 0; k < sizeof(num_threads)/sizeof(num_threads[0]); k++) {
                double execution_time;
                int prime_count;
                setup(scheduling_types[i], chunk_sizes[j], num_threads[k], &prime_count, &execution_time);
                printf("Scheduling type: %d, Chunk size: %d, Number of threads: %d\n", scheduling_types[i], chunk_sizes[j], num_threads[k]);
                printf("Execution time: %.2f milliseconds\n", execution_time * 1000);
                printf("Number of prime numbers: %d\n", prime_count);
            }
        }
    }

    return 0;
}