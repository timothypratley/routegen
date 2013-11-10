(defproject routegen "0.1.1"
  :description "Generates HTTP routes for public functions in a namespace"
  :url "http://github.com/timothypratley/routegen"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring-server "0.3.0"]
                 [hiccup "1.0.4"]
                 [lib-noir "0.7.2"]
                 [cheshire "5.2.0"]
                 [clj-http-lite "0.2.0"]
                 [slingshot "0.10.3"]
                 [clj-time "0.6.0"]
                 [org.clojure/data.csv "0.1.2"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :plugins [[lein-midje "3.0.0"]
            [lein-ancient "0.5.1"]])



