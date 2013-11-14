# routegen

Generate Compojure routes for functions in a namespace.
Routegen makes exposing webservices simpler.

## Usage

Add `[routegen "0.1.2"]` to your project.clj `:dependencies`

```clojure
(def app-routes
  (apply routes
         (concat
          (page-routes 'myns.pages)
          (path-routes 'myns.services)
          (post-routes 'myns.services))))
```

Pass a namespace to create a page or service for each function in that namespace:
* page-routes "Returns routes for pages defined in a namespace as public functions with no arguments"
* path-routes "Returns service routes with arguments passed as parameters."
* post-routes "Returns service routes where arguments are passed in the URL path."

Why?
* All service calls have their arguments checked and return helpful error messages if the signatures do not match.
* Generate webservice documentation using standard Clojure tools because they are just functions.
* Implement a function in your services namespace, and now you can call it from JavaScript :)
* All services can be accessed as json/csv/datasource
* path-routes are for GET http://mysite/fmt/function/arg1/arg2/arg3 where fmt is one of json/csv/datasource
* post-routes are for POST http://mysite/fmt/function with arguments in the request

## License

Copyright © 2013 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.





