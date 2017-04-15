;1
(define call/cc call-with-current-continuation)
(define exit #f) ; Точка выхода
(define (use-assertions)
  (call/cc (lambda (escape) (set! exit escape))))

(define-syntax assert
  (syntax-rules ()
    ((_ expr) (if (not expr) (begin (display "FAILED :") (display (quote expr)) (exit))))))

(use-assertions)
(define (1/x x)
  (assert (not (zero? x)))
  (write (/ 1 x))
  (newline))

(map 1/x '(1 2 3 0 5))

;2

(define (save-data xs filename)
  (with-output-to-file filename
    (lambda ()
      (write xs (current-output-port))
      (newline (current-output-port)))))

(define (load-data filename)
  (with-input-from-file filename
    (lambda ()
      (let ((expr (read)))
        (write expr)
        (newline)))))

(define (newline-count filename)
  (let ((in (open-input-file filename)))
    (define (count number_string)
      (let ((expr (read in)))
        (if (eof-object? expr)
            (begin 
              (close-input-port in) 
              number_string)
            (if (not (null? expr))
                (count (+ number_string 1))
                (count number_string)))))
    (count 0)))
(define (read-string input-port)
  (let ((c (read-char input-port)))
    (cond
      ((eof-object? c) c); проверка текста на конец-файла
      ((eq? c #\newline) '()); 
      (else (cons c (read-string input-port))))))
(define (newline-counter filename)
  (let ((in (open-input-file filename)))
    (define (count number_string)
      (let ((expr (read-string in)))
        (if (eof-object? expr)
            (begin 
              (close-input-port in) 
              number_string)
            (if (not (null? expr))
                (count (+ number_string 1))
                (count number_string)))))
    (count 0)))

;3

(define memoized-tribonacci
  (let ((memo '()))
    (lambda (n)
      (let ((memoized (assq n memo)))
        (if (not (equal? memoized #f))
            (cadr memoized)
            (let ((new-value
                   (if (<= n 1)
                       0
                       (if (= n 2)
                           1
                           (+ (memoized-tribonacci (- n 3)) (memoized-tribonacci (- n 2)) (memoized-tribonacci (- n 1)))))))
              (set! memo (cons (list n new-value) memo))
              (display memo)
              (newline)
              new-value))))))

(define count
  (let ((c 0))
    (lambda ()
      (set! c (+ c 2))
      c)))

;4
(define-syntax my-if
  (syntax-rules ()
    ((_ condition then_action else_action) (begin (let ((a (delay then_action))
                                                        (b (delay else_action)))
                                                    (or (and condition (force a)) (force b)))))))




