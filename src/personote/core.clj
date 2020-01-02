(ns personote.core
  (:require [cljfx.api :as fx]
            [personote.note-view :refer [start!]])
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

(defn -main []
  (start!))
