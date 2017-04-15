(define pi (acos (- 1)))
(define (number-of-pots d h s)
  (ceiling (/ (+ (* 2 pi d h) (/ (* pi d d) 2)) s)))

