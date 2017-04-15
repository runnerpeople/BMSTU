(define (sign? a)
  (and (or (equal? a #\+) (equal? a #\-)) #t))

(define (digit? a)
  (and (> (char->integer a) 47) (< (char->integer a) 58)))

(define (separator? a)
  (and (or (equal? a #\space) (equal? a #\tab) (equal? a #\newline)) #t))

(define (error? a)
  (and (or (equal? a "+") (equal? a "-")) #t))

(define (check-integer str)
  (define (f a i)
    (cond ((null? a))
          ((and (= i 0) (> (length a) 1) (sign? (car a))) (f (cdr a) 1)) ;переменная i идет не как индекс. Она используется для проверки первого символа строки на знак, то есть процедуры sign
          ((digit? (car a)) (f (cdr a) 1))
          (else #f)))
  (f (string->list str) 0))

(define (scan-integer str)
  (cond ((check-integer str) str)
        (else #f)))

(define (scan-many-integers str)
  (define (f a stack result)
    (cond ((null? a) (make-numbers (append result (cons (list->string stack) '()))))
          ((and (separator? (car a)) (null? stack)) (f (cdr a) stack result))
          ((separator? (car a)) (f (cdr a) '() (append result (cons (list->string stack) '()))))
          ((or (and (null? stack) (sign? (car a))) (digit? (car a))) (f (cdr a) (append stack (cons (car a) '())) result))
          (else #f)))
  (f (string->list str) '() '()))

(define (make-numbers lst)
  (if (null? lst) '()
      (if (error? (car lst)) #f 
          (if (not (equal? (car lst) ""))
              (cons (string->number (car lst)) (make-numbers (cdr lst)))
              '()))))
