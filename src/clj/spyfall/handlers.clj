(ns spyfall.handlers
  (:require [spyfall.views :as views]
            [spyfall.utils :as u]
            [manifold.stream :as s]
            [clojure.core.async :as a]
            [muuntaja.core :as m]))

(defonce lobbies (atom {}))

(defn add-default-headers
  [res]
  (assoc-in res [:headers "Cache-Control"] "no-cache, max-age=0, must-revalidate, proxy-revalidate"))

(defn- ok-res
  [view req]
  (-> {:status 200
       :body   (view (:path-params req))}
      (add-default-headers)))

(defn- redirect-res
  [to]
  (-> {:status  303
       :headers {"Location" to}}
      (add-default-headers)))

(defn- simple-page-handler
  "Takes no params from the route, just return a page"
  [view]
  (fn [req] (ok-res view req)))

(def home-page
  (simple-page-handler views/home-view))

(defn lobby-page-handler
  [req]
  (let [lobby-id (-> req :path-params :lobbyid)
        lobby (get @lobbies lobby-id)]
    (if lobby
      (ok-res views/lobby-view req)
      (redirect-res "/"))))

(def new-lobby-handler
  (fn [_]
    (let [lobby-code (u/gen-lobby-code)]
      (swap! lobbies assoc lobby-code {})
      (redirect-res (str "/lobby/" lobby-code)))))

(defn streaming-numbers-handler
  [_]
  (let [body (a/chan)]
    (a/go-loop [i 0]
      (if (< i 100)
        (let [_ (a/<! (a/timeout 100))]
          (a/>! body (format "id: %s\n" i))
          (a/>! body "event: onProgress\n")
          (a/>! body (format "data: %s\n" i))
          (a/>! body "\n")
          (recur (inc i)))
        (a/close! body)))
    {:status  200
     :headers {"content-type"  "text/event-stream"
               "Cache-Control" "no-cache"}
     :body    (s/->source body)}))

;(defn streaming-numbers-handler
;  [_]
;  (let [body (a/chan)]
;    (a/go-loop [i 0]
;      (if (< i 100)
;        (let [_ (a/<! (a/timeout 100))]
;          (a/>! body (->> {:id    i
;                           :event "onProgress"
;                           :data  {:progressPercentage i}}
;                          (m/encode "application/json")))
;          (recur (inc i)))
;        (a/close! body)))
;    {:status  200
;     :headers {"content-type"  "text/event-stream"
;               "Cache-Control" "no-cache"}
;     :body    (s/->source body)}))

; #(str (swap! sent inc) "\n")
; (str i "\n")