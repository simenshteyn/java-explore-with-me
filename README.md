# Дипломный проект
Template repository for ExploreWithMe project.

**Deployment on local machine without tests:**
```
docker run --rm -d -p 5432:5432 -e POSTGRES_DB=billboard -e POSTGRES_PASSWORD=password postgres && mvn spring-boot:run
```