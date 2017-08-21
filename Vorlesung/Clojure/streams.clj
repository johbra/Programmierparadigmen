;; Streams
;;;;;;;;;

;; Ein stream ist eine unendliche Folge von Werten. Selbstverständlich kann
;; ein solche Folge nicht tatsächlich erzeugt werden - dies beanspruchte
;; unendlich viel Zeit. Es ist aber möglich Code zu erzeugen, der weiß, wie
;; die Folge zu erzeugen ist und anderen Code, der weiß, wieviel Elemente
;; Elemente benötigt werden.
;;
;; Die UNIX pipe (cmd1 | cmd2) ist ein stream; sie sorgt dafür, dass cmd1
;; genau so viel Output erzeugt wie cmd2 an Input verlangt.
;; Web-Programme, die auf Klicks von Benutzern auf Web-Seiten reagieren,
;; können die Benutzeraktivitäten als stream betrachten - ohne zu wissen,
;; wann die nächste Aktivität kommt und wie viele es sein werden - und
;; entsprechend antworten.

;; Allgemeiner gesprochen: streams stellen eine Art Arbeitsteilung dar:
;; Ein Teil der Software weiß, wie aufeinanderfolgende Werte der unendlichen
;; Folge zu erzeugen sind, weiß aber nicht, wie viele benötigt werden und
;; was mit ihnen passieren soll. Ein anderer Teil kann ermitteln, wie viele
;; Werte benötigt werden und was mit ihnen geschehen soll, weiß aber nicht,
;; wie sie zu erzeugen sind.

;; Es gibt viele Möglichkeiten, streams zu implementieren. Hier werden wir
;; eine einfache Variante realisieren, die einen stream als thunk repräsentiert,
;; der, wenn er aufgerufen wird, einen zweielementigen Vektor erzeugt, dessen
;; erstes Element den ersten Wert der unendlichen Folge enthält. Im zweiten
;; Element wird ein thunk abgelegt, der den stream für das zweite und die
;; übrigen Elemente der unendlichen Folge repäsentiert.

;; Aus "nostalgischen" Gründen werden hier zunächst zwei Funktionen für den
;; Zugriff auf das erste (car) und das zweite (cdr) Element eines Vektors
;; definiert
(def car (fn [v] (v 0))) ;; (v 0) ist das Gleiche wie (get v 0), wenn v ein Vektor
(def cdr (fn [v] (v 1)))

;; Die Definition von thunks für die Repäsentation unendlicher Folgen erfolgt
;; üblicherweise rekursiv.

;; 1. Beispiel: eine unendliche Folge von Einsen
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ones (fn [] [1 ones]))

;; Machen Sie sich die Wirkung der folgenden Ausdrücke klar. Beachten Sie die
;; Klammern!
(ones) ;=> [1 #function[user/ones]]
(car (ones)) ;=> 1
(car ((cdr (ones)))) ;=> 1

;; 2. Beispiel: die natürlichen Zahlen
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def nats
  (letfn [(f [x] [x (fn [] (f (+ x 1)))])]
    (fn [] (f 1))))

;; Machen Sie sich die Wirkung der folgenden Ausdrücke klar. Beachten Sie die
;; Klammern!

(car (nats)) ;=> 1
(car ((cdr (nats)))) ;=> 2
(car ((cdr ((cdr (nats))))))  ;=> 3

;; 3. Beispiel: die Zweierpotenzen
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def powers-of-two
  (letfn [(f [x] [x (fn [] (f (* x 2)))])]
    (fn [] (f 2))))

;; Machen Sie sich die Wirkung der folgenden Ausdrücke klar. Beachten Sie die
;; Klammern!

(car (powers-of-two)) ;=> 2
(car ((cdr (powers-of-two)))) ;=> 4
(car ((cdr ((cdr (powers-of-two)))))) ;=> 8

;; Man könnte eine Funktion höherer Ordnung schreiben, die einen stream und
;; ein Prädikat als Argumente akzeptiert und eine Zahl zurückgibt, die sagt,
;; wieviele Elemente des streams erzeugt werden müssen bevor das Prädikat
;; true liefert:

(def number-until
  (fn [stream tester]
    (letfn [(f [stream answer]
              (let [pr (stream)]
                (if (tester (car pr))
                  answer
                  (f (cdr pr) (+ answer 1)))))]
      (f stream 1))))

;; Beispielanwendung:
(number-until powers-of-two (fn [x] (> x 16))) ;=> 5


;; Aufgabe:
;;;;;;;;;;;

;; Schreiben Sie eine Funktion stream-for-n-steps, die einen stream s und
;; eine Zahl n nimmt. Sie gibt eine Liste mit den ersten n Elementen von
;; s zurück. (Lösung erfordert 4 Zeilen.

(def stream-for-n-steps
  (fn [s n]
    (cond
      (= n 0) ()
      :else (cons (car (s)) (stream-for-n-steps (cdr (s)) (- n 1))))))

;; Beispielanwendung:
(stream-for-n-steps powers-of-two 5) ;=> (2 4 8 16 32)

;; Aufgabe:
;;;;;;;;;;;

;; Schreiben Sie einen stream funny-number-stream, der dem für natürliche
;; Zahlen ähnelt, nur sollen alle durch 5 teilbaren Zahlen negiert werden,
;; z. B. 1, 2, 3, 4, -5, 6, 7, 8, 9, -10, 11, ... . Testen Sie die Funktion
;; mithilfe von stream-for-n-steps.

(def funny-number-stream
  (letfn [(f [x] [(if (= (mod x 5) 0) (- x) x) (fn [] (f (+ x 1)))])]
    (fn [] (f 1))))

(stream-for-n-steps funny-number-stream 12)
                                        ;=> (1 2 3 4 -5 6 7 8 9 -10 11 12)

;; Zusatzaufgabe (optional)
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Die streams ones, nats und powers-of-two haben gemeinsam, dass für die
;; Berechnung des nächstes Element maximal das vorherige bekannt sein
;; muss. Daher ist es nahe liegend, eine Funktion höherer Ordnung
;; stream-maker zu schreiben, die aus dem ersten Element und einer
;; Funktion zur Berechnung des nächsten einen stream baut.

(def stream-maker
  (fn [fkt arg]
    (letfn [(f [x]
              [x (fn [] (f (fkt x arg)))])]
      (fn [] (f arg)))))

(stream-for-n-steps (stream-maker (fn [x y] (+ 1 0)) 1) 12)
