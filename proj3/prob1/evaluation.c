#include <stdio.h>
#include "prob1_functions.h"

int main(int argc, char *argv[]) {
    int num_threads, prime_count;
    double execution_time;
    int scheduling_types[] = {1, 2, 3, 4}; // static(0), dynamic(0), static(10), dynamic(10)
    int threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int num_tests = sizeof(threads) / sizeof(threads[0]);

    printf("Threads\tstatic (0)\tdynamic (0)\tstatic (10)\tdynamic (10)\n");

    for (int i = 0; i < num_tests; i++) {
        num_threads = threads[i];
        printf("%d\t", num_threads);

        for (int i = 0; i < sizeof(scheduling_types)/sizeof(scheduling_types[0]); i++) {
            setup(scheduling_types[i], num_threads, &prime_count, &execution_time);
            printf("%.2f\t", execution_time * 1000);
        }

        printf("\n");
    }

    return 0;
}