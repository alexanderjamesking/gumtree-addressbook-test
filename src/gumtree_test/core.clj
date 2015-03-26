(ns gumtree-test.core
  (:require [clojure.data.csv :refer [read-csv]]
            [clojure.string :refer [trim]]
            [clj-time.core :refer [interval in-days]]
            [clj-time.format :refer [parse formatter]]))

(def address-book (read-csv (slurp "resources/address-book.csv")))

(defn male? [p]
  (let [[n g d] p] 
    (= (trim g) "Male")))

(defn parse-dob [p]
  (let [[n g d] p]
    (parse (formatter "dd/MM/YY") (trim d))))

(defn dob-as-timestamp [p]
  (let [[n g d] p
         dob-datetime (parse-dob p)] 
    (.getMillis dob-datetime)))

(defn person-is-called? [expected p]
  (let [[n g d] p
         n1 (trim n)
         n2 (trim expected)]
    (= n1 n2)))

(defn find-by-name [n ab]
  (first (filter #(person-is-called? n %) ab)))

(defn days-between [n1 n2 ab]
  (let [d1 (parse-dob (find-by-name n1 ab))
        d2 (parse-dob (find-by-name n2 ab))
        i (interval d1 d2)]
    (in-days i)))

(defn -main []
  (println "Males: " (filter male? address-book))
  (println "Oldest person: " (first (sort-by dob-as-timestamp address-book)))
  (println "Days between Bill and Paul:" (days-between "Bill McKnight" "Paul Robinson" address-book)))
