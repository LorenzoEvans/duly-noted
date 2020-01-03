(ns personote.database.state
    (:require [cljfx.api :as fx]
              [clojure.java.jdbc :as jdbc]))
              

(def db-spec
    {:dbtype "postgresql"
     :classname "org.postgresql.Driver"
     :dbname "personote"
     
     :port 5432})
; (jdbc/execute! db-spec (jdbc/drop-table-ddl :users))
; (def user-table-ddl
;         (jdbc/create-table-ddl :users
;                                [[:username "varchar(32)"]
;                                 [:email "varchar(32)"]
;                                 [:password "varchar(32)"]
;                                 [:user_id :serial "PRIMARY KEY"]
;                                 [:notes "text[]"]]))
                            
; ; (jdbc/execute! db-spec (jdbc/drop-table-ddl :notes))
; (def note-table-ddl
;         (jdbc/create-table-ddl :notes
;                            [[:title "varchar(32)"]
;                             [:user_id :int "REFERENCES users"]
;                             [:note_id :serial "PRIMARY KEY"]
;                             [:content :text]
;                             [:tags "text[]"]]))

; (println "User Table: " user-table-ddl)
; (println "Note Table: " note-table-ddl)

; (jdbc/execute! db-spec [user-table-ddl])
; (jdbc/execute! db-spec [note-table-ddl])

(defn add-user [db table username email password] 
    (jdbc/insert! db table {:username username :email email :password password}))

(add-user db-spec :users "Jack_Sparrow" "pirate@bperl.com" "yohoho")
