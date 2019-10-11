# Introduction

The pet service application was developed to, as its name implies, provide the basic operator to create/update/delete/search pets.

# Overview

* This service is implemented using Java and DropWizard framework. 
* H2 will be used as embeded database to manage the pet operations. 
* JPA annotations and Hibernate also be used to handle database operation.


# Running The Application

To test the application run the following commands. (This project has included jar file, so it is ready to run with out package the jar file)

* To setup the h2 database run (creating required table).

        java -jar pet-service-1.0.jar db migrate local.yml

* To run the server run.

        java -jar pet-service-1.0.jar server local.yml

* To hit the Health check service

	    http://localhost:8081/healthcheck

* To hit the Pet service application.

	    http://localhost:8080/pets
	

### Create Pets
- Endpoint: `http://localhost:8080/pets`
- Http Method: `POST`

**Example Request Json**

        
	     {
                "type": "BIRD",
                "age": 2,
                "sex": "M",
                "description": "My lovely bird",
                "owner_email": "bird@gmail.com",
                "image_url": "http://www.bird.com/bird.jpg"
            },
            {
                "type": "CAT",
                "age": 1,
                "sex": "F",
                "description": "My lovely cat",
                "owner_email": "cat@gmail.com",
                "image_url": "http://www.cat.com/cat.jpb"
            }
	

**Example Request Json**

	     {
                "id": 2,
                "type": "BIRD",
                "age": 2,
                "sex": "M",
                "description": "My lovely bird",
                "createdDate": 1570665241173,
                "updatedDate": null,
                "owner_email": "bird@gmail.com",
                "image_url": "http://www.bird.com/bird.jpg"
            },
            {
                "id": 3,
                "type": "CAT",
                "age": 1,
                "sex": "F",
                "description": "My lovely cat",
                "createdDate": 1570665241175,
                "updatedDate": null,
                "owner_email": "cat@gmail.com",
                "image_url": "http://www.cat.com/cat.jpb"
            }
	
### Update Pets
- Endpoint: `http://localhost:8080/pets`
- Http Method: `PUT`

**Example Request Json**

        [
             {
                    "id": 2,
                    "description": "My bird can fly",
                    "image_url": "http://www.bird.com/birdfly.jpg"
                },
                {
                    "id": 3,
                    "owner_email": "cat123@gmail.com",
                    "image_url": "http://www.cat.com/cat.jpb"
                }
        ]

**Example Response Json**

        [
            {
                "id": 2,
                "type": "BIRD",
                "age": 2,
                "sex": "M",
                "description": "My bird can fly",
                "createdDate": 1570665241173,
                "updatedDate": 1570665689754,
                "owner_email": "bird@gmail.com",
                "image_url": "http://www.bird.com/birdfly.jpg"
            },
            {
                "id": 3,
                "type": "CAT",
                "age": 1,
                "sex": "F",
                "description": "My lovely cat",
                "createdDate": 1570665241175,
                "updatedDate": 1570665689758,
                "owner_email": "cat123@gmail.com",
                "image_url": "http://www.cat.com/cat.jpb"
            }
        ]

### Delete Pets
- Endpoint: `http://localhost:8080/pets`
- Http Method: `DELETE`

**Example Request Json**

     {
        "ids" : [1,2]
     }


### Search Pets
- Endpoint: `http://localhost:8080/pets`
- Http Method: `GET`
- Valid parameter for Search operation
    1. id
    2. type 
    3. age
    4. sex
    5. owner_email
  
 
**Example Request**
- Endpoint: `http://localhost:8080/pets?type=bird`

**Example Response**

            [
                {
                    "id": 2,
                    "type": "BIRD",
                    "age": 2,
                    "sex": "M",
                    "description": "My bird can fly",
                    "createdDate": 1570665241173,
                    "updatedDate": 1570665689754,
                    "owner_email": "bird@gmail.com",
                    "image_url": "http://www.bird.com/birdfly.jpg"
                }
            ]