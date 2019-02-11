Minishift
---------
Installation:
minishift start --memory=8192 --cpus=4 --disk-size=50g
Start:
minishift start
Stop:
minishift stop
ssh docker image:
minishift ssh

Installation JBoss Fuse 7 images in minishift:
---------------------------------------------
oc login -u system:admin
BASEURL=https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-000081-redhat-4
oc create -n openshift -f ${BASEURL}/fis-image-streams.json
oc create -n openshift -f https://raw.githubusercontent.com/jboss-fuse/application-templates/application-templates-2.1.fuse-000099-redhat-5/fis-console-namespace-template.json

Postgresql port forward connect
-------------------------------
oc project MyProject
oc get pods
-- with postgres pod name
oc port-forward postgresql-pod-name 5432:5432

Erstellen volume in minishift
-----------------------------
minishift ssh -> Windows putty docker@192.168.64.7 Password: tcuser
sudo -i
mkdir /opt/certs
chmod 777 /opt/certs

Erstellen pv und pvc (certs)
----------------------------
oc login -u systemå:admin
oc project myproject
oc create -f certspv.yaml
oc create -f certspvc.yaml

Kopieren von profilen nach cert-volume
--------------------------------------
scp *.jks docker@192.168.64.7:/opt/certs -> Windows pscp
Password: tcuser

Wichtigste oc-Befehle
---------------------
Pods:
oc get pods
Volumes:
oc get pv
Volume claims:
oc get pvc
