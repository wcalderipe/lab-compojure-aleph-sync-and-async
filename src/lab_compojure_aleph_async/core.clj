(ns lab-compojure-aleph-async.core
  (:require [compojure.core :refer [routes GET]]
            [manifold.deferred :as d]
            [aleph.http :as http]
            [byte-streams :as bs])
  (:gen-class))

(defn response [body]
  {:status 200 :headers {} :body body})

(defn sync-handler [request]
  (response "sync"))

(defn async-handler [request respond raise]
  (respond (response "async")))

(defn wrap-ring-async-handler
  "Converts given asynchronous Ring handler to Aleph-compliant handler.

   More information about asynchronous Ring handlers and middleware:
   - https://www.booleanknot.com/blog/2016/07/15/asynchronous-ring.html
   - https://github.com/ztellman/aleph/blob/5cc7bcab723bf3b64de7998fc782531c554636e5/src/aleph/http.clj#L436-L445"
  [handler]
  (fn [request]
    (let [response (d/deferred)]
      (handler request #(d/success! response %) #(d/error! response %))
      response)))

(defn make-app []
  (-> (routes (GET "/sync"  [] sync-handler)
              (GET "/async" [] async-handler))))

(def app (make-app))

(defn make-request [endpoint]
  (let [response @(http/get (str "http://localhost:3000" endpoint))]
    (assoc response :body (bs/to-string (:body response)))))

(comment
  ;; (def server (http/start-server (wrap-ring-async-handler #'app) {:port 3000}))
  (def server (http/start-server #((make-app) %) {:port 3000}))
  (.close server)

  ;; Is it possible to have sync and async handlers as they're defined
  ;; to coexisting at the same time? If yes, what changes are required?
  (make-request "/sync")
  (make-request "/async")

  )
