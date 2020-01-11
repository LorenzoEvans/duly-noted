(ns personote.note-view
    (:require [cljfx.api :as fx]
              [cljfx.css :as css]
              [clojure.pprint :as pp]
              [personote.database.state :refer [*note-state*]])
    (:import [javafx.scene.input KeyCode KeyEvent]
             [javafx.application Platform]
             [javafx.scene.paint Color]
             [javafx.scene.control Button]
             [javafx.scene.canvas Canvas]))

(def root-style 
  (css/register ::root-style 
    (let [xs 2
          s 5
          m 10
          l 15]
      {::padding s
       ::spacing m
       ".root" {:-fx-padding s}
       ".label" {:-fx-padding m}
       ".button" {:-fx-padding ["4px" "8px"]
                  ":hover" {:-fx-text-fill :red}}
       ".vbox" {:-fx-background-color :black}})))
  
(def note-view-style 
  (css/register ::note-style
    (let [xs 2
          s 5
          m 10
          l 15]
      {::padding s
       ::spacing s
       ".label" {:-fx-padding s :-fx-text-fill :green}})))
           
(defn note-view [{:keys [text id title]}]
  {:fx/type :v-box
   :spacing 5 
   :padding 5
   :children [
              {:fx/type :label
               :style {:-fx-text-fill :black}
               :text title}
              {:fx/type :label
               :pref-height 200
               :style {:-fx-text-fill :black}
               :text text}
              {:fx/type :button
               :text "Delete"
               :on-mouse-clicked {:event/type ::delete-item :id id}}]}) 
; (defn note-view [{:keys [text id tit]}])
(defn root [{:keys [notes typed-text]}]
  {:fx/type :stage
   :title "Personote"
   :showing true
   :scene {:fx/type :scene
           :stylesheets [(::css/url root-style)]
           :root {:fx/type :v-box
                  :pref-width 700
                  :pref-height 700
                  :children [{:fx/type :scroll-pane
                              :v-box/vgrow :always
                              :fit-to-width true
                              :content {:fx/type :v-box
                                        :children (->> 
                                                       notes
                                                       vals
                                                       (sort-by (juxt :done :id))
                                                      ;  (as-> (map #(assoc % :fx/type note-view :fx/key (int (:id %))) notes) test-fn
                                                      ;    (try test-fn
                                                      ;     (catch Throwable ex (println))))
                                                      ; mutating a javafx component seems in bad taste
                                                      ; thinking style => add database => re-write crud 
                                                       (map #(assoc  % :fx/type note-view :fx/key (int (:id %)))))}}
                                                          
                             {:fx/type :text-field
                              :v-box/margin 5
                              :pref-height 300
                              :text typed-text
                              :prompt-text "Add new note and press ENTER"
                              :on-text-changed {:event/type ::type}
                              :on-key-pressed {:event/type ::press}}]}}})
; (println (:arglists (meta #'root)))
(defn map-event-handler [event]
  (case (:event/type event)
    ::set-done (swap! *note-state* assoc-in [:notes (:id event) :done] (:fx/event event))
    ::type (swap! *note-state* assoc :typed-text (:fx/event event))
    ::delete-item (swap! *note-state* assoc-in [:notes (:id event)] nil)
                  ;  (swap! *note-state* update-in [:notes (:id event)] (fn [elements] (filterv (fn [itm] (= (:id event) (:id itm))) (get-in *note-state* [:notes]))))
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
