(use-syntax (ice-9 syncase))
(define cont 0)
(define-syntax use-assertions
  (syntax-rules ()
    ((_) (call-with-current-continuation (lambda (escape) (set! cont (lambda (x) (display "FAILED: ") (display x) (escape))))))))
(define-syntax assert
  (syntax-rules ()
    ((_ expr) (if (not expr) (cont 'expr)))))
