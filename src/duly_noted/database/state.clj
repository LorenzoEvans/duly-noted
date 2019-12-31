(ns duly-noted.database.state
    (:require [cljfx.api :as fx]))


(def ^:dynamic *state*
    (atom {:title "Duly Notes"}))

(def ^:dynamic *todo-state*
    (atom {:typed-text ""
           :by-id {0 {:id 0
                      :text "Write Code."
                      :done false}
                   1 {:id 1
                      :text "Drink coffee."
                      :done true}}}))
