(ns personote.database.state
    (:require [cljfx.api :as fx]
              [clojure.java.jdbc :as jdbc]))
              

(def db-spec
    {:dbtype "postgresql"
     :classname "org.postgresql.Driver"
     :dbname "personote-db"
     :user "lorenzo-evans"
     :port 
     :password "password"})

(def user-table-ddl
    (jdbc/db-do-commands "postgresql://localhost:5432/personote"
        (jdbc/create-table-ddl :users
                               [[:username "varchar(32)"]
                                [:email "varchar(32)"]
                                [:password "varchar(32)"]
                                [:id :primary :key]])))
(def note-table-ddl
    (jdbc/db-do-commands "postgresql://localhost:5432/personote"
        (jdbc/create-table-ddl :notes
                           [[:title "varchar(32)"]
                            [:owner :integer :references :users :id]
                            [:content :text]
                            [:tags "text[]"]])))
                           
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