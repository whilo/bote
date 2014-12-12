(defproject net.polyc0l0r/bote "0.1.0"
  :description "A SMTP server as a Clojure library."
  :url "https://github.com/ghubber/bote"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojars.nakkaya/subethasmtp "3.1.7"]
                 [com.draines/postal "1.11.3"]
                 [org.clojars.kjw/slf4j "1.5.5"]
                 [org.clojars.kjw/slf4j-simple "1.5.5"]]
  :jvm-opts ["-Djavax.net.ssl.keyStore=resources/serverKeyStore.key"
             "-Djavax.net.ssl.keyStorePassword=123456"])
