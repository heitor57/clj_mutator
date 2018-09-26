(ns mutant-tool.core
  (:require [mutant-tool.mutator :as mut])
  (:require [mutant-tool.filehelper :as fh])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (fh/mapoperators "(+ 2 3 (+ 5 2))")))
