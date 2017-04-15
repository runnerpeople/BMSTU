int maxarray(void *base, unsigned long nel, unsigned long width, int (*compare)(void *a, void *b)) {
    int i,imax,r;
    i = imax = 0;
    for (i=1; i<nel-1;++i) {
        r = (compare ((char*)base + imax*width, (char*)base + i*width)); 
        if (r<0) 
            imax=i;
    }
    return imax;
}
