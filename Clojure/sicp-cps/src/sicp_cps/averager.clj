(ns sicp-cps.averager
  (:require [sicp-cps.core :refer :all]))

(def averager 
  (fn [a b c d]
    (let [w (make-connector)
          x (make-connector)
          y (make-connector)]
      (adder a b x )
      (adder x c y)
      (constant 3 w)
      (multiplier w d y)
      'ok)))

(def A (make-connector))
(def B (make-connector))
(def C (make-connector))
(def D (make-connector))

(averager A B C D)
(probe "a" A)
(probe "b" B)
(probe "c" C)
(probe "d" D)

;; (set-value! A 6 'user)
;; (set-value! B 4 'user)
;; (set-value! C 5 'user)
;; (set-value! D 20 'user)
;; (forget-value! A  'user)
;; (forget-value! B  'user)
;; (forget-value! C  'user)
;; (forget-value! D  'user)

