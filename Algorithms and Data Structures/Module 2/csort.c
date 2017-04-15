#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int wcount(char *s) {
        long n,i,r;
        n = i = r = 0;
        n=strlen(s);    
        int count = 0;
        for (i=0;i<n-1;i++) {
                r = isgraph ((*(s + i)));
                        if ((*(s + i + 1)) == ' ' && r != 0)
                                count+=1;
        }
        r = isgraph ((*(s + i)));
                if (r != 0)
                        count+=1;
        return count;
}

void csort (char *src, char *dest) {
        int k,i,j,n,q,u,z;
        n=0;
        n=wcount(src);
	char count [n];
        char key[n];
        char help2[n];
	char help[n];
        j = 0;
        q = 0;
	for (i=0;i<n;i++) {
		help2[i]=0;
		count[i]=0;
		key[i]=0;
		help[i]=0;
	}
        for (i=0;src[i]!='\0';i++) {
                if (src[i]==' ' && j>0) {
                        key[q]=j;
                        j=0;
                        q+=1;
                }
                else {
                        if (src[i]!=' ')
                                j+=1;
                }
        }
        key[q]=j;
        q=0;	
        for (i=0;src[i]!='\0';) {
                if (src[i]!= ' ') {
                        count[q]=src[i];
                        help2[q]=i;
                        i+=key[q];
                        q+=1;    
                }
                else i+=1;
        }
        i=0;   
        j=0;
        while (j < n) {
                i = j + 1;
                while (i < n) {
                	if (key[i]<key[j])
                      		help[j] += 1;
                        else 
                          	help[i] += 1;
                        i += 1;
                }
                j += 1;
        }
        z=0;
        u=0;
        for(i=0;i<n;i++) {
                if (help[i]==z) {
                        for (j=help2[i];j<help2[i]+key[i];j++) {
                                dest[u]=src[j];
                                u+=1;
                        }
                        if ((z+1)!=n) {
                            	dest[u]=' ';
                            	u+=1;
                        }
                        z+=1;
                        i=-1;
                }     
        }
        dest[u]='\0';
}

unsigned int length (char *s) {
        int i,k;
        k =0;
        for (i=0;s[i]!='\0';i++) {
                k+=1;
        }
        return k;
}

int main() {
        int i;
        char src[500];
        gets(src);
        char dest[500];
        csort(src,dest);
        for (i=0;dest[i]!='\0';i++) {
                printf("%c", dest[i]);
        }
        return 0;
}
