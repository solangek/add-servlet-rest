# add servlet
A React frontend that can add two numbers, using a Java servlet as backend REST endpoint.

## In order to run this example:
* run the server side; with IntelliJ configuration at the upper right (or open the terminal and run `/<path to your tomcat>/bin/catalina.sh run`)
* run the client side: open the terminal: `cd add-servlet-client`, `npm install`,  run with the command `npm start`

Then browse:
* your react client at http://localhost:3000

Your server REST endpoint will be available for example at http://localhost:8080/add?left=1&right=3.
Note that you should never specify the host and port in your React code! (use '/add' instead of 'http://localhost:8080/add')

## The code depends on:
* your local installation of tomcat, this template uses
  tomcat 9.0.45 that can be downloaded from https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.45/bin/apache-tomcat-9.0.45.tar.gz.
  In order to point to your own installation of tomcat, edit configuration in IntelliJ change the application server.
* your local installation of nodejs, this template is based on nodejs v18.15.0 (npm 9.5.0). You can download it from https://nodejs.org/en/download.
* your local installation of java (check File->Project Structure->Platform SDK). You can add SDK from IntelliJ: File->Project Structure->Platform Settings-> +).
  This template is based on version 19, you can download it from https://jdk.java.net/19/).
  
