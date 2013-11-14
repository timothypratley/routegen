(ns routegen.private
  "Implementation details, do not refer"
  (:require [clojure.edn]
            [ring.util.codec :as codec]
            [noir.response :refer [content-type status]]
            [noir.session :as session]
            [clojure.data.csv :refer [write-csv]]
            [cheshire.custom :as custom]
            [slingshot.slingshot :refer [try+]]
            [clj-time.format :refer [unparse formatter]]
            [clj-time.core :refer [year month day now]]))

(defn readable-date
  [date]
  (unparse (if (= (year date) (year (now)))
                    (formatter "MMM dd")
                    (formatter "MMM dd yyyy"))
                    date))

(custom/add-encoder org.joda.time.DateTime
  (fn [d jsonGenerator]
    (.writeString jsonGenerator (readable-date d))))

(defn json
  "Wraps the response in the json content type
   and generates JSON from the content"
  [request content]
  (content-type "application/json; charset=utf-8"
                (custom/generate-string content)))

(defn csv
  "Formats content as csv and prompts the user to save"
  [request content]
  (assoc-in
    (content-type "text/csv"
      (str (doto (java.io.StringWriter.) (write-csv content))))
    [:headers "Content-Disposition"]
    (str "attachment;filename=" (clojure.string/join "_" (vals (dissoc (request :params) :tqx))) ".csv")))

(defn parse-int
  [s]
  (if s
    (Integer/parseInt s)
    0))

(defn reqId
  "Google datasource format requires reqId processing"
  [tqx]
  (if tqx
    (let [match (re-find #"reqId:(\d+)" tqx)]
      (parse-int (second match)))
    0))

(defn column-type
  [s]
  (cond
    (number? s) "number"
    (instance? org.joda.time.DateTime s) "date"
    :else "string"))

(defn tostr-ds-date
  "Converts a joda time into a javascript zero based month date"
  [date]
  (str "Date(" (year date) "," (dec (month date)) "," (day date) ")"))

(defn make-value
  [value]
  (hash-map :v (if (instance? org.joda.time.DateTime value)
                 (tostr-ds-date value)
                 value)))

(defn tabulate
  "Construct a datasource table"
  [content]
  {:cols (map #(hash-map :label %1 :type %2)
              (first content)
              (map column-type (second content)))
   :rows (map #(hash-map
                 :c (map make-value %))
              (rest content))})

(defn datasource
  "Google charts datasource formatted content"
  [request content]
  (json request
        {:reqId (reqId (get-in request [:params :tqx]))
         :table (tabulate content)}))

(defn doc-str
  "Basic doc string for a var (usually a function)"
  [v]
  (clojure.string/join \newline
                       (map (meta v)
                            [:name :arglists :doc])))

;TODO: this should be a multimethod so that consumers can add their own parsers
(defn parse
  "Parse a string as a type"
  ([s] (parse s nil))
  ([s t]
   (condp = t
     String s
     Integer (Integer/parseInt s)
     Boolean (Boolean/parseBoolean s)
     Double (Double/parseDouble s)
     (let [r (clojure.edn/read-string s)] (if (symbol? r) s r)))))

(defn err-parse
  "Return a tuple of [value error-message]
  nil as error-message means value is good"
  [s arg]
  (let [t (:tag (meta arg))]
    (try
      [(parse s (when t (resolve t))) nil]
      (catch Exception e
        [nil (str "Failed to parse " (name arg) "=" s (when t (str " as " t)) ": " e)]))))

;TODO: handle optional arguments
(defn call
  "Calls f with arguments taken from the request parameters.
  Returns a 400 with a helpful error message if params do not match."
  [f fmt request]
  (let [params (dissoc (request :params) :tqx)
        request-arity (count (keys params))
        arglists (-> f meta :arglists)
        match #(and (if params
                      (empty %)
                      (every? params (map keyword %)))
                    (= (count params) (count %)))
        arglist (first (filter match arglists))
        args (map params (map keyword arglist))
        decoded (map codec/url-decode args)
        parsed (map err-parse decoded arglist)
        parse-errors (map last parsed)
        parse-vals (map first parsed)
        error (cond
               (not-any? #(= request-arity (count %)) arglists) "Wrong number of arguments"
               (nil? arglist) "Parameters do not match"
               (some identity parse-errors) (clojure.string/join \newline parse-errors))]
    (if error
      (status 400 (str error \newline (doc-str f)))
      (fmt request (apply f parse-vals)))))

(defn functions
  "Get the public functions of a namespace"
  [n]
  (filter ifn? (ns-publics n)))








