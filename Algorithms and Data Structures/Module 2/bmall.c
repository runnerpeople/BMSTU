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

void suffix (char *s,char *dest) {
        int t,i;
        int n=length(s);
        char c[n];
        for (i=0;i<n;i++) {
                c[i]=0;
        }
        c[n-1]=n-1;
        t=n-1;
        i=n-2;
        while (i>=0) {
                while ((t<n-1) && (s[t] != s[i])) {
                        t=c[t+1];
                }
                if (s[t]==s[i])
                        t-=1;
                c[i]=t;
                i-=1;
        }
        for (i=0;i<n;i++) {
                dest[i]=c[i];
        }
}

void tabl2 (char *s,char *dest2) {
        int i,t;
        int n=length(s);
        char b2[n];
        char dest[1000];
	for (i=0;i<n;i++) {
                b2[i]=0;
        }
	for (i=0;i<1000;i++) {
                dest[i]=0;
        }
        suffix(s,dest);
        i=0;
        t = dest[0];
        while (i < n) {
                while (t < i)
                        t = dest[t + 1];
                b2[i] = -i + t + n;
                i +=1;
        }
        i = 0;
        while (i < n-1) {
                t = i;
                while (t < n - 1) {
                        t = dest[t + 1];
                        if (s[i] != s[t])
                                b2[t] = -(i + 1) + n;
                }
                i+=1;
        }
        for (i=0;i<n;i++) {
                dest2[i]=b2[i];
        }
}

void tabl1 (char *s,char *dest1) {
        int i,j,a=0;
        int n=length(s);
        char b1[256];
	for (i=0;i<256;i++) {
                b1[i]=0;
        }
        while (a < 26) {
                b1[a] = n;
                a+=1;
        }
        j = 0;
        while (j < n) {
                b1[s[j]] = n - j - 1;
                j += 1;
        }
        for (i=0;i<n;i++) {
                dest1[i]=b1[i];
        }
}


void bmall(char *s,char *t) {
        int k,i;
        int n=length(s);
        int m=length(t);
        char dest1[1000];
	char dest2[1000];
	for (i=0;i<1000;i++) {
                dest1[i]=0;
		dest2[i]=0;
        }
        tabl1(s,dest1);
	tabl2(s,dest2);
        k=n-1;
        while (k<m) {
                i=n-1;
                while (t[k]==s[i]) {
                        if (i==0) {
                                printf("%d ", k);
				break;
                        }
                        i-=1;
                        k-=1;
                }
                if ((k+dest1[t[k]])>(k+dest2[i]))
                        k+=dest1[t[k]];
                else k+=dest2[i];
        }
}


int main(int argc, char** argv) {
    bmall(argv[1],argv[2]);
    return 0;
}
