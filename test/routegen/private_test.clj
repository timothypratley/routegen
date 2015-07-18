(ns routegen.private-test
  (:require [clojure.test :refer :all]
            [routegen.private :refer :all]))


(defn foo [^Integer x] (inc x))

(deftest about-routegen-private
  (is (string? (doc-str #'inc)))
  (is (= 2 (parse "2")))
  (is (= 2 (parse "2" Integer)))
  (is (= 2.0 (parse "2" Double)))
  (is (= true (parse "true" Boolean)))
  (is (= false (parse "false" Boolean)))
  (is (string? (parse "this is a string")))
  (is (= clojure.lang.Symbol (type (clojure.edn/read-string "this is a string"))))
  (is (thrown? Exception (parse "ad1" Integer)))
  (is (string? (second (err-parse "ad1" (-> #'foo meta :arglists first first)))))
  (is (= 2 (call #'foo (fn [request content] content) {:params {:x "1"}} identity)))
  (is (= 2 (call #'foo (fn [request content] content) {:body {:x "1"}} identity)))
  (is (string? (let [f #'inc] (:doc (meta f)))))
  (is (= 'Integer (-> #'foo meta :arglists first first meta :tag)))
  (is (map? (-> #'inc meta))))
