README

Recommender Installation:
1. Download, install and start OpenLink Virtuoso Server v. 7.10 http://virtuoso.openlinksw.com/dataspace/doc/dav/wiki/Main/VOSUbuntuNotes
1.1 Configure Virtuoso Server in docs\properties\main.properties
default properties:
VIRTUOSO_HOST=localhost
VIRTUOSO_PORT=1111
VIRTUOSO_CHARSET=UTF-8
VIRTUOSO_USER=dba
VIRTUOSO_PASSWORD=dba
VIRTUOSO_URI=http://localhost:8890

2. Download and install Database Server f.e MySQL http://dev.mysql.com/downloads/mysql/
2.1 Set Database properties in src/main/resources/hibernate.cfg
default properties: 
<property name="hibernate.bytecode.use_reflection_optimizer">false</property>
<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.dialect">org.hibernate.dialect.MySQLInnoDBDialect</property>
<property name="show_sql">false</property>

3. Download and install Maven http://maven.apache.org/download.cgi
4. Configure the required data structure. A sample data is provided in data/db_backup.sql
5. Run install Shell script. 
It will copy required libraries from the lib directory to the local maven repository.
After that it will create a single jar file for ProductRecommender 
(already configured jar file is available in target/ProductRecommender-1.0-SNAPSHOT-jar-with-dependencies.jar)
6. The recommendation process consists of three main steps:
6.1. java -jar <ProductRecommender>.jar --conf docs/properties 
sets the property path
6.2  java -jar <ProductRecommender>.jar --dbinit
starts the ETL step  (this step can be skiped if the data from the dump is used)
6.3 java -jar <ProductRecommender>.jar --populate
fills the Virtuoso server with the corresponding attribute and entity relatedness values
6.4 java -jar <ProductRecommender>.jar --recommend <PRODUCT_ID>
creates recommendations for <PRODUCT_ID>
available product ids in the dataset: 
49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 63, 65, 66, 67, 68, 69, 70, 72, 73, 74, 77, 91, 92, 93, 
100, 101, 102, 103, 104, 125, 126, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 
144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 157, 158, 159, 160, 163, 164, 165, 166, 171, 178, 
179, 180, 181, 182, 183, 184, 195, 222, 224, 225, 230, 231, 234, 237, 238, 244, 259, 264, 267, 268, 
271, 283, 292, 293

The output is generated into docs/out_<CUSTOMER>/recommendation_<PRODUCT_ID>.html and is saved to the database Recommendation table.

	