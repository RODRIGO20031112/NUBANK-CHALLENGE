;; Requer a biblioteca para manipulação de JSON em Clojure
(require '[clojure.data.json :as json])

;; Define a função `calcular-imposto` que recebe uma lista de operações de compra e venda
(defn calcular-imposto [operacoes]
  ;; Define variáveis atômicas para armazenar os resultados e cálculos durante o processo
  (let [resultado (atom []) ;; Armazena o resultado final (taxas de imposto)
        saldo-prejuizo (atom 0) ;; Armazena o saldo de prejuízo (não utilizado no código atual)
        quantidade-total (atom 0) ;; Armazena a quantidade total de ações compradas
        preco-medio-ponderado (atom 0)] ;; Armazena o preço médio ponderado das ações compradas
    ;; Itera sobre cada operação na lista de operações
    (doseq [operacao operacoes]
      ;; Verifica se a operação é de compra
      (if (= (:operation operacao) "buy")
        (do
          ;; Calcula o total atual e o novo total com base nas compras realizadas
          (let [total-atual (* @preco-medio-ponderado @quantidade-total)
                total-novo (* (:unit-cost operacao) (:quantity operacao))] 
            ;; Atualiza a quantidade total de ações compradas
            (swap! quantidade-total + (:quantity operacao))
            ;; Calcula o novo preço médio ponderado
            (reset! preco-medio-ponderado (/ (+ total-atual total-novo) @quantidade-total)))
          ;; Se for uma operação de compra, não há imposto, então armazena 0.00 como imposto
          (swap! resultado conj {:tax 0.00}))
        ;; Caso contrário (se for uma venda)
        (let [total-operacao (* (:unit-cost operacao) (:quantity operacao))] 
          ;; Verifica se o valor da operação de venda é superior a 20.000
          (if (> total-operacao 20000)
            (let [lucro (if (> (:unit-cost operacao) @preco-medio-ponderado)
                          (* (- (:unit-cost operacao) @preco-medio-ponderado) (:quantity operacao))
                          0)
                  imposto (if (> lucro 0) (* lucro 0.20) 0)]
              ;; Se houver lucro, calcula o imposto e adiciona ao resultado
              (swap! resultado conj {:tax (/ (Math/round (* imposto 100.0)) 100.0)}))
            ;; Caso não haja imposto (se o valor da operação for menor ou igual a 20.000), armazena 0.00
            (swap! resultado conj {:tax 0.00})))))
    ;; Retorna o resultado final com as taxas de imposto calculadas
    @resultado))

;; Exibe uma mensagem para o usuário pedindo para digitar as entradas
(println "Digite as entradas (pressione Enter sem digitar nada para finalizar):")

;; Lê as entradas do usuário e as armazena em uma lista
(let [entradas (loop [lines []]
                 (let [linha (read-line)]
                   (if (empty? linha)
                     lines
                     (recur (conj lines linha)))))  ; Continua lendo até a linha vazia
      dados-json (map #(json/read-str % :key-fn keyword) entradas)  ; Converte as linhas em JSON
      resultado-imposto (map calcular-imposto dados-json)]  ; Calcula o imposto para cada entrada
  (println "Entrada(s):")
  (doseq [entrada dados-json]
    (println entrada))  ; Exibe o que foi digitado
  (println "Resultado(s) do(s) imposto(s):")
  (doseq [resultado resultado-imposto]
    (println resultado)))  ; Exibe o resultado do imposto para cada entrada

