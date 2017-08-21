(ns relativ-absolut-akku.clj)
(use 'clojure.test)

(def relativ->absolut
  (fn [lvz]
    (letfn
        [;; Akkumulatorinvariante: akku-distanz ist die akku-
         ;; mulierte Distanz derjenigen Elemente von lvz, die
         ;; denen aus lvz1 vorangehen.
         (relabs [lvz1 akku-distanz]
           (cond
             (empty? lvz1) ()
             :else 
             (cons (+ akku-distanz (first lvz1))
                   (relabs (rest lvz1) 
                           (+ akku-distanz (first lvz1))))))]
      (relabs lvz 0))))

(deftest test-relativ->absolut
  (is (= (relativ->absolut '(50 40 70 30 30))
         (list 50 90 160 190 220))))
(run-tests)
