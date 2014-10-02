(ns overlord-simple.core
  (:require
   [om.core :as om]
   [om-tools.core :refer-macros [defcomponent]]
   [om-tools.dom :as dom :include-macros true]))

(enable-console-print!)

(def vm-state (atom {:bytecode [{:i 0, :op :KSHORT, :a 0, :d 1}
                                {:i 1, :op :KSHORT, :a 1, :d 2}
                                {:i 2, :op :LOOP, :a nil, :d nil}
                                {:i 3, :op :MOV, :a 2, :d 0}
                                {:i 4, :op :MOV, :a 3, :d 1}
                                {:i 5, :op :BULKMOV, :a 0, :b 2, :c 2}
                                {:i 6, :op :TRANC, :a 2, :d 4}
                                {:i 7, :jt-nr 2, :op :JUMP, :a nil, :d -5}
                                {:i 8, :op :MOV, :a 0, :d 2}
                                {:i 9, :op :TRANC, :a 1, :d 2}
                                {:i 10, :op :EXIT, :a 0, :d nil}]
                     :current 0
                     :base 0
                     :slots [{:val 1
                              :type :int}
                             {:val 2
                              :type :int}
                             {:val 5.6
                              :type :float}
                             {:val :uuid-0
                              :type :ref}
                             {:val :uuid-1
                              :type :ref}]}))

(defn instruction-point-change [vm-state new-point]
  (assoc-in vm-state [:current] new-point))

(defn instruction-point-update [vm-state f]
  (update-in vm-state [:current] f))

(defn pprint-bc [bc sel]
  (dom/p {:style {:background-color (if (= sel :sel) "red" "white")}}
         (dom/code
          (if (:b bc)
            (str (:i bc) "   op: " (:op bc) " a: " (:a bc) " b: " (:b bc) " c: " (:c bc))
            (str (:i bc) "   op: " (:op bc) " a: " (:a bc) "             d: " (:d bc))))))

(defcomponent bytecode-list [data owner]
  (render [_]
          (dom/div
           {:style {:width 250}}
           (dom/h1 "Bytecode List")
           (map #(dom/p
                  (if (= (:i %)
                         (:current data))
                    (pprint-bc % :sel)
                    (pprint-bc % :not))
                  )
                (:bytecode data)))))

(defcomponent bytecode-list [data owner]
  (render [_]
          (dom/div
           {:style {:width 250}}
           (dom/h1 "Bytecode List")
           (map #(dom/p
                  (if (= (:i %)
                         (:current data))
                    (pprint-bc % :sel)
                    (pprint-bc % :not)))
                (:bytecode data))
           (dom/button {:onClick (fn [_] (swap! vm-state instruction-point-update inc))} "inc")
           (dom/button {:onClick (fn [_] (swap! vm-state instruction-point-update dec))} "dec"))))


(om/root bytecode-list
         vm-state
         {:target (. js/document (getElementById "fn"))})

#_(om/root bytecode-list
         vm-state
         {:target (. js/document (getElementById "simple-slots"))})



