(define (string-trim-left str)
  (if (or (equal? (car (string->list str)) #\tab) (equal? (car (string->list str)) #\newline) (equal? (car (string->list str)) #\space))
      (string-trim-left (list->string (cdr (string->list str))))
      str))
(define (string-trim-right str)
  (if (or (equal? (car (reverse (string->list str))) #\tab) (equal? (car (reverse (string->list str))) #\newline) (equal? (car (reverse (string->list str))) #\space))
      (string-trim-right (list->string (reverse (cdr (reverse (string->list str))))))
      (list->string (string->list str))))
(define (string-trim str)
  (string-trim-right (string-trim-left str)))
(define (string-prefix? str str1)
  (if (not (null? (string->list str)))
      (begin
        (if (equal? (string-length str) (string-length str1))
            (begin
              (if (null? (cdr (string->list str)))
                  (= 1 1)
                  (begin
                    (if (equal? (car (string->list str)) (car (string->list str1)))
                        (string-prefix? (list->string (cdr (string->list str))) (list->string  (cdr (string->list str))))
                        (= 1 2)))))
            (begin
              (if (< (string-length str) (string-length str1))
                  (string-prefix? str (list->string (reverse (cdr (reverse (string->list str1))))))
                  (string-prefix? (list->string (reverse (cdr (reverse (string->list str))))) str1)))))                 
        (= 1 1)))
(define (string-suffix? str str1)
  (if (not (null? (string->list str)))
      (begin
        (if (equal? (string-length str) (string-length str1))
            (begin
              (if (null? (cdr (string->list str)))
                  (= 1 1)
                  (begin
                    (if (equal? (car (string->list str)) (car (string->list str1)))
                        (string-suffix? (list->string (cdr (string->list str))) (list->string  (cdr (string->list str1))))
                        (= 1 2)))))
            (begin
              (if (< (string-length str) (string-length str1))
                  (string-suffix? str (list->string (cdr (string->list str1))))
                  (string-suffix? (list->string (cdr (string->list str))) str1)))))
      (= 1 1)))
(define (string-infix? str str1)
  (if (not (null? (string->list str1)))
      (begin
        (if (or (string-prefix? str str1) (string-suffix? str str1))
            (= 1 1)
            (begin
              (if (equal? (string-length str) (string-length str1))
                  (= 1 2)
                  (begin
                    (if (< (string-length str) (string-length str1))
                        (string-infix? str (list->string  (cdr (string->list str1))))
                        (string-infix? (list->string  (cdr (string->list str))) str1)))))))                        
      (= 1 2)))        
(define (delete str sep)
  (if (not (null? (string->list sep)))
      (begin
        (if (not (null? (string->list str)))
            (begin
              (if (equal? (car (string->list str)) (car (string->list sep)))
                  (delete (list->string (cdr (string->list str))) (list->string (cdr (string->list sep))))
                  (cons (list->string (cons (car (string->list str)) '())) (delete (list->string (cdr (string->list str))) sep)))) 
            '()))
      str))
(define (string-split str sep)
  (define k 1)
  (if (and (not (equal? (string->list str) '())) (not (equal? (cdr (string->list str)) '())) (member (car (string->list sep)) (string->list str)))
      (begin
        (if (equal? (car (string->list str)) (car (string->list sep)))
            (begin
              (if (= (string-length sep) 1)
                  (string-split (list->string (cdr (string->list str))) sep)
                  (string-split (delete str sep) sep)))
            (begin
              (if (equal? (car (cdr (string->list str))) (car (string->list sep)))
                  (begin
                    (cons (make-string k (car (string->list str))) (string-split (list->string (cdr (string->list str))) sep)))          
                  (begin
                    (list->string (append (list->string (car (string->list str))) (string-split (list->string (cdr (string->list str))) sep))))))))    
      (cons str '())))
