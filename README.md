# lambda-tools

Tools for building serverless Clojure applications in AWS Lambda

## Usage

```clojure
(ns demoaws.core
  (:require [demoaws.lambda :refer [gen-lambda]]
            [demoaws.middleware :refer [ring-adapter]]))

;; Use a run-of-the-mill Ring handler
(defn app [request]
  {:status 200
   :headers {}
   :body "<h1>Hello, world</h1>"})

(def handler
  (-> app
      ;; Apply the HTTP Ring adapter middleware
      ;; to convert the AWS HTTP event to a Ring-
      ;; compatible request
      ring-adapter))

(gen-lambda
  {:name demoaws.core.Greet
   :handler handler})
```

## License

Copyright © 2018 Will Acton

Distributed under MIT License
