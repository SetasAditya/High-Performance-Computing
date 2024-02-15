#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <math.h>
#include <sys/time.h> 

int main(){

    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;
    
    int n=3;
    int p=4;
    int c=log2(p); //for the while loop
    double matrix[3][3]={{0.1,0.3,0.4},{0.5,0.6,0.8},{0.2,0.4,0.7}};
    
      for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            printf("%lf ",matrix[i][j]);
        }
        
        printf("\n");
    }
    
    // for(int i=0;i<n;i++){
    //     for(int j=0;j<n;j++){
    //     matrix[i][j]=(double)rand()/(double)RAND_MAX;
    //     }
    // } 
    
    
    gettimeofday(&tv1, &tz);
    while(c>0){
    //#pragma omp parallel for private(i,j,k) shared(A,B,C)
    double temp[3][3]={{0,0,0},{0,0,0},{0,0,0}};
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < n; ++k) 
            { temp[i][j]+=matrix[i][k] * matrix[k][j];  }
        }
    }
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
        matrix[i][j]=temp[i][j];
        }
    }
    c--;

    }
    gettimeofday(&tv2, &tz);
    elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
    printf("elapsed time = %4.2lf seconds.\n", elapsed);
    
    
    for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            printf("%lf ",matrix[i][j]);
        }
        
        printf("\n");
    }
    
        
} 