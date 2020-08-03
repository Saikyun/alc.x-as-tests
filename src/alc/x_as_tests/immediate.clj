(ns alc.x-as-tests.immediate
  (:require [clojure.java.io :as io]
            [alc.x-as-tests.impl.rewrite :as rewrite]))

(defmacro run-tests!
  []
  (let [f *file*]
    (when (not= "NO_SOURCE_PATH" *file*)
      (let [code (try (slurp (io/resource f))
                      (catch Exception e
                        (println "Warning during `alc.x-as-tests.cljs.immediate/run-tests!`")
                        (println "Failed reading file: " f)
                        (println e)
                        ""))]
        (read-string (str "(do " (rewrite/rewrite-without-non-comment-blocks code) ")"))))))
