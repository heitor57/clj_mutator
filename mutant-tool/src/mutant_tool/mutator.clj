(ns mutant-tool.mutator
  (:require [mutant-tool.operators :refer [opstr operators]]
            [mutant-tool.filehelper :as fh]
            [rewrite-clj.zip :as z])
  )

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

(defn mutate
  [zip]
  (loop [head (fh/mapoperators zip)
         mutations []]
    (if (empty? head)
      mutations
      (recur (rest head) 
             (conj mutations (z/replace (first head) (-> head first z/sexpr changeop)) )
             )
      )
    )
  )
(defn mutate-file
  [filename]
  (-> filename fh/file->zipper mutate)
  )
(defn mutations-string
  [mut]
  (for [x mut]
    (z/root-string x))
  )
(defn mutations-print
  [mut]
  (loop [x (mutations-string mut)
         i 1]
    (if (empty? x)
      nil
      (do
        (println "--==Mutation " i "\n" (first x)) 
        (recur (rest x) (inc i))
        )
      )
    )
  )
