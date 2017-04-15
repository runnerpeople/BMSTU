unsigned long peak(unsigned long nel, int (*less)(unsigned long i, unsigned long j)) {
        int r;
        unsigned long a,c,d,e,b;
        c=d=e=b=r=0;
        a=0;
	d=nel-1;
        while (a<d) {
		c=a+(d-a)/2;
                b=c+1;
		e=c-1;
                r=less(e,c);
		if (r == 0)
			d = c;
                else {
                        r=less(c,b);
                        if (r == 1) {
                                a = c;
		        }	
                        else break;
		}
        }
        return c;
}
