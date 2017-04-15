void revarray(void *base, unsigned long nel, unsigned long width)
{
        int i,j;
        unsigned char *c;
        unsigned char *d;
        unsigned char f;
                for (i=0; i<nel/2;i++) {
                        for (j=0; j<width;j++) {
                                c=((char*)base + width*i+j);
                                d=((char*)base + width*(nel- i-1)+j);
                                f=*c;
                                *c=*d;
                                *d=f;
                        }
                }  
}
