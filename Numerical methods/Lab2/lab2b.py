#!python
# -*- coding: utf-8 -*-


def tridiagonalMatrixAlgorithm(d,c,a,b):
    alpha = [-c[0]/d[0]]
    beta = [b[0]/d[0]]
    n = len(b)

    for i in range(1,n):
        alpha.append(-c[i]/(d[i]+alpha[i-1]*a[i]))
        beta.append((b[i]-a[i]*beta[i-1])/(d[i]+alpha[i-1]*a[i]))

    x = [0 for _ in range(n+2)]
    x[n+1]=beta[n-1]
    n -= 1
    for i in range(n,0,-1):
        x[i]=x[i+1]*alpha[i-1]+beta[i-1]
    x[0] = 2*x[1]-x[2]
    x[n+2]=2*x[n+1]-x[n]
    return x

def spline(len,ys,h):
    size = len
    n = size-1
    d = [1]
    c = [0]
    a = [0]
    b = [ys[0]]

    for i in range(1,n):
        d.append(4)
        c.append(1)
        a.append(1)
        b.append(6*ys[i])

    d.append(1)
    c.append(0)
    a.append(0)
    b.append(0)

    for i in range(0,n):
        b[i] /= (h*h*h)

    e = tridiagonalMatrixAlgorithm(d,c,a,b)

    return e

def baseX(k,xs,x,h,kh):
    x0 = xs[0]
    if k == 0:
        if x0 + 2 * h <= x: return 0
        if x0 + h <= x and x < x0 + 2 * h: return (2 * h + x0 - kh - (x - kh)) ** 3 / 6
        if x0 <= x and x <= x0+h: return 2*h**3/3 - ((x-x0) ** 2 * (2*h + x0 - kh - (x - kh))) / 2
        if x0 - h <= x and x < x0: return 2 * h ** 3 / 3 - ((x - x0) ** 2 * (2 * h - x0 + kh + (x - kh))) / 2
        if x0 - 2 * h <= x and x < x0 - h: return (2 * h - x0 + kh + (x - kh)) ** 3 / 6
        if x <= x0 - 2 * h: return 0
    else:
        return baseX(0,xs,x-k*h,h,-k*h)

def countSpline(spl,val,n,xs,h):
    i = 0
    for i in range(0,n+2):
        if xs[i] <= val and val <= xs[i+1]:
            break

    if n+2 == i:
        i-=1

    return spl[i+0] * baseX(i-1,xs,val,h,0) +\
           spl[i+1] * baseX(i,  xs,val,h,0) + spl[i+2] * baseX(i+1,xs,val,h,0)  + spl[i+3] * baseX(i+2,xs,val,h,0)


if __name__ == "__main__":

    xs = [0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5]
    ys = [2.78, 3.13, 3.51, 3.94, 4.43, 4.97, 5.58, 6.27, 7.04, 7.91, 8.89]

    xsNew = [0.50, 0.75, 1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75, 3.00, 3.25, 3.50, 3.75,
             4.00, 4.25, 4.50, 4.75, 5.00, 5.25, 5.50]
    ysNew = [2.78, 2.95, 3.13, 3.31, 3.51, 3.72, 3.94, 4.18, 4.43, 4.69, 4.97, 5.27, 5.58, 5.92, 6.27, 6.65, 7.04, 7.47, 7.91, 8.38, 8.89]
    N = 10
    h = 0.5
    spl = spline(N+2,ys,h)
    for i in range(0,2*N+1):
        val = xsNew[i]
        bsVal = countSpline(spl,val,N,xs,h)
        #print(str(val) + " & " + str(ysNew[i])  + " & " + str(bsVal) + " & " + str(bsVal-ysNew[i]) + "\\\\ \\hline")
        print("x=%f|f(x)=%f ... spl(x)=%f ... %f" %(val,ysNew[i],bsVal,bsVal-ysNew[i]))








