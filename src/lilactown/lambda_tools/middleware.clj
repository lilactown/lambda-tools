(ns lilactown.lambda-tools.middleware
  (:require [clojure.string :as s]
            [clojure.pprint :refer [pprint]]))

(defn- make-query-string [query-string k v]
  (str query-string "&" (name k) "=" v))

(defn- map->query-string [m]
  (-> m
      (reduce-kv make-query-string "")
      (subs 1)))

;; Eventually map the AWS event payload to a ring request map
(defn- aws->ring
  "Maps the AWS event payload to a Ring request"
  [event]
  {:request-method (keyword (s/lower-case (get event "httpMethod"))) 
   :server-port (Integer/parseInt (get-in event ["headers" "X-Forwarded-Port"]))
   :server-name (get event "stage")
   :remote-addr (get-in event ["requestContext" "caller" "sourceIp"])
   :uri (get event "path")
   :query-params (get event "queryStringParameters")
   ;; TODO: reconstruct query string maybe?
   :query-string ""
   :scheme (keyword (get-in event ["headers" "X-Forwarded-Proto"]))
   :headers (reduce-kv #(assoc %1 (s/lower-case %2) %3) {} (get event "headers"))
   ;; TODO: this should be an InputStream
   :body (get event "body")
   ;; add original AWS event
   :aws/event event})

(defn http-ring-adapter [handler]
  (fn [event ctx]
    (let [ring-req (aws->ring event)
          ring-res (handler ring-req)]
      ;; Convert ring-style response to AWS Gateway response
      {:statusCode (:status ring-res)
       :headers (:headers ring-res)
       :body (:body ring-res)})))

(defn simple-pprint-adapter [handler]
  (fn [event ctx]
    ;; prints the event map to the dev logs
    (pprint event)
    (handler event ctx)))
