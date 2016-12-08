(ns advent-of-code-2016.day7
  (:require [clojure.string :as str]
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

(assert (supports-tls? "abba[mnop]qrst"))
(assert (not (supports-tls? "abcd[bddb]xyyx")))
(assert (not (supports-tls? "aaaa[qwer]tyui")))
(assert (supports-tls? "ioxxoj[asdfgh]zxcvbn"))

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

(assert (supports-ssl? "aba[bab]xyz"))
(assert (not (supports-ssl? "xyx[xyx]xyx")))
(assert (supports-ssl? "aaa[kek]eke"))
(assert (supports-ssl? "zazbz[bzb]cdb"))

;; Part A + B
(->> problem-input
     str/split-lines
     ((juxt #(filter supports-tls? %) #(filter supports-ssl? %)))
     (map count)
     (map (partial str) ["Supporting TLS: " "Supporting SSL: "]))
