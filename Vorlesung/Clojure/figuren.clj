(use 'clojure.test)

;; Eine Rechteck besteht aus
;; - einer Zahl für die Höhe,
;; - einer Zahl für die Breite
;; ->rechteck:  number number -> rechteck
;; :breite :    (rechteck -> number))
;; :hoehe :     (rechteck -> number)

(defrecord rechteck
    [breite hoehe])

;; Rechteckfläche berechnen
(def rechteck-flaeche
  (fn [r]
    {:pre  [instance? rechteck r]
     :post [number? %]}
    (* (:breite r) (:hoehe r))))

(deftest test-rechteck
  (is (= (rechteck-flaeche (->rechteck 3 4)) 12)))

;; Ein Kreis besteht aus
;; - einer Zahl für den Radius,
;; ->kreis:  number -> kreis
;; :radius : kreis -> number

(defrecord kreis
    [radius])

;; Rechteckfläche berechnen
(def kreis-flaeche
  (fn [k]
    {:pre  [instance? kreis k]
     :post [number? %]}
    (* (:radius k) (:radius k) 3.14159)))

(deftest test-kreis
  (is (= (* 0.01 (Math/round (* 100  (kreis-flaeche (->kreis 1.0))))) 3.14)))

;; Eine figur ist entweder
;; - ein Rechteck
;;     (->rechteck b h)
;; oder
;; - ein Kreis
;;     (->kreis r)
(def figur-flaeche
  (fn [f]
    {:pre [(or (instance? rechteck f)
               (instance? kreis f))]
     :post [number? %]}
    (cond
      (instance? kreis f)
      (kreis-flaeche f)
      (instance? rechteck f)
      (rechteck-flaeche f))))

(deftest test-figuren
  (is (= (* 0.01 (Math/round (* 100  (figur-flaeche (->kreis 1.0))))) 3.14))
  (is (= (figur-flaeche (->rechteck 3 4)) 12)))

(run-tests)
(figur-flaeche (->rechteck 3 4))

