(ns duly-noted.core
  (:require [cljfx.api :as fx]
            [duly-noted.database.state :refer [*state*]])
  (:gen-class))


; Components in cljfx are described by maps with the :fx/type key.
; The type can be: 
; - a keyword corresponding to a JavaFX class
; - a function, which recieves this map as an argument and returns a new description
; - an implementation of a life cycle protocol.

; (create-component)
; "Create component from description and optional opts for manual management
; `:opts` is map that every lifecycle receives as an argument and uses for various
; purposes. You can provide your data to custom lifecycles, or extend default cljfx
; behavior via these keys:
; - `:fx.opt/type->lifecycle` — a function that gets called on every `:fx/type` value
;   in descriptions to determine what lifecycle will be used for that description
; - `:fx.opt/map-event-handler` — a function that gets called when map is used in place
;   of change-listener, event-handler or any other callback-like prop. It receives that
;   map with `:fx/event` key containing appropriate event data"
; (def renderer (fx/create-renderer))

    
    ; (defn root [{:keys [showing]}]
    ;   (fx/on-fx-thread ; Execute body in implicit do, if current thread is fx thread, body executes immediately.
    ;    (fx/create-component
    ;     {:fx/type :stage
    ;      :showing true
    ;      :title "CLJFX EX"
    ;      :width 600
    ;      :height 500
    ;      :scene {:fx/type :scene
    ;              :root {:fx/type :v-box
    ;                     :alignment :center
    ;                     :children [{:fx/type :label
    ;                                 :text "Welcome to your application!"}]}}})))  


(defn title-input [{:keys [title]}]
  {:fx/type :text-field
   :on-text-changed #(swap! *state* assoc :title %)
   :text title})

(defn root [{:keys [title]}]
  {:fx/type :stage
   :showing true
   :title title
   :width 500
   :height 500
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :children [{:fx/type :label
                              :text "Title input"}
                             {:fx/type title-input
                              :title title}]}}}) 


(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc assoc :fx/type root)))
    
(defn -main []
  (fx/mount-renderer *state* renderer))
    
