node {
  jdk = tool name: 'JDK8'
  env.JAVA_HOME = "${jdk}"

  echo "jdk installation path is: ${jdk}"

  // next 2 are equivalents
  sh "${jdk}/bin/java -version"

  // note that simple quote strings are not evaluated by Groovy
  // substitution is done by shell script using environment
  sh '$JAVA_HOME/bin/java -version'

  def mvnHome

  stage('Preparation') {

    // for display purposes
    // Get some code from a GitHub repository
    git 'https://github.com/dwi67/crm-reference.git'

    // Get the Maven tool.
    // ** NOTE: This 'M3' Maven tool must be configured
    // **       in the global configuration.
    mvnHome = tool 'M3'
  }

  stage('Build') {
    // Run the maven build
    if (isUnix()) {
      sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean install"
    } else {
      bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean install/)
    }
  }

  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archive 'target/*.jar'
  }

  stage('Deploy') {
    dir("crm-os-starter") {
      sh "'${mvnHome}/bin/mvn' fabric8:deploy"
    }
  }
}