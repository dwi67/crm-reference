# CRM Demo for Fuse 7.2 on Karaf
This project shows how to develop an application using Red Hat Fuse on Karaf. Both OpenShift based and standard deployments are supported. This project shows how to build applications with database access using the integrated postgreSQL database of the OpenShift platform.

## Preparations for minishift deployments

## Installation of minishift
First install MiniShift on your computer for your platform

* Start minishift with the following parameters

    ```
    minishift profile set redhatfuse
    minishift config set memory 8GB
    minishift config set cpus 4
    minishift config set image-caching true
    minishift config set disk-size 50g
    minishift start
    ```

* You can stop and start minishift Installation:

    ```
    minishift stop
    ```
    
    ```
    minishift start
    ```

## Installation of the OpenShift Red Hat Fuse templates

It is required to install the current Red Hat Fuse templates first

* Login as system admin and create templates
        
    ```bash
    oc login -u system:admin
    BASEURL=https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-000099-redhat-5
    oc create -n openshift -f ${BASEURL}/fis-image-streams.json
    oc create -n openshift -f https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-000099-redhat-5/fis-console-namespace-template.json
    ```

## Instalation of the PostgreSQL database

The postgresql database is used by the crm demo application to persist the data for both the standalone and OpenShift version. To install the database the following steps are required:

* Start the postgresql database by applying the postgesql tempate provided by OpenShift
 
    ```bash
    oc new-app postgresql-persistent -p POSTGRESQL_USER=admin -p POSTGRESQL_PASSWORD=admin
    ```

* Open the postgresql service port to connect to from outside minishift

    - First get thename of the created postgresql pod by typing

        ```bash
        oc get pods
        ```
    
    - Now create a port forward by typing
    
        ```bash
        oc port-forward [name of postgesql pod] 5432:5432
        ```

* Connect to the postgresql using a postgresql client (PGAdmin)

    - Host: localhost, User: admin, Password: admin, database: sampledb
    
* Run the following database script to create the database

    - https://github.com/dwi67/crm-reference/blob/master/crm-os-datasource/schemas/schema.sql
    
## Installation of the Red Hat Fuse management console

* Optional with the following step the Red Hat Fuse console can be installed. 

    ```bash
    oc new-app fuse70-console
    ```

## Running the crm application on Karaf Standalone

* Build the application using maven (project root)

   ```bash
   mvn clean install
   ```

* Download and start Red Hat Fuse 7.2

* On the Fuse console install the features (be aware that postgesql must be running)

   ```bash
   features:addurl mvn:ch.adesso/crm-features/1.0.0-SNAPSHOT/xml/features 
   features:install crm-standalone
   ```

## Running the crm application on OpenShift manually

* Be aware that the fuse templates for openshift must be installed

* Build the application using maven (project root)

   ```bash
   mvn clean install
   ```

* Deploy the application (project crm-os-starter)

   ```bash
   cd crm-os-starter
   mvn clean fabric8:deploy
   ```

* To undeploy the application:

   ```bash
   mvn clean fabric8:undeploy
   ```
   
## Automate the deployment using the Jenkins pipelines

You can add Jenkins to openshift to build and deploy the project with jenkins

* To add jenkins use the following command

   ```bash
   oc new-app -e JENKINS_PASSWORD=admin openshift/jenkins-2-centos7
   ```

* Create a route to access jenkins

   You can login into jenkins with admin/admin 

To support the fabric8 deploy add the admin role to myproject

   ```bash
   oc adm policy add-role-to-user admin system:serviceaccount:myproject:default
   ```
    
## Experimental

#### Create a volume

   ```bash
   minishift ssh -> Windows putty docker@192.168.64.7 Password: tcuser
   sudo -i
   mkdir /opt/certs
   chmod 777 /opt/certs
   ```

#### Create pv und pvc (certs)

   ```bash
   oc login -u systemå:admin
   oc project myproject
   oc create -f certspv.yaml
   oc create -f certspvc.yaml
   ```

#### Copy profiles to the cert-volume

   ```bash
   scp *.jks docker@192.168.64.7:/opt/certs -> Windows pscp
   Password: tcuser
   ```

#### Handling of templates

   ```bash
   oc get templates
   oc process --parameters -n openshift fuse72-console
   ```

