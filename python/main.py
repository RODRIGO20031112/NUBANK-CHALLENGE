import json

def calcular_imposto(operacoes):
    saldo_prejuizo = 0  # Prejuízo acumulado
    resultado = []  # Lista para armazenar os resultados de cada operação

    quantidade_total = 0  # Total de ações compradas
    preco_medio_ponderado = 0  # Preço médio ponderado

    for operacao in operacoes:
        if operacao["operation"] == "buy":
            # Atualiza o preço médio ponderado
            total_atual = preco_medio_ponderado * quantidade_total
            total_novo = operacao["unit-cost"] * operacao["quantity"]
            quantidade_total += operacao["quantity"]
            preco_medio_ponderado = (total_atual + total_novo) / quantidade_total
            resultado.append({"tax": 0.00})  # Não paga imposto na compra

        elif operacao["operation"] == "sell":
            total_operacao = operacao["unit-cost"] * operacao["quantity"]
            lucro = 0

            if total_operacao > 20000:  # Só paga imposto se o valor total da operação for maior que R$ 20.000
                if operacao["unit-cost"] > preco_medio_ponderado:
                    lucro = (operacao["unit-cost"] - preco_medio_ponderado) * operacao["quantity"]
                    imposto = lucro * 0.20  # 20% de imposto sobre o lucro
                    resultado.append({"tax": round(imposto, 2)})
                else:
                    # Prejuízo, não paga imposto
                    resultado.append({"tax": 0.00})
                    saldo_prejuizo += -lucro  # Deduz prejuízo
            else:
                # Valor total da operação menor que R$ 20.000, não paga imposto
                resultado.append({"tax": 0.00})

    return resultado

print("Digite as entradas (pressione Enter sem digitar nada para finalizar):")

# Leitura da entrada JSON
entrada = []
try:
    while True:
        linha = input().strip()
        
        if linha == "":
            break
        entrada.append(json.loads(linha))
except EOFError:
    pass

# Printa as entradas no console
print("Entrada(s)")
for operacoes in entrada:
    print(operacoes)

# Printa os impostos no console
print("Resultado(s) do(s) imposto(s):")
for operacoes in entrada:
    resultado = calcular_imposto(operacoes)
    print(json.dumps(resultado))
