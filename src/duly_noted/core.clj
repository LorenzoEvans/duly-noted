(ns duly-noted.core
  (:require [seesaw.core :as s :refer [native! invoke-later
                                       frame pack!
                                       show! select
                                       config! label
                                       button listen
                                       id-of input
                                       listbox scrollable
                                       selection text
                                       text! left-right-split
                                       scroll! radio
                                       border-panel horizontal-panel
                                       button-group]]
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

; (alert "I'm alerting you!")
; (input "What's your favorite programming language?")
;notice inputs and alerts block frames until resolved.
(def b (button :text "Click Me"))

(display b)

; (listen b :action 
;         (fn [evnt] (alert evnt "Clicked!")))
; (config! f :content lbl)

(listen b :mouse-entered #(config! % :foreground :blue)
          :mouse-exited #(config! % :foreground :red))

(def lb (listbox :model (-> 'clojure.core ns-publics keys sort)))

(display (scrollable lb))

(config! lb :background :gray :foreground :white)
(selection lb {:multi? true})

(listen lb :selection (fn [evnt] (println "Selection is " (selection evnt))))

(def field (display (text "This is a text field.")))

; There seems to be a ! function for setting the value of anything.

(config! field :font "MONOSPACED-PLAIN-12" :background "#f88")

(def area (text :multi-line? true :font "MONOSPACED-PLAIN-14"
                :background :blue
                :foreground :pink :text "This
Is
Multi
Lined"))

; (display area)

; (text! area (java.net.URL. "https://clojure.org"))

(display (scrollable area))

(def lrs left-right-split)
(def split (lrs (scrollable lb) (scrollable area) :divider-location 1/3))

(defn doc-str [s] 
  (-> 
   (symbol "clojure.core" (name s)) 
   resolve 
   meta 
   :doc))

(listen lb :selection
        (fn [evnt]
          (when-let [s (selection evnt)]
            (-> area
                (text! (doc-str s))
                (scroll! :to :top)))))

(def rbs 
  (for [i [:source :doc]]
    (radio :id i :class :type :text (name i))))
(display split)

(display 
 (border-panel
  :north (horizontal-panel :items rbs)
  :center split
  :vgap 5 :hgap 5 :border 5))

(def group (button-group))

(config! (select f [:.type]) :group group)

(listen group :selection
        (fn [evnt]
          (when-let [s (selection group)]
            (println "Selection is " (id-of s)))))
(selection group)
(defn -main [& args]
  (invoke-later
   (-> f
       pack! ; autosizes a frame to it's contents size. it & show! return args.
       show!)))