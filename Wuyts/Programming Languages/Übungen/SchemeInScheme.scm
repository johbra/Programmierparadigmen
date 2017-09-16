;; A scimple scheme and lisp interpreter

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Closure representation for Scheme
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define magic-closure-tag '*closure*)
(define (%closure? clos)   
  (and (pair? clos) 
       (eq? (car clos) magic-closure-tag)))

(define (%make-closure args body env)
  (list magic-closure-tag args body env))

(define (%closure-args clos)
  (cadr clos))
(define (%closure-body clos)
  (caddr clos))
(define (%closure-env clos)
  (cadddr clos))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; primitive representation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define magic-primitive-tag '*primitive*)

(define (%primitive? proc)   
  (and (pair? proc) 
       (eq? (car proc) magic-primitive-tag))) 

(define (%make-primitive symbol function)
  (list magic-primitive-tag symbol function))

(define (%primitive-function prim)
  (caddr prim))
(define (%primitive-symbol prim)
  (cadr prim))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;binding, binding table, and environment
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define %top-level-env '())

(define %primitive-symbols
  '(+ - * / = < > not equal? null? cons car cdr))

(define %primitive-functions
  (list + - * / = < > not equal? null? cons car cdr))

(define (%make-binding id val)
  ;; creates a binding for a table binding element
  (cons id val))

(define (%initialize-top-level-env)
  (set! %top-level-env 
        (%extend-env %primitive-symbols 
                     (map %make-primitive %primitive-symbols %primitive-functions) 
                     %top-level-env)))

(define (%extend-env lids lvals env)
  ;; add a new a binding table in front of the env
  (cons (map %make-binding lids lvals) env))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Controlling verbosity
;;    Flag: set to #f (default) to be non-verbose.
;;          set to #t to be verbose (print info during evaluation)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define *verbose* #f)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Simple Unit Testing
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;Add your code here so that when executing this file the transcript 
;; prints the following:
;        Nb of tests Run 43
;        Nb of tests Succeeded 43
;        Nb of tests Failed: 0


;;When this is done, uncomment the following two failing tests:
; (%%assert 'failing1 (+ 2 5) 400)
; (%%assert 'failing2 (> 3 0) #f)
;; The transcript should then show the following when you execute:
;        Nb of tests Run 45
;        Nb of tests Succeeded 43
;        Nb of tests Failed: 2
;        
;        Failed tests description
;        Format: (test-name result-of-expression expected-result)
;        ((failing2 #t #f) (failing1 7 400))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; test-environment
;;    Some simple tests use this simple test-environment
;;     for their assertions. Define and initialize it here.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define %test-env '())
(define (%initialize-test-env)
  (%initialize-top-level-env)
  (set! %test-env (%extend-env '(a b c z) '(1 2 3 26) %top-level-env)))
(%initialize-test-env)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; binding manipulation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define (%binding id env)
  ;; symbol * env -> binding
  ;; return the first binding whose car is id.
  ;; return #f when no binding is found
  (if (null? env)
      #f
      (or (assq id (car env)) (%binding id (cdr env)))))
   
(%%assert 'binding1 (%binding 'a %test-env) '(a . 1))
(%%assert 'binding2
        (%binding 'a '(((a . 200) (a . 1) (b . 2) (c . 3) (z . 26)))) '(a . 200))
(%%assert 'binding3
        (%binding 'e '(((a . 200) (a . 1) (b . 2) (c . 3) (z . 26)))) #f)
(%%assert 'binding4
        (%binding 'a '(((a . 200) (b1 . 3)) ((a . 1) (b . 2) (c . 3) (z . 26)))) '(a . 200))



(define (%lookup id env)
  ;; return the val of id in env
  ;; error if id is not defined in env
  
  (let ((binding (%binding id env)))
    (if (boolean? binding)
        (error "Error: unknown identifier " id)
        (cdr binding))))

(%%assert 'lookup1
        (%lookup 'a %test-env) 1)
 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; predicates to identify different objects
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; self-evaluating
(define (%self-evaluating? expr)
  (or (number? expr) (boolean? expr)))

;; special form
(define (%special? expr)
  (and (pair? expr)
       (member (car expr) 
               '(if let letrec lambda begin set! define quote))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; application and evaluation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (%apply proc largs)  
  (cond ((%primitive? proc) (%apply-internal proc largs))
        ((%closure? proc) (%apply-closure proc largs))
        (else (error "Bad ! Un-apply-able object !" proc))))

(define (%apply-internal primitive largs)   
  (apply (%primitive-function primitive) largs))

(define (%apply-closure proc largs)
  ;; apply a closure: evaluate proc body in 
  ;; extended the closure environment with
  ;; proc arguments and largs 
  (if *verbose* (printf "ApplyClosure extended env: ~a\n" (%extend-env (%closure-args proc) largs (%closure-env proc))))
  (%eval (%closure-body proc) 
         (%extend-env (%closure-args proc) largs (%closure-env proc))))
                      

(%%assert 'apply1
        (%apply-internal (%make-primitive '+ +) '(1 2 3)) 6)

(define (%eval-list l env)
  ;; list * env -> list of val
  (if (null? l)
      '()
      (cons (%eval (car l) env) (%eval-list (cdr l) env))))
          
(define (%eval expr env)
  (if *verbose* (printf "in eval ~a\n" expr) ())
  (cond ((%self-evaluating? expr) expr)                     
        ((symbol? expr) (%lookup expr env))                 
        ((%special? expr) (%eval-special expr env))
        (else 
         (%apply (%eval (car expr) env)                        
                 (%eval-list (cdr expr) env)))))

;; evaluating special forms

(define (%eval-special expr env)   
  (case (car expr)     
    ((lambda) (%eval-lambda (cdr expr) env))    
    ((if)     (%eval-if (cdr expr) env))    
    ((let)    (%eval-let (cdr expr) env))         
    ((letrec) (%eval-letrec (cdr expr) env))            
    ((set!)   (%eval-set! (cdr expr) env))        
    ((begin)  (%eval-sequence (cdr expr) env))    
    ((define) (%eval-define (cdr expr) env))      
    ((quote)  (%eval-quoted (cdr expr)))          
    (else     (error "Not yet implemented" (car expr))))) 

(define (%eval-quoted expr)
  (car expr))


(define (%eval-sequence lexpr env)
  ;; lexpr = (e1 e2 ...en)
  (if (null? lexpr) '()
      (let ((expr (car lexpr))
            (rest (cdr lexpr)))
        (if (null? rest)
            (%eval expr env)
            (begin
              (%eval expr env)
              (%eval-sequence rest env))))))
                      
(%%assert 'evl-sequence1
        (%eval-sequence '(a b c) %test-env) 3)
(%%assert 'eval-sequence2
        (%eval-sequence '(a b) %test-env) 2)
(%%assert 'eval-sequence3
        (%eval-sequence '(a) %test-env) 1)
(%%assert 'eval-sequence4
        (%eval-sequence '((+ 2 3) (+ 3 3)) %top-level-env) 6)

(%%assert 'apply-closure1
        (%apply-closure (%make-closure '(x) '(+ x 3) %test-env) '(100)) 103)

(define (%eval-if expr env)
  ;; evaluates the three-element list expr
  ;; expr = (e1 e2 e3)
  ;; where first element represents the conditional
  ;; second is the true case, and third is the false case
  (let ((bool (%eval (car expr) env)))
    (if bool
        (%eval (cadr expr) env)
        (%eval (caddr expr) env))))

(%%assert 'eval1
        (%eval '(if #t 3 4) %test-env) 3)
(%%assert 'eval2
        (%eval '(if #f 3 4) %test-env) 4)



(define (modify-env! id val env)
  (let ((binding (%binding id env)))
    (if (boolean? binding)
        (set-car! env (cons (%make-binding id val) (car env)))
        (set-cdr! binding val))))

(define (%eval-define expr env)
  ;; expr = (id expression)
  (modify-env! (car expr) (%eval (cadr expr) env) env)
  'undefined)

(%%assert 'eval-define1 
  (begin (%eval-define '(a 200) %test-env) (%lookup 'a %test-env))
  200)
(%%assert 'eval-define2
  (begin (%eval-define '(k 200) %test-env) (%lookup 'k %test-env))
  200)

    
(define (%eval-set! expr env)
  ;; expr = (id expression)
  (let ((binding (%binding (car expr) env)))
    (if (boolean? binding)
        (error "Identifier not defined" (car expr))
        (set-cdr! binding (%eval (cadr expr) env)))))

(%%assert 'eval-set!
  (begin (%eval-set! '(a 1999) %test-env) (%lookup 'a %test-env))
  1999)
;; to reset test-env to the revious state.
;;    (a propoer unit testing framework would do this for us automatically!)
(%eval-set! '(a 1) %test-env)


(define (%eval-let expr env)   
  ;; expr = (((x 3) (y 4)) x)
  (let ((lvars (map car (car expr)))          
        (lvals (map cadr (car expr)))          
        (body (cdr expr)))
    (%eval-sequence body (%extend-env lvars (%eval-list lvals env) env))))

(define (%eval-lambda expr env)
  ;; lambda in Scheme captures the environment at compile time
  ;; expr = ((x) (+ x 2))
  (if *verbose* (printf "evallambda arg: ~a env~a\n" (car expr) env) ())
  (%make-closure (car expr) (cadr expr) env))



  
(define (%extend-env-rec lids lexp env)
  ;; return a new environment in which 
  ;; lexp have been evaluated in an extended envronment in which
  ;; lids were predefined. 
  (let ((envRec (%extend-env lids lexp env)))
    (let ((newBindingTable (car envRec)))
      (for-each (lambda (binding exp)
                  (set-cdr! binding (%eval exp envRec)))
                newBindingTable lexp)
      envRec)))

(%%assert 'extend-env-rec
        (car (%extend-env-rec '(a b) '((+ 2 3) (+ 45)) %test-env)) '((a . 5) (b . 45)))

(define (%eval-letrec expr env)
  ;; expr = (((x 3) (y 4)) body)
  
  (let ((lvars (map car (car expr)))          
        (lvals (map cadr (car expr)))          
        (body (cdr expr)))
    (%eval-sequence body (%extend-env-rec lvars lvals env ))))

(%%assert 'eval-letrec1
        (%eval '(letrec ((fac 3) (yop 2))
                  (+ fac yop))
               %top-level-env) 5)

(%%assert 'eval-letrec2
        (%eval '(letrec ((fac (+ 3 4)) (yop 2))
                  (+ fac yop))
               %top-level-env) 9)

(%%assert 'eval-letrec3
        (%eval '(letrec ((fac (= 3 4)) (yop #f))
                   fac)
               %top-level-env) #f)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; the read-eval-loop
(define (%read)            
  (printf "? ")   
  (read)) 

(define (%print val)      
  (printf "--> ~a\n" val)
  (newline))

(define (%rep)
  ;; the read-eval-loop
  ;; enter quit to quit !sch
  
  (let ((expr (%read)))
    (if (eq? 'quit expr)
        ()
        (begin
          (%print (%eval expr %top-level-env))
          (%rep)))))

(define (%sch)
  (%initialize-top-level-env)
  (%rep))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(%%assert 'extend-test-env
          (%closure? (%eval-lambda '((x) (+ x 2)) %test-env))
          #t)
(%%assert 'eval-2 (%eval '(let ((x 99) (z 44)) x) %test-env) 99)
(%%assert 'eval-3 (%eval 'z %test-env) 26)
(%%assert 'eval-4 (%eval '(begin a) %test-env) 1)
(%%assert 'eval-5 (%eval 'a %test-env) 1)
(%%assert 'eval-6 (%eval ''a %test-env) 'a)
(%%assert 'eval-primitive (%primitive? (%eval '+ %test-env)) #t)
(%%assert 'eval-7 (%eval '(+ 2 3) %test-env) 5)
(%%assert 'eval-8 (%eval '(let ((x 2) (z 3)) (+ x z)) %test-env) 5)
(%%assert 'eval-9 (%eval '((lambda (x) (+ x x)) 5) %top-level-env) 10)
(%%assert 'eval-10 (%eval '(let ((a 1000)) (+ a 2)) %test-env) 1002)
(%%assert 'addLambda
          (%closure? (%eval '(let ((a 1000)) 
                              (lambda (x) (+ a 2))) %top-level-env))
          #t)
(%%assert 'eval-11 (%eval '(let ((a 1000)) 
          ((lambda () (+ a 22)))) %top-level-env) 1022)

(%%assert 'eval-12 (%eval '(let ((x (+ 4 5))) x) %top-level-env) 9)

(%%assert 'eval-13 (%eval '(let ((f (lambda (x) (+ x 2))))
                  (f 5)) %top-level-env) 7)

 
(%%assert 'eval-14 (%eval '(let ((a 10))
          (let ((f (lambda (x) (+ x a))))
            (let ((a 0))
              (f 5)))) %top-level-env) 15)


(%%assert 'eval-15 (%eval  '(let ((a 10))
                   (let ((f (lambda (x) (+ x a))))
                     (set! a 0)
                     (f 5))) %top-level-env) 5)

(%%assert 'eval-16 (%eval '(begin 
                  (define fac 
                    (lambda (n) 
                      (if (= 0 n) 
                          1 
                          (* n (fac (- n 1))))))
                  (fac 5))
               %top-level-env) 120)

(%%assert 'eval-17 (%eval '(let ((app (lambda (f y) (f y)))
                      (y 10))
                  (app (lambda (z) (+ z y)) 1)) 
               %top-level-env) 11)


(%%assert 'eval-18 (%eval '(let ((add (lambda (x) (lambda (y) (+ x y)))))
                  (let ((add2 (add 2)))
                    (add2 4)))
               %top-level-env) 
        6)



(%%assert 'eval-19 (%eval '(if (= 2 3) #t #f) %top-level-env) #f)

(%%assert 'eval-20 (%eval '(letrec ((fac (lambda (n) 
                                (if (= 0 n) 
                                    1 
                                    (* n (fac (- n 1)))))))
                  (fac 5))
               %top-level-env) 120)


(%%assert 'eval-21 (%eval '(letrec ((f (lambda (x) (if (= 0 x) 0 (+ 1 (g (- x 1))))))
                         (g (lambda (x) (if (= 0 x) 0 (f (- x 1))))))
                  (f 5))                               
               %top-level-env) 3)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Test for normal order evaluation
;;;   Only uncomment after exercise 1 is finished!
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;(%%assert 'define-try
;          (%eval '(define try (lambda (a b) (if (= a 0) 1 b))) %test-env)
;          'undefined)
;(%%assert 'normal-order1 (%eval '(try 0 (/ 1 0)) %test-env) 1)


(%%show-test-results)
