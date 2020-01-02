(ns personote.database.state
    (:require [cljfx.api :as fx]))
              


(def ^:dynamic *state*
    (atom {:title "Duly Notes"}))

(def ^:dynamic *note-state*
    (atom {:typed-text ""
           :notes  {0 {:id 0
                       :text "Write Code."
                       :done false
                       :tags #{}}
                    1 {:id 1
                       :text "Drink coffee."
                       :done true
                       :tags #{}}}}))