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


### Reading Properties from Config Server 
    
    1. Firstly add config client dependencies

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>

    and
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>${spring-cloud.version}</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
	    </dependencyManagement>

    2. Then, add properties in the application.yml for config server to read configurations

        spring:
          config:
            import: "optional:configserver:http://localhost:8071/"

        here, optional means, if we can't read config from config server then it can also able to start account microservice,
        here configserver is not the configserver application name, it is by default for spring
        if config server is necessary to start account microservice, then we shouldn't add optional here,
        In our case configserver application should be running while account microservice is reading configuration from configserver

### Change record to class
    We will change config and read using the AccountsContactInfoDto object, 
    but as it is record class and immutable, so need to change it from record


### Refresh Actuator Path

    management:
      endpoints:
        web:
          exposure:
            include: "*"
    
    in the application.yml, I have set spring actuator management config, where we have /actuator/refresh endpoint.
    this is a post endpoint, so when we hit it we can see if any configuration from github repo for this account microservice is changed
    or not.

### Section-7 :: Start
    
    1. Firstly remove bus amqp related dependency and rabbitmq related config from this branch, 
        because I am not going to track config changes using the rabbitmq bus

    2. Run the MySql docker container for account microservice , this microservice will use accountsdb database
        
        sudo docker run -p 3307:3306 --name <CONTAINER_NAME> -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=<DB_NAME> -d <MYSQL_IMAGE>

        ex : sudo docker run -p 3307:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql
        here, d is detached mode for running container

    3. Download sqlectron app from https://github.com/sqlectron/sqlectron-gui/releases/tag/v1.38.0
        download the sqlectron-1.38.0.tar.gz version
    
    4. Extract the folder and goto the folder,
    5. then run ./sqlectron command to run the GUI
    6. click add option and set configuration, username and password is root and port is 3307:3306 <PC_PORT:CONTAINER_PORT>

### Section-7 :: MySql docker compose container

    1. Firstly create new docker images for all services with tag s7
        like: configserver:s7, accounts:s7, loans:s7, cards:s7

    2. Then, change docker compose with mysql container related changes

    3. Change application datasource properties as per docker compose
    4. Make package before creating new docker image
    5. so run mysql docker container for accountsdb locally first, before creating package. otherwise it
        will throw exception
    6. then create docker image with tag s7


### Section-8 :: Service Discovery and Service Registration
    
    1. Add eureka client maven dependency to connect with eureka server

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    2. Add service registration related dependency in application.yml
    3. Settnig some information properties using info tag in application.yml to show in eureka dashboard
    4. enable shutdown properties in actuator section, to enable deregistering from eureka server when account microservice is disabled or stopped
    5. Run the application, and before running make sure eureka server, configserver and docker container for mysql db is running

### Shutdown and DeRegistering from eureka server

    we have enabled spring actuator shutdown and also setting endpoints shutdown enabled in the application.yml .
    this, will enable shutting down out application without closing it from intelliJ.
    So, spring actuator provide us a POST api which is : http://localhost:8080/actuator/shutdown
    when, we hit it our application will be shutting down and also deregistering from eureka server.
    

### Section :: 9 :: Get custom header from GatewayService to keep track of co-relation id
    
    we need to change in the CustomerController , @GetMapping("/fetchCustomerDetails") api to accept request header of co-relation id from 
    the gateway service and pass it to loans and cards microservices


### Section - 10 : Circuit Breaker Pattern | Retry Pattern | Rate Limiter Pattern

    1. Implementing circuit breaker pattern in the Feign Client in account microservice for getting information from loans and
        cards microservices. And also setting fallback mechanism in the Feign Client for loans and cards microservices.
    2. We are setting Fallback mechanism in the Feign Client, because when accounts microservices try to get information from
        loans and cards microservices, if loans or cards microservices is not available, then the loans or cards microservices
        response will be delayed and this will effect to the accoutns microservices too. which is known as ripple effect

    3. Ripple effect : a situation in which one event produces effects which spread and produce further effects. Means it is a chain effect

    4. After setting circuit breaker configs, dependency and fallback mechanism. Start all the services.
        accounts, loans and cards microservices

    5. Firstly create all information using gateway service by hitting the post request for creating account, loans and cards informations
    6. Then try to fetch details from gateway service of accounts, loan and cards microservices
 
    7. As the services are up and running, so response will be returned
    8. Now, stop loans microservices
    9. Try to fetch details from using gateway service, now we will see the loans information is null
    10. But the overall result is not affected, as the fallback mechanism is working
    11. Hit the fetch request again and again and look into the following urls to see circuit breaker state transition and events

        http://localhost:8080/actuator/circuitbreakers
        http://localhost:8080/actuator/circuitbreakerevents
    

    *** FallBack Method purpose ***

    Fallback Method: If the primary method encounters an error, takes too long to respond, or is unavailable for any reason, 
    the system can invoke a fallback method as an alternative path to handle the request.


    *** Retry mechanism in accounts microservices ***
    1. add properties in application.yml
    2. make an api which will return the application build-info
    3. add retry in the api, and intentionally throw RuntimeException from the api
    4. also put a fallback method
    5. so in the application.yml we have set retry mechanism,
    and also skip retrying while getting nullPointer exception

    6. run the gateway server and account microservice
    7. hit the api : http://localhost:8072/eazybank/accounts/api/build-info
    8. see the logs of account microservice
    9. we can see, the api retry count is 3 as described in application.yml
    10. and after that the fallback method is triggered


