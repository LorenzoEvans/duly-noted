(ns duly-noted.core
  (:require [cljfx.api :as fx]))


; Components in cljfx are described by maps with the :fx/type key.
; The type can be: 
; - a keyword corresponding to a JavaFX class
; - a function, which recieves this map as an argument and returns a new description
; - an implementation of a life cycle protocol.

(fx/on-fx-thread ; Execute body in implicit do, if current thread is fx thread, body executes immediately.
  
  (fx/create-component))
  
