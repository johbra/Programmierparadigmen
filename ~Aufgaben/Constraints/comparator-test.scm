;;;SECTION 3.3.5

(load "cps-comparator.scm") 

(define comp-test 
  (lambda (v1 v2 i> i< o)
    (let ((w (make-connector))
          (y (make-connector)))
      (comparator  v1 v2 i> i< o)
      'ok)))

 (define V1 (make-connector))
 (define V2 (make-connector))
 (define I> (make-connector))
 (define I< (make-connector))
 (define O (make-connector))
 (comp-test V1 V2 I> I< O )
 (probe "V1" V1)
 (probe "V2" V2)
 (probe "I>" I>)
 (probe "I<" I<)
 (probe "O" O)

 (set-value! I< 1 'user)
 (set-value! I> 2 'user)
 (set-value! V1 300 'user)
 (set-value! V2 500 'user)

 

