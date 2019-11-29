(ns duly-noted.core
  (:require [seesaw.core :as s :refer [native! invoke-later
                                       frame pack!
                                       show! config
                                       config! label]]))

(native!)

(def f (frame :title "Write"
              :content "Welcome, writer. Thoughts?"
              :on-close :exit))

(config! f :content "Express them here.")

(def lbl (label "Notepad" ))

(config! f :content lbl)
(defn -main [& args]
  (invoke-later
   (-> f
       s/pack!
       s/show!)))