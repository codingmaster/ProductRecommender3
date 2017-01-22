call mvn install:install-file -Dfile="./lib/virt_jena.jar" -DgroupId=virtuoso.jena -DartifactId=virtuoso-jena -Dversion=1.10 -Dpackaging=jar
call mvn install:install-file -Dfile="./lib/virtjdbc3.jar" -DgroupId=virtuoso.jdbc3 -DartifactId=virtuoso-jdbc3 -Dversion=3.62 -Dpackaging=jar
call mvn clean compile assembly:single
cd target
copy ProductRecommender-1.0-SNAPSHOT-jar-with-dependencies.jar ..
cd ..
ren ProductRecommender-1.0-SNAPSHOT-jar-with-dependencies.jar recommender.jar
call java -jar recommender.jar --conf docs/properties
