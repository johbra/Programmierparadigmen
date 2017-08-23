(ns sicp-cps.primitives
  (:require [sicp-cps.core :refer :all]))


(def A (make-connector))
(def B (make-connector))
(def C (make-connector))

;; (multiplier A B C )
(potentiator A B C)
(probe "a" A)
(probe "b" B)
(probe "c" C)


;; (set-value! A 3 'user)
;; (set-value! B 0.5 'user)
;; (set-value! C 27 'user)
;; (forget-value! A  'user)
;; (forget-value! B  'user)
;; (forget-value! C  'user)

