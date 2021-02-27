(ns lineart.core
  (:require
   [clojure.string :as s]))

(def canvas-width 500)
(def canvas-height 100)
(def canvas-size (str canvas-width " " canvas-height))

(defn qs->hash-map
  "
  Parse the query string into a hash-map with keyword keys

  Takes a string like \"bg=000000&fg=ffffff\"

  Returns a hash-map like:
  {:bg \"#000000\"
   :fg \"#ffffff\"}
  "
  [qs]
  (->> (s/split qs #"&")
       (map #(s/split % #"="))
       (map #(vector (keyword (first %)) (str "#" (second %))))
       (into {})))

(defn rand-range
  "
  Generate an integer between the begin and end params

  Takes a begin integer and an end integer.
  The begin number must be less than end integer.

  Returns an integer
  "
  [begin end]
  (-> (- end begin)
      (* (rand))
      (+ begin)
      (int)))

(defn canvas-rect
  "
  Create a rectangle the same dimensions as viewBox and canvas

  Optionally takes other attributes to merge together

  Returns the rectangle hiccup vector
  [:rect
    {...}]
  "
  [& [opts & _]]
  [:rect
   (merge
    {:x 0
     :y 0
     :width canvas-width
     :height canvas-height}
    opts)])

(defn long-diag-lines
  "
  Default row of long diagonal lines across the image

  Lines are 10px apart

  Takes a hash-map:
  - :fg Foreground color used for line stroke

  Returns a hiccup group vector:
  [:g
    ...]
  "
  [{:keys [fg]}]
  [:g
   (for [i (range 0 53)]
     [:line
      {:x1 (+ (* i 10) 0)
       :x2 (+ (* i 10) -25)
       :y1 20
       :y2 80
       :stroke fg
       :stroke-opacity "15%"}])])

(defn rand-diag-lines
  "
  Random smaller lines that make the pattern a bit more visually interesting

  Parallel to the long lines is -8px from (- x2 x1)

  Takes a hash-map:
  - :x-seed random int for x-coords
  - :y-seed random int for y-coords
  - :fg Foreground color used for line stroke

  Returns a hiccup group vector:
  [:g
    ...]
  "
  [{:keys [x-seed y-seed fg]}]
  [:g
   (for [i (range 0 54)]
     [:line
      {:x1 (+ (* i 10) x-seed)
       :x2 (+ (* i 10) (- x-seed 8.25))
       :y1 y-seed
       :y2 (+ y-seed 20)
       :stroke fg
       :stroke-width 2
       :stroke-opacity "15%"}])])

(defn generate
  "
  Generates an svg document in hiccup syntax (vectors) displaying two layers of
  diagonal lines.

  Takes a query string:
  \"bg=363333&fg=f3c581\"

  Returns the hiccup xml doc:
  [:xml
   {...}
   ...]
  "
  [qs]
  (let [{:keys [bg fg]} (qs->hash-map qs)]
    [:svg
     {:version "1.1"
      :xmlns "http://www.w3.org/2000/svg"
      :xmlns:xlink "http://www.w3.org/1999/xlink"
      :x "0px"
      :y "0px"
      :viewBox (str "0 0 " canvas-size)
      :xml:space "preserve"
      :clip-path "url(#clip-box)"
      :fill "#000"}

     [:clipPath
      {:id "clip-box"
       :clipPathUnits "objectBoundingBox"}
      (canvas-rect)]

     (canvas-rect
      {:fill bg})

     (long-diag-lines
      {:fg fg})

     (rand-diag-lines
      {:fg fg
       :x-seed (rand-range -25 -10)
       :y-seed (rand-range 20 60)})]))


(comment
  (require '[hiccup2.core :refer [html]])
  (generate "bg=363333&fg1=F3C581&fg2=FFFFFF"))
