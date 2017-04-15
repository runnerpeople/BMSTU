#include <stdio.h>
#include <stdlib.h>

unsigned int length (char *s) {
        int i,k;
        k =0;
        for (i=0;s[i]!='\0';i++) {
                k+=1;
        }
        return k;
}

void prefix (char *s,char *dest) {    
        int u=length(s),t,i,q,k,help;
        int pi[u];
        for(i=0;i<u;i++) {
                pi[i]=0;
        }
        pi[0]=t=0;
        i=1;
        while (i<u) {
                while ((t>0) && (s[t] != s[i])) {
                        t=pi[t-1];
                }
                if (s[t] == s[i])
                        t+=1;
                pi[i]=t;
                i+=1;
        }
        for (i=0;i<u;i++) {
                dest[i]=pi[i];
        }
}
   
int kmpall (char *s , char *t) {
        int q,k,y,z;
        q=0;
        k=0;
        y=length(t);
        z=length(s);
        char dest[1000];
        prefix(s,dest);
        while (k < y) {
                while ((q > 0) && (s[q] != t[k])) 
                        q = dest[q-1];
                if (s[q] == t[k]) 
                        q+=1;
                if (q == z) {
                        k = k - z + 1;
                        printf("%d ", k);
                        k = k + z - 1;
                }               
                k += 1;
        }
}

int main(int argc, char** argv) {
        kmpall(argv[1],argv[2]);
        return 0;
}
