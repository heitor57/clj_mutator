(ns mutant-tool.filehelper
  (:require [mutant-tool.operators :refer [opstr]])
  (:require [clojure.walk :as walk]))
(def ^:private s-exp-start-regex "(?<=\\(\\s*)")

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

(defn expandstr
  "This function was made to be used with mapoperators to expand all the text and catch all operators that may be hidden
  Example:
  Entry: \"(when (> 2 1) (println \"Nice\"))\"
  Result: \"[(if (> 2 1) (do (println \"Nice\")))]\""
  [text]
  (str (walk/macroexpand-all (read-string (str "[" text "]"))))
  )

(defn mapoperators
  "This maps all the operators position to mutate after
  Normally use slurp to get the text..."
  [text]
  (re-seq-pos (re-pattern (str s-exp-start-regex (regexgroup opstr))) text)
  )
