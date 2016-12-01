(defproject advent-of-code-2016 "0.0.1-SNAPSHOT"
  :description "Code for solving the 'Advent of Code 2016' problem set."
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :aot [advent-of-code-2016.core]
  :main advent-of-code-2016.core)
