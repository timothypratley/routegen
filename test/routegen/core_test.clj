(ns routegen.core-test
  (:require [midje.sweet :refer :all]
            [routegen.core :refer :all]))


(facts "about routingen core"
       (fact "one page per function"
             (count (page-routes 'routegen.core)) => 3)
       (fact "csv/json/ds per function"
             (count (post-routes 'routegen.core)) => 9)
       (fact "csv/json/ds per function"
             (count (post-routes 'routegen.core clojure.edn/read-string)) => 9)
       (fact "csv/json/ds per function arities (4)"
             (count (path-routes 'routegen.core)) => 12))


