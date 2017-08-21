(ns wechsle)
(use 'clojure.test)

(def sum
  (fn [lon]
    (cond
      (empty? lon) 0
      :else (+ (first lon) (sum (rest lon))))))

(def wechseln
  (fn [betrag muenzen]
    (cond
      (= 0 betrag) ()
      (empty? muenzen) false
      :else (let
                [l1 (wechseln betrag (rest muenzen)) 
                 l2 (wechseln (- betrag (first muenzen)) (rest muenzen))]
              (cond
                l1 l1 
                l2 (cons (first muenzen) l2)
                :else false)))))

(deftest wechsel-test
  (is (= (wechseln 100 '(10 100))         '(100)))
  (is (= (wechseln 100 '(10 50))          false))
  (is (= (wechseln 100 '(10 10 50 50 20)) '(50 50)))
  (is (= (wechseln 100 '(10 10 50 20 20)) '(10 50 20 20)))
  (is (= (wechseln 100 '(10 10 50 30))    '(10 10 50 30)))
  (is (= (wechseln 100 '(10 10 50 60))    false))
  (is (= (wechseln 100 '(90 60 50 20 10)) '(90 10))))

(run-tests)

