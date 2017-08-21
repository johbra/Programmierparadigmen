(use 'clojure.test)

;; Konstanten - monatliche Sollarbeitszeit in Stunden
(def sollstunden 160)


;; Ein Festangestellter wird definiert durch
;; - seinen Namen
;; - sein Grundgehalt,
;; - die im letzten Monat geleisteten Arbeitsstunden.
;; ->angestellter : string number number -> angestellter
;; :angestellter-name    : angestellter -> string
;; :angestellter-gehalt  : angestellter -> number
;; :angestellter-stunden : angestellter -> number

(defrecord angestellter
    [angestellter-name angestellter-gehalt angestellter-stunden])

;; Ein Werkstudent wird definiert durch
;; - seinen Namen,
;; - seinen Stundenlohn,
;; - die im letzten Monat geleisteten Arbeitsstunden.
;; ->student : string number number -> student
;; :student-name    : student -> string
;; :student-lohn    : student -> number
;; :student-stunden : student -> number

(defrecord student
    [student-name student-lohn student-stunden])


;; berechnet den Ueberstundenzuschlag eines Angestellten:
;; Gehalt pro Ueberstunde = (Gehalt / sollstunden) * 1.25 
;; ueberstunden = ist stunden - soll stunden
;; zuschlag = ueberstunden * gehalt pro ueberstunde 
(def ueberstunden-zuschlag
  (fn [fa]
    {:pre [(instance? angestellter fa)]
     :post [(number? %)]}
    (cond
      (> (:angestellter-stunden fa) sollstunden)
      (* (/ (:angestellter-gehalt fa) sollstunden) 5/4
         (- (:angestellter-stunden fa) sollstunden))
      :else 0)))

(deftest test-ueberstundenZuschlag
  (is (= (ueberstunden-zuschlag (->angestellter "Karl" 2000 170)) (+ 156 1/4)))
  (is (= (ueberstunden-zuschlag (->angestellter "Franz" 2000 140)) 0)))

;; berechnet Bruttomonatslohn eines Angestellten aus:
;; Grundgehalt + Überstundenentgelt

(def angestellter-monatslohn
  (fn [a]
    {:pre [(instance? angestellter a)]
     :post [(number? %)]}
    (+ (:angestellter-gehalt a) (ueberstunden-zuschlag a))))

(deftest test-angestellter-monatslohn
  (is (= (angestellter-monatslohn (->angestellter "Meier" 1000 160)) 1000))
  (is (= (angestellter-monatslohn 
          (->angestellter "Mueller" 2000 170)) (+ 2156 1/4))))

;; berechnet Bruttomonatslohn eines Studenten aus:
;; Stundenlohn x Arbeitsstunden
(def student-monatslohn
  (fn [st]
    {:pre [(instance? student st)]
     :post [(number? %)]}
    (* (:student-stunden st) (:student-lohn st))))

(deftest test-student-monatslohn
  (is (= (student-monatslohn (->student "Karl" 8 150)) 1200)))


;; Ein Mitarbeiter ist entweder
;; - ein Festangesteller
;; - ein Werksstudent
(def mitarbeiter? #(or (instance? angestellter %)
                       (instance? student %)))

;; berechnet Bruttomonatslohn eines Mitarbeiters
(def monatslohn
  (fn [m]
    {:pre [(mitarbeiter? m)]
     :post [(number? %)]}
    (cond
      (instance? angestellter m) (angestellter-monatslohn m)
      (instance? student m) (student-monatslohn m))))

(deftest test-monatslohn
  (is (= (monatslohn (->angestellter "Mueller" 2000 170)) (+ 2156 1/4)))
  (is (= (monatslohn  (->angestellter "Hans" 2000 140)) 2000))
  (is (= (monatslohn  (->student "Franz" 10 150)) 1500)))

(run-tests)
