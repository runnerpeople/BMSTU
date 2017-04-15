int fib (int n) {
        if (n==0) return 1;
        if (n==1) return 1;
        return (fib(n-2)+fib(n-1));
}

void shellsort(unsigned long nel, 
        int (*compare)(unsigned long i, unsigned long j), 
        void (*swap)(unsigned long i, unsigned long j)) { 
        unsigned long i,p;
        long k,j,s;
        s=0;
        k=nel-1;
        p=0;
        for (i=3;i<nel;i++) { 
                p=i;
                if (fib(i)>=nel) 
                        break;          
        }
        if (p>3) {
                p-=1;
                k=fib(p);          
        }
        j=0;
        while (k>0) {               
                while(j<nel-k) {  
                        s=j;
                        while (s>=0 && (compare(s,s+k)==1)) {
                                swap(s,s+k);
                                s-=k;
                        }
                        j++;
                }               
                if (p>1) {
                        p-=1;
                        k=fib(p);                   
                }
                else k-=1;
                j=0;      
        }
}  
