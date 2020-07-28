(ns alc.x-as-tests.immediate)


(def ^:dynamic *rewrite* true)

(defmacro run-tests!
  []
  (let [f *file*]
    (when (and (not= "NO_SOURCE_PATH" *file*) *rewrite*)
      `(binding [*rewrite* false]
         (eval (read-string (str "(do " (rewrite/rewrite-with-tests (slurp ~f)) ")")))))))
