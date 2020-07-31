(ns alc.x-as-tests.immediate
  (:require [alc.x-as-tests.impl.rewrite :as rewrite]))

(defmacro run-tests!
  []
  (let [f *file*]
    (when (not= "NO_SOURCE_PATH" *file*)
      (read-string (str "(do " (rewrite/rewrite-without-non-comment-blocks (slurp f)) ")")))))
