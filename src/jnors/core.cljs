(ns jnors.core
  "Supplementary API.")

(defn between?
  "Checks whether `lower <= x <= upper` is true."
  [x upper lower]
  (and
   (>= x lower)
   (<= x upper)))

(defn find-first
  "Returns the first item from `coll` that matches the given `pred`."
  [pred coll]
  (->> coll
       (filter pred)
       first))

(defn slice
  "Retrieves a sequence from the item number `start` to number `endx`
  exclusive, from `coll`."
  [start endx coll]
  (->> coll (drop start) (take (- endx start))))

(defn throws?
  "Invokes the provided `f` to check whether it throws an exception."
  [f]
  (try
    (not (any? (f)))
    (catch js/Error _ true)))
