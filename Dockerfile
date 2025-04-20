# Use official Tomcat base image
FROM tomcat:9.0

# Remove default web apps (optional)
RUN rm -rf /usr/local/tomcat/webapps/*

# RUN apt-get install sssd-tools -y \
RUN  groupadd --gid 1001 calc \
    && useradd --uid 1001 --gid 1001 calc \
    && chown calc:calc /usr/local/tomcat/webapps/ \
    && chmod 755 /usr/local/tomcat/webapps/
USER calc
# Copy WAR file to Tomcat's webapps directory
COPY target/CalculatorWebApp.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
