FROM tomcat:8.5.99-jdk17

COPY target/app.war /usr/local/tomcat/webapps/
COPY setenv.sh /usr/local/tomcat/bin/setenv.sh
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

EXPOSE 8080
CMD ["catalina.sh", "run"]