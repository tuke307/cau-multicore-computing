#include <stdio.h>
#include "prob1_functions.h"

int main(int argc, char *argv[]) {
    int scheduling_types[] = {1, 2, 3, 4}; // 1=static(default), 2=dynamic(default), 3=static(10), 4=dynamic(10)
    const char* sched_names[] = {"static", "dynamic", "static", "dynamic"};
    const char* chunk_descriptions[] = {"default", "default", "10", "10"};
    int threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int num_tests = sizeof(threads) / sizeof(threads[0]);
    int columnWidth = 10;

    // Print the header
    printf("%-*s | %-*s | ", columnWidth, "exec time", columnWidth, "chunk size");
    for (int n = 0; n < num_tests; n++) {
        printf("%-*d | ", columnWidth, threads[n]);
    }
    printf("\n");

    // Print the rows for each scheduling type
    for (int i = 0; i < sizeof(scheduling_types)/sizeof(scheduling_types[0]); i++) {
        printf("%-*s | %-*s | ", columnWidth, sched_names[i], columnWidth, chunk_descriptions[i]);
        for (int j = 0; j < num_tests; j++) {
            double execution_time;
            int prime_count;
            // Assuming setup function calculates and updates execution_time
            setup(scheduling_types[i], threads[j], &prime_count, &execution_time);
            printf("%-*.*f | ", columnWidth, 2, execution_time * 1000);
        }
        printf("\n");
    }

    return 0;
}