(ns advent-of-code-2016.day6
  (:require [advent-of-code-2016.core :as ad-core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-6-input.txt")))

(->> problem-input
     str/split-lines
     ad-core/transpose
     (map frequencies)
     (map #(sort-by (comp - second) %))
     (map first))

(->> problem-input
     str/split-lines
     ad-core/transpose
     (map frequencies)
     (map #(sort-by second %))
     (map first))
