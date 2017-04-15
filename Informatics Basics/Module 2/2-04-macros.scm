(use-syntax (ice-9 syncase))

(define-syntax when
  (syntax-rules ()
    ((_ cond-expr expr . exprs)
     (if cond-expr
         (begin expr . exprs)))))
(define-syntax unless
  (syntax-rules ()
    ((_ cond-expr expr . exprs)
     (if (not cond-expr)
         (begin expr . exprs)))))
(define-syntax for
  (syntax-rules (as in)
    ((_ x in xs . expr) (for-each (lambda (x) (begin . expr)) xs))
    ((_ xs as x . expr) (for-each (lambda (x) (begin . expr)) xs))))
(define-syntax while
  (syntax-rules ()
    ((_ cond? . expr) (let end () (when cond? (begin . expr) (end))))))
(define-syntax repeat
  (syntax-rules (until)
    ((_ (exp . expr) until cond?) (let end () (begin exp . expr) (when (not cond?) (end))))))
(define-syntax cout
  (syntax-rules (<< endl)
    ((_ << endl) (newline))
    ((_ << endl . exprs) (begin (newline) (cout . exprs)))
    ((_ << expr1 . exprs) (begin (display expr1) (cout . exprs)))))
