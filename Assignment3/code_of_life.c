#include<stdio.h>
#include<stdlib.h>
#include<sys/time.h>
//#include<omp.h>

int **create_Mat(int size);
void input_Mat(int **A, int size);
int boundary(int size);
void print_initial(int **A, int size);
void compute(int **a, int **b, int size);
int count_live(int **a, int r, int c, int size);
void print_nextGen(int **b, int size);


int **create_Mat(int size) {
    int **Mat = (int **)calloc(sizeof(int *), size);

    for(int i=0; i<size; i++)
        Mat[i] = (int *)calloc(sizeof(int), size);

    return Mat;
}

void input_Mat(int **A, int size) {
    for(int i=0; i<size; i++){
		for(int j=0; j<size; j++){
			A[i][j] = rand() % 2;
		}
	}
}

int boundary(int size) {
	printf("\n");
	for(int i=0; i<size; i++)
        printf(" --");
	printf("\n");
    return 0;
}

void print_initial(int **A, int size) {
    printf("Initial Stage:");
	printf("\n");
	//boundary(size);
	for(int i=0; i<size; i++){
		printf(" ");
		for(int j=0; j<size; j++){
			printf(" %d ",A[i][j]);
		}
		//boundary(size);
		printf("\n");
	}
}

void print_nextGen(int **b, int size) {
    printf("\nNext Generation:");
	//boundary(size);
	printf("\n");
	for(int i=0; i<size; i++){
		printf(" ");
		for(int j=0; j<size; j++){
			printf(" %d ",b[i][j]);
		}
		//boundary(size);
		printf("\n");
	}
}

int count_live(int **a, int r, int c, int size) {
	int i, j, count=0;
	//#pragma omp parallel for private(i,j,count) shared(a)
	for(int i=r-1; i<=r+1; i++) {
		for(int j=c-1; j<=c+1; j++) {
			if((i==r && j==c) || (i<0 || j<0) || (i>=size || j>=size)) {
				continue;
			}
			if(a[i][j]==1) {
				count++;
			}
		}
	}
	return count;
}

void compute(int **a, int **b, int size) {
    int live_neighbour;
	int i,j;
	//#pragma omp parallel for private(i,j,live_neighbour) shared(a,b)
    for(i=0; i<size; i++) {
		for(j=0; j<size; j++) {
			live_neighbour = count_live(a, i, j, size);
			if(a[i][j]==1 && (live_neighbour==2 || live_neighbour==3)) {
				b[i][j]=1;
			}

			else if(a[i][j]==0 && live_neighbour==3){
				b[i][j]=1;
			}

			else{
				b[i][j]=0;
			}
		}
	}
}

int main()
{
    int size = 10000;
    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;
	//omp_set_num_threads(6);
    int **matrix;
    matrix =  create_Mat(size);
    input_Mat(matrix, size);

    int **updated_mat;
    updated_mat =  create_Mat(size);

    gettimeofday(&tv1, &tz);

    //print_initial(matrix, size);

    compute(matrix, updated_mat, size);

    //print_nextGen(updated_mat, size);

    gettimeofday(&tv2, &tz);
    elapsed = (double)(tv2.tv_sec - tv1.tv_sec) + (double)(tv2.tv_usec - tv1.tv_usec) * 1.e-6;
    printf("elapsed time = %4.2lf \n", elapsed);
    return 0;
}