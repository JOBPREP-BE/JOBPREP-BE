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
    stage('Generate and Move application.yml') {
      steps {
        script {
          withCredentials([file(credentialsId: 'SECRETS', variable: 'SECRETS_FILE')]) {
            sh '''
              echo "Creating application.yml from secrets..."
              cp $SECRETS_FILE ./src/main/resources/application.yml
            '''
          }
        }
      }
    }
    stage('Gradle Build') {
      steps {
        sh './gradlew clean build'
      }
    }
    stage('Docker Login') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Docker Build') {
      steps {
        sh 'docker build -f Dockerfile -t jobpreb/jobprep .'
      }
    }
    stage('Docker Push') {
      steps {
        sh 'docker push jobpreb/jobprep'
      }
    }
  }
  post {
    always {
      sh '''
        rm -rf ./src/main/resources/application.yml
        docker logout
      '''
    }
  }
}
