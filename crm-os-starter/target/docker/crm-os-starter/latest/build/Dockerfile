FROM jboss-fuse70-karaf-openshift:1.0
EXPOSE 8181 8778
COPY maven /tmp/3b91c4b7-ad62-4f5a-aca4-8989e8dd2b51/deployments//
USER root
RUN chown -R jboss:jboss /tmp/3b91c4b7-ad62-4f5a-aca4-8989e8dd2b51 && cp -rp /tmp/3b91c4b7-ad62-4f5a-aca4-8989e8dd2b51/* / && rm -rf /tmp/3b91c4b7-ad62-4f5a-aca4-8989e8dd2b51
USER jboss
CMD /deployments/deploy-and-run.sh
