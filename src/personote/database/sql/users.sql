-- src/personote/database/sql/users
-- Personote Users
-- :name create-users-table
-- :command :execute
-- :result :raw
-- :doc Create users table

create table users (
  id integer auto_increment primary key,
  user_name not null varchar(40),
  email not null varchar(40),
  password not null varchar(40),
  created_at timestamp not null default current_timestamp 
)

-- :name drop-users-table
-- :doc Drop users table if exists
drop table if exists users

-- :name insert-user :! :n
-- :doc insert a single character
insert into users (user_name, email, password)
values (:user_name, :email, :password)

-- :name insert-users :! :n
-- :doc insert multiple users with tuple param type
insert into users (user_name, email, password)
values :tuple*users

-- :name update-username
update users
set user_name = :user_name
where id := id

-- :name update-email
update users
set email = :email
where id = :id

-- :name update-password
update users 
set password = :password
where id =  :id

-- :name delete-user


