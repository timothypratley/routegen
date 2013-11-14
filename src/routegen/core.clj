(ns routegen.core
  "Expose functions as services that have their arguments checked"
  (:require [compojure.core :refer :all]
            [routegen.private :refer :all]))


(defn page-routes
  "Returns routes for pages defined in a namespace as public functions with no arguments"
  [page-ns]
  (for [[page-name page] (functions page-ns)]
    (GET (str "/" page-name) request (page))))

(defn post-routes
  "Returns service routes with arguments passed as parameters.
  Optionally supply a body-decoder function which will deserialize additional arguments to a hashmap."
  ([service-ns]
   (post-routes service-ns (fn ignore-body [x] nil)))
  ([service-ns body-decoder]
   (for [[service-name service] (functions service-ns)
         fmt [#'json #'datasource #'csv]
         :let [route (str "/" (-> fmt meta :name) "/" service-name)]]
     (POST route request
           (call service fmt request body-decoder)))))

(defn path-routes
  "Returns service routes where arguments are passed in the url path."
  [service-ns]
  (for [[service-name service] (functions service-ns)
        fmt [#'json #'datasource #'csv]
        arglist (-> service meta :arglists)
        :let [route (str "/" (-> fmt meta :name)
                         "/" service-name (when (seq arglist) "/")
                         (clojure.string/join "/" (map keyword arglist)))]]
      (GET route request
           (call service fmt request))))


