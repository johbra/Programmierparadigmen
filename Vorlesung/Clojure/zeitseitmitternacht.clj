(use 'clojure.test)
;; die Uhrzeit besteht aus
;; - einer Zahl für die Stunden
;; - einer Zahl für die Minuten
;; - einer Zahl für die Sekunden
;; ->uhrzeit: (number number number -> uhrzeit)
;; :uhrzeit-h : (uhrzeit -> number)
;; :uhrzeit-m : (uhrzeit -> number)
;; :uhrzeit-s : (uhrzeit -> number))

(defrecord uhrzeit
    [uhrzeit-h uhrzeit-m uhrzeit-s])

;; berechnet die Sekunden seit Mitternacht
(def zeit-seit-mitternacht 
  (fn [u]
    {:pre  [(instance? uhrzeit u)]
     :post [(number? %)]}
    (+ (* 3600 (:uhrzeit-h u))
       (* 60   (:uhrzeit-m u))
       (:uhrzeit-s u))))

(deftest uhrzeit-test
  (is (= (zeit-seit-mitternacht (->uhrzeit 1 2 3)) 3723)))

(run-tests)

