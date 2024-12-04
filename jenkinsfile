pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '3'))
  }
  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerhub')
  }
  stages {
    stage('Grant Gradlew Permission') {
      steps {
        sh 'chmod +x ./gradlew'
      }
    }
    stage('Gradle Build') {
      steps {
        sh './gradle clean build'
      }
    }
    stage('Docker Login') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Docker Build') {
      steps {
        sh 'docker build -f Dockerfile -t $DOCKERHUB_CREDENTIALS_USR/jobprep .'
      }
    }
    stage('Docker Push') {
      steps {
        sh 'docker push $DOCKERHUB_CREDENTIALS_USR/jobprep:$BUILD_NUMBER'
      }
    }
  }
  post {
    always {
      sh 'docker logout'
    }
  }
}
