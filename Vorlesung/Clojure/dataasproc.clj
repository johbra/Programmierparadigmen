(ns dataasproc)
(use 'clojure.test)
;; (def cons
;;   (fn [x y]
;;     (let [dispatch
;;           (fn [m]
;;             (cond (= m 0) x
;;                   (= m 1) y
;;                   :else (throw (Exception. "Argument not 0 or 1 -- CONS"))))]
;;       dispatch)))

;; (def first (fn [z] (z 0)))

;; (def rest (fn [z] (z 1)))


(def cons
  (fn [x y]
    (fn [m] (m x y))))

(def first
  (fn [z] (z (fn [p q] p))))

(def rest
  (fn [z] (z (fn [p q] q))))

(def empty? (fn [z] (= z ())))

(def sum
  (fn [lon]
    (cond
      (empty? lon) 0
      :else (+ (first lon) (sum (rest lon))))))

(deftest test-sum
  (is (= 0 (sum ())))
  (is (= 12 (sum (cons 7 (cons 3 (cons 2 ())))))))

(run-tests)

(first (cons 3 4))
(first (fn [m] (m 3 4)))

(cons 3 4)

(first (fn [m] (m 3 4)) )

((fn [z] (z (fn [p q] p))) (fn [m] (m 3 4)) )

((fn [m] (m 3 4)) (fn [p q] p))

((fn [p q] p) 3 4)
