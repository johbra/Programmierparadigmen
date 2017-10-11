;;;SECTION 3.3.5

(load "cps-comparator.scm") 

(define n-f 
  (lambda (p c n f q e)
    (let (
;          (q (make-connector))
          (r (make-connector))
          (d (make-connector))
          (ff (make-connector))
;          (g (make-connector))
          (pp (make-connector))
          (h (make-connector))
;          (e (make-connector))
          )
      (buffer p pp q)
      (buffer c h e)
;      (tor n d d h)
;      (tor n d r f)
;      (adder e g d) 
;      (multiplier q e r)
      (tor2 n e ff pp f)
      (multinc q e r d)
      (d-gate n e d h r ff) 
;      (constant 1 g)
      'ok)))

 (define N (make-connector))
 (define F (make-connector))
 (define P (make-connector))
 (define C (make-connector))
 (define Q (make-connector))
 (define E (make-connector))
 (n-f P C N F Q E) 
 (probe "N" N)
 (probe "Fak" F)
 (probe "Q" Q)
 (probe "E" E)

(set-value! C 1 'user)
(set-value! P 1 'user)

