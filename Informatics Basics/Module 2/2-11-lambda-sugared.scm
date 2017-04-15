(use-syntax (ice-9 syncase))
(define-syntax &
  (syntax-rules (->)
    ((_ -> expr) (lambda () (begin expr)))
    ((_ -> body1 . body2) (lambda (c) (begin body1 . body2))) 
    ((_ expr -> body1 . body2) (lambda (expr) (begin body1 . body2)))
    ((_ expr expr1 -> body1 . body2) (lambda (expr expr1) (begin body1 . body2)))))
