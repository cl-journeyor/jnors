(ns jnors.core
  "Supplementary API.")

;;;; Types.

(defprotocol Else
  "Specifies a type that can handle an alternative operation."
  (else [this f] "Takes `f` as the alternative operation."))

(defprotocol Then
  "Specifies a type that can handle a primary operation."
  (then [this f] "Takes `f` as the primary operation."))

(defrecord FetchElse [url opts tfn]
  Else (else [{:keys [url opts tfn]} f]
         (-> (js/fetch url (clj->js opts))
             (.then #(.json %))
             (.then tfn)
             (.catch f))
         nil))

(defrecord FetchThen [url opts]
  Then (then [{:keys [url opts]} f]
         (FetchElse. url opts f)))


;;;; Functions.

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

(defn fetch
  "Function pipeline to perform a JavaScript fetch to `url` with an `opts` map
  to configure the request.
   
  The pipeline can be invoked like this:
  ```
  (-> (fetch \"https://site.io\" {:method \"GET\"})
      (then #(println \"The jso:\" %))
      (else #(println \"Network error:\" %)))
  ```
   
  The else clause is mandatory as it evaluates the pipeline and returns nil."
  ([url]
   (fetch url nil))
  ([url opts]
   (FetchThen. url opts)))

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
