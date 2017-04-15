unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i))     {
        unsigned long a, b, c;
        int i,f;
        a = 0;
        b = nel-1;
        for (i=a; i<nel; i++) {
                c = (a + b+1)/2;
                f = compare(c);
                if (f == 0) return c;
                        else if (f == 1) b = c-1; 
                                else if (f == -1) a = c+1;
                                        else return nel;
        }
}
