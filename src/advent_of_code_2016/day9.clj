(ns advent-of-code-2016.day9
  (:require [advent-of-code-2016.core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (str/trim (slurp (io/resource "day-9-input.txt"))))

(defn decompress-length
  [s]
  (let [[_ num-chars-s rep-s remaining] (re-matches #"\((\d+)x(\d+)\)(.*)" s)
        num-chars (parse-int num-chars-s)
        rep (parse-int rep-s)]
    {:length (* rep num-chars)
     :remaining (apply str (drop num-chars remaining))}))

(defn next-length
  [s]
  (if (= \( (first s))
    (decompress-length s)
    {:length 1
     :remaining (apply str (next s))}))

(defn total-length
  [s]
  (loop [s s
         accum 0]
    (if (empty? s)
      accum
      (let [{:keys [length remaining]} (next-length s)]
        (recur remaining (+ accum length))))))

(assert (= 6 (total-length "ADVENT")))
(assert (= 7 (total-length "A(1x5)BC")))
(assert (= 9 (total-length "(3x3)XYZ")))
(assert (= 11 (total-length "A(2x2)BCD(2x2)EFG")))
(assert (= 6 (total-length "(6x1)(1x3)A")))
(assert (= 18 (total-length "X(8x2)(3x3)ABCY")))

;; Part A
(total-length problem-input)

(declare total-nested-length)

(defn decompress-nested-length
  [s]
  (let [[_ num-chars-s rep-s remaining] (re-matches #"\((\d+)x(\d+)\)(.*)" s)
        num-chars (parse-int num-chars-s)
        rep (parse-int rep-s)
        [next-n rest] (split-at num-chars remaining)]
    {:length (* rep (total-nested-length (apply str next-n)))
     :remaining (apply str rest)}))

(defn next-nested-length
  [s]
  (if (= \( (first s))
    (decompress-nested-length s)
    {:length 1
     :remaining (apply str (next s))}))

(defn total-nested-length
  [s]
  (loop [s s
         accum 0]
    (if (empty? s)
      accum
      (let [{:keys [length remaining]} (next-nested-length s)]
        (recur remaining (+ accum length))))))

(assert (= 9 (total-nested-length "(3x3)XYZ")))
(assert (= 20 (total-nested-length "X(8x2)(3x3)ABCY")))
(assert (= 241920 (total-nested-length "(27x12)(20x12)(13x14)(7x10)(1x12)A")))
(assert (= 445 (total-nested-length "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")))

;; Part B
(total-nested-length problem-input)
