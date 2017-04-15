(define (loans s p n)
  (define r (/ p 100))
  (exact->inexact (/ (round (* (/ (* s r (expt (+ r 1) n)) (* (- (expt (+ r 1) n) 1) 12)) 100)) 100)))
