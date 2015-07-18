(defproject routegen "0.1.4"
  :description "Generates HTTP routes for public functions in a namespace"
  :url "http://github.com/timothypratley/routegen"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring-server "0.4.0"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [cheshire "5.5.0"]
                 [clj-http-lite "0.2.1"]
                 [clj-time "0.10.0"]
                 [org.clojure/data.csv "0.1.2"]]
  :plugins [[codox "0.6.6"]]
  :codox {:include routegen.core})
