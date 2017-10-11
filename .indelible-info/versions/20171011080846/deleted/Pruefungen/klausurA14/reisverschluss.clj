(use 'clojure.test)
;; Die Funktion liefert, angewendet auf zwei gleich lange Listen von Symbolen
;; eine Liste, die jeweils abwechselnd ein Symbol der ersten und
;; ein Symbol der zweiten Argumentliste enthält.
(def reisverschluss
  (fn [lst1 lst2]
    {:pre  [(every? symbol? lst1) (every? symbol? lst2)
            (= (count lst1) (count lst2))]
     :post [(every? symbol? %)]}
    (cond
      (empty? lst1) '()
      :else (cons (first lst1)
                  (cons (first lst2)
                        (reisverschluss (rest lst1) (rest lst2)))))))

(deftest rv-test
  (is (= (reisverschluss '() '()) '()))
  (is (= (reisverschluss '(a b c) '(x y z)) '(a x b y c z))))

(run-tests)


