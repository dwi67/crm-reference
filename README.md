<h1>CRM Demo for Fuse 7.2 on Karaf</h1>
This project shows how to develop an application using Red Hat Fuse on Karaf. Both OpenShift based and standard deployments are supported. This project shows how to build applications with database access using the integrated postgreSQL database of the OpenShift platform.

<h1>Preparations for minishift deployments</h1>

<h2>Installation of minishift</h2>
First install MiniShift on your computer for your platform
- Start minishift with the following parameters

    <code>minishift profile set redhatfuse<br />
    minishift config set memory 8GB<br />
    minishift config set cpus 4<br />
    minishift config set image-caching true<br />
    minishift config set disk-size 50g<br />
    minishift start</code>

- You can stop and start minishift Installation:

    <code>minishift stop</code>
    
    <code>minishift start</code>

<h2>Installation of the OpenShift Red Hat Fuse templates</h2>

It is required to install the current Red Hat Fuse templates first

- Login as system admin and create templates
    
    <code>oc login -u system:admin<br />
    BASEURL=https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-720018-redhat-00001<br />
    oc create -n openshift -f ${BASEURL}/fis-image-streams.json<br />
    oc create -n openshift -f https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-720018-redhat-00001/fis-console-namespace-template.json<br />
    oc create -n openshift -f ${BASEURL}/fuse-apicurito.yml<br />

<h2>Instalation of the PostgreSQL database</h2>

The postgresql database is used by the crm demo application to persist the data for both the standalone and OpenShift version. To install the database the following steps are required:

- Start the postgresql database by applying the postgesql tempate provided by OpenShift
 
    <code>oc new-app postgresql-persistent -p POSTGRESQL_USER=admin -p POSTGRESQL_PASSWORD=admin</code>

- Open the postgresql service port to connect to from outside minishift

    - First get the name of the created postgresql pod by typing

        <code>oc get pods</code>
    
    - Now create a port forward by typing
    
        <code>oc port-forward <b>(name of postgesql pod)</b> 5432:5432</code>

- Connect to the postgresql using a postgresql client (PGAdmin)

    - Host: localhost, User: admin, Password: admin, database: sampledb
    
- Run the following database script to create the database

    - https://github.com/dwi67/crm-reference/blob/master/crm-os-datasource/schemas/schema.sql
    
<h2>Installation of the Red Hat Fuse management console</h2>

- Optional with the following step the Red Hat Fuse console can be installed. 

    <code>oc new-app fuse72-console</code>

<h2>Running the crm application on Karaf Standalone</h2>

- Todo 

<h2>Running the crm application on OpenShift</h2>

- Todo 

<h2>Experimental</h2>

- Create a volume

    <code>minishift ssh -> Windows putty docker@192.168.64.7 Password: tcuser<br />
    sudo -i<br/>
    mkdir /opt/certs<br/>
    chmod 777 /opt/certs</code>

- Create pv und pvc (certs)

    <code>oc login -u systemå:admin<br/>
    oc project myproject<br/>
    oc create -f certspv.yaml<br/>
    oc create -f certspvc.yaml</code>

- Copy profiles to the cert-volume

    <code>scp *.jks docker@192.168.64.7:/opt/certs -> Windows pscp<br/>
    Password: tcuser</code>

- Handling of templates

    <code>oc get templates<br/>
    oc process --parameters -n openshift fuse72-console</code>