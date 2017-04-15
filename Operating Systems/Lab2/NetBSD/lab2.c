#include <sys/module.h>
MODULE(MODULE_CLASS_MISC, lab2, NULL);
static int lab2_modcmd(modcmd_t cmd, void* arg) {
    printf("driver LAB2");
    return 0;
}