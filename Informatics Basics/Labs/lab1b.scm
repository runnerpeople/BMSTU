;1
(define (count x0 xs)
  (if (null? xs)
      0
      (if (eq? (car xs) x0)
          (+ 1 (count x0 (cdr xs)))
          (count x0 (cdr xs)))))
(define (count1 x0 xs)
  (define (iter counter list)
    (if (null? list)
        counter
        (if (eq? (car list) x0)
            (iter (+ 1 counter) (cdr list))
            (iter counter (cdr list)))))
  (iter 0 xs))
;2
(define (delete pred? xs)
  (define (iter xs1 new)
      (if (null? xs1)
          new
          (if (pred? (car xs1))
                (iter (cdr xs1) new)
                (iter (cdr xs1) (append new (list (car xs1)))))))
  (iter xs '()))
;3
(define (iterate f x n)
  (define (iter count buff list_new)
    (if (= count 0)
        list_new
        (iter (- count 1) (f buff) (append list_new (list buff)))))
  (iter n x '()))
;4
(define (intersperse e xs)
  (define (iter xs1 new)
    (if (null? xs1)
        new
        (if (null? (cdr xs1))
            (iter (cdr xs1) (append new (list (car xs1))))
            (iter (cdr xs1) (append new (list (car xs1) e))))))
  (iter xs '()))
;5
(define (any? pred? xs)
  (define (iter xs1)
    (and (not (null? xs1)) (or (pred? (car xs1)) (iter (cdr xs1)))))
  (iter xs))
(define (all? pred? xs)
  (define (iter count xs1)
    (or (and (null? xs1) (= count (length xs))) (and (pred? (car xs1)) (iter (+ count 1) (cdr xs1)))))
  (iter 0 xs))
;6
(define (o . args)
  (define (iter funcs)
    (if (null? funcs)
        (lambda (x) x)
        (let ((func1 (car funcs)))
          (if (null? (cdr funcs))
              func1
              (let ((func2 (iter (cdr funcs))))
                (lambda (x) (func1 (func2 x))))))))
  (iter args))
;7
(define (find-number a b c)
  (define (iter count)
    (if (>= b count)
        (if (= (remainder count c) 0)
            count 
            (iter (+ count 1)))
        (>= b count)))
  (iter a))