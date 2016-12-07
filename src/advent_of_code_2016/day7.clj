(ns advent-of-code-2016.day7
  (:require [advent-of-code-2016.core :as ad-core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-7-input.txt")))

(defn has-abba?
  [s]
  (some (fn [[a b c d]] (and (not= a b) (= b c) (= a d))) (partition 4 1 s)))

(re-seq #".*(\w)(\w)\2\1.*" "aaaabba")

(has-abba? "aaaa")

(defn strip-hypernet
  [ip]
  (->> ip
    (re-seq #"(\w*)(?:\[\w*?\])?")
    (map second)
    (filter (complement nil?))
    (filter (complement empty?))))

(strip-hypernet "piftqfdhtumcmjmsge[qrsntvxhtfurcgcynx]oyswvuklvtmivlhen[syqhqtijyiduoxb]pdtdrhijqqzvcnl[xivmeqcwyafxvnok]jvlbkrwbgcgzaqms")

(defn only-hypernet
  [ip]
  (re-seq #"\[(\w*)\]" ip))

(defn supports-tls?
  [ip]
  (and (some has-abba? (strip-hypernet ip))
       (not (some has-abba? (only-hypernet ip)))))

(map supports-tls? ["abba[mnop]qrst"
                    "abcd[bddb]xyyx"
                    "aaaa[qwer]tyui"])
