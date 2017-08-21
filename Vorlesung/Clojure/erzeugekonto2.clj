;; erzeuge-konto (number -> (symbol -> (number -> (mixed number symbol))))
;; nimmt einen Betrag als Startkapital und erzeugt ein "Konto-Object"
(def erzeuge-konto
  (fn [startkapital]
    (let
        [;; Exemplarvariable
         konto (atom startkapital)

         ;; Exemplarmethoden:
         ;; belaste: (number -> (mixed number symbol))
         ;; Effekt: bucht vom konto betrag ab, liefert neuen 
         ;; Kontostand als Resultat, falls Konto nicht ueberzogen
         belaste
         (fn [betrag]
           (cond
             (>= @konto betrag)
             (do
               (swap! konto - betrag)
               @konto)
             :else 'konto-ueberzogen))
         ;; schreibegut: (number -> number)
         ;; Effekt: schreibt konto betrag gut
         ;; liefert neuen Kontostand als Resultat
         schreibegut
         (fn [betrag]
           (do
             (swap! konto + betrag)
             @konto))

         ;; method dispatcher
         ;; verteile: (number -> (mixed number symbol))
         ;; verwaltet die von Konten verstandenen Nachrichten
         verteile
         (fn [nachricht]
           (cond
             (= nachricht 'belaste) belaste
             (= nachricht 'schreibegut) schreibegut
             :else (throw (Exception. "unbekannte Nachricht"))))]
      verteile)))

(def konto (erzeuge-konto 200))
((konto 'schreibegut) 60)
((konto 'belaste) 120)
