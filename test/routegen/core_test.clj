(ns routegen.core-test
  (:require [clojure.test :refer :all]
            [routegen.core :refer :all]))


(deftest about-routingen-core
  (testing "one page per function"
    (is (= 3 (count (page-routes 'routegen.core)))))
  (testing "csv/json/ds per function"
    (is (= 9 (count (post-routes 'routegen.core)))))
  (testing "csv/json/ds per function"
    (is (= 9 (count (post-routes 'routegen.core clojure.edn/read-string)))))
  (testing "csv/json/ds per function arities (4)"
    (is (= 12 (count (path-routes 'routegen.core))))))
