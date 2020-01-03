(ns personote.database.state
    (:require [cljfx.api :as fx]
              [clojure.java.jdbc :as jdbc]))
              

(def db-spec
    {:dbtype "postgresql"
     :classname "org.postgresql.Driver"
     :dbname "personote-db"
     :user "lorenzo-evans"
     :port 5432
     :password "password"})
; (jdbc/execute! personote (jdbc/drop-table-ddl :users))
(def user-table-ddl
        (jdbc/create-table-ddl :users
                               [[:username "varchar(32)"]
                                [:email "varchar(32)"]
                                [:password "varchar(32)"]
                                [:user_id :serial "PRIMARY KEY"]]))
                            
; (jdbc/execute! personote (jdbc/drop-table-ddl :notes))
(def note-table-ddl
        (jdbc/create-table-ddl :notes
                           [[:title "varchar(32)"]
                            ["CONSTRAINT id PRIMARY KEY (user_id)"]
                            [:content :text]
                            [:tags "text[]"]]))

(println "User Table: " user-table-ddl)
(println "Note Table: " note-table-ddl)

(defonce db-tables (jdbc/execute! db-spec [user-table-ddl note-table-ddl]))

(defn add-user [db table username email password] 
    (j/insert db table {:username username :email email :password password}))

(add-user db-spec :users "Jack_Sparrow" "pirate@bperl.com" "yohoho")

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