(ns nat)
(use 'clojure.test)

;; z ist eine natürliche Zahl (steht für die 0, zero).
(def z 'z)

;; + (s N) ist eine natürliche Zahl, wenn N eine natürliche Zahl ist.
;; s: Nat -> Nat
(def s
  (fn [n] (list 's n)))

(deftest test-s
  (is (= (s z) (s z)))
  (is (=  (s (s z)) (s (s z)))))

;; liefert den Term des Vorgaengers ihres Arguments
;; pred: Nat -> Nat
(def pred
  (fn [n]
    (cond
      (= n z) (throw  (Exception. "z hat keinen Vorgaenger"))
      :else
      (first (rest n)))))

(deftest test-pred
  (is (=  (pred (s z)) z))
  (is (=  (pred (s (s z))) (s z)))
  (is (thrown? Exception (pred z))))

;; berechnet die Summe ihrer Argumente
;; plus: Nat Nat -> Nat
(def plus
  (fn [n m]
    (cond 
      (= n z) m
      :else (s (plus (pred n) m)))))

(deftest test-plus
  (is (= (plus z (s z))  (s z)))
  (is (= (plus (s z) (s z))
         (s (s z))))
  (is (= (plus (s z)
               (plus (s z) (s z)))
         (s (s (s z))))))

;; Multiplikation von zwei natürlichen Zahlen aus Nat
;; times: Nat Nat -> Nat
;; Axiome
;; n,m aus Nat
;; 1. times(0,m) = 0
;; 2. times(n,m) = plus(times(pred(n),m),m)
(def times
  (fn [f1 f2]
    (cond
      (= f1 z) z
      :else (plus (times (pred f1) f2) f2))))

(deftest test-times
  (is (= (times z z) z))
  (is (= (times z (s z)) z))
  (is (= (times (s (s z)) (s (s z))) (s (s (s (s z)))))))


(run-tests)
