(ns personote.note-view
    (:require [cljfx.api :as fx])
    (:import [javafx.scene.input KeyCode KeyEvent]))

(defn todo-view [{:keys [text id done]}]
    {:fx/type :h-box
     :spacing 5
     :padding 5
     :children [{:fx/type :check-box
                 :selected done
                 :on-selected-changed}]})
     