docker-compose down

docker build -t backend-graphlib:latest ./

docker-compose up --build --force-recreate --remove-orphans