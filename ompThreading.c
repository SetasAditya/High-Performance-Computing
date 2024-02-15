#include<omp.h>
#include<stdio.h>

int main()
{
    omp_set_num_threads(5);
    int maxThreads = omp_get_max_threads();
    printf("Maximum number of threads is %d\n", naxThreads);

    #pragma omp parallel
    {
        printf("Hello\n");
    }
    omp_set_num_threads(4);
    maxThreads = omp_get_max_threads();
    printf("Maximum number of threads is updated to %d\n", maxThreads);
    
    #pragma omp parallel
    {
        #pragma omp master
        {
            printf("www.openmp.org %d\n", omp_get_thread_num());
        }
        printf("Welcome to HPC %d\n" omp_get_thread_num());
        #pragma omp barrier
        printf("Are you able to understand the concepts! %d\n", omp_get_thread_num());
    }
    return 0;
}