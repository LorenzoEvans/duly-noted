(ns personote.note-view
    (:require [fn-fx.fx-dom :as dom]
              [fn-fx.diff :refer [component defui render should-update?]]
              [fn-fx.controls :as ui]))


(def init-state 
    {:typed-text ""
     :notes [
             {:id 0 :text "Write code." :done false :tags #{"Code"}}
             {:id 0 :text "Go to a meet-up" :done false :tags #{"Tech" "Code"}}
             {:id 0 :text "Meditate" :done false :tags #{"Health" "Self-Care"}}
             {:id 0 :text "Buy Lions Mane" :done false :tags #{"Health"}}]
     :root-stage? true})

(defonce app-state (atom init-state))

(defmulti handle-event (fn [_ {:keys [event]}] event))

(defmethod handle-event :reset [_ {:keys [root-stage?]}] (assoc init-state :root-stage? root-stage?))

(defn start!
    ([] (start! {:root-stage? true}))
    ([{:keys [root-stage?]}]
     (swap! app-state assoc :root-stage? root-stage?)
     (let [handler-fn (fn [e] (println e) (try (swap! app-state handle-event e)
                                               (catch Throwable exception (println exception))))
           ui-stage (agent (fx/dom/app (stage @app-state) handler-fn))])))
        
     
