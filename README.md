# Marvel API Technical Test

This is the solution provided to the technical test outlined in the provided pdf from yourselves.


# Variables

There are two different sets of environment variables that need configuring for the full solution to work. This section will go over both of them and how to set them up.

## Marvel API

You will need to sign up for the marvel developer API (http://developer.marvel.com/) and gain the keys that they provide. There are two keys provided, the public key and private key. Both will need to be supplied through the application.properties before the solution will be able to connect to the API. They are provided as such:

    marvel.api.url=https://gateway.marvel.com:443/v1/public/characters 
    marvel.api.publickey=123  
    marvel.api.privatekey=123
 with the url already provided. If you wish to access the API at another url, you can change that property.
 
## Translator API

The API of choice for translating the description is the service provided by google. This is a solution that is hosted on Google Cloud and has a free option with up to 500,000 characters per month. The steps to set up this service can be accessed at [https://cloud.google.com/translate/docs/setup](https://cloud.google.com/translate/docs/setup). An API key will then need to be set up and added into application.properties to enable usage of the translation service. The information neeeded to enable the functionality is the API key. This is provided to the application.properties in the format:

    translator.api.endpoint=https://api.cognitive.microsofttranslator.com
    translator.api.key=123

# Getting it running
As it is a spring boot application with maven, this means it is quite flexible in the way of running it. I have been doing it from inside of IntelliJ during development so I will go through that but I will also touch on how to do it from command-line.
## IDE
The project can be imported to an IDE, in my case, this is IntelliJ but I believe it should be able to be imported into any and re-configured to work within that. Once the project is imported into the IDE, it can then be ran from `MarvelTechTestApplication` and this will start the spring boot web server on localhost:8080.

## Command-line
The project can be ran through command-line by making use of maven. In the command window, navigate to the uncompressed folder containing all of the code but ensure it is the root, you can do this by checking that the pom.xml is contained here (DIR command on windows to list the files in the current directory). Once here, you can run the commandd `mvn spring-boot:run` and this should allow maven to trigger the server to run.

# Improvements
## Code Structure
The code could probably be better re-factored to be more modular and allow the re-use of methods. This could be done to allow all of the marvel API services to be more consistent with eachother and allow the re-use of code inside of them where appropriate.

## Documentation
JavaDocs need to be added on the public methods and classes to demonstrate what they do. This would aide in expanding out the project without having to gain full knowledge of what each method does. This isn't currently in place due to the scope of the project and only having 4 classes which actually contain any logic.

## Testing
An end-to-end test isn't in place due to time constraints. This would aide in performance measuring along with ensuring that the code as a whole works and not just the units that are currently tested. This would require mocking the services that are being called and returning realistic responses, all via the controller.

## Swagger
The swagger documentation is in place but it could be expanded upon to try and build out better definitions to whoever is accessing it.

## Error Handling
There is no error handling in place, the error essentially propogates up to the user without being helpful. The project could make use of the error handling provided by spring through the annotation `@ControllerAdvice` and allow the project to provide good error handling when any of the API's give an error back along with any internal errors that are caught.