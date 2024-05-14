#include "prob1.h"
#include <stdio.h>

int main(int argc, char *argv[]) {
    int scheduling_type, num_threads;
    double execution_time;
    int threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int num_tests = sizeof(threads) / sizeof(threads[0]);

    printf("Threads\tType 1\tType 2\tType 3\tType 4\n");

    for (int i = 0; i < num_tests; i++) {
        num_threads = threads[i];
        printf("%d\t", num_threads);

        for (scheduling_type = 1; scheduling_type <= 4; scheduling_type++) {
            execution_time = setup(argc, argv, &scheduling_type, &num_threads);
            printf("%.2f\t", execution_time);
        }

        printf("\n");
    }

    return 0;
}