(ns mutant-tool.filehelper
  (:require [mutant-tool.operators :refer [opstr operators]]
            [clojure.walk :as walk]
            [rewrite-clj.zip :as z]
            )
  )
(defn read-string-all
  [text]
  (read-string (str "[" text "]"))
  )
(defn expandstr
  "This function was made to be used with mapoperators to expand all the text and catch all operators that may be hidden
  Example:
  Entry: \"(when (> 2 1) (println \"Nice\"))\"
  Result: \"[(if (> 2 1) (do (println \"Nice\")))]\""
  [text]
  (str (walk/macroexpand-all (read-string-all text)))
  )
(defn file->zipper
  [filename]
  (-> filename slurp z/of-string)
  )
(defn mapoperators
  "This maps all the operators position to mutate after
  Normally use slurp to get the text..."
  [zip]
  (loop [zip zip
         opmap []]
    (if (z/end? zip)
      opmap
      (if (z/seq? zip)
        (recur (z/next zip) opmap)
        (if (and (z/leftmost? zip) (-> zip z/sexpr keyword operators nil? not)) 
          (recur (z/next zip) (conj opmap zip))    
          (recur (z/next zip) opmap)
          )
        )
      )
    )
  )

