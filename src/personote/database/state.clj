(ns personote.database.state
    (:require [cljfx.api :as fx]
              [clojure.java.jdbc :as jdbc]))
              

(def db-spec
    {:dbtype "postgresql"
     :dbname "personote-db"
     :user "lorenzo-evans"
     :password "password"})


(def note-table-ddl
    (jdbc/create-table-ddl :notes
                           [[:title "varchar(32"]]))
                           
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