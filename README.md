# Project Dragon recruitment exercise 
## Requirements
This exercise is to create a feature flag service using Spring in Java:
- As an admin user, I want to be able to create a feature which defaults to disabled
- As an admin user, I want to be able to switch on feature for a user
- As a user, I want to be able to get all the enabled features (a mix of all the globally enabled ones and the ones enabled just for my user)

## Assumptions
- All requests and response are in JSON format
- To keep the exercise simple, all data including both users and feature flags is stored in memory instead of a real database instance.

## How to run
The application is purely built with Java Spring and gradle. 

To run, please execute
>./gradlew bootRun

under the project root directory. After the application starts, requests can be sent to http://localhost:8080/feature. For details, please refer to [APIs](#APIs)

To test, please execute
>./gradlew test


## APIs
To be more secure, the API would only return the enabled features to user instead of the full list. 

| Endpoint       | Access      | Descirption                                                                                                                                                                                                               | Sample                                                           |
|----------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| GET  /feature  | ADMIN, USER | Allow authenticated user to obtain the enabled features for his own.                                                                                                                                                      |                                                                  |
| POST  /feature | ADMIN       | Enable admin to create new feature which defaults to disabled.  If the feature has already existed, error 409 will be returned.  Otherwise, http status 201 is returned.                                                  | {   "name": "feature 1" }                                        |
| PUT  /feature  | ADMIN       | Enable admin to enable/disable an existing feature. if `user` is not specified, the feature will be changed globally.  If the feature doesn't exist, error 404 will be returned.  Otherwise, http status 200 is returned. | {   "name": "feature 1",   "enabled": true,   "user": "user A" } |


## Security
Only authorized users are allowed to access the endpoint, by default, two users are created:

| username | password | role  |
|----------|----------|-------|
| admin    | iamadmin | ADMIN |
| user     | iamuser  | USER  |

For simplicity, the endpoints are protected by Http Basic Authentication and the user details are stored in memory. In production, more secured authentication such as OAuth should be used.


## Test coverage
The unit test mainly covers the following:
- verify the security setting
- verify the business logic in FeatureService
- verify if the repository can return correctly


## Further Improvement
- To lower the code complexity as well as increasing the maintainability , the authentication could be moved from the application side to infrastructure level by using API gateway or service mesh.
- Some key features are missing such as allowing admin to delete a feature, partitioning the features by introducing feature groups 