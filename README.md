# UpCar

Aplicativo Android de caronas universitárias. O passageiro busca motoristas com destino ao campus, negocia o valor por chat e confirma a carona.

Projeto do Trabalho 2 (MAF - Mínimo Aplicativo Funcional) da A2.

## Integrantes

- [Luan Neuwirth ]
- [Murilo Opis]
- [Lorenzo Vanelli]

## Funcionalidades

- Mapa (OpenStreetMap) com os motoristas e o campus.
- Busca de motoristas por destino.
- Cadastro e remoção de ofertas de carona.
- Chat por carona, com contraproposta de valor.
- Confirmação da carona com resumo final.

Os dados ficam apenas em memória: ao fechar o app, o que foi adicionado se perde.

## Tecnologias

- Kotlin e Jetpack Compose (Material 3)
- Navigation Compose
- osmdroid (mapa OpenStreetMap)

## Como rodar

Requisitos: Android Studio e um emulador ou celular com Android 7.0 (API 24) ou superior. O mapa precisa de internet.

1. Clone o repositório: `git clone <URL-DO-REPOSITORIO>`.
2. Abra a pasta no Android Studio e aguarde a sincronização do Gradle.
3. Selecione um emulador ou celular e execute o módulo `app`.

## Documentação

A documentação do projeto, com prints das telas, está em docs/Documentacao_projeto_upcar.pdf
