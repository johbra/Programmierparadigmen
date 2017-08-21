(ns dosl2clj.clojure-session
  (:require [dosl2clj.core :refer :all]
            [serializable.fn :as sf]))

;; Ein paar Kleinigkeiten zu Clojure
;;; (Beispiele zum Teil aus dem Clojure Cookbook)

+ Objekte sind Maps:

{:name ""
 :class :barbarian
 :race :half-orc
 :level 20
 :skills [:bashing :hacking :smashing]}


;;; Retrieving Values from a Map

(get {:name "Kvothe" :class "Bard"} :name)

(get {:name "Kvothe" :class "Bard"} :race)

(get {:name "Kvothe" :class "Bard"} :race "Human")

(:name {:name "Marcus" :class "Paladin"})

(:race {:name "Marcus" :class "Paladin"} "Human")

(def character {:name "Brock" :class "Barbarian"})

(character :class)

(def square (fn [x] (* x x)))
(def square {1 1, 2 4, 3 9, 4 16, 5 25})
(square 6)

;; convenience function for looking up items in nested maps: get-in.

(get-in {:name "Marcus" :weapon {:type :greatsword :damage "2d6"}}
        [:weapon :damage])

(get-in [{:name "Marcus" :class "Paladin"}
         {:name "Kvothe" :class "Bard"}
         {:name "Felter" :class "Druid"}]
        [1 :class])

;;; Setting Keys in a Map

(def villain {:honorific "Dr." :name "Mayhem"})
villain

(assoc villain :occupation "Mad Scientist" :status :at-large)

(def villain {:honorific "Dr.", :name "Mayhem",
              :occupation "Mad Scientist", :status :at-large})
(assoc villain :status :deceased)

(def book {:title "Clojure Cookbook" :author {:name "Ryan Neufeld"
                                              :residence {:country "USA"}}})

;;; Ändern von "USA" in "Canada"

(assoc book :author
       (assoc (:author book) :residence
              (assoc (:residence (:author book)) :country "Canada")))

;;;; oder besser
(assoc-in book
          [:author :residence :country]
          "Canada")

;;;; Ändern eines Eintrags auf der Grundlage seines alten Wertes:

(def website {:clojure-cookbook {:hits 1236}})
;; Register 101 new hits to the Cookbook website (update-in website ;
[:clojure-cookbook :hits] ; +;
(update-in website 
           [:clojure-cookbook :hits]
           +
           101)

;;; Using Composite Values as Map Keys

(def chessboard {[:a 5] [:white :king] [:a 4] [:white :pawn]
                 [:d 4] [:black :king]})

(chessboard [:a 5])

(defn move
  "Given a map representing a chessboard, move the piece at src to dest"
  [board source dest]
  (-> board
      (dissoc source)
      (assoc dest (board source))))

(move chessboard [:a 5] [:a 4])

(defn mov
  [board source dest]
  (assoc (dissoc board source) dest (board source)))

;;; OOP with functional objects
;; für objektorientierte Programmierung bedarf es keiner speziellen
;; Programmiersprachen -- auch in Assembler möglich.

;; Objektorientierung besteht nur aus einer Reihe von Konventionen zum
;; Umgang mit Daten.

;;Diese Konventionen werden von objektorientierten Sprachen explizit und implizit unterstützt.

;; Objekte sind nichts weiter als
;;  + eine Menge von =Name->Wert=-Abbildungen
;;  + eine Reihe von Funktionen, die solche Abbildungen als erstes Argument akzeptieren und
;;  + eine Verteilfunktion, die ermittelt, welche dieser Funtkion aufzurufen ist.

;; Eine Programmiersprache sollte vielleicht nicht das Programmierparadigma vorschreiben.

(defn person [n v]
  (let [name n
        vorname v
        get-name (fn [] name)
        get-vorname (fn [] vorname)
        vollname (fn [] (str vorname " " name))
        gruss (fn [grussformel] (str grussformel " " 
                                     (vollname)))]
    {:get-name get-name
     :get-vorname get-vorname
     :vollname vollname
     :gruss gruss}))

(def p0 (person "Gans" "Gustav"))

                                        ;(def p (person "Gans" "Gustav"))

((p0 :gruss) "Guten Tag")

;;DOSL2
(obj {:name "Gans" :vorname "Gustav"} {})

(def p1 (obj {:name "Gans" :vorname "Gustav"} {}) )

(p1 :vorname)
(p1 :inspect)
(p1 :data)
(p1 :methods)
(p1 :class)

(def p2
  (obj {:name "Gans" :vorname "Gustav"}
       {:vollname (fn [] (str (self :vorname) " " (self :name)))} ))
(p2 :self)
(p2 :vollname)

((obj {:name "Gans" :vorname "Gustav"} {:vollname (fn [] (str (self :vorname) " " (self :name)))} ) :kappa 'Person)

((Person :new) :data)

((Person :new {:nachname "Düsentrieb"}) :data)

(def p3 (Person :new {:name "Düsentrieb" :vorname "Daniel"}))

(p3 :vollname)

((p3 :class) :class-name)
((p3 :class) :super-class)

(def p4 (p3 :add-instvar {:studienrichtung "AI"}))
(p4 :data)
((p4 :class) :class-name)

(p4 :kappa 'Studi 'Person)

(def p5 (Studi :new))

(p5 :data)

(p5 :vollname)

(((p5 :class) :super-class) :class-name)


