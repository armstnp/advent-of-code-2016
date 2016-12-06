(ns advent-of-code-2016.day3
  (:require [advent-of-code-2016.core :as ad-core]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-3-input.txt")))

(defn valid-triangle?
  [lengths]
  (let [[small mid large] (sort lengths)]
    (> (+ small mid) large)))

(assert (not (valid-triangle? [5 10 25])))

(defn parse-input
  [s]
  (->> s
       str/split-lines
       (map #(map ad-core/parse-int
                  (re-seq #"\d+" %)))))

(defn count-valid-triangles
  [triangles]
  (count (filter valid-triangle? triangles)))

(->> problem-input
     parse-input
     count-valid-triangles
     prn)

(defn read-triangles-vertically
  [triangles]
  (mapcat ad-core/transpose (partition 3 triangles)))

(assert (apply = 1
                 (->> [[101 301 501]
                       [102 302 502]
                       [103 303 503]
                       [201 401 601]
                       [202 402 602]
                       [203 403 603]]
                      read-triangles-vertically
                      first
                      (map #(quot % 100)))))

(->> problem-input
     parse-input
     read-triangles-vertically
     count-valid-triangles
     prn)
