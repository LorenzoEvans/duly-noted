(ns duly-noted.database.state
    (:require [cljfx.api :as fx]))


(def ^:dynamic *state*
    (atom {:title "Duly Notes"}))

(def ^:dynamic *todo-state*
    (atom {:typed-text ""
           :by-id}))
