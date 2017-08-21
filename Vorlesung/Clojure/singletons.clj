(ns singletons.clj)
(use 'clojure.test)

;; Loesung ohne Verwendung akkumulierender Parameter

;;Test-Hilfsprozedur
;;erzeugt Liste mit anzahl-maligem Auftreten von x
(def vervielfache
  (fn [x anzahl]
    (cond (<= anzahl 0) ()
          :else (cons x (vervielfache x (- anzahl 1))))))


;; entfernt jedes Duplikat aus list
;; singletons: (list-of atom) -> (list-of atom)
(def singletons
  (fn [list]
    (cond
      (empty? list) ()
      :else 
      (let [singlerest (singletons (rest list))]
        (cond
          (some #(= % (first list)) (rest list))
          (remove #(= % (first list)) singlerest)
          :else (cons (first list) singlerest))))))

(deftest test-singletons
  (is (= (singletons ()) ()))
  (is (= (singletons '(1 2 3 3 2 1 4 5 4)) '(5)))
  (is (= (singletons '(1 1 5 2 3 3 2 1 4 5 4)) '()))
  (is (= (singletons (vervielfache 'a 100)) '())))

(run-tests)
