(ns personote.note-view
    (:require [fn-fx.fx-dom :as fx-dom]
              [fn-fx.diff :refer [component defui render should-update?]]
              [fn-fx.controls :as ui]))




(defonce app-state 
    (atom 
     {:typed-text ""
      :notes [{:id 0 :text "Write code." :done? false :tags #{"Code"}}
              {:id 0 :text "Go to a meet-up" :done? false :tags #{"Tech" "Code"}}
              {:id 0 :text "Meditate" :done? false :tags #{"Health" "Self-Care"}}
              {:id 0 :text "Buy Lions Mane" :done? false :tags #{"Health"}}]
      :root-stage? false}))

@app-state

(defn force-exit [root-stage?]
    (reify javafx.event.EventHandler
        (handle [this event]
            (when-not root-stage?
                (println "Closing application")
                (javafx.application.Platform/exit)))))

(defmulti handle-event (fn [state event] (:event event)))

; (defmethod handle-event :reset [_ {:keys [root-stage?]}] (assoc state :root-stage? root-stage?))
(defmethod handle-event :add-item [state {:keys [fn-fx/includes]}] (update-in state :notes conj {:done? false :text (get-in includes [::new-item :text]) :tags #{}}))
(defmethod handle-event :delete-item [state {:keys [id]}] (update-in state [:notes] 
                                                           (fn [items]
                                                            (vec (concat (take id items) (drop (inc id) items))))))
(defmethod handle-event :swap-status [state {:keys [id done?]}] (update-in state [:notes id done?] (fn [x] (not x))))
(defmethod handle-event :default [state event] (println (:type event) event state))
(def main-font (ui/font :family "Helvetica" :size 20))

(defui TodoItem
    (render [this {:keys [done? id text]}]
        (ui/border-pane :padding (ui/insets :top 10 :bottom 10 :left 0 :right 0)
                        :left (ui/check-box :font main-font
                                            :text text
                                            :selected done?
                                            :on-action {:event :swap-status :id id})
                        :right (ui/button :text "x"
                                          :on-action {:event :delete-item :id id}))))

(defui MainWindow
    (render [this {:keys [notes text]}]
        (ui/v-box :style "-fx-base: rgb(30, 30, 35)"
                  :padding (ui/insets :top-right-bottom-left 25)
                  :children [(ui/text-field :id ::new-item
                                            :prompt-text "What's on your mind?"
                                            :font main-font
                                            :on-action {:event :add-item
                                                        :fn-fx/include {::new-item #{text}}})
                             (ui/v-box :children (map-indexed (fn [id note] (todo-item (assoc note :id id))) notes))])))
                                            
(defui Stage
    (render [this args]
        (ui/stage :title "Personote"
                  :min-height 600
                  :listen/height {:event :height-change :fn-fx/include {::new-item #{:text}}}
                  :shown true
                  :scene (ui/scene :root (main-window args)))))

(defn start!
    ([] (start! {:root-stage? true}))
    ([{:keys [root-stage?]}]
     (swap! app-state assoc :root-stage? root-stage?)
     (let [handler-fn (fn [e] (println e) (try (swap! app-state handle-event e) (catch Throwable exception (println exception))))
           ui-state (agent (fx-dom/app (stage app-state) handler-fn))]
      (add-watch app-state :ui
        (fn [_ _ _ _]
            (send ui-state (fn [old-ui]
                             (println "-- State Updated --")
                             (println app-state)
                             (fx-dom/update-app old-ui (stage app-state)))))))))
        
     
