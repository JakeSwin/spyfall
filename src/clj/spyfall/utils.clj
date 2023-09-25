(ns spyfall.utils
  (:require [clojure.string :as s]))

(def all-chars
  "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")

(defn rand_char
  []
  (rand-nth
    (s/split all-chars #"")))

(defn gen-lobby-code
  "Generates a random 5 character lobby code from characters and numbers"
  []
  (->> rand_char
       (repeatedly)
       (take 5)
       (apply str)))