#include<stdlib.h>
#include<stdio.h>
#include<sys/time.h>

double **mult_Mat(double **A, double **C, int size);
double **create_Mat(int size);


double **create_Mat(int size) {
    double **Mat = (double **)calloc(sizeof(double *), size);

    for(int i=0; i<size; i++)
        Mat[i] = (double *)calloc(sizeof(double), size);

    return Mat;
}


double **mult_Mat(double **A, double **C, int size) {

    double **result_Mat = (double **)calloc(sizeof(double *), size);

    for(int i=0; i<size; i++)
        result_Mat[i] = (double *)calloc(sizeof(double), size);

    //if(size == 2) 
        //return OMM(A, C, size);
    
    
    int split_index = size/2;

        // double **result_Mat00 = (double **)calloc(sizeof(double *), split_index);
        // double **result_Mat01 = (double **)calloc(sizeof(double *), split_index);
        // double **result_Mat10 = (double **)calloc(sizeof(double *), split_index);
    double **result_Mat11;
    result_Mat11 = create_Mat(split_index);

    return result_Mat11;
}



// int main(int argc, char *argv[]){
int main() {
    int i,j,k;
    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;
    // int size = atoi(argv[1]);
    // int numTh = atoi(argv[2]);
    int size = 5;
    int N = size;
    double sum = 0.0;

    // Difference Between calloc and malloc is that they use 2 and 1 arguments respectively

    double **A = (double **)calloc(sizeof(double *), size);
    double **C = (double **)calloc(sizeof(double *), size);

    for(int i=0; i<size; i++){
        A[i] = (double *)calloc(sizeof(double), size);
        C[i] = (double *)calloc(sizeof(double), size);
    }

    for(int i=0; i<size; i++){
        for(int j=0; j<size; j++){
            A[i][j] = (double)rand() / (double)RAND_MAX;
        }
    }

    for(int i=0; i<size; i++){
        for(int j=0; j<size; j++){
            printf("%lf ", A[i][j]);
        }
        printf("\n");
    }

    double **result_Matrix;
    result_Matrix = mult_Matrix(A, C, size);

}

