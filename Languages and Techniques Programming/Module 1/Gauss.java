import java.util.Scanner;

public class Gauss {

        static int gcd(int a, int b) {
                return b != 0 ? gcd(b, a % b) : Math.abs(a);
        }

        public static int lcm(int a, int b) {
                if ((a ==  0) || (b == 0)) return 0;
                else return a/gcd(a,b)*b;
        }
        
        public static void gaussHelper(int[][] a,int[][] b) {
                int k, m, n=a.length+1, i, j, u,k1;
                for (u = 0; u < n-2; u++) {
                        for (i = u; i < n-2; i++) {
                		if (a[u][u]==0) {
					swapMatrixString(a);
					swapMatrixString(b);
				}
                                k = lcm(a[u][u], a[i + 1][u]);
				if (k==0) continue;
                                m = k / a[u][u];
                                for (j = 0; j < n; j++)
                                        a[u][j] *= m;
                                k1 = k / a[i + 1][u];
                                for (j = 0; j < n; j++)
                                        a[i + 1][j] *= k1;
                                for (j = 0; j < n; j++)
                                        a[i + 1][j] -= a[u][j];
                                if (a[i+1][u]==0) {
                                        for(j=0;j<n;j++)
                                                a[u][j]=b[u][j];
                                        }
                                }
                        for(i=0;i<n-1;i++) {
                                for(j=0;j<n;j++) {
                                        b[i][j]=a[i][j];
                                }
                        }
                }
        }

        public static void gaussHelper2(int[][] a,int[][] b) {
                int k, m, n=a.length+1, i, j, u,k1;
                for (u = n-2; u > 0; u--) {
                        for (i = u; i > 0; i--) {
                                k = lcm(a[u][u], a[i - 1][u]);
				if (k==0) continue;
                                m = k / a[u][u];
                                for (j = 0; j < n; j++)
                                        a[u][j] *= m;
                                k1 = k / a[i - 1][u];
                                for (j = 0; j < n; j++)
                                        a[i - 1][j] *= k1;
                                for (j = 0; j < n; j++)
                                        a[i - 1][j] -= a[u][j];
                                if (a[i-1][u]==0) {
                                        for(j=0;j<n;j++)
                                                a[u][j]=b[u][j];
                                }       
                        }
                        for(i=0;i<n-1;i++) {
                                for(j=0;j<n;j++) {
                                        b[i][j]=a[i][j];
                                }
                        }
                }
        }

        public static void Solution(int[][] a) {
                int n=a.length+1,i,l;
                for (i=0;i<n-1;i++) {
                        l=gcd(a[i][i],a[i][n-1]);
			if(l==0) continue;
                        a[i][i]/=l;
                        a[i][n-1]/=l;
                }
        }

	public static void swapMatrixString(int[][] a) {
		int i,j,y;
		int r;
		for (i=0;i<a.length;i++) {
			if(a[i][i]!=0) continue;
			for(j=i;a.length>j && a[j][i]==0;j++);
			if(j==a.length) continue;
			for(y=0;y<a.length+1;y++) {
				r=a[i][y];
				a[i][y]=a[j][y];
				a[j][y]=r;
			}
		}
	}

	public static int FSR(int[][] a) {
		int count=a.length,i,j;
		for (i=0;i<a.length;i++) {
			for(j=0;j<a.length+1;j++) {
				if (a[i][j]!=0)
					break;
				if (j==a.length)
					count-=1;
			}
		}
		return a.length-count;	
	}
			

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n = in.nextInt(),i,j;
                int[][] a = new int [n][n+1];
                int[][] b = new int [n][n+1];
		int rg;
                for(i=0;i<n;i++) {
                        for(j=0;j<n+1;j++) {
                                a[i][j] = in.nextInt();
                                b[i][j]=a[i][j];
                        }
                }
                String s="";
                gaussHelper(a,b);
		gaussHelper2(a,b);
		rg=FSR(a);
		if (rg>0) {
			System.out.println("No solution");
                	System.exit(0);
		}
                Solution(a);
		for(i=0;i<n;i++) {
            		if (a[i][i] == 0 && a[i][n] != 0) {
                		System.out.println("No solution");
                		System.exit(0);
            		}
        	}
               for(i=0;i<n;i++) {
                        if ((a[i][i]>0 && a[i][n]>0) || (a[i][i]<0 && a[i][n]<0) || a[i][i]==0 || a[i][n]==0)
                                s=Math.abs(a[i][n]) + "/" + Math.abs(a[i][i]);
                        else s="-" + Math.abs(a[i][n]) + "/" + Math.abs(a[i][i]);
                        System.out.println(s);
                        s=""; 
                }  
        }
}
