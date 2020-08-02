(ns alc.x-as-tests.cljs.immediate
  (:require [cljs.test]
            [cljs.analyzer :as ana]
            #?@(:clj [[alc.x-as-tests.impl.rewrite :as rewrite]
                      [clojure.java.io :as io]])))

#?(:clj (defmacro run-tests!
          []
          (read-string (str "(do " (rewrite/rewrite-without-non-comment-blocks-cljs (slurp (io/resource ana/*cljs-file*))) ")"))))

(defmacro remove-tests
  []
  (let [fs (->> (keys (:defs (:ns &env)))
                (filter (fn [test-name]
                          (re-matches #"^test-at-line-.*"
                                      (name test-name)))))]
    `(do ~@(map (fn [test-name]
                  `(cljs.core/ns-unmap '~ana/*cljs-ns* '~test-name)) fs))))
