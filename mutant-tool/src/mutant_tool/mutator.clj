(ns mutant-tool.mutator
  (:require [mutant-tool.operators :refer [opstr]])
  (:require [mutant-tool.filehelper :as fh]))

(defn ^:private createop
  "Used to generate a hashmap of operators and their relationship
  (createop [\"or\" \"and\" \"+\" \"-\"])
  that generate: {:or 'and, :and 'or, :+ '-, :- '+}"
  [value]
  (loop [queue value
         finalval {}]
    (if (empty? queue)
      finalval
      (let [[part sec & remaining] queue]
        (recur 
          remaining 
          (into finalval {(keyword part) (symbol sec) (keyword sec) (symbol part)}))))))


(def ^:private operators (createop opstr))

(defn ^:private changeop
  [op]
  ((keyword op) operators))
(defn ^:private changeexp
  "Entry: Expression
  Return: New changed expression"
  [exp]
  (let [newexp (list)
        remaining (rest exp)]
    (conj (into newexp remaining) (changeop (first exp))))
  )

(defn mutations
  [text]
  (loop [head (fh/mapoperators text)
         mutations []]
    (if (empty? head)
      mutations
      (recur (rest head) 
             (conj mutations (let [elem (first head)]
                               (str (subs text 0 (:start elem)) (changeop (:group elem)) (subs text (:end elem)))
                               ))
             )
      )
    )
  )
