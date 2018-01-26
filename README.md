# MiniDAO

Librairie qui permet de lier un modèle avec une base de données de type MySQL

## Sommaire


## Annotations

MiniDAO s'appuie sur des annotations pour lier le modèle avec la base de données. 
Ces annotations sont susceptibles d'être modifiées ultérieurement afin de répondre 
au mieux aux besoins des applications l'utilisant.

### MDEntity

### MDField

### MDId

### MDOneToMany

### MDManyToMany

## Maven Local Repository

Afin de faciliter l'utilisation de la librairie, il est fortement conseillé de la pousser
sur le repository maven local. Pour se faire, il suffit de faire comme suit:

```sh
mvn clean package

cd target

mvn install:install-file -Dfile=minidao-1.0-SNAPSHOT.jar -DgroupId=com.thomaskint -DartifactId=minidao -Dversion=1.0-SNAPSHOT -Dpackaging=jar
```

Ensuite, il faudra l'inclure dans le projet maven avec les dépendances suivantes:

```xml
<dependencies>
    <!-- Other dependencies -->
    <dependency>
        <groupId>com.thomaskint</groupId>
        <artifactId>minidao</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.45</version>
    </dependency>
</dependencies>
```