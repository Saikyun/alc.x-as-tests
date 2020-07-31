(ns x-as-tests.examples.immediate
  (:require [alc.x-as-tests.immediate :refer [run-tests!]]))

(comment
  (+ 1 1)
  ;;=> 2
  )

(comment
  (* 5 5)
  ;;=> 25
  )

(run-tests!)
