;; TODO
;;
;; XXX: possibly better to detect and abort for following:
;;
;;      1) more than one ns form
;;      2) more than one in-ns form
;;      3) both ns and in-ns forms exist
;;
;;      this motivates the idea of "directives" to instruct
;;      specifically where to place the clojure.test require and old
;;      test erasing forms.
;;
;; XXX: another thing to be wary of is a file that already has deftest
;;      or other testing constructs in it
;;
;; XXX: consider supporting "directives" -- e.g. comment blocks that
;;      contain particular sequences that affect the "transformation".
;;      one possible application is to indicate where to place the
;;      require form for clojure.test.  another obvious one might be
;;      something to indicate where test execution should happen.
;;      these sorts of things might allow more fine-grained control of
;;      the "transformation" to handle situations where there is more
;;      than one ns form or there are no ns forms (and say e.g. only
;;      an in-ns form).  there could be special "directives" within
;;      comment blocks to indicate what mode of processing to
;;      use (e.g. no such directives gives default behavior (insert
;;      require and run-tests), but existence of a particular one
;;      might turn off the default behavior).
;;
;; XXX: whitespace issues seem to make things kind of complex.  is
;;      there some way to postpone them to the end or ignore them when
;;      testing?  since there are depdencies anyway, would including
;;      zprint, cljfmt, cljstyle, or similar and using that sort of thing to
;;      format the results be worth it?  zprint in default mode didn't
;;      seem to help.  cljstyle seems promising.
;;
;; XXX: some large values are more readable formatted.  unfortunately,
;;      this can be manual work.  it would be nice to have a more
;;      automated way to achieve formatting.
;;
;; XXX: when an expected value is a long string, it is cumbersome to
;;      see and thus manually verify, edit, etc.  is there anything
;;      that can be done about this?
;;
;; XXX: there may be other issues noted as comments in the source.  look for
;;      "XXX"

;; QUESTIONS:
;;
;; XXX: printing results out seems to be helpful when manually
;;      verifying some kinds of results.  for automation, printed
;;      results aren't obviously available?  isn't there something
;;      like "print" that returns the value that it prints?
;;
;; XXX: rename some things in ast to end in "-node"?
;;      e.g. whitespace-node?
;;
;; XXX: should wrap-forms call parcera.core/code?  if so, rename?
;;      there is now a separate function that does this.  try to remove
;;      wrap-forms?
;;
;; XXX: test if metadata is handled in actual / expected expressions?
;;
;; XXX: clojure.string is used within comment blocks but not outside,
;;      is it worth being concerned about this?
;;
;; XXX: replace use of parcera nodes (e.g. [:whitespace " "]) with
;;      results of calling pc/ast (e.g. (pc/ast " ")) in implementations
;;      (not necessarily tests in comment blocks) as this will shield
;;      the code from parcera changes.  is this a relevant concern?
;;      if so, should the comment block tests also not refer to
;;      parcera nodes (e.g. [:whitespace " "])?
;;
;; XXX: if no metadata, how to choose test name?  it shouldn't stay
;;      static upon repeated invocations?  should this even be
;;      supported?

;; OBSERVATIONS
;;
;; * parcera returns things that look almost like hiccup
;;
;; * parcera/code can be fed hiccup
;;
;; * the keywords in nodes seem verbose -- :whitespace vs :ws
;;
;; * values like: (:list (:symbol "def") ...) might be inconvenient to
;;   express as "expected" values (eval-ing them leads to errors)
;;   converting them to use vector notation is a work-around, but for
;;   nested things seems rather more work than one might like.  using
;;   a single quote at the beginning seems to work ok.
;;
;; * comment blocks within comment blocks remain comment blocks after
;;   transformation.  this can be seen as a feature to have things
;;   that one only evaluates via the repl.

(ns alc.x-as-tests.main
  (:require
   [alc.x-as-tests.impl.rewrite :as rewrite])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main
  [& _]
  (let [slurped (slurp *in*)]
    (print (rewrite/rewrite-with-tests slurped)))
  (flush)
  (System/exit 0))