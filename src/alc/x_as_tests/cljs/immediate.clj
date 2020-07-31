(ns alc.x-as-tests.cljs.immediate
  (:require [cljs.test]
            [alc.x-as-tests.impl.rewrite :as rewrite]
            [cljs.analyzer :as ana]))

(defmacro run-tests!
  [path]
  (read-string (str "(do " (rewrite/rewrite-without-non-comment-blocks-cljs (slurp path)) ")")))

(defmacro remove-tests
  []
  (let [fs (->> (keys (:defs (:ns &env)))
                (filter (fn [test-name]
                          (re-matches #"^test-at-line-.*"
                                      (name test-name)))))]
    `(do ~@(map (fn [test-name]
                  `(cljs.core/ns-unmap '~ana/*cljs-ns* '~test-name)) fs))))
