;; Beispiel für die Nutzung von thunks
(def my-if (fn [x y z] (if x (y) (z))))

(def factorial
  (fn [x]
    (my-if (= x 0)
           (fn [] 1)
           (fn [] (* x (factorial (- x 1)))))))

(factorial 5)

