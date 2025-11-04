(ns jnors.core
  "Supplementary API.")

(defn throws?
  "Invokes the provided `f` to check whether it throws an exception."
  [f]
  (try
    (not (any? (f)))
    (catch js/Error _ true)))
