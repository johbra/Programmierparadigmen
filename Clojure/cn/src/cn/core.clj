(ns cn.core
  (:refer-clojure :exclude [fn])
  (:require 
   [clojure.pprint :refer :all :as pp]
   [serializable.fn :refer :all :as fn]) )
(use 'clojure.test)

;; Eine Programmiersprache mit Funktionen als Werte erste Ordnung ermöglicht es, auf
;; ganze Zahlen zu verzichten und stattdeesen diese durch Funktionen darzustellen.
;; Alonzo Church ist Erfinder der Church numerals (cn)

;; Dafür brauchen wir zunächst die Defintion der 0 und einer Nachfolger-Operation:

(def zero (fn [f] (fn [x] x)))

(def succ
  (fn [n] (fn [f] (fn [x] (f ((n f) x))))))

;; Man beachte, dass die Funktion zero nicht zur 0 ausgewertet wird, sie ist lediglich
;; eine abstrakte Repäsentation:
zero ;;=> (serializable.fn/fn [f] (fn [x] x))

;; Ein Church numeral ist eine Funktion, die eine Funktion (f) als Argument erwartet,
;; die ihrerseits ein Argument erwartet. Die Funktion zero reräsentiert die natürliche
;; Zahl 0 dadurch, dass sie ihre Argumentfunktion f null mal anwendet.
;; Das Church numeral one sollte eine Funktion liefern, die die Argumentfunktion einmal
;; anwendet, two sollte es zweimal tun usw.
;; Verallgemenert gesprochen repräsentieren Church numerals natürliche Zahlen durch
;; Anwendung der Argumentfunktion (f) genau so oft, wie es der zu repräsentierenden
;; natürlichen Zahl entspricht.

;; Diesem Schema folgend können weitere natürliche Zahlen als Church numerals definiert
;; werden. Zuvor soll aber ein Mechanismus eingeführt werden, mit dessen Hilfe geprüft
;; werden kann, ob die Definitionen der Church numerals auch der obigen Regel ent-
;; sprechen. Der "Trick" besteht darin den Church numerals eine Argumentfunktion zu
;; übergeben, mit der Hilfe geprüft werden kann, wie oft diese angewendet wird.
;; Benutzt man hierfür z. B. die Standardfunktion inc, kann man zeigen, dass die
;; Funktion zero ihre Arugmentfunktion kein mal aufruft:

((zero inc) 0) ;;=> 0
((zero inc) 1) ;;=> 1

;; Der Aufruf von (succ zero) mit inc liefert hingegen:
(((succ zero) inc) 0) ;;=> 1
(((succ zero) inc) 1) ;;=> 2
;; Hier ist also erkennbar, dass die Funktion (succ zero) ihre Argumentfunktion einmal
;; aufruft.

;; Zwei weitere Konstanten (one und two) könnten wie folgt definiert werden:
(def one (succ zero))
(def two (succ one))
((one inc) 0) ;;=> 1
((one inc) 1) ;;=> 2
((two inc) 0) ;;=> 2
((two inc) 1) ;;=> 3

;; Nun sollen diese beiden Konstanten direkt - d. h. ohne Verwendung von succ -
;; definiert werden, nach dem Muster von zero. Zur Ableitung der Funktion one wandeln
;; wir den Ausdruck (succ zero) nach dem Ersetzungsmodell um:
;; 1. Schritt: Ersetze im Rumpf von succ jedes Auftreten von n durch zero:
;; (succ zero)
;; = (fn [f] (fn [x] (f ((zero f) x))))
;; 2. Schritt: Ersetze zero durch seine Definition und wende sie an:
;; = (fn [f] (fn [x] (f ((fn [x] x) x))))
;; 3. Schritt: Vereinfache ((fn [x] x) x) zu x:
;; = (fn [f] (fn [x] (f x)))
;; Damit kann one wie folgt definiert werden:
(def one (fn [f] (fn [x] (f x))))

;; Wendet man die gleiche Vorgehensweise an, um den Ausdruck (succ one) umzuformen,
;; erhält man für die Definition von two:
(def two (fn [f] (fn [x] (f (f x)))))

(deftest zero-one-two
  (is (= ((zero inc) 0) 0))
  (is (= ((one inc)  0) 1))
  (is (= ((two inc)  0) 2)))

;; Für die Defintion der Addition von zwei Church numerals kann folgende Überlegung
;; angestellt werden: Die Funktion succ nimmt ihr Argument n (ein Church numeral)
;; und verpackt es in *einem* zusätzlchen Aufruf der Argumentfunktion f. Für die
;; Addition von zwei Church numerals m und n verpacken wir n in m Aufrufe von f:

(def plus (fn [m n] 
            (fn [f] 
              (fn [x] 
                ((m f) ((n f) x)))))) 


;; Um das Formulieren von Tests für Church numerals zu erleichtern, definieren wir
;; das folgende Gleichheitsprädikat:
(def =cn (fn [a b]
           (= ((a inc) 0) ((b inc) 0))))

(deftest test-succ
  (is (=cn (succ zero) (succ zero)))
  (is (=cn (succ (succ zero)) (succ (succ zero)))))

(deftest test-plus
  (is (=cn (plus zero (succ zero))  (succ zero)))
  (is (=cn (plus (succ zero) (succ zero))
           (succ (succ zero))))
  (is (=cn (plus (succ zero)
                 (plus (succ zero) (succ zero)))
           (succ (succ (succ zero))))))


;; zählt die Anzahl der Elemente ihres Arguments
;; countElements: (list-of any) -> Nat
(def countElements
  (fn [lst]
    (cond
      (empty? lst) zero
      :else (succ (countElements (rest lst)))))) 

(deftest test-countElements
  (is (=cn  (countElements () ) zero))
  (is (=cn  (countElements '(19 27 36)) (succ (succ (succ zero))))))

;; int->nat verwandelt eine beliebige positive ganze Zahl
;; in eine äquivalentes Element des Datentyps Nat 
;; int->nat: Number -> Nat
(def int->nat
  (fn [int]
    (cond
      (= int 0) zero
      :else (succ (int->nat (- int 1))))))

(deftest test-int->nat
  (is (=cn (int->nat 0) zero))
  (is (=cn (int->nat 3) (succ (succ (succ zero))))))


;; nat->int, wandelt eine natürliche Zahl aus Nat in eine
;; normale Clojure-Number
;; nat->int: Nat -> Number
(def nat->int
  (fn [n]
    ((n inc) 0)))

(deftest test-nat->int
  (is (= (nat->int zero) 0))
  (is (= (nat->int (succ (succ (succ zero)))) 3)))


(run-tests)
