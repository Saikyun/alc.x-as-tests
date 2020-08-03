(ns alc.x-as-tests.cljs.immediate
  (:require [cljs.test]
            [cljs.analyzer :as ana]
            #?@(:clj [[alc.x-as-tests.impl.rewrite :as rewrite]
                      [clojure.java.io :as io]])))

#?(:clj (defmacro run-tests!
          []
          (let [code (try (slurp (io/resource ana/*cljs-file*))
                          (catch Exception e
                            (println "Warning during `alc.x-as-tests.cljs.immediate/run-tests!`")
                            (println "Failed reading file: " ana/*cljs-file*)
                            (println e)
                            ""))]
            `(do ~(read-string (rewrite/rewrite-without-non-comment-blocks-cljs code))))))

(defmacro remove-tests
  []
  (let [fs (->> (keys (:defs (:ns &env)))
                (filter (fn [test-name]
                          (re-matches #"^test-at-line-.*"
                                      (name test-name)))))]
    `(do ~@(map (fn [test-name]
                  `(cljs.core/ns-unmap '~ana/*cljs-ns* '~test-name)) fs))))
