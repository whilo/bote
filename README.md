# bote

[Inspired by](http://nakkaya.com/2012/07/25/yet-another-disposable-e-mail-web-application-in-clojure/) `bote` implements an smtp server as a small clojure library. The underlying [subethasmtp library](https://code.google.com/p/subethasmtp/) supports encryption with TLS and implementation of authentication exchanges.

## Usage

~~~clojure
user> (require '[bote.core :refer [create-smtp-server]])
nil
user> (def received (atom nil))
#'user/received
user> (def smtp-server (create-smtp-server #(reset! received %)
                                       :port 2525
                                       :enable-tls? true
                                       :require-tls? true))
#'user/smtp-server
user> (.start smtp-server)
nil
user> (require '[postal.core :refer [send-message]])
nil
user> (send-message {:host "localhost"
                 :port 2525
                 :tls true}
                {:from "foo@bar.com"
                 :to "baz@bars.com"
                 :subject "IMPORTANT!!!1!"
                 :body "hello world!"})
SunCertPathBuilderException unable to find valid certification path to requested target  sun.security.provider.certpath.SunCertPathBuilder.engineBuild (SunCertPathBuilder.java:196)
user> ; this happens because it is a self-signed test cert, try without tls
user> (.stop smtp-server)
nil
user> (def smtp-server (create-smtp-server #(reset! received %)
                                       :port 2525))
#'user/smtp-server
user> (.start smtp-server)
nil
user> (send-message {:host "localhost"
                 :port 2525}
                {:from "foo@bar.com"
                 :to "baz@bars.com"
                 :subject "IMPORTANT!!!1!"
                 :body "hello world!"})
{:code 0, :error :SUCCESS, :message "messages sent"}
user> (clojure.pprint/pprint @received)
{:encoding "7bit",
 :bote.core/raw-data
 #<ReceivedHeaderStream org.subethamail.smtp.io.ReceivedHeaderStream@55aeedd>,
 :content "hello world!\r\n",
 :bote.core/mime-message
 #<MimeMessage javax.mail.internet.MimeMessage@222b4467>,
 :recipients ("baz@bars.com"),
 :headers
 {"Content-Transfer-Encoding" "7bit",
  "MIME-Version" "1.0",
  "To" "baz@bars.com",
  "User-Agent" "postal/1.11.3",
  "Date" "Fri, 12 Dec 2014 11:26:32 +0100 (CET)",
  "Subject" "IMPORTANT!!!1!",
  "Content-Type" "text/plain; charset=utf-8",
  "From" "foo@bar.com",
  "Received"
  "from benjamin.polyc0l0r.net (localhost [127.0.0.1])\r\n        by benjamin.polyc0l0r.net\r\n        with SMTP (SubEthaSMTP 3.1.7) id I3LETQDL\r\n        for baz@bars.com;\r\n        Fri, 12 Dec 2014 11:26:32 +0100 (CET)",
  "Message-ID"
  "<zwS5ffDFFWG05Uyx5C84A.1418379992298@postal.benjamin>"},
 :from "foo@bar.com",
 :sent-date #inst "2014-12-12T10:26:32.000-00:00",
 :content-type "text/plain; charset=utf-8",
 :subject "IMPORTANT!!!1!",
 :to "baz@bars.com"}
nil

~~~

## TODO
* Wrap authentication in a clojuresque way.
* parse attachments

## License

Copyright © 2014 Christian Weilbach

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
