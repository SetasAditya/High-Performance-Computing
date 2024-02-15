#include <stdio.h>
#include <pthread.h>
#include "PetersonLock.h"

int counter = 0;

void *increment(void *arg) {
    int thread_id = *(int *)arg;
    for (int i = 0; i < 1000; i++) {
        lock(thread_id);
        counter++;
        unlock(thread_id);
    }
    return NULL;
}

void *decrement(void *arg) {
    int thread_id = *(int *)arg;
    for (int i = 0; i < 1000; i++) {
        lock(thread_id);
        counter--;
        unlock(thread_id);
    }
    return NULL;
}

int main() {
    pthread_t thread1, thread2;
    int thread_id1 = 0, thread_id2 = 1;
    pthread_create(&thread1, NULL, increment, &thread_id1);
    pthread_create(&thread2, NULL, decrement, &thread_id2);
    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);
    printf("Counter value: %d\n", counter);
    return 0;
}
