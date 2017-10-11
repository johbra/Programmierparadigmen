;; constraint propagation system nach 
;; Harold Abelson and Gerald Jay Sussman:
;; Structure and Interpretation of Computer Programs
;; http://mitpress.mit.edu/sicp/full-text/book/book.html

(define adder
  (lambda (a1 a2 sum)
    (letrec 
        ([process-new-value
          (lambda ()
            (cond ((and (has-value? a1) (has-value? a2))
                   (set-value! sum
                               (+ (get-value a1) (get-value a2))
                               me))
                  ((and (has-value? a1) (has-value? sum))
                   (set-value! a2
                               (- (get-value sum) (get-value a1))
                               me))
                  ((and (has-value? a2) (has-value? sum))
                   (set-value! a1
                               (- (get-value sum) (get-value a2))
                               me))))]
         [process-forget-value
          (lambda ()
            (forget-value! sum me)
            (forget-value! a1 me)
            (forget-value! a2 me)
            (process-new-value))]
         [me 
          (lambda (request)
            (cond ((eq? request 'I-have-a-value)  
                   (process-new-value))
                  ((eq? request 'I-lost-my-value) 
                   (process-forget-value))
                  (else 
                   (error "Unknown request -- ADDER" request))))])
      (connect a1 me)
      (connect a2 me)
      (connect sum me)
      me)))

(define inform-about-value
  (lambda (constraint)
    (constraint 'I-have-a-value)))

(define inform-about-no-value
  (lambda (constraint)
    (constraint 'I-lost-my-value)))

(define multiplier
  (lambda (m1 m2 product)
    (letrec
        ([process-new-value 
          (lambda ()
            (cond ((or (and (has-value? m1) (= (get-value m1) 0))
                       (and (has-value? m2) (= (get-value m2) 0)))
                   (set-value! product 0 me))
                  ((and (has-value? m1) (has-value? m2))
                   (set-value! product
                               (* (get-value m1) (get-value m2))
                               me))
                  ((and (has-value? product) (has-value? m1))
                   (set-value! m2
                               (/ (get-value product) (get-value m1))
                               me))
                  ((and (has-value? product) (has-value? m2))
                   (set-value! m1
                               (/ (get-value product) (get-value m2))
                               me))))]
         [process-forget-value (lambda ()
                                 (forget-value! product me)
                                 (forget-value! m1 me)
                                 (forget-value! m2 me)
                                 (process-new-value))]
         [me (lambda (request)
               (cond ((eq? request 'I-have-a-value)
                      (process-new-value))
                     ((eq? request 'I-lost-my-value)
                      (process-forget-value))
                     (else
                      (error "Unknown request -- MULTIPLIER" request))))])
      (connect m1 me)
      (connect m2 me)
      (connect product me)
      me)))

(define constant 
  (lambda (value connector)
    (letrec 
        ([me 
          (lambda (request)
            (error "Unknown request -- CONSTANT" request))])
      (connect connector me)
      (set-value! connector value me)
      me)))

(define probe
  (lambda (name connector)
    (letrec 
        ([print-probe 
          (lambda (value)
            (newline)
            (display "Probe: ")
            (display name)
            (display " = ")
            (display value))]
         [process-new-value 
          (lambda ()
            (print-probe (get-value connector)))]
         [process-forget-value
          (lambda ()
            (print-probe "?"))]
         [me 
          (lambda (request)
            (cond ((eq? request 'I-have-a-value)
                   (process-new-value))
                  ((eq? request 'I-lost-my-value)
                   (process-forget-value))
                  (else
                   (error "Unknown request -- PROBE" request))))])
      (connect connector me)
      me)))

(define make-connector
  (lambda ()
    (let ((value false) (informant false) (constraints '()))
      (letrec 
          ([set-my-value 
            (lambda (newval setter)
              (cond ((not (has-value? me))
                     (set! value newval)
                     (set! informant setter)
                     (for-each-except setter
                                      inform-about-value
                                      constraints))
                    ((not (= value newval))
                     (error "Contradiction" (list value newval)))
                    (else 'ignored)))]
           [forget-my-value 
            (lambda (retractor)
              (if (eq? retractor informant)
                  (begin (set! informant false)
                         (for-each-except retractor
                                          inform-about-no-value
                                          constraints))
                  'ignored))]
           [connect 
            (lambda (new-constraint)
              (if (not (memq new-constraint constraints))
                  (set! constraints 
                        (cons new-constraint constraints)))
              (if (has-value? me)
                  (inform-about-value new-constraint))
              'done)]
           [me 
            (lambda (request)
              (cond ((eq? request 'has-value?)
                     (if informant true false))
                    ((eq? request 'value) value)
                    ((eq? request 'set-value!) set-my-value)
                    ((eq? request 'forget) forget-my-value)
                    ((eq? request 'connect) connect)
                    (else (error "Unknown operation -- CONNECTOR"
                                 request))))])
        me))))

(define for-each-except 
  (lambda (exception procedure list)
    (letrec 
        ([loop 
          (lambda (items)
            (cond ((null? items) 'done)
                  ((eq? (car items) exception) (loop (cdr items)))
                  (else (procedure (car items))
                        (loop (cdr items)))))])
      (loop list))))

;; has-value?: connector -> boolean
;; sagt, ob Konnektor einen Wert hat
(define has-value?
  (lambda (connector)
    (connector 'has-value?)))

;; get-value: connector -> any
;; liefert den Wert eines Konnektors
(define get-value 
  (lambda (connector)
    (connector 'value)))

;; set-value!: connector any constraint -> unspecified
;; zeigt an, dass ein constraint den Wert eines Konnektors
;; setzen will
(define set-value! 
  (lambda (connector new-value informant)
    ((connector 'set-value!) new-value informant)))

;; forget-value!: connector constraint -> unspecified
;; zeigt an, dass ein constraint den Wert eines Konnektors
;; vergessen machen will
(define forget-value! 
  (lambda (connector retractor)
    ((connector 'forget) retractor)))

;; connect: connector constraint -> ?
;; verbindet einen Konnektor mit einem neuen constraint
(define connect 
  (lambda (connector new-constraint)
    ((connector 'connect) new-constraint)))
