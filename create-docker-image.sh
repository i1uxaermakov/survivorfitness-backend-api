#!/bin/zsh
# Color for output
blue=$(tput setaf 4)
bold=$(tput bold)
boldBlue=${bold}${blue}
reset=$(tput sgr0)

imageTag="v7-measurements"

echo "\n\n${boldBlue}STARTING THE SCRIPT"
echo "${boldBlue}Building a jar of the project"
mvn package -Dmaven.test.skip=true

echo -e "\n\n${boldBlue}Building a Docker image based on the local DockerFile and the jar"
docker image build --tag ilyaermakov/survivor-fitness-backend:${imageTag} .

echo -e "\n\n${boldBlue}Pushing the image to the repository${reset}"
docker push ilyaermakov/survivor-fitness-backend:${imageTag}