# Section - 10.6 :: Redis RateLimiter in Account Microservice

    add the following config in the application.yml

    resilience4j.ratelimiter:
      configs:
        default:
          timeout-duration: 1000
          limit-refresh-period: 5000
          limit-for-period: 1

    => Explanations ::
    1. timeout-duration: 1000:
        Definition: This is the maximum time (in milliseconds) that a request will wait to acquire permission before it times out.
        Behavior: If a request hits the rate limit and tokens are not available, it will wait for up to 1000 milliseconds (1 second) before giving up and triggering a fallback (or failure). If the tokens are replenished within that time, the request is processed; otherwise, it fails or times out.
        Example: If a request can't get a token immediately, it will wait for up to 1 second before timing out.
    
    2. limit-refresh-period: 5000:
        Definition: This is the interval (in milliseconds) after which the tokens in the bucket are refreshed.
        Behavior: In this case, the tokens are refreshed every 5000 milliseconds (or 5 seconds). After each limit-refresh-period, the rate limiter replenishes the token bucket based on the limit-for-period.
        Example: Every 5 seconds, the rate limiter will replenish the tokens, allowing requests to pass through again.

    3. limit-for-period: 1:
        Definition: This defines the maximum number of permits (tokens) that can be consumed during each limit-refresh-period.
        Behavior: In this configuration, only 1 request is allowed during each 5-second window (as specified by limit-refresh-period).
        Example: In a 5-second period, only 1 request is allowed to pass through. Once that single request is made, any other requests during that period will have to wait for the next refresh period or time out (if they exceed the timeout-duration).
        

    => add RateLimiter to a controller api which is /contact-info
    => Run account microservice and hit the API again and again, then we will see the rate-limiter is working

    *** Implement FallBack method when API is rate-limited ***

        Fallback :: (পিছু হটা (turn back)) is a term used in software development that refers to the backup plan or alternative solution,
        that is implemented when the primary plan fails or is not possible.

        
        In software systems, particularly in resilient systems like those using Resilience4j, Hystrix, or similar frameworks, a fallback is a method or mechanism that gets executed when the primary operation fails. This failure can occur due to reasons such as:

         1. An exception is thrown
         2. A service being unavailable
         3. A timeout occurs
         4. The system is rate-limited

        Purpose of a Fallback Method
        The fallback method serves as a backup plan to ensure your system can still respond gracefully when a failure occurs. Rather than the system failing entirely and returning an error to the user, the fallback provides a default behavior, possibly returning cached data, default values, or directing the user to an alternative service.
        
        The key purposes of a fallback method are:

         1. Graceful Degradation: Ensures that when a service fails, the system can still provide a partial or degraded service instead of complete failure.
         2. Resilience: Increases the overall resilience of the system by handling failures in a controlled manner.
         3. User Experience: Improves user experience by preventing sudden crashes or displaying error messages. Users might receive cached or default data instead of seeing an error.


        => Real-Life Example: Online Food Delivery Service
        Imagine an online food delivery service where customers can order food through a mobile app. The app communicates with different services like:
        
         1. Menu Service: Provides the available menu items.
         2. Delivery Service: Calculates delivery time and assigns delivery agents.
         3. Payment Service: Processes payment for orders.

        => Scenario without Fallback:
        A customer places an order, but the Payment Service is down due to network issues or server maintenance.
        The customer sees an error message like "Payment Failed," and the order process terminates abruptly.
        The user has to retry or wait until the payment service is restored.

        => Scenario with Fallback:
        If the Payment Service is unavailable, the system triggers a fallback method.
        The fallback could be a method that offers the user Cash on Delivery (COD) as an alternative payment option or retries the transaction after a short delay.
        This way, the system gracefully handles the failure of the primary payment method, and the user can still complete the order without disruption.


    *** Aspect default order of resiliency4J patterns ***
        
        1. Bulkhead
        2. TimeLimiter
        3. RateLimiter
        4. CircuitBreaker
        5. Retry
    
        we can change it by our implementation,
        for better understanding read the spring docs