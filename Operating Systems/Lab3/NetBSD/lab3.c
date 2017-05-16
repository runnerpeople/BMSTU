#include <sys/module.h>
MODULE(MODULE_CLASS_MISC, lab2, NULL);
static int lab2_modcmd(modcmd_t cmd, void* arg) {
    printf("Driver GOSHA IVANOV\n");
    struct proc *it;
    PROCLIST_FOREACH(it,&allproc);
       printf("%d  %s \n". it->p_pid,it->p_comm);
    return 0;
}
