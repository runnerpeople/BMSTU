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
(define (replace pred? proc xs)
  (define (iter xs1 new)
      (if (null? xs1)
          new
          (if (pred? (car xs1))
                (iter (cdr xs1) (append new (list (proc (car xs1)))))
                (iter (cdr xs1) (append new (list (car xs1)))))))
  (iter xs '()))
(define (replicate x n)
  (define (iter num list_new)
    (if (= num 0)
        list_new
        (iter (- num 1) (append list_new (list x)))))
  (iter n '()))
(define (cycle xs n)
  (define (iter num list_new)
    (if (= num 1)
        list_new
        (iter (- num 1) (append list_new xs))))
  (iter n xs))
(define (and-fold . args)
  (define (iter result xs)
    (if (or (null? xs) (not result))
        result
        (if (eq? result (car xs))
            (iter result (cdr xs))
            (iter (not result) (cdr xs)))))
  (iter #t args))
(define (or-fold . args)
  (define (iter result xs)
    (if (or (null? xs) result)
        result
        (if (eq? result (car xs))
            (iter result (cdr xs))
            (iter #t (cdr xs)))))
  (iter #f args))
(define (or-fold . args)
  (define (iter result xs)
    (if (null? xs) 
        result
        (if (result)
            result 
            (if (eq? result (car xs))
                (iter result (cdr xs))
                (iter #t (cdr xs))))))
  (iter #f args))
(define (f x)
  (* x 2))
(define (g x)
  (* x 3))
(define (h x)
  (- x))
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








