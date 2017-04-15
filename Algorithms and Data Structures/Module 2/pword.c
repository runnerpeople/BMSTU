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

int pword (char *s, char *str) {    
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
        q=0;
        char dest[10000];
        memset(dest,0,10000);
        strcpy(dest,s);
        strcat(dest,str);
	k=0;
        k=length(dest);
        int pii[k];
        for (i=0;i<k;i++) {
            pii[k]=0;
        }       
        pii[0]=t=0;
        i=1;
        while (i<k) {
                while ((t>0) && (dest[t] != dest[i])) {
                        t=pii[t-1];
                }
                if (dest[t] == dest[i])
                        t+=1;
                pii[i]=t;
                i+=1;
        }
        help=0;
        for (i=u;i<k;i++) {
                if (pii[i]==0) {
                        help=1;
                        break;
                }
        }
        if (help==1) 
                return 0;
        else return 1;
}

int main(int argc, char** argv) {
        if (pword(argv[1],argv[2])==1)
                printf("yes");
        else printf ("no");
        return 0;
}
