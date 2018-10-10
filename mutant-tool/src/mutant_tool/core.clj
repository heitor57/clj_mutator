(ns mutant-tool.core
  (:require [mutant-tool.mutator :as mut]
            [mutant-tool.filehelper :as fh]
            [rewrite-clj.zip :as z]
            )

  (:gen-class))
(def code-test "(+ 1 2)
               (+ 3 2)
               (or nil 1)")
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> "src/mutant_tool/core.clj" fh/file->zipper fh/mapoperators (get 0) z/root-string println)
  (-> "src/mutant_tool/filehelper.clj" mut/mutate-file mut/mutations-print)
  (-> code-test z/of-string z/next z/root-string println)
  )
