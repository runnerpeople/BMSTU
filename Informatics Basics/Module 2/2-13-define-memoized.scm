(use-syntax (ice-9 syncase))
(define-syntax define-memoized
  (syntax-rules ()
    ((_ (tag arg . args) body)
     (define tag
     (let ((memo '()))
       (define (proc arg . args) body)
       (lambda (arg . args)
         (let ((memoized (assoc (list arg . args) memo)))
           (if (not (equal? memoized #f))
               (cadr memoized)
               (begin 
                 (set! memo (cons (list (list arg . args) (proc arg . args)) memo))
                 (cadr (assoc (list arg . args) memo)))))))))
    ((_ tag (lambda (arg . args) body)) (define-memoized (tag arg . args) body))))
