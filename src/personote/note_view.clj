(ns personote.note-view
    (:require [cljfx.api :as fx]
              [personote.database.state :refer [*note-state*]])
    (:import [javafx.scene.input KeyCode KeyEvent]
             [javafx.application Platform]
             [javafx.scene.paint Color]
             [javafx.scene.canvas Canvas]))


(defn note-view [{:keys [text id done]}]
  {:fx/type :v-box
   :spacing 5 
   :padding 5
   :children [{:fx/type :check-box
               :selected done
               :on-selected-changed {:event/type ::set-done :id id}}
              {:fx/type :label
               :style {:-fx-text-fill (if done :grey :black)}
               :text text}
              {:fx/type :button
               :text "X"
               :on-action {:event/type ::delete-item :id id}}]}) 

(defn root [{:keys [notes typed-text]}]
  {:fx/type :stage
   :title "Personote"
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :pref-width 700
                  :pref-height 700
                  :children [{:fx/type :scroll-pane
                              :v-box/vgrow :always
                              :fit-to-width true
                              :content {:fx/type :v-box
                                        :children (->> notes
                                                       vals
                                                       (sort-by (juxt :done :id))
                                                       (map #(assoc % :fx/type note-view :fx/key (println (int (:id %)))) notes))}}
                             {:fx/type :text-field
                              :v-box/margin 5
                              :pref-height 300
                              :text typed-text
                              :prompt-text "Add new note and press ENTER"
                              :on-text-changed {:event/type ::type}
                              :on-key-pressed {:event/type ::press}}]}}})

(defn map-event-handler [event]
  (case (:event/type event)
    ::set-done (swap! *note-state* assoc-in [:notes (:id event) :done] (:fx/event event))
    ::type (swap! *note-state* assoc :typed-text (:fx/event event))
    ::delete-item ; (swap! *note-state* dissoc [:notes (:id event)] (:id event) (:fx/event event))
                   (swap! *note-state* update-in [:notes (:id event)] (fn [elements] (filterv (fn [itm] (= (:id event) (:id itm))) (get-in *note-state* [:notes]))))
    ::press (when (= KeyCode/ENTER (.getCode ^KeyEvent (:fx/event event)))
              (swap! *note-state* #(-> %
                                    (assoc :typed-text "")
                                    (assoc-in [:notes (count (:notes %))]
                                           {:id (count (:notes %))
                                            :text (:typed-text %)
                                            :done false}))))
    nil))

(defn start! []
 (fx/mount-renderer
  *note-state*
  (fx/create-renderer
    :middleware (fx/wrap-map-desc assoc :fx/type root)
    :opts {:fx.opt/map-event-handler map-event-handler})))
