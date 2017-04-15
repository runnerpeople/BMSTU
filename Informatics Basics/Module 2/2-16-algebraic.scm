(use-syntax (ice-9 syncase))
  (define (s->s arg)
    (if (symbol? arg)
        (symbol->string arg)
        (string->symbol arg)))
  
  (define (strs->sym . strings)
    (s->s (apply string-append strings)))
  
  (define-syntax gen-pred
    (syntax-rules ()
      ((_ type sts ...) (begin (eval '(define type (list 'sts ...)) (interaction-environment))
                               (eval (list 'define (list (strs->sym (s->s 'type) "?") 'x) '(and (list? x) (member (car x) type) (list? x))) (interaction-environment))))))
  (define-syntax gen-ctor
    (syntax-rules ()
      ((_ type args ...) (eval '(define (type args ...) (list 'type args ...)) (interaction-environment)))))

  (define-syntax define-data
    (syntax-rules ()
      ((_ type ((st args ...) ...)) (begin
                                (gen-pred type st ...)
                                (gen-ctor st args ...) ...))))
  (define (mmatch expr pat dict)
    (if (null? expr)
        dict
        (if (symbol? (car expr))
            (if (equal? (car expr) (car pat))
                (mmatch (cdr expr) (cdr pat) dict)
                #f)
            (mmatch (cdr expr) (cdr pat) (cons (list (car pat) (car expr)) dict)))))

  (define (instance pat dict)
    (eval `(let ,dict ,pat) (interaction-environment)))
  (define (mmmatch expr rules)
    (let loop ((i rules))
      (if (null? i)
          #f
          (let ((pat (caar i)) (skel (cadar i)))
            (let ((res (mmatch expr pat '())))
              (if res
                  (instance skel res)
                  (loop (cdr i))))))))

(define-syntax match
  (syntax-rules ()
    ((_ expr (pat skel) ...) (mmmatch expr '((pat skel) ...)))))
