;;;SECTION 3.3.5

(load "cps-comparator.scm") 

(define iter 
  (lambda (in out x)
    (let ((a (make-connector))
          (b (make-connector))
          (f (make-connector))
          (h (make-connector))
          (y (make-connector))
          (z (make-connector))
          (ou (make-connector))
          (g (make-connector)))
      (buffer in ou a)
      (buffer g z b)
      (adder a g f)
      (multiplier b f y)
      (tor2 h f f ou  out)
      (tor2 h f y z x)
      (constant 1 g)
      (constant 6 h)
      'ok)))

 (define In (make-connector))
 (define Out (make-connector))
 (define X (make-connector))
 (iter In Out X)
 (probe "IN" In)
 (probe "Out" Out)
 (probe "X" X)

(set-value! In 1 'user)

