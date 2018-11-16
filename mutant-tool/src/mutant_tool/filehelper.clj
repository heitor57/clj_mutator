(ns mutant-tool.filehelper
  (:require [mutant-tool.operators :refer [opstr operators]]
            [clojure.walk :as walk]
            [rewrite-clj.zip :as z]
            [clojure.tools.namespace
             [find :as find]]
            [clojure.java.io :as io]
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


(defn clj-files
  [dir]
  (find/find-clojure-sources-in-dir (io/file dir))
  #_(-> dir clojure.java.io/file find/find-sources-in-dir)
  )


(defn safe-delete
  [file-path]
  (if (.exists (clojure.java.io/file file-path))
    (try
      (clojure.java.io/delete-file file-path)
      (catch Exception e (str "exception: " (.getMessage e))))
    false))

(defn delete-directory
  [directory-path]
  (let [directory-contents (file-seq (clojure.java.io/file directory-path))
        files-to-delete (filter #(.isFile %) directory-contents)]
    (doseq [file files-to-delete]
      (safe-delete (.getPath file)))
    (safe-delete directory-path)))

