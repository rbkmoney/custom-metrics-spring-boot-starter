#!groovy
build('custom-metrics-spring-boot-starter', 'docker-host') {
    checkoutRepo()
    loadBuildUtils()

    def javaLibPipeline
    runStage('load JavaLib pipeline') {
        javaLibPipeline = load("build_utils/jenkins_lib/pipeJavaLib.groovy")
    }

    def buildImageTag = "07d3946f8f005782697de20270ac58cdcd18b011"
    javaLibPipeline(buildImageTag)
}