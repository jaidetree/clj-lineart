#!/bin/env /home1/jayzawro/bin/bb

(ns cgi.lineart
  (:require
   [babashka.fs :as fs]
   [babashka.classpath :refer [add-classpath]]
   [clojure.string :as s]
   [hiccup2.core :refer [html]]))

;; Dynamic Libs
(def LIB-DIR "/home1/jayzawro/lib/")
(def CWD
  (if-let [filename (System/getenv "SCRIPT_FILENAME")]
    (str (fs/parent filename))
    (System/getenv "PWD")))

(defn lib
  "
  Create an absolute path to a jar file in sibling lib directory
  Takes a string filename like \"honeysql.jar\"
  Returns a string like \"/path/to/dir/lib/honeysql.jar\".
  "
  [path]
  (str LIB-DIR path))

(defn path
  [& paths]
  (str (apply fs/path paths)))

;; Add jars and current directory to classpath to import library code

(add-classpath (s/join ":" [(path CWD "src")]))

;; Require our main page code
;; Must be placed here after updating the class path

(require
 '[lineart.core :as lineart])

;; CGI scripts must print headers then body

(println "Content-type:image/svg+xml\r\n")
(println "<?xml version=\"1.0\" encoding=\"utf-8\"?>")
(println (str (html (lineart/generate (System/getenv "QUERY_STRING")))))

(System/exit 0)
