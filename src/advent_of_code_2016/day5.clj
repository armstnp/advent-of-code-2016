(ns advent-of-code-2016.day5
  (:require [advent-of-code-2016.core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set])
  (:import [java.security MessageDigest]
           [java.math BigInteger]
           [java.util Arrays]
           [java.nio ByteBuffer])
  (:gen-class))

(def problem-input (slurp (io/resource "day-5-input.txt")))

;; https://gist.github.com/jizhang/4325757
(defn valid-hash [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        [byte-1 byte-2 byte-3] raw]
    (when (and (= byte-1 byte-2 0)
               (>= byte-3 0)
               (<= byte-3 15))
      (let [sig (.toString (BigInteger. 1 raw) 16)
            padding (apply str (repeat (- size (count sig)) "0"))]
        (apply str (take 7 (str padding sig)))))))

;; Day 5-A
(->> (range)
     (map #(str problem-input %))
     (map valid-hash)
     (filter (complement nil?))
     (take 8)
     (map #(nth % 5))
     (apply str))

;; Day 5-B
(->> (range)
     (map #(str problem-input %))
     (map valid-hash)
     (filter (complement nil?))
     (filter #((set "01234567") (nth % 5)))
     (map #(take 2 (drop 5 %)))
     (take 20))
