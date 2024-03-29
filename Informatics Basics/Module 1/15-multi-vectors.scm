(define fill 0)
(define (make-multi-vector sizes . args)
  (if (not (null? args))
      (begin
        (set! fill (car args))
        (make-multi-vector+ sizes fill))
      (if (not (null? sizes))
          (cons (make-vector (car sizes)) (make-multi-vector (cdr sizes)))
          '())))
(define (make-multi-vector+ sizes fill)
  (if (not (null? sizes))
      (cons (make-vector (car sizes) fill) (make-multi-vector (cdr sizes) fill))
      '()))
(define (multi-vector? m)
  (if (list? m)
      (if (and (vector? (car m)) (vector? (car (cdr m))))
          (= 1 1)
          (= 1 2))
      (= 1 2)))
(define (multi-vector-set! m indices x)
  (if (or (not (null? indices)) (not (null? m)))
      (if (list? m)
          (cons (vector-set! (car m) (car indices) x) (multi-vector-set! (cdr m) (cdr indices) x))
          '())
      '()))
(define (multi-vector-ref m indices)
  (if (or (not (null? (cdr indices))) (not (null? (cdr m))))
      (if (list? m)
          (multi-vector-ref (cdr m) (cdr indices)))
          '())
      (vector-ref (car m) (car indices)))
