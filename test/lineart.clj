(ns lineart.cli
  (:require
   [clojure.tools.cli :refer [parse-opts]]
   [hiccup2.core :refer [html]]
   [lineart.core :refer [generate]]))

(println "<?xml version=\"1.0\" encoding=\"utf-8\"?>")
(println
 (str (html (generate (first *command-line-args*)))))
