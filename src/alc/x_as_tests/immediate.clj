(ns alc.x-as-tests.immediate
  (:require [clojure.java.io :as io]
            [alc.x-as-tests.impl.rewrite :as rewrite]))

(defmacro run-tests!
  []
  (if (boolean (:ns &env))
    (do (require 'cljs.analyzer)
        (let [code (try (slurp (io/resource cljs.analyzer/*cljs-file*))
                        (catch Exception e
                          (println "Warning during `alc.x-as-tests.cljs.immediate/run-tests!` (cljs)")
                          (println "Failed reading file: " cljs.analyzer/*cljs-file*)
                          (println e)
                          ""))]
          (read-string (str "(do" (rewrite/rewrite-without-non-comment-blocks-cljs code) ")"))))
    (let [f *file*]
      (when (not= "NO_SOURCE_PATH" *file*)
        (let [code (try (slurp (io/resource f))
                        (catch Exception e
                          (println "Warning during `alc.x-as-tests.cljs.immediate/run-tests!` (clj)")
                          (println "Failed reading file: " f)
                          (println e)
                          ""))]
          (read-string (str "(do " (rewrite/rewrite-without-non-comment-blocks code) ")")))))))

(defmacro remove-tests
  []
  (require 'cljs.analyzer)
  (let [fs (->> (keys (:defs (:ns &env)))
                (filter (fn [test-name]
                          (re-matches #"^test-at-line-.*"
                                      (name test-name)))))]
    `(do ~@(map (fn [test-name]
                  `(cljs.core/ns-unmap '~cljs.analyzer/*cljs-ns* '~test-name)) fs))))
