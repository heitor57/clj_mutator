(ns mutant-tool.mutator
  (:require [rewrite-clj.zip :as zip]))
(def opstr ["+" "-" "or" "and" "empty?" "seq"])

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
