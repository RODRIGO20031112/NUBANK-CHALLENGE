# Certfique-se que tenha o Clojure CLI instalado

### Na raiz do projeto

```bash
clj main.clj
```

## Exemplo

### Insira no terminal

- Lembre-se que as entradas são listas das quais devem ser inseridas na linha uma por uma sem quebra pressionando (ENTER)

```c
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000},{"operation":"sell", "unit-cost":20.00, "quantity": 5000}] // ENTER

[{"operation":"buy", "unit-cost":20.00, "quantity": 10000},{"operation":"sell", "unit-cost":10.00, "quantity": 5000}]
```

- Saída

```text
[{:tax 0.0} {:tax 10000.0}]
[{:tax 0.0} {:tax 0.0}]
```
