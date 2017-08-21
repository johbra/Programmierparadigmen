;;;SECTION 3.3.5

(load "cps-comparator.scm") 

(define tor-test 
  (lambda (v1 v2 i o)
    (let ((w (make-connector))
          (y (make-connector)))
      (tor  v1 v2 i o)
      'ok)))

 (define V1 (make-connector))
 (define V2 (make-connector))
 (define I (make-connector))
 (define O (make-connector))
 (tor-test V1 V2 I O )
 (probe "V1" V1)
 (probe "V2" V2)
 (probe "I" I)
 (probe "O" O)

 (set-value! I 1 'user)
 (set-value! V1 800 'user)
 (set-value! V2 700 'user)

 

