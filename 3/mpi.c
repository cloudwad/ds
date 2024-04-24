#include <stdio.h>
#include <mpi.h>

int main(int argc, char *argv[]) {
    int rank, size;
    int N = 1000; // Array length (change as needed)
    int n = 4; // Number of processes (change as needed)
    int array[N];
    int local_sum = 0, global_sum = 0;
    int local_start, local_end;
    int elements_per_process;

    // Initialize MPI
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Ensure the number of processes matches the specified value
    if (size != n) {
        if (rank == 0) {
            printf("Please run with %d processes.\n", n);
        }
        MPI_Finalize();
        return 1;
    }

    // Initialize the array (only on rank 0)
    if (rank == 0) {
        for (int i = 0; i < N; i++) {
            array[i] = i + 1; // Initialize the array with values 1 to N
        }
    }

    // Calculate the number of elements per process
    elements_per_process = N / n;

    // Calculate the start and end indices for each process
    local_start = rank * elements_per_process;
    local_end = local_start + elements_per_process;

    // Last process handles any remainder elements
    if (rank == n - 1) {
        local_end = N;
    }

    // Distribute the array to all processes (only one array transfer in this simple case)
    MPI_Scatter(array + local_start, elements_per_process, MPI_INT,
                array + local_start, elements_per_process, MPI_INT,
                0, MPI_COMM_WORLD);

    // Each process calculates its local sum
    for (int i = local_start; i < local_end; i++) {
        local_sum += array[i];
    }

    // Display the intermediate sum from each process
    printf("Process %d calculated local sum: %d\n", rank, local_sum);

    // Gather the local sums at rank 0
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    // Only rank 0 displays the final sum
    if (rank == 0) {
        printf("Total sum of array: %d\n", global_sum);
    }

    // Finalize MPI
    MPI_Finalize();
    return 0;
}
