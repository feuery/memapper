(ns merpg.UI.main-layout
  (:require [seesaw.core :refer [frame border-panel
                                 vertical-panel top-bottom-split
                                 button]]
            [seesaw.chooser :refer :all]
            [merpg.IO.tileset :refer [load-tileset]]
            [merpg.UI.map-controller :refer [map-controller]]
            [merpg.UI.tileset-controller :refer :all]
            [merpg.UI.tool-box :refer [tool-frame!]]
            [merpg.UI.BindableCanvas :refer :all]
            [merpg.UI.BindableList :refer :all]
            [merpg.immutable.basic-map-stuff :refer :all]))

(defn get-content []
  (let [map-width  10
        map-height  10] ;;The following atoms are needed on the top-level...
    (def map-data-image (atom (make-map map-width
                                       map-height
                                       2)))
    (def current-layer-atom (atom nil))
    (def current-layer-index-atom (atom 0 :validator #(and (>= % 0) (< % (layer-count @map-data-image)))))                                                       
    
    (def tool-atom (atom {}))
    (def current-tool-fn (atom nil))
    
    (def tileset-atom (atom [(load-tileset "/Users/feuer2/Dropbox/memapper/tileset.png")]))
    (def current-tileset-index-atom (atom 0))
    (def current-tileset-atom (atom nil))
    (def current-tile (ref (tile 0 0 0 0))))
  
  (border-panel
   :center
   (top-bottom-split
    (map-controller map-data-image tool-atom current-tool-fn tileset-atom
                    current-tile current-layer-index-atom)
    (tileset-controller tileset-atom
                        current-tileset-index-atom
                        current-tile)
    :divider-location 3/4)
   :west (vertical-panel
          :items
          [(tool-frame! tool-atom current-tool-fn)
           (bindable-canvas current-tile
                            (fn [tile]
                              (-> @tileset-atom
                                  (get (:tileset tile))
                                  (get (:x tile))
                                  (get (:y tile)))))
           "Layers"
           (bindable-list map-data-image
                          current-layer-atom
                          :custom-model-bind #(-> % meta :name)
                          :selected-index-atom current-layer-index-atom
                          :reverse? true)
           (button :text "New layer"
                   :listen
                   [:action (fn [_]
                              (swap! map-data-image conj (make-layer (width @map-data-image)
                                                                     (height @map-data-image))))])
           
           "Tilesets"
           (bindable-list tileset-atom
                          current-tileset-atom
                          :custom-model-bind (fn [_]
                                               (str (inc @current-tileset-index-atom) "th"))
                          :selected-index-atom current-tileset-index-atom)
           (button :text "Load tileset"
                   :listen
                   [:action (fn [_]
                              (choose-file :filters [["Tilesetit" ["png" "jpg" "jpeg"]]]
                                           :remember-directory? true
                                           :multi? true :success-fn
                                           (fn [_ files]
                                             (let [tilesets (->> files
                                                                 (map str)
                                                                 (map load-tileset))]
                                               (swap! tileset-atom #(vec (concat % tilesets)))))))])
                                                  
                                                  ])))