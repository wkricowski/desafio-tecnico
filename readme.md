# 🏆 Desafio Técnico – EMV

## 🎯 Objetivo  
Criar um módulo básico de processamento de transações EMV, que valide e simule a comunicação entre um terminal de pagamento (POS) e um cartão de crédito/débito com chip.  

## 📌 Requisitos  

1. **Linguagem**: Escolha entre **Golang, Kotlin, Java ou Rust**.  
2. **Protocolo EMV**: Implementar uma lógica básica para processar uma transação com base em um conjunto de TLVs (Tag-Length-Value).  
3. **Fluxo da Transação**:  
   - Entrada de dados do cartão (simulado).  
   - Decodificação de TLVs da transação.  
   - Validação de dados essenciais (PAN, data de validade, CVM).  
   - Simulação de comunicação com gateway de pagamento para autorização da transação.  
   - Retorno do resultado da transação (aprovada/rejeitada).  
4. **Testes**: Criar **casos de teste unitários** para validar a implementação.  

## 🔍 Detalhes da Implementação  

- **Leitura dos Dados do Cartão**: Simular a extração dos seguintes TLVs:  
  - `5A` – PAN (Primary Account Number).  
  - `5F24` – Data de validade.  
  - `9F34` – CVM (Cardholder Verification Method).  
- **Validação**:  
  - O PAN deve ter entre 13 e 19 dígitos e passar no algoritmo de Luhn.  
  - A data de validade não pode ser anterior à data atual.  
  - O CVM deve conter pelo menos um método suportado.  
- **Simulação de Autorização**: Criar um serviço mock que retorne aleatoriamente uma autorização aprovada ou rejeitada.  
- **Log de Transações**: Registrar transações processadas em um arquivo JSON ou banco de dados SQLite.  

## ✅ Critérios de Avaliação  
✔ Qualidade do código e boas práticas.  
✔ Correta implementação do fluxo EMV.  
✔ Tratamento de erros e logs.  
✔ Testes automatizados.  
✔ Organização e documentação do código.  

## 📦 Entrega  
- Código-fonte hospedado no **GitHub/GitLab**.  
- Um README explicando a implementação e como rodar o projeto.  

🚀 **Boa sorte e mãos à obra!**  