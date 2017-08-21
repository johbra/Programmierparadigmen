(ns sicp-cps.core)

(defn in? 
  "true if coll contains elm"
  [coll elm]  
  (some #(= elm %) coll))

(def inform-about-value
  (fn [constraint]
    (constraint 'I-have-a-value)))

(def inform-about-no-value
  (fn [constraint]
    (constraint 'I-lost-my-value)))

(def for-each-except 
  (fn [exception procedure list]
    (letfn
        [(loop [items]
           (cond
             (empty? items) 'done
             (= (first items) exception) (loop (rest items))
             :else (do (procedure (first items))
                       (loop (rest items)))))]
      (loop @list))))

;; has-value?: connector -> boolean
;; sagt, ob Konnektor einen Wert hat
(def has-value?
  (fn [connector]
    (connector 'has-value?)))

;; get-value: connector -> any
;; liefert den Wert eines Konnektors
(def get-value 
  (fn [connector]
    @(connector 'value)))

;; set-value!: connector any constraint -> unspecified
;; zeigt an, dass ein constraint den Wert eines Konnektors
;; setzen will
(def set-value! 
  (fn [connector new-value informant]
    ((connector 'set-value!) new-value informant)))

;; forget-value!: connector constraint -> unspecified
;; zeigt an, dass ein constraint den Wert eines Konnektors
;; vergessen machen will
(def forget-value! 
  (fn [connector retractor]
    ((connector 'forget) retractor)))

;; connect: connector constraint -> ?
;; verbindet einen Konnektor mit einem neuen constraint
(def connect 
  (fn [connector new-constraint]
    ((connector 'connect) new-constraint)))

(def constant 
  (fn [value connector]
    (letfn 
        [(me [request]
           (throw (Exception. "Unknown request -- CONSTANT" request)))]
      (connect connector me)
      (set-value! connector value me)
      me)))

(def probe
  (fn [name connector]
    (letfn 
        [(print-probe [value]
           (println "Probe: " name " = " value))
         (process-new-value []
           (print-probe (get-value connector)))
         (process-forget-value []
           (print-probe "?"))
         (me [request]
           (cond
             (= request 'I-have-a-value) (process-new-value)
             (= request 'I-lost-my-value) (process-forget-value)
             :else (throw (Exception. "Unknown request -- PROBE" request))))]
      (connect connector me)
      me)))

(def make-connector
  (fn []
    (let [value (atom false) informant (atom false) constraints  (atom '())]
      (letfn [(set-my-value [newval setter]
                (cond (not (has-value? me))
                      (do (reset! value newval)
                          (reset! informant setter)
                          (for-each-except setter
                                           inform-about-value
                                           constraints))
                      (not (= @value newval))
                      (throw (Exception. (str "Contradiction" (list @value newval))))
                      :else 'ignored))
              (forget-my-value [retractor]
                (if (= retractor @informant)
                  (do (reset! informant false)
                      (for-each-except retractor
                                       inform-about-no-value
                                       constraints))
                  'ignored))
              (connect [new-constraint]
                (if (not (in? @constraints new-constraint ))
                  (swap! constraints conj new-constraint))
                (if (has-value? me)
                  (inform-about-value new-constraint))
                'done)
              (me [request]
                (cond
                  (= request 'has-value?) (if @informant true false)
                  (= request 'value)      value
                  (= request 'set-value!) set-my-value
                  (= request 'forget)     forget-my-value
                  (= request 'connect)    connect
                  :else (throw (Exception. "Unknown operation -- CONNECTOR"
                                           request))))]
        me))))

(def adder
  (fn [a1 a2 sum]
    (letfn
        [(process-new-value []
           (cond (and (has-value? a1) (has-value? a2))
                 (set-value! sum
                             (+ (get-value a1) (get-value a2))
                             me)
                 (and (has-value? a1) (has-value? sum))
                 (set-value! a2
                             (- (get-value sum) (get-value a1))
                             me)
                 (and (has-value? a2) (has-value? sum))
                 (set-value! a1
                             (- (get-value sum) (get-value a2))
                             me)))
         (process-forget-value []
           (forget-value! sum me)
           (forget-value! a1 me)
           (forget-value! a2 me)
           (process-new-value))
         (me [request]
           (cond
             (= request 'I-have-a-value)  (process-new-value)
             (= request 'I-lost-my-value) (process-forget-value)
             :else 
             (throw (Exception. "Unknown request -- ADDER" request))))]
      (connect a1 me)
      (connect a2 me)
      (connect sum me)
      me)))

(def multiplier
  (fn [m1 m2 product]
    (letfn [(process-new-value []
              (cond (or (and (has-value? m1) (= (get-value m1) 0))
                        (and (has-value? m2) (= (get-value m2) 0)))
                    (set-value! product 0 me) 
                    (and (has-value? m1) (has-value? m2))                    
                    (set-value! product
                                (* (get-value m1) (get-value m2))
                                me)
                    (and (has-value? product) (has-value? m1))
                    (set-value! m2
                                (/ (get-value product) (get-value m1))
                                me)
                    (and (has-value? product) (has-value? m2))
                    (set-value! m1
                                (/ (get-value product) (get-value m2))
                                me)))
            (process-forget-value []
              (forget-value! product me)
              (forget-value! m1 me)
              (forget-value! m2 me)
              (process-new-value))
            (me [request]
              (cond (= request 'I-have-a-value)  (process-new-value)
                    (= request 'I-lost-my-value) (process-forget-value)
                    :else
                    (throw (Exception. "Unknown request -- MULTIPLIER" request))))]
      (connect m1 me)
      (connect m2 me)
      (connect product me)
      me)))
