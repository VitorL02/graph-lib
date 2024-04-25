
#  Back-end Graph Lib

Realizei a criação desse repositorio, para guardar os conhecimentos adquiridos ao realizar a criação dessa API/LIB que trabalha com o processamento e geração de um relatorio baseado em um grafo enviado via .txt, para a cadeira de Algoritimos em Grafos.


## Referência

 - [Linkedin Pessoal](https://www.linkedin.com/in/vitorlucascrispim/)



## Documentação da API

#### O projeto está no ar ate o presente momento para acessar sua Documentação acesse a url abaixo: 

```http
https://graph-lib.onrender.com/swagger-ui/index.html#
```
#### O servico na sua verão 1.0 conta com três requisições

```http
 POST:/graph/upload
 que espera uma aquivo variado no formato de "multipart/form-data" .txt
 com o seguinte o formato :
    4 -> Quantidade de vertices do grafo
    1 2 -> Arestas do grafo
    4 5 -> Arestas do grafo
    1 4 -> Arestas do grafo
    5 7 -> Arestas do grafo

    *Qualquer formato diferente pode causar erro na geração dos arquivos*
    *Devido a limitação da ram do render a biblioteca processa somente grafos pequenos*    
    *Caso opte por clonar o codigo, grafos maiores são processados*

GET:/graph/status
 um GET que traz o status da geração do relatorio atual

GET:/graph/download"
 um GET que traz o download do arquivo de relatorio do ultimo solicitado.
```


## Instalação

Necessario todo ambiente spring caso vá rodar localmente
  - [Documentação Spring - Intellij](https://www.jetbrains.com/help/idea/spring-support.html)   
- [Documentação Spring - Eclipse](https://www.eclipse.org/community/eclipse_newsletter/2018/february/springboot.php)   

## Aprendizados

Foi utilizado somente o ecossistema Spring para o gerenciamento de todo o processo. Schedules foram utilizadas para melhor performance de geração do relatorio.






## Stack utilizada

**Back-end:** Spring boot

