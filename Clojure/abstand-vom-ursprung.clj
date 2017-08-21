(use 'clojure.test)
;; Abstand eines Punktes vom Ursprung berechnen
;; abstand-vom-ursprung: punkt -> number
(def abstand-vom-ursprung
  (fn [p]
    {:pre  [(instance? punkt p)]
     :post [(number? %)]}
    (Math/sqrt
     (+ (* (:x p) (:x p))
        (* (:y p) (:y p))))))
;; Tests
(deftest test-absabstand-vom-ursprung
  (is (= (* 0.01 (Math/round (* 100 (abstand-vom-ursprung (->punkt 2 2)))))
         2.83)))

(run-tests)
