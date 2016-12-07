(ns advent-of-code-2016.day7
  (:require [advent-of-code-2016.core :as ad-core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(def problem-input (slurp (io/resource "day-7-input.txt")))

(defn is-abba?
  [[a b c d]]
  (and (not= a b)
       (= b c)
       (= a d)))

(defn has-abba?
  [s]
  (some is-abba? (partition 4 1 s)))

(defn strip-hypernet
  [ip]
  (->> ip
    (re-seq #"(\w*)(?:\[\w*?\])?")
    (map second)
    (filter #(not (or (nil? %) (empty? %))))
    (filter (complement empty?))))

(defn only-hypernet
  [ip]
  (map second (re-seq #"\[(\w*)\]" ip)))

(defn supports-tls?
  [ip]
  (and (some has-abba? (strip-hypernet ip))
       (every? (complement has-abba?) (only-hypernet ip))))

(defn aba?
  [[a b c]]
  (and (= a c) (not= a b)))

(defn flip [[a b]] [b a])

(defn abas
  [s]
  (->> s
       (partition 3 1)
       (filter aba?)
       (map #(take 2 %))))

(defn supports-ssl?
  [ip]
  (let [supernet-abas (->> ip strip-hypernet (mapcat abas) set)
        hypernet-babs (->> ip only-hypernet (mapcat abas) (map flip) set)]
    (not (empty? (set/intersection supernet-abas hypernet-babs)))))

;; Part A + B
(->> problem-input
     str/split-lines
     ((juxt #(filter supports-tls? %) #(filter supports-ssl? %)))
     (map count)
     (map (partial str) ["Supporting TLS: " "Supporting SSL: "]))
