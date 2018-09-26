(ns mutant-tool.filehelper
  (:require [mutant-tool.mutator :as mut]))
(def ^:private s-exp-start-regex "\\(\\s*")

(defn ^:private re-seq-pos [pattern string] 
  (let [aux (re-matcher pattern string)] 
    ((fn step [] 
      (when (. aux find) 
        (cons {:start (. aux start) :end (. aux end) :group (. aux group)} 
              (lazy-seq (step))))))))
(defn ^:private literalstr
  "Transform a string to a 100% literal in regex form"
  [string]
  (loop [head string
         result ""]
    (if (empty? head)
      (identity result)
      (recur (rest head) (str result "[" (first head) "]"))
      )
    )
  )
(defn ^:private regexgroup
  "Create regex for the operators"
  [v]
  (str "(" 
       (clojure.string/join "" (drop-last (loop [head v
                                                 result ""]
                                            (if (empty? head)
                                              (identity result)
                                              (recur (rest head) (str result "(" (literalstr (first head)) ")|"))
                                              )
                                            )
                                          )
                            ) 
       ")"
       )
  )
(defn mapfileoperators
  "This maps all the file operators position to mutate after
  Normally use slurp to get the text..."
  [text]
    (println (re-pattern (str s-exp-start-regex (regexgroup mut/opstr))))
    (re-seq-pos (re-pattern (str s-exp-start-regex (regexgroup mut/opstr))) text)
  )
