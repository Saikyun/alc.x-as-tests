(ns alc.x-as-tests.immediate2
  (:require [clojure.java.io :as io]
            [alc.x-as-tests.impl.rewrite :as rewrite]))

(defmacro run-tests!
  []
  (if (boolean (:ns &env))
    (println "woop")
    (let [f *file*]
      (when (not= "NO_SOURCE_PATH" *file*)
        (let [code (try (slurp (or (io/resource f) f) )
                        (catch Exception e
                          (println "Warning during `alc.x-as-tests.cljs.immediate/run-tests!` (clj)")
                          (println "Failed reading file: " f)
                          (println e)
                          ""))]
          (read-string {:read-cond :allow}
                       (str "(do " (rewrite/rewrite-without-non-comment-blocks code) ")")))))))
(defmacro remove-tests
  []
  (println "lul"))
