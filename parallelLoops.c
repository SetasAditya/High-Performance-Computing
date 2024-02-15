/**

* @file

* @author Aditya Sai

* @version 1.0

*

* @section LICENSE

*

* This program is free software; you can redistribute it and/or

* modify it under the terms of the GNU General Public License as

* published by the Free Software Foundation; either version 2 of

* the License, or (at your option) any later version.

*

* @section DESCRIPTION

*

* The time class represents a moment of time.

*/

/**

* @brief A sample Time class

* @author Future DB Guru

*

* This is a simple class to demonstrate how Doxygen is used.

* It implements a dummy Time class.

*/

#include<omp.h>
#include<stdio.h>

int x=0;
int main()
{
    omp_set_num_threads(4);
    #pragma omp parallel for
    for(int i=0; i<100000; i++)
    {
        //#pragma omp critical
        x = x+1;
    }
    printf("Result is %d\n", x);
    return 0;
}
