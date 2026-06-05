(ns jnors.core
  "Supplementary API.")

(defn between?
  "Checks whether `lower <= x <= upper` is true."
  [x lower upper]
  (and
   (>= x lower)
   (<= x upper)))

(defn effect-on-ineq
  "Takes a parameterless `proc`, presumably for side-effects, and a
  parameterless `suppl`, and returns a new parameterless function that executes
  `proc` the first time and when (suppl) not= the previously cached value."
  [proc suppl]
  (let [state (atom (js-obj))]
    (fn []
      (let [new-state (suppl)]
        (when-not (= new-state @state)
          (reset! state new-state)
          (proc))))))

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
