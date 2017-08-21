(def person
  (fn [name vorname]
    (let [get-name    (fn [] name)
          get-vorname (fn []vorname)
          vollname    (fn [] (str vorname " " name))
          gruss       (fn [g] (str g " " (vollname)))]
      (fn [nachricht]
        (cond
          (= nachricht 'get-name) get-name
          (= nachricht 'get-vorname) get-vorname
          (= nachricht 'get-vollname) vollname
          (= nachricht 'gruss) gruss
          :else (throw (Exception. "unbekannte Nachricht")))))))

(def p1 (person "Gans" "Gustav"))
((p1 'get-vorname))     ;;=> "Gustav"
((p1 'get-name))        ;;=> "Gans"
((p1 'get-vollname))    ;;=> "Gustav Gans"
((p1 'gruss) "hallo")   ;;=> "hallo Gustav Gans"
(p1 'x)                 ;; Unhandled java.lang.Exception unbekannte Nachricht
