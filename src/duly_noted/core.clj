(ns duly-noted.core
  (:require [seesaw.core :as s]))


(def f (s/frame :title "Write"
                :content "Welcome, writer. Thoughts?"))

(def l)
(defn -main [& args]
  (s/native!)
  (s/invoke-later
   (-> (s/frame
        :title "Write"
        :content "Welcome, writer. Thoughts"
        :on-close :exit)
       s/pack!
       s/show!)))