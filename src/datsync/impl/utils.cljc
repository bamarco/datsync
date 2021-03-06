(ns datsync.impl.utils
  (:require #?(:clj [clojure.core.match :as match :refer [match]]
               :cljs [cljs.core.match :refer-macros [match]])))

(declare inner-merge)

(defn deep-merge
  ([map1] map1)
  ([map1 map2]
   (merge-with inner-merge map1 map2)))

(defn inner-merge-dispatch-signature
  [x]
  {:seq? (seq? x) :map? (map? x)})

(defn merge-zip
  [xs ys]
  (mapv deep-merge xs ys))

(def default-inner-merge-opts
  {:coll-merge concat ;; other options; merge-zip
   })

(defn inner-merge
  ([x y] (inner-merge default-inner-merge-opts x y))
  ([{:keys [coll-merge] :as opts} x y]
   (match (mapv inner-merge-dispatch-signature [x y])
     ;; Then merge the two maps
     [{:map? true} {:map? true}]
     (deep-merge x y)
     ;; Merge two maps of collections
     [{:col? true} {:col? true}]
     (coll-merge x y)
     ;; default to the second value in any other case
     :else y)))


(defn tr
  ([message x]
   (println message (with-out-str (#?(:clj clojure.pprint/pprint :cljs cljs.pprint/pprint) x)))
   x)
  ([x]
   (tr "" x)))



