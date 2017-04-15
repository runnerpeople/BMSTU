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

int mygcd (int a,int b) {
        int c,d;
        c=a%b;
        d=b%a;
        if (c==0 || d==0) {
                if (c==0) return d;
                if (d==0) return c;
        }
        else {
                if (c > d)
                        mygcd(d,a);
                else mygcd(c,b);
        }    
}

void prefix (char *s) {    
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
        for(i=1;i<u+1;i++) {
                if (pi[i-1]!=0) {
                        q=mygcd(i,pi[i-1]);
                        help=0;
                        for(k=0;k+q<i;k++) {
                                if (s[k]!=s[k+q]) {
                                        help=1;
                                }
                        }
                        if (help==0) 
                                printf("%d %d\n", i,i/mygcd(i,pi[i-1]));
                }
        }
}

int main(int argc, char** argv) {
        prefix(argv[1]);
        return 0;
}
