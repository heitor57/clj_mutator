(ns mutant-tool.core
  (:require [mutant-tool.mutator :as mut])
  (:require [mutant-tool.filehelper :as fh])
  (:gen-class))
(def code-test "(+ 1 2)
(+ 3 2)
(or nil 1)")
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (fh/mapoperators "(+ 2 3 (+ 5 2))"))
  (println (fh/expandstr "(when (> 2 1) (println \"Nice\"))"))
  (println (fh/mapoperators code-test))
  (println (eval (read-string (fh/expandstr code-test))))
  (println (->  code-test mut/mutations))
  )
