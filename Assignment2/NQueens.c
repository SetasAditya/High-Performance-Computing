#include<stdio.h>
#include<math.h>
#include<stdlib.h>
#include <sys/time.h>

//#include <omp.h>

int solving(int row, int n);
int place(int row, int col);

int board[12];
int count = 0;

int place(int row, int col) 
{
    for(int i=1; i<row; i+=1) 
    {
        if(board[i] == col)
            return 0;
        else
            if(abs(board[i]-col)==abs(i-row))
                return 0;
    }
    return 1;
}



int solving(int row, int n) 
{
    //#pragma omp parallel for
    for(int col=1; col<=n; col+=1) 
    {
        if(place(row, col)) 
        {
            board[row] = col;
            if(row == n)
            //#pragma omp atomic
                count+=1;
            else
                solving(row+1, n);
        }
    }
    return count;
}



int main(int argc, char *argv[]) 
{
    struct timeval tv1, tv2;
	struct timezone tz;
	double elapsed; 
    int n;
	
	int num_threads = 1;
        
    //omp_set_num_threads(num_threads);
    
    printf("\n\nEnter number of Queens:");
    scanf("%d",&n);
    gettimeofday(&tv1, &tz);
    printf("Number of Solutions = %d\n", solving(1, n));
    gettimeofday(&tv2, &tz);
    elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
	printf("elapsed time = %4.2lf seconds.\n", elapsed);

    return 0;
}

