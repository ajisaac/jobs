#!/bin/zsh
mvn clean package
rsync -avz -e "ssh -i ~/.ssh/JobScraperKey.pem" ./target/jobs-0.0.1.jar ubuntu@server-host:~/jobs-0.0.1.jar
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo mv ~/jobs-0.0.1.jar /home/aaron/jobs-0.0.1.jar"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo chown aaron:aaron /home/aaron/jobs-0.0.1.jar"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl restart jobs.service"
