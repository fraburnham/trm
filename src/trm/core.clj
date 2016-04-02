(ns trm.core
  (:gen-class))

;; the type should also provide a standard "type" for response
;; that would force the response type to implement `success?`
(defprotocol DataType
  (fetch [this args] "Return a sensible result for the DataType")
  (new! [this args] "Allow persisting of a new record of DataType")
  (update! [this args] "Allow persisting changes to DataType"))
