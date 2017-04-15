(define (proc-definition? lst)
  (cond ((pair? (cadr lst)))
        ((and (or (equal? (car lst) 'lambda) (equal? (car lst) 'define)) (and (not (number? (caddr lst))) (proc-definition? (caddr lst)))))
        (else #f)))
