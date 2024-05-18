### Connect H2 with file system
    jdbc:h2:file:/home/ashraful/Documents/Microservices_EzyBytes/h2_db_account

    so that, it will save data to my local file system

### Dto Pattern 
    https://medium.com/@enocklubowa/why-you-need-to-use-dtos-in-your-rest-api-d9d6d7be5450

### Writing JavaDoc shortcut in Service Layer
    /** + enter


### Dto Validation
    
    1. @NotNull: This annotation validates that the annotated property value is not null. 
                 It's typically applied to object references.

    2. @NotEmpty: This annotation validates that the annotated property is not null,
                  and its size or length is greater than zero. 
                  It's typically applied to collections, arrays, or strings.

                    Ex : String name = "             "
                    it will pass NotEmpty Check

    3. @NotBlank: This annotation validates that the annotated property is not null,
                  and contains at least one non-whitespace character. 
                  It's typically applied to string properties.

                    Ex : String name = "        "
                    it won't pass NotBlank check

                    But, String name = "     a   "
                    it will pass NotBlank check

### Audit

    @CreatedDate, @LastModifiedDate, @CreatedBy, @LastMOdifiedBy, in the BaseEntity
    these don't track by JPA automatically, for this, we need to use auditing in the audit package

    Also, need to enable auditing in the main class of spring boot app using : @EnableJpaAuditing annotation
    

### Build Jar

    mvn clean install

### Run Jar

    java -jar target/account-0.0.1-SNAPSHOT.jar

### Run DockerFile for making docker image
    
    docker build . -t ashrafulsohag/accounts:s4

    here ashrafulsohag is my docker hub repository name and accounts is the image name and s4 is the tag name

### Run Docker Container from image

    sudo docker run -d -p 8080:8080 ashrafulsohag/accounts:s4


### Push Docker images to Docker Hub

    1. Login to Docker : docker login
    2. Create Tag for making docker hub repository for image : 
        docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
        ex : docker tag ashrafulsohag/loans:s4 ashrafulsohag/loans:s4

    3. Push Docker images to Docker Hub
        docker push <IMAGE_NAME>
        ex : docker push ashrafulsohag/loans:s4


### Pull Docker image from Docker Hub
    
    Goto Tags section, then there is a command available to pull Docker image
    ex : docker pull ashrafulsohag/accounts:s4

### Run Container

    sudo docker-compose up -d


### We can read properties using the following ways:
    1. @Value
    2. Environment
    3. @ConfigurationProperties

    but reading properties from multiple server properties by these ways is not a good idea,
    so we have spring profile concepts here

