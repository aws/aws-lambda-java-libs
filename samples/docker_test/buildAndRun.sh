docker build . -t ric/java
docker run -it -p 9000:8080 ric/java