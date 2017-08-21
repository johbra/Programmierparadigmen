(ns singletons-akku.clj)
(use 'clojure.test)

;; Loesung mit akkumulierenden Parametern

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
    (letfn
        [;; Fuer den Listenkopf ueberpruefe zuerst, ob er schon in
         ;; multis vorhanden ist. 
         ;; Wenn ja, braucht er nicht weiter verarbeitet zu werden. 
         ;; Wenn nein, muss er zu multis hinzugefuegt werden, 
         ;; wenn er im Listenrumpf noch einmal auftritt, sonst zu
         ;; singles.
         (singletonsAccu
           [list0 singles multis]
           (cond 
             (empty? list0) singles

             (some #(= % (first list0)) multis)
             (singletonsAccu (rest list0) singles multis)

             (some #(= % (first list0)) (rest list0))
             (singletonsAccu (rest list0) 
                             singles 
                             (cons (first list0) multis))
             :else
             (singletonsAccu (rest list0) 
                             (cons (first list0) singles) 
                             multis)))]
      (singletonsAccu list () ()))))

(deftest test-singletons
  (is (= (singletons ()) ()))
  (is (= (singletons '(1 2 3 3 2 1 4 5 4)) '(5)))
  (is (= (singletons '(1 1 5 2 3 3 2 1 4 5 4)) '()))
  (is (= (singletons (vervielfache 'a 100)) '())))

(run-tests)
