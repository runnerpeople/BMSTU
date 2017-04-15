(define c 0)
(define (integrate f a b n)
  (define n+ 
    (/ (- b a) n))
  (do ((x a (+ x n+)))
      ((> x (- b n+))) (set! c (+ c (* (/ (+ (f x) (f (+ x n+))) 2) n+))))
   (/ (ceiling (* c 1000)) 1000))
