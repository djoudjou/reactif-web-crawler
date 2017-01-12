node {
echo "Running build number ${env.BUILD_ID}"
  
stage "Build"
  echo "Running stage Build test manu 2"
  //sh 'mvn clean compile'
  
stage "Test"
  echo "Running stage Test"
  //sh 'mvn test'
stage "Deploy"
  echo "Running stage Deploy"
  echo "Deploy on Nexus"
  //sh 'mvn deploy'
  echo "Deploy on OpenShift"

stage "Quality"
  echo "Running stage Quality"
  //sh 'mvn sonar:sonar'
}
