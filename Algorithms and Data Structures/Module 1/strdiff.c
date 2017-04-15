int strdiff(char *a, char *b) {
        char c,d;
        int u,i;
        u=1;
        i=1;
        if (strlen(a) == 0 || strlen(b) == 0)
                return 0;
        if (strcmp(a,b) == 0) {
                return -1;
        }
        else {
                c=*a;
                d=*b;
                while ((c & 1) == (d & 1)) {
                        u+=1;
                        c=c>>1;
                        d=d>>1;
                        if (!(u % 8)) {
                                c=*(a+i);
                                d=*(b+i);
                                i+=1;
                        }
                }
        }
        return u;
}
