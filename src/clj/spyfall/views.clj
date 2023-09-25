(ns spyfall.views
  (:require [hiccup.page :as page]))

(defn gen-page-head
  [title]
  [:head
   [:title title]
   (page/include-css "/assets/css/output.css")
   (page/include-js "https://unpkg.com/htmx.org@1.9.5")
   [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
   [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin "anonymous"}]
   (page/include-css "https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;700&display=swap")])

(defn base-view [title content]
  (page/html5
    (gen-page-head title)
    [:body.bg-slate-800.text-zinc-100 content]))

(defn home-view
  [params]
  (base-view "Spyfall"
             [:div.container.mx-auto.mt-8
              [:h1.text-center.text-5xl "Spyfall"]
              [:main
               [:button {"hx-post"        "/new_lobby"
                         "hx-swap"        "outerHTML"
                         "hx-target"      "body"
                         "hx-replace-url" "true"} "Create a new Lobby"]]]))

(defn lobby-view
  [params]
  (base-view "Spyfall"
             [:div.container.mx-auto.mt-8
              [:h1.text-center.text-5xl (str "Spyfall Lobby " (:lobbyid params))]
              [:main
               [:button.mt-8 {"hx-get"         "/"
                              "hx-swap"        "outerHTML"
                              "hx-target"      "body"
                              "hx-replace-url" "true"} "Return to Main Menu"]]]))