(ns advent-of-code-2016.day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [swiss.arrows :refer :all])
  (:import [java.security MessageDigest]
           [java.math BigInteger]
           [java.nio ByteBuffer]))

(def problem-input (slurp (io/resource "day-5-input.txt")))

;; Code for calculating an MD5 hash pulled from:
;; https://gist.github.com/jizhang/4325757
;; Thank you!

(def md5-algorithm (MessageDigest/getInstance "MD5"))
(def digest-size (* 2 (.getDigestLength md5-algorithm)))

(defn raw-md5
  "Gets the raw MD5 digest bytes for the given string."
  [s]
  (.digest md5-algorithm (.getBytes s)))

(defn valid-hash?
  "Returns whether the hash is valid (for our purposes),
  i.e. begins with a sequence of five zero nybbles."
  [[byte-1 byte-2 byte-3]]
  (and (= byte-1 byte-2 0)
       (>= byte-3 0)
       (<= byte-3 15)))

(defn hex-rep
  "Converts the given hash into a hexidecimal string representation."
  [hash]
  (let [sig (.toString (BigInteger. 1 hash) 16)
        padding (apply str (repeat (- digest-size (count sig)) "0"))]
    (str padding sig)))

(defn pos-val-nybbles
  "Returns the position and value nybbles from a given hash."
  [[_ _ _ _ _ position value]]
  {:position (- (int position) (int \0))
   :value value})

(def valid-positions (set (range 0 8)))

(defn valid-position?
  "Returns whether the given hash position is a valid
  password position."
  [{:keys [position]}]
  (valid-positions position))

(def empty-pass (vec (repeat 8 nil)))

(defn insert-pass-char
  "Inserts the given hash position-value pair into the given
  password, or returns the password untouched if the position
  has already been filled in."
  [pass {:keys [position value]}]
  (if (nil? (nth pass position))
    (assoc pass position value)
    pass))

(defn pass-complete?
  "Returns whether the password has all its slots filled."
  [pass]
  (every? (complement nil?) pass))

;; Day 5-B
(-<>> (range)
      (map (comp raw-md5 #(str problem-input %)))
      (filter valid-hash?)
      (-<:p <>
        ;; Part A
        (->>
          (take 8)
          (map hex-rep)
          (map #(nth % 5))
          (apply str "Part A: ")
          println)
        ;; Part B
        (->>
          (map (comp pos-val-nybbles hex-rep))
          (filter valid-position?)
          (reductions insert-pass-char empty-pass)
          (filter pass-complete?)
          first
          (apply str "Part B: ")
          println)))
