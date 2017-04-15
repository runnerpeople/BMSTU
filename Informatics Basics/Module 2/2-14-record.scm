(use-syntax (ice-9 syncase))
(define-syntax meval
  (syntax-rules ()
  ((_ list) (eval list (interaction-environment)))))
(define (set-cadr! l v)
  (set-car! (cdr l) v))
(define (s->s arg)
  (if (symbol? arg)
      (symbol->string arg)
      (string->symbol arg)))
(define (strs->sym . strings)
  (s->s (apply string-append strings)))
(define-syntax gen-getter
  (syntax-rules ()
    ((_ name field) (meval (list 'define (list (strs->sym (s->s 'name) "-" (s->s 'field)) 'struct) '(cadr (assoc 'field (cdr struct))))))))
(define-syntax gen-setter
  (syntax-rules ()
    ((_ name field) (meval (list 'define (list (strs->sym "set-" (s->s 'name) "-" (s->s 'field) "!") 'struct 'val) '(set-cadr! (assoc 'field (cdr struct)) val))))))
(define-syntax gen-getters
  (syntax-rules ()
    ((_ name (field)) (gen-getter name field))
    ((_ name (field fields ...)) (begin (gen-getter name field) (gen-getters name (fields ...))))))
(define-syntax gen-setters
  (syntax-rules ()
    ((_ name (field)) (gen-setter name field))
    ((_ name (field fields ...)) (begin (gen-setter name field) (gen-setters name (fields ...))))))
(define-syntax gen-pred
  (syntax-rules ()
    ((_ name) (meval (list 'define (list (strs->sym (s->s 'name) "?") 'struct) '(and (list? struct) (symbol? (car struct)) (equal? (car struct) 'name)))))))
(define-syntax gen-record
  (syntax-rules ()
    ((_ (field)) (list (list 'list ''field (strs->sym (s->s 'field) "_arg"))))
    ((_ (field fields ...)) (append (list (list 'list ''field (strs->sym (s->s 'field) "_arg"))) (gen-record (fields ...))))
    ((_ name (fields ...)) (append '('name) (gen-record (fields ...))))))
(define-syntax gen-make
  (syntax-rules ()
    ((_ name (fields ...)) (meval (list 'define (list (strs->sym "make-" (s->s 'name)) (strs->sym (s->s 'fields) "_arg") ...) (append '(list) (gen-record name (fields ...))))))))
(define-syntax define-struct
  (syntax-rules ()
    ((_ name (fields ...))
     (begin
       (gen-getters name (fields ...))
       (gen-setters name (fields ...))
       (gen-pred name)
       (gen-make name (fields ...))))))
