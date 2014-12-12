(ns bote.core
  (:import [org.subethamail.smtp.helper SimpleMessageListener]))



(defn- message-listener [accept-fn? message-fn]
  (proxy [SimpleMessageListener] []
    (accept [from to] (accept-fn? from to))

    (deliver [from to data]
      (let [mime-message (javax.mail.internet.MimeMessage.
                          (javax.mail.Session/getDefaultInstance
                           (java.util.Properties.)) data)]
        (message-fn {:from from
                     :to to
                     :subject (.getSubject mime-message)
                     :headers  (->> (.getAllHeaders mime-message)
                                    enumeration-seq
                                    (map (fn [h] [(.getName h)
                                                 (.getValue h)]))
                                    (into {}))
                     :recipients (map str (.getAllRecipients mime-message))
                     :content-type (.getContentType mime-message)
                     :content (.getContent mime-message)
                     :encoding (.getEncoding mime-message)
                     :sent-date (.getSentDate mime-message)
                     ::mime-message mime-message
                     ::raw-data data})))))


(defn create-smtp-server [message-fn & {:keys [accept-fn? port enable-tls? require-tls?]
                                   :or {accept-fn? (fn [from to] true)
                                        port 2525}}]
  (let [server
        (org.subethamail.smtp.server.SMTPServer.
         (org.subethamail.smtp.helper.SimpleMessageListenerAdapter.
          (message-listener accept-fn? message-fn)))]
    (when enable-tls? (.setEnableTLS server true))
    (when require-tls? (.setRequireTLS server true))
    (.setPort server port)
    server))

(comment

  (clojure.pprint/pprint (System/getProperties))

  (clojure.pprint/pprint (clojure.reflect/reflect javax.mail.Header))
  (clojure.pprint/pprint (clojure.reflect/reflect javax.mail.internet.MimeMessage))

  (def received (atom nil))
  (def smtp-server (create-smtp-server #(reset! received %)
                                       :port 2525
                                       :enable-tls? true
                                       :require-tls? true))


  (.getRequireTLS smtp-server)
  (.start smtp-server)
  (.stop smtp-server)


  (require '[postal.core :refer [send-message]])
  (send-message {:host "localhost"
                 :port 2525
                 :tls true}
                {:from "foo@bar.com"
                 :to "baz@bars.com"
                 :subject "IMPORTANT!!!1!"
                 :body "hello world!"}))
