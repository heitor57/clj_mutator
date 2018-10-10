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

(defn mutations
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
  (-> filename fh/file->zipper mutations)
  )
(defn mutations-string
  [mut]
  (for [x mut]
    (-> x z/root-string))
  )
