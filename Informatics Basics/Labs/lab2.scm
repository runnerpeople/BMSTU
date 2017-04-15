;1

(define-syntax trace-ex
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (display " => ")
       (let ((result expr))
       (write result)
       (newline)
         result)))))

(define (zip . xss)
  (if (or (null? xss)
          (null? (trace-ex (car xss))))
      '()
      (cons (map car xss)
            (apply zip (map cdr (trace-ex xss))))))
;2

(define (run-test the-test)
  (let ((expr (car the-test)))
    (write expr)
    (let* ((result (eval expr (interaction-environment)))
           (status (equal? (cadr the-test) result)))
      (if status
          (display " ok")
          (display " FAIL"))
      (newline)
      (if (not status)
          (begin (display "  Expected: ") (write (cadr the-test)) (newline)
                 (display "  Returned: ") (write result) (newline)))
      status)))
(define (run-tests the-tests)
  (define (and-fold x xs)
    (if (null? xs)
        x
        (and-fold (and x (car xs)) (cdr xs))))
  (and-fold #t (map run-test the-tests)))

(define-syntax test
  (syntax-rules ()
    ((_ expr expected-result) (list (quote expr) expected-result))))


(define (mul a b)
  (* a b))

(define tests
  (list (test (mul 2 3) 6)
        (test (mul 3 4) 12)))
(run-tests tests)

;3                 
(define (ref xs index elem)
  (define (get-type xs)
    (cond ((vector? xs) 'vector)
          ((string? xs) 'string)
          (else 'list)))
  (define (ref-insert xs index new)
    (if (eq? index 0)
        (append (append new (list elem)) xs)
        (if (not (null? (cdr xs)))
            (ref-insert (cdr xs) (- index 1) (append new (list (car xs))))
            (not (null? (cdr xs))))))
  (define (translate xs)
    (cond ((vector? xs) (vector->list xs))
          ((string? xs) (string->list xs))
          (else xs)))
  (define (list-of-char? xs status) 
    (if (null? xs)
        status
        (list-of-char? (cdr xs) (and (char? (car xs)) status))))
  (define (re-translate xs type)
    (cond ((eq? type 'vector) (list->vector xs))
          ((eq? type 'string) 
             (if (list-of-char? xs #t)
                 (list->string xs)
                 (= 1 2)))
          (else xs)))
  (let ((buf (ref-insert (translate xs) index '())))
    (if (not buf)
        buf
        (re-translate buf (get-type xs)))))

(define (ref_ xs index)
  (define (ref-help xs index)
    (if (eq? index 0)
        (car xs)
        (if (not (null? (cdr xs)))
            (ref-help (cdr xs) (- index 1))
            (not (null? (cdr xs))))))
  (define (translate xs)
    (cond ((vector? xs) (vector->list xs))
          ((string? xs) (string->list xs))
          (else xs)))
  (ref-help (translate xs) index))
