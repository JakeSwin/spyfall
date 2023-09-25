(ns spyfall.core
  (:require [reitit.ring :as ring]
            [aleph.http :as http]
            [ring.middleware.reload :refer [wrap-reload]]
            [spyfall.handlers :as h]))

(def app
  (ring/ring-handler
    (ring/router
      [["/" {:get {:handler h/home-page}}]
       ["/lobby/:lobbyid" {:get {:handler h/lobby-page-handler}}]
       ["/new_lobby" {:post {:handler h/new-lobby-handler}}]
       ["/stream_numbers" {:get {:handler h/streaming-numbers-handler}}]
       ["/assets/*" (ring/create-resource-handler)]])
    (ring/create-default-handler)))

(def reload-app
  (wrap-reload #'app))

(defonce server (atom nil))

(defn start! []
  (when-not @server
    (reset! server (http/start-server
                     reload-app
                     {:port 3000
                      :join? false}))))

(defn stop! []
  (when @server
    (.close @server)
    (reset! server nil)))

(comment
  (start!)
  (stop!))

(defn -main [_]
  (start!))