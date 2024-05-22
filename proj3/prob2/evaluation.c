#include <stdio.h>
#include "prob2_functions.h"

int main(int argc, char *argv[]) {
    int scheduling_types[] = {1, 2, 3}; // 1=static, 2=dynamic, 3=guided
    int num_threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int chunk_sizes[] = {1, 5, 10, 100};
    const char* sched_names[] = {"static", "dynamic", "guided"};

    // Redirect stdout to a file
    freopen("output.csv", "w", stdout);

    // Print the headers
    printf("%s,%s,", "Chunk Size", "Scheduling");
    for (int n = 0; n < sizeof(num_threads)/sizeof(num_threads[0]); n++) {
        printf("%d,", num_threads[n]);
    }
    printf("\n");

    // Print data rows
    for (int j = 0; j < sizeof(chunk_sizes)/sizeof(chunk_sizes[0]); j++) {
        for (int i = 0; i < sizeof(scheduling_types)/sizeof(scheduling_types[0]); i++) {
            printf("%d,%s,", chunk_sizes[j], sched_names[i]);
            for (int k = 0; k < sizeof(num_threads)/sizeof(num_threads[0]); k++) {
                double pi, execution_time;
                calculate_pi(scheduling_types[i], chunk_sizes[j], num_threads[k], &pi, &execution_time);
                printf("%.4f,", execution_time * 1000);
            }
            printf("\n");
        }
    }

    return 0;
}