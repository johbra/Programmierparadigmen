
(load "cps-exponent.scm")

(define annuitaeten 
  (lambda (a n p r)
    (let* (
          (u (make-connector))
          (v (make-connector))
          (w (make-connector))
          (eins (make-connector))
          (y (make-connector))
          (x (make-connector))
          )
      (adder r eins u)
      (exponent u n v)
      (multiplier w v eins)
      (adder x w eins)
      (multiplier p x y)
      (multiplier a r y)
      (constant 1 eins)
      'ok)))

 (define A (make-connector))
 (define N (make-connector))
 (define P (make-connector))
 (define R (make-connector))
 
(annuitaeten A N P R)
 (probe "A" A)
 (probe "R" R)
 (probe "P" P)
 (probe "N" N)

(set-value! A 10000 'user)
(set-value! P 888.487886783417 'user)
;(set-value! R 0.01 'user)
(set-value! N 12 'user)
