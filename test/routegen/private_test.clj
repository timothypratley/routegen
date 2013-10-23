(ns routegen.private-test
  (:require [midje.sweet :refer :all]
            [routegen.private :refer :all]))


(defn foo [^Integer x] (inc x))

(facts "about routing"
       (fact (doc-str #'inc) => string?)
       (fact (parse "2") => 2)
       (fact (parse "2" Integer) => 2)
       (fact (parse "2" Double) => 2.0)
       (fact (parse "true" Boolean) => true)
       (fact (parse "false" Boolean) => false)
       (fact (parse "this is a string") => string?)
       (fact (type (clojure.edn/read-string "this is a string")) => clojure.lang.Symbol)
       (fact (parse "ad1" Integer) => (throws Exception))
       (fact (err-parse "ad1" (-> #'foo meta :arglists first first)) => (comp string? second))
       (fact (call #'foo {:params {:x "1"}} identity) => 2)
       (fact (let [f #'inc] (:doc (meta f))) => string?)
       (fact (-> #'foo meta :arglists first first meta :tag) => 'Integer)
       (fact (-> #'inc meta) => map?))

