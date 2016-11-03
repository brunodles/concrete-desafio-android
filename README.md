# GitHub Popular
Aplicativo android que consulta api do Github como solicitado no [desafio android](https://bitbucket.org/suporte_concrete/desafio-android).

Esse aplicativo tem várias libs e classes que irão se tornar bibliotécas
por isso movi algumas delas para o modulo `libraries`.

O projeto foi dividido em 3 módulos
* api
* app
* libraries

**Api** contém a camada de comunicação com o github. Também é aqui que os DTOs estão.

**Libraries** é um conjunto de classes utilitárias, a ideia é publicar essas classes em projetos
separados.

**App** é o aplicativo android, onde tudo acontece.

Apesar de tudo, não separei o projeto como gostaria, por conta do tempo, mas gostei muito de usar
*data binding*, já que nunca havia utilizado antes.

Esse projeto me deu ideias para utilizar o *Aspect Oriented Programing* e *Annotation Processor*
 para criar as classes do banco e as injetar no `SqlOpenHelper`.

Também não consegui completar a tempo o aplicativo em modo `mock`, por isso é possível ver um
buildType `mocked` dentro de `app/build.gradle`.

## App
O app foi dividído em vários buildTypes, cada um com o seu propósito.
* release - padrão do android se mantém como buildType de produção do app.
* debug - esse buildType irá acessar um servidor no *computador host*, a url será resolvida em tempo de compilação.
* staging - irá usar a api de produção, mas será um app debugável.
* mocked - não acessa nenhuma api, os dados retornados será criados dentro do aplicativo.

Utilizei várias libs que já tenho costume como `Retrofit`, `OkHttp`, `support-compat`, `RxJava`,
`RxAndroid` e `Picasso`.
Ainda assim adicionei algumas que estava interessado como `DataBind`, `Parceler`, `Stetho`,
`SqlBrite` e `RxLifecycle`.

Posso dizer que gostei muito do `DataBind` por possibilitar a customização de views sem ser
necessário criar uma view customizada. Isso também me fez parar de usar o `ButterKnife` e agora não
é mais necessário criar ViewHolders customizados.

## Wiremock
Dentro do projeto existe uma pasta `wiremock`, lá estão alguns scripts para usar o wiremock em modo
standalone. Dessa forma ele irá subir um servidor proxy e irá guardar as respostas na máquina,
da próxima vez que uma requisição chegar com os mesmos parâmetros a resposta salva será utilizada.

