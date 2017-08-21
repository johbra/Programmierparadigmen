(ns sicp-cps.celsius-fahrenheit
  (:require [sicp-cps.core :refer :all]
            ))

(def celsius-fahrenheit-converter 
  (fn [c f]
    (let [u (make-connector)
          v (make-connector)
          w (make-connector)
          x (make-connector)
          y (make-connector)]
      (multiplier c w u)
      (multiplier v x u)
      (adder v y f)
      (constant 9 w)
      (constant 5 x)
      (constant 32 y)
      'ok)))

(def C (make-connector))
(def F (make-connector))
(celsius-fahrenheit-converter C F)
(probe "Celsius temp" C)
(probe "Fahrenheit temp" F)
;; (set-value! C 25 'user)
;; (set-value! F 212 'user)
;; (forget-value! C 'user)
;; (set-value! F 212 'user)
;; (forget-value! F 'user)
