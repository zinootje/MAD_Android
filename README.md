# Mobile Application Development: Android (end assignment)

App is based on own api that scrapes data from https://www.hogent.be/student/catering/.
Api is currently hosted on heroku.

## Features

- [x] Find all HoGent resto's
- [x] favorite restaurants
- [x] find menu or all HoGent resto's
- [x] works offline

## App Architecture

The app is split up in the following modules:

| Module       | Description                                                         |
|--------------|---------------------------------------------------------------------|
| app          | contains the main code for the app and the UI                       |
| data         | contains the repository and the database                            |
| core         | contains the domain objects(models) and utilities                   |
| network      | contains the api interface and the retrofit based implementation    |
| testingUtils | contains the testing utilities and shared test code between modules |

documentation can be found in the docs folder in the root of the project.

### Tests

the tests are placed in the corresponding module.

| Module       | Tests                                                                                  |
|--------------|----------------------------------------------------------------------------------------|
| app          | ui tests, viewmodel tests, navigation tests, state restoration tests,integration tests |
| data         | repository tests, dao tests                                                            |
| core         | /                                                                                      |
| network      | /                                                                                      |
| testingUtils | shared code used for testing                                                           |

There is an integration test that tests the happy path of the app.

in testingsUtils there are test and fake implementations of the api and the database, and repositories.
Fake implementations are simple implementations that return predefined data.
Test implementations are implementations that are more advanced and can be used to test the app in a more realistic way.
They also have test only functions for example to simulate network errors.





