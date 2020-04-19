# Tutorial: Creating a REST api with Kotlin + SpringBoot + JPA + Flyway

## Setup

You need to have a mysql database running with a database named: 'kotlin_crud_db'
Let's run this command and you will have it:
```shell script
$ docker run --rm --name test-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=kotlin_crud_db -d mysql:latest
```

## Running the app

```shell script
$ gradle bootRun 
```

## Manual testing

CRUD tests:
Now you can test your changes on your API and go to the **Graph_i_QL** http://localhost:8080/graphiql

Create:

```graphql
mutation {
  createTask(
    title: "He is cool Update"
    description: "asdas"
    status: 1
    priority: 2
  ) {
    id
    title
    description
    status
  }
}
```

Read:
All tasks

```graphql
query {
  getTasks {
    id
    title
    description
    status
    priority
  }
}
```

Or task by ID

```graphql
query {
  getTaskById(taskId: 1) {
    id
    title
    description
    status
    priority
  }
}
```

Update:

```graphql
mutation {
  updateTask(
    taskId: 4
    title: "He is cool Update"
    description: "asdas"
    status: 1
    priority: 2
  ) {
    id
    title
    description
    status
  }
}
```

Delete:

```graphql
mutation {
  deleteTask(taskId: 4)
}
```