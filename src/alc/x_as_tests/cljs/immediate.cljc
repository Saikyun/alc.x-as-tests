(ns alc.x-as-tests.cljs.immediate
  (:require #?@(:clj [[alc.x-as-tests.impl.rewrite :as rewrite]
                      [cljs.analyzer :as ana]])))

(def ^:dynamic *rewrite* true) 

#?(:clj (do (defmacro run-tests!
              []
              (let [f cljs.analyzer/*cljs-file*]
                (when (and (not= "NO_SOURCE_PATH" cljs.analyzer/*cljs-file*)
                           *rewrite*)
                  (read-string (str "(binding [alc.x-as-tests.cljs.immediate/*rewrite* false]
" (rewrite/rewrite-with-tests-cljs (slurp f))
                                    ")")))))
            
            (defmacro remove-tests
              []
              (let [fs (->> (keys (:defs (:ns &env)))
                            (filter (fn [test-name]
                                      (re-matches #"^test-at-line-.*"
                                                  (name test-name)))))]
                `(do ~@(map (fn [test-name]
                              `(cljs.core/ns-unmap '~ana/*cljs-ns* '~test-name)) fs))))))
