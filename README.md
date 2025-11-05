<div align="center">

# 🦊🐇 Simulador de Ecossistema  
### Expansão do modelo **Raposas & Coelhos** (Barnes & Kolling)

[![Status](https://img.shields.io/badge/status-em%20desenvolvimento-blue)](#)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](#)
[![License](https://img.shields.io/badge/license-MIT-green)](#)
[![Build](https://img.shields.io/badge/build-Gradle%2FMaven-lightgrey)](#)

</div>

> Projeto acadêmico que **refatora** o simulador clássico de *Raposas e Coelhos* e o **expande** com novos atores (Lobo, Planta), **obstáculos**, **estações do ano**, **clima** e **interface gráfica aprimorada**.  
> Foco em **POO**: herança, polimorfismo, abstração, composição e coesão.

---

## 🧭 Sumário
- [📘 Descrição](#-descrição)
- [🧩 Funcionalidades Principais](#-funcionalidades-principais)
- [⚙️ Estrutura do Projeto](#️-estrutura-do-projeto)
- [🚀 Como Executar](#-como-executar)
- [🗺️ Mapa (mapa.txt)](#️-mapa-mapatxt)
- [🎓 Conceitos de POO Aplicados](#-conceitos-de-poo-aplicados)
- [🛣️ Roadmap](#️-roadmap)
- [👥 Autores](#-autores)
- [🧠 Referência](#-referência)
- [📸 Prévia Visual (opcional)](#-prévia-visual-opcional)

---

## 📘 Descrição
Este projeto é uma **expansão** do simulador de *Raposas e Coelhos* do livro *Programação Orientada a Objetos com Java* de **Barnes e Kolling**.  
A primeira etapa consiste em **refatorar** o código original, criando uma classe abstrata `Ator` que generaliza comportamentos e elimina duplicações.  
A segunda etapa adiciona **novos atores**, **condições ambientais** e uma **interface visual aprimorada** para aumentar o realismo da simulação.

---

## 🧩 Funcionalidades Principais

### 🔹 Estrutura & Atores
| Componente | Descrição |
|-------------|------------|
| `Ator (abstrata)` | Base comum com comportamentos genéricos. |
| `Coelho` | Presa herbívora; alimenta-se de plantas. |
| `Raposa` | Predador de coelhos. |
| `Lobo` | Predador mais forte; caça raposas e coelhos. |
| `Planta (Grama)` | Fonte de alimento; cresce em áreas específicas. |

---

### 🗺️ Ambiente & Obstáculos
- O campo da simulação é definido por um arquivo externo `mapa.txt`.  
- Cada célula é representada por:
  - `X` → obstáculo (floresta, montanha, etc.)  
  - `.` → espaço livre  

Esses obstáculos bloqueiam movimento e tornam o ambiente mais realista.

---

### 🌦️ Condições Ambientais
- **Estações do ano**:
  - 🌸 **Primavera:** aumento da reprodução de coelhos  
  - ❄️ **Inverno:** escassez de alimento e aumento da mortalidade
- **Fenômenos climáticos:**
  - 🌧️ **Chuva:** acelera crescimento das plantas  
  - ☀️ **Seca:** reduz a disponibilidade de alimento  

Esses fatores trazem **dinamismo** e **variabilidade** à simulação.

---

### 🖥️ Interface Gráfica
- Botões: **Iniciar**, **Pausar**, **Resetar**  
- Informações exibidas:
  - População por espécie  
  - Rodada atual  
- Cores distintas para cada tipo de ator e obstáculo  

> A interface foi redesenhada para facilitar a visualização e controle da simulação.

---

## ⚙️ Estrutura do Projeto

```text
src/
│
├── modelo/
│   ├── Ator.java
│   ├── Raposa.java
│   ├── Coelho.java
│   ├── Lobo.java
│   ├── Planta.java
│
├── ambiente/
│   ├── Campo.java
│   ├── Localizacao.java
│   ├── Mapa.java
│
├── simulador/
│   ├── Simulador.java
│   ├── SimuladorGUI.java
│
├── dados/
│   └── mapa.txt
│
└── Main.java

---

## 🚀 Como Executar

### 🔧 Pré-requisitos
- **Java 17** ou superior  
- IDE recomendada: **VS Code**, **IntelliJ IDEA** ou **BlueJ**

### ▶️ Passos
```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/simulador-ecossistema.git
cd simulador-ecossistema

# 2. Abra o projeto na IDE

# 3. Execute
# Rode a classe principal:
# src/Main.java
..................
...XX.............
..XXX.............
..................
.....XX...........
..................
🎓 Conceitos de POO Aplicados

✅ Herança e Polimorfismo

✅ Classes Abstratas e Interfaces

✅ Composição e Coesão

✅ Leitura de Arquivos Externos

✅ Simulação Baseada em Agentes

✅ Separação de Responsabilidades

🛣️ Roadmap

 Classe abstrata Ator

 Inclusão de Lobo e Planta

 Sistema de mapa e obstáculos

 Interface com botões e status

 Parâmetros configuráveis via arquivo

 Gráficos de população

 Testes automatizados

👥 Autores

Raynner Gabriel Taniguchi Silva

(Adicione aqui os demais integrantes do grupo, se houver)

🧠 Referência

Barnes, D. J., & Kolling, M. (2012). Programação Orientada a Objetos com Java. Pearson.
