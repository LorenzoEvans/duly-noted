(ns personote.core
  (:require [personote.note-view :as core])
  (:import [javafx.application Application])
  (:gen-class
   :extends javafx.application.Application))

(defn -start [app stage]
  (core/start! {:root-stage? false}))

(defn -main [& args]
  (javafx.application.Application/launch personote.core (into-array String args)))
