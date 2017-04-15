(define (bisection f a b ε)
  (let ((c (/ (+ a b) 2)))
    (if (<= (- b a) ε)
        c
        (cond ((< (* (f a) (f c)) 0) (bisection f a c ε))
              ((> (* (f a) (f c)) 0) (bisection f c b ε))
              (else c)))))
