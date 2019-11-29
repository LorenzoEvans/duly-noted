(ns duly-noted.core
  (:require [seesaw.core :as s :refer [native! invoke-later
                                       frame pack!
                                       show! config
                                       config! label
                                       button listen
                                       alert input
                                       listbox scrollable
                                       selection]]
            [seesaw.font :refer [font]]))

(native!)
; This commands Seesaw to make things look like the OS that's running it.
; Call this as early as possible in the program.


(def f (frame :title "Write"
              :content "Welcome, writer. Thoughts?"
              :on-close :exit))
; This is a frame. We put things in frames.

(config! f :content "Express them here.")

; we can query a widget: (config widget-name :keyword)
; we can alter the content of a widget with (config! widget-name :keyword new-value)

(def lbl (label "Notepad" ))
; We can create a labeled frame. Seesaw will make labels of strings on it's own.

(defn display [content]
  (config! f :content content)
  content)

(config! lbl :background :pink :foreground "#00f")
; We can manipulate colors and other properties with config!

(config! lbl :font (font :name :monospaced
                         :style #{:bold :italic}
                         :size 18))

(alert "I'm alerting you!")
(input "What's your favorite programming language?")
(def b (button :text "Click Me"))

(display b)

; (listen b :action 
;         (fn [evnt] (alert evnt "Clicked!")))
; (config! f :content lbl)

(listen b :mouse-entered #(config! % :foreground :blue)
          :mouse-exited #(config! % :foreground :red))

(def lb (listbox :model (-> 'seesaw.core ns-publics keys sort)))

(display (scrollable lb))

(selection lb {:multi? true})

(listen lb :selection (fn [evnt] (println "Selection is " (selection evnt))))

(defn -main [& args]
  (invoke-later
   (-> f
       pack! ; autosizes a frame to it's contents size. it & show! return args.
       show!)))