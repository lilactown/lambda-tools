# lambda-tools

Tools for building serverless Clojure applications in AWS Lambda

## Usage

Lein/boot coordinates: `[lilactown/lambda-tools "0.1.5"]`

deps.edn: `lilactown/lambda-tools {:mvn/version "0.1.5"}`

```clojure
(ns demoaws.core
  (:require [lilactown.lambda-tools :refer [gen-lambda]]
            [lilactown.lambda-tools.middleware :refer [http-ring-adapter]]))

;; Use a Ring handler like you would with Jetty/etc.
(defn app [request]
  {:status 200
   :headers {}
   :body "<h1>Hello, world</h1>"})

(def handler
  (-> app
      ;; Wrap in the http-ring-adapter middleware to convert
      ;; the AWS HTTP event to a Ring-compatible request
      http-ring-adapter))

;; Create our lambda function
(gen-lambda
  {:handler handler
   :name demoaws.core.Greet})
```

## License

Copyright © 2018 Will Acton

Distributed under MIT License
