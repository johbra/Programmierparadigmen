(ns sicp-cps.zinseszins
  (:require [sicp-cps.core :refer :all]
            ))

(def zinseszins 
  (fn [kn k0 p n]
    (let [a (make-connector)
          b (make-connector)
          c (make-connector)
          eins (make-connector)
          hundert (make-connector)]
      (multiplier k0 a kn)
      (adder eins b c)
      (multiplier hundert b p)
      (potentiator c n a)
      (constant 1 eins)
      (constant 100 hundert)
      'ok)))

 (def KN (make-connector))
 (def K0 (make-connector))
 (def P (make-connector))
 (def N (make-connector))
 (zinseszins KN K0 P N)
 (probe "KN" KN)
 (probe "K0" K0)
 (probe "P" P)
 (probe "N" N)

;; (set-value! N 50.93048347119194 'user)
;; (set-value! P 5 'user)
;; (set-value! K0 1000 'user)

;; (set-value! N 50.93048347119194 'user)
;; (set-value! P 5 'user)
;; (set-value! KN 12000 'user) 

;; (set-value! N 50.93048347119194 'user)
;; (set-value! KN 12000 'user)
;; (set-value! K0 1000 'user)

;; (set-value! KN 12000 'user)
;; (set-value! P 5 'user)
;; (set-value! K0 1000 'user)
