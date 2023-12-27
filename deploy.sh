#!/bin/zsh
mvn clean package
rsync -avz ./target/scraper-frontend-0.0.1.jar aaron@job-scraper.homelab.io:~/scraper-frontend-0.0.1.jar
ssh aaron@job-scraper.homelab.io "sudo systemctl restart job-scraper.service"
