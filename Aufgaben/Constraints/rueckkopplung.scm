

(load "cps-exponent.scm")

(define rueck 
  (lambda (x y)
    (let ((u (make-connector))
          (z (make-connector))
;          (hundert (make-connector))
          )
      (adder x u z)
      (multiplier u y z)
      'ok)))


 (define X (make-connector))
 (define Y (make-connector))
 (rueck  X Y)
 (probe "X" X)
 (probe "Y" Y)


(set-value! X 12 'user)

