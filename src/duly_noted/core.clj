(ns duly-noted.core
  (:require [cljfx.api :as fx]
            [duly-noted.database.state :refer [*state* *todo-state*]])
  (:import [javafx.scene.input KeyCode KeyEvent])
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


; (defn title-input [{:keys [title]}]
;   {:fx/type :text-field
;    :on-text-changed #(swap! *state* assoc :title %)
;    :text title})
(defn root [{:keys [by-id typed-text]}]
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :pref-width 300
                  :pref-height 400
                  :children [{:fx/type :scroll-pane
                              :v-box/vgrow :always
                              :fit-to-width true
                              :content {:fx/type :v-box
                                        :children (->> by-id
                                                       vals
                                                       (sort-by (juxt :done :id))
                                                       (map #(assoc % :fx/type todo-view :fx/key (:id %))))}}
                             {:fx/type :text-field
                              :v-box/margin 5
                              :text typed-text
                              :prompt-text "Add a new to-do item."
                              :on-text-changed {:event/type ::type}
                              :on-key-pressed {:event/type ::press}}]}}})

(defn todo-view [{:keys [text id done]}]
  {:fx/type :h-box
   :spacing 5
   :padding 5
   :children [{:fx/type :check-box
               :selected done
               :on-selected-changed {:event/type ::set-done :id id}}
              {:fx/type :label}
              :style {:-fx-text-fill (if done :grey :black)}
              :text text]})
   
            
(defn map-event-handler [event]
  (case (:event/type event)
    ::set-done (swap! *todo-state* assoc-in [:by-id (:id event) :done] (:fx/event event))
    ::type (swap! *todo-state* assoc :typed-text (:fx/event event))
    ::press (when (= KeyCode/ENTER (.getCode ^KeyEvent (:fx/event event)))
             (swap! *todo-state* #(-> %
                                      (assoc :typed-text "")
                                      (assoc-in [:by-id (count (:by-id %))]))))))

(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc assoc :fx/type root)))
    
(defn -main []
  (fx/mount-renderer *state* renderer))
    
