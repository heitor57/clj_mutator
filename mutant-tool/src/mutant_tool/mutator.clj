(ns mutant-tool.mutator
  (:require [mutant-tool.operators :refer [opstr operators]]
            [mutant-tool.filehelper :as fh]
            [rewrite-clj.zip :as z]
            [clj-diffmatchpatch :as dmp]
            [jansi-clj [core :as js] auto]))

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
             (into mutations 
                   (for [tmpop (-> head first z/sexpr changeop)]
                     (z/replace (first head) tmpop)
                     )
                   )
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
        (println "--==Mutation " i)
        (println (first x) "\n") 
        (recur (rest x) (inc i))
        )
      )
    )
  )
(defn mutations-string-diff
  [file mut]
  (for [v (for [x (mutations-string mut)]
            (dmp/wdiff file x))]
    (apply str (for [diffvector v]
                 (case (first diffvector)
                   :equal (second diffvector)
                   :insert (str (js/green "|+ ") (js/green (second diffvector)) (js/green " +|"))
                   :delete (str (js/red "|- ") (js/red (second diffvector)) (js/red " -|"))))))
  )
(defn mutations-print-diff
  [file mut]
  (loop [strs (mutations-string-diff file mut)
         i 1] 
    (if (empty? strs)
      nil
      (do
        
        (println "--==Mutation " i)
        (println (first strs) "\n") 
        (recur (rest strs) (inc i))
        )
      )
    )
  )
