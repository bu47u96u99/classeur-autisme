properties([[$class: 'jenkins.model.BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '3', artifactNumToKeepStr: '3']]])

pipeline {

  agent any

  tools {
          jdk 'Java'
  }

  stages {
    stage ('Prepare') {
        steps {
            sh '''
            echo "PATH = ${PATH}"
            chmod a+x ./gradlew
            touch local.properties
            echo "sdk.dir=/opt/android-sdk" > local.properties
            '''
         }
    }

    stage('Compile') {
        steps {
            sh './gradlew clean build'
        }
    }

    stage('Test') {
        steps {
            sh './gradlew test'
        }
    }

    stage('Analyse') {
        steps {
            withSonarQubeEnv('sonar') {
                sh './gradlew sonar'
            }
        }
    }

    stage('Assemble') {
        steps {
            sh './gradlew assembleDebug'
        }
    }

    stage('Archive') {
        steps {
            sh 'echo "no archive"'
            //archiveArtifacts(artifacts: 'app//build//outputs//apk//debug//apk-debug.apk', onlyIfSuccessful: true)
        }
    }
  }

  post {
    failure {
       mail bcc: '', body: 'Build failure on project Classeur-autisme', cc: '', from: 'Jenkins <jenkins@polaris.ovh>', replyTo: '', subject: '[JENKINS] Build failure on project Classeur autisme', to: 'admin@polaris.ovh'
    }
  }
}