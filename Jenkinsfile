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
            echo "ANDROID_HOME = ${ANDROID_HOME}"
            chmod a+x ./gradlew
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
            sh 'echo "no tests"'
        }
    }

    stage('Assemble') {
        steps {
            sh './gradlew assembleDebug'
        }
    }

    stage('Archive') {
        steps {
            archiveArtifacts(artifacts: 'app/build/outputs/apk/apk-debug.apk', onlyIfSuccessful: true)
        }
    }
  }

  post {
    failure {
       mail bcc: '', body: 'Build failure on project Classeur-autisme', cc: '', from: 'jenkins@polaris.ovh', replyTo: '', subject: '[JENKINS] Build failure on project Mobifresh', to: 'admin@polaris.ovh'
    }
  }
}