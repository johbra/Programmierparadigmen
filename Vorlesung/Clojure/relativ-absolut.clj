(ns relativ-absolut.clj)
(use 'clojure.test)

(def addiere-auf-jede 
  (fn [zahl lvz]
    (map (fn [z] (+ z zahl)) lvz)))

(def relativ->absolut
  (fn [lvz]
    (cond
      (empty? lvz) ()
      :else 
      (cons (first lvz)
            (addiere-auf-jede 
             (first lvz) 
             (relativ->absolut (rest lvz)))))))

(deftest test-relativ->absolut
  (is (= (relativ->absolut '(50 40 70 30 30))
         (list 50 90 160 190 220))))
(run-tests)
