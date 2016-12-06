(ns advent-of-code-2016.day4
  (:require [advent-of-code-2016.core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(def problem-input (slurp (io/resource "day-4-input.txt")))

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn str->room
  [s]
  (let [[name id checksum] (rest (re-matches #"([a-z-]+)-(\d+)\[(\w{5})\]" s))]
    {:name name
     :id (parse-int id)
     :checksum checksum}))

(def compare-freq-desc (juxt (comp - second) first))

(defn letter-freqs
  [s]
  (->> s
    (filter (set alphabet))
    frequencies
    (map #(update % 0 str))
    (sort-by compare-freq-desc)))

(defn expected-checksum
  [name]
  (->> name
       letter-freqs
       (take 5)
       (map first)
       (apply str)))

(defn matches-checksum?
  [{:keys [name checksum]}]
  (= checksum (expected-checksum name)))

(assert (= [true true true false]
           (map (comp matches-checksum? str->room)
                ["aaaaa-bbb-z-y-x-123[abxyz]"
                 "a-b-c-d-e-f-g-h-987[abcde]"
                 "not-a-real-room-404[oarel]"
                 "totally-real-room-200[decoy]"])))

;; Solution to Day 4-A
(->> problem-input
     str/split-lines
     (map str->room)
     (filter matches-checksum?)
     (map :id)
     (reduce +)
     prn)

;; Quick inspiration for Caesar cipher implementation from
;; https://clojurebridge.github.io/community-docs/docs/exercises/caesar-cipher/
;; - Thanks!
(defn cipher
  [shift-n]
  (->> alphabet
       cycle
       (drop (mod shift-n 26))
       (take 26)
       (zipmap alphabet)))

(defn shift-by-id
  [{:keys [name id] :as room}]
  (let [room-cipher (assoc (cipher id) \- \space)]
    (assoc room :name (apply str (map room-cipher name)))))

(assert (= "very encrypted name" (->> "qzmt-zixmtkozy-ivhz-343[xxxxx]"
                                      str->room
                                      shift-by-id
                                      :name)))

;; Solution to Day 4-B
(->> problem-input
     str/split-lines
     (map str->room)
     (filter matches-checksum?)
     (map shift-by-id)
     (filter #(re-matches #".+(pole).+" (:name %)))
     (map :id)
     prn)
