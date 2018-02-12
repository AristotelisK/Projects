#include <iostream>
#include <cstdlib>
#include <fstream>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/wait.h>
#include <chrono>
#include <fstream>



using namespace std;
using namespace std::chrono;

#define NB (sizeof(int) + sizeof(high_resolution_clock::time_point))

union senum {
    int val;
    struct semid_ds *buff;
    unsigned short *array;
} arg = {0};


void run(int N, int M);



int main(int argc, char** argv) {
    if (argc == 3) {
        int M = atoi(argv[1]);
        int N = atoi(argv[2]);

        cout << " M  = " << M << endl;
        cout << " N  = " << N << endl;
        cout << " NB = " << NB << endl;
        run(N, M);
    } else {
        cout << " Please give N and M " << endl;
    }
    return 0;
}

void run(int N, int M) {
    // -------------------------------
    // setup
    // -------------------------------
    int * data = new int[M];
    int shmid = shmget(15, NB, 0666 | IPC_CREAT);
    int * number = (int*) shmat(shmid, 0, 0);
    high_resolution_clock::time_point * chrono = (high_resolution_clock::time_point *) (number + 1);
    double diff = 0;

    if (shmid <= 0 || number == NULL) {
        cout << "SHM error" << endl;
        return;
    }

    int semempty_id = semget((key_t) 93, 1, 0666 | IPC_CREAT);
    int semfull_id = semget((key_t) 94, N, 0666 | IPC_CREAT);

    if (semempty_id < 0 || semfull_id < 0) {
        cout << "SEM error" << endl;
        return;
    }

    semctl(semempty_id, 0, SETVAL, arg);
    for (int i = 0; i < N; i++) {
        semctl(semfull_id, i, SETVAL, arg);
    }

    // -------------------------------
    // up down setup
    // -------------------------------
    struct sembuf upsemempty;
    struct sembuf downsemempty;
    struct sembuf upsemfull;
    struct sembuf downsemfull;

    upsemempty.sem_num = 0;
    upsemempty.sem_op = 1;
    upsemempty.sem_flg = 0;

    downsemempty.sem_num = 0;
    downsemempty.sem_op = -N;
    downsemempty.sem_flg = 0;

    upsemfull.sem_num = 0;
    upsemfull.sem_op = 1;
    upsemfull.sem_flg = 0;

    downsemfull.sem_num = 0;
    downsemfull.sem_op = -1;
    downsemfull.sem_flg = 0;

    // fork children
    for (int i = 0; i < N; i++) {
        int k = fork();

        if (k == 0) {
            // child code

            int mysem = i;
            downsemfull.sem_num = mysem;

            for (int i = 0; i < N; i++) {
                data[i] = 0;
            }

            for (int i = 0; i < M; i++) {
                semop(semfull_id, &downsemfull, 1);

                data[i] = *number;
                diff = diff + duration_cast<nanoseconds>(high_resolution_clock::now() - *chrono).count();

                semop(semempty_id, &upsemempty, 1);
            }
            diff = diff / M;

            cout << "Child PID: " << getpid() << " : " << diff << " nanoseconds . " << endl;

            string name = "stats" + to_string(i) + ".log";
            cout <<"name = " <<name<< endl;
            
            ofstream fp(name);
            
            fp << "Child PID: " << getpid() << " : " << diff << " nanoseconds . " << endl;
            for (int i = 0; i < M; i++) {
                fp << i << "," << data[i] << endl;
            }

            fp.close();

            return;
        } //end of child
    }

    // feeder
    for (int i = 0; i < M; i++) {
        data[i] = rand() ;
    }

    for (int i = 0; i < M; i++) {
        *number = data[i];
        *chrono = high_resolution_clock::now();


        for (int i = 0; i < N; i++) {
            upsemfull.sem_num = i;
            semop(semfull_id, &upsemfull, 1);  
        }

        semop(semempty_id, &downsemempty, 1);
    }

    // -------------------------------
    // clean up
    // -------------------------------
    for (int i = 0; i < N; i++) {
        wait(NULL);
    }

    semctl(semempty_id, 0, IPC_RMID, 0);

    semctl(semfull_id, 0, IPC_RMID, 0);

    shmdt((const void *) number);

    shmctl(shmid, 0, IPC_RMID);
}
