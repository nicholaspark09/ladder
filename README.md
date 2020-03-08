# Ladder
This library is intended to make using AWS Lambda and APIGateway much 
easier just by declaring this dependency in your build.gradle file.

## Uses
`AppExample` shows how you should initialize the library (Ladder.initialize)
but it doesn't include any of the controllers you need to include.

If you want to handle requests coming in to a url with path `/apples`
(i.e. https://awsurl.com/apples), then you should create a controller
`ApplesController` with the annotation `@ApiMethod("/apples")`
 - You should then extend `ApplesController` from `BaseController`


## Architecture
This library uses MVC as its main architectural pattern and kotlin as the 
main language. `Kotlin` is used for its concise syntax.

## Further plans
This library is still an on-going process and done completely free and in 
my part time. 