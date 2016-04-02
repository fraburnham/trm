(ns trm.example.core
  (:require [korma
             [core :as kc]
             [db :as db]]
            [trm.core :refer [fetch new! update!]])
  (:import [trm.core DataType]))

(db/defdb default (db/postgres {:db "trm"
                                :user "fraburnham"
                                :password "wapatowillie"
                                :host "localhost"}))

(defrecord StandaloneModel [id message])

(def is-standalone? (partial instance? StandaloneModel))

(deftype Standalone []
  DataType
  (fetch
    [_ {:keys [id]}]
    (if id
      (-> (kc/exec-raw ["select * from standalone where id = ?" [id]] :results)
          first
          (map->StandaloneModel))
      (->> (kc/exec-raw ["select * from standalone;"] :results)
           (map map->StandaloneModel))))

  (new!
    [_ model]
    (if (is-standalone? model)
      (let [{:keys [message]} model]
        (-> (kc/exec-raw ["insert into standalone (message) values (?) returning *" [message]] :results)
            (map->StandaloneModel)))
      (throw (Exception. "Invalid model"))))

  (update!
    [_ model]
    (if (is-standalone? model)
      (let [{:keys [message id]} model]
        (-> (kc/exec-raw ["update standalone set message = ? where id = ? returning *" [message id]] :results)
            (map->StandaloneModel)))
      (throw (Exception. "Invalid model")))))
