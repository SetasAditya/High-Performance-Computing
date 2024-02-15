#include <stdbool.h>
#include "PetersonLock.h"

bool flag[2] = {false, false};
int victim;

void lock(int thread_id) {
    int other_thread_id = 1 - thread_id;
    flag[thread_id] = true;
    victim = thread_id;
    while (flag[other_thread_id] && victim == thread_id) {
        // wait for the other thread to release the lock
    }
}

void unlock(int thread_id) {
    flag[thread_id] = false;
}
