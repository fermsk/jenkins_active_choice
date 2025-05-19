import jenkins.model.Jenkins
import hudson.model.Job

...

properties(
        [
                disableConcurrentBuilds(),
                buildDiscarder(
                        logRotator(
                                artifactNumToKeepStr: '5',
                                numToKeepStr:  '5'
                        )
                ),
                parameters([
                        [
                                $class: 'ChoiceParameter',
                                choiceType: 'PT_SINGLE_SELECT',
                                description: 'Select branch',
                                name: 'JOB_BRANCH',
                                script: [
                                        $class: 'ScriptlerScript',
                                        scriptlerScriptId:'actionChoice.groovy'
                                ]
                        ],
                        [
                                $class: 'CascadeChoiceParameter',
                                choiceType: 'PT_SINGLE_SELECT',
                                description: 'Select build display name',
                                name: 'BUILDS',
                                referencedParameters: 'JOB_BRANCH',
                                script: [
                                        $class: 'ScriptlerScript',
                                        scriptlerScriptId:'actionChoiceBuilds.groovy',
                                        parameters: [
                                                [name:'JOB_BRANCH', value: '$JOB_BRANCH']
                                        ]
                                ]
                        ],
//                        [
//                                $class: 'CascadeChoiceParameter',
//                                choiceType: 'PT_SINGLE_SELECT',
//                                description: 'Its Jenkins work build name:',
//                                name: 'BUILD_NUM',
//                                referencedParameters: 'BUILDS, JOB_BRANCH',
//                                script: [
//                                        $class: 'ScriptlerScript',
//                                        scriptlerScriptId:'actionChoiceBuildsNumber.groovy',
//                                        parameters: [
//                                                [name:'BUILDS', value: '$BUILDS'],
//                                                [name:'JOB_BRANCH', value: '$JOB_BRANCH']
//                                        ]
//                                ]
//                        ]
                ])
        ]
)

def getBuildNumber(JOB_BRANCH, BUILDS) {
    def JOB_NAME = 'osinit/aeca-certificate-authority/osinit-mirror%2Faeca%2Fcertificate-authority%2Faeca-ca'
    def folder = Jenkins.instance.getItemByFullName("${JOB_NAME}")
    def jobs = folder.getAllItems(hudson.model.Job.class)
    def builds = []

    echo "Searching for builds in job: ${JOB_NAME}"
    echo "JOB_BRANCH: ${JOB_BRANCH}"
    echo "BUILDS: ${BUILDS}"

    jobs.each { job ->
        job.getBuilds().each { build ->
            builds << [fullName: "${build}", displayName: build.getDisplayName(), number: build.getNumber()]
        }
    }

    echo "Total builds found: ${builds.size()}"

    def result = [:]
    builds.each { buildInfo ->
        def matcher = buildInfo.fullName =~ /.*?\/([^\/]+)\s+#(\d+)/
        if (matcher) {
            def key = matcher[0][1]
            def value = [displayName: buildInfo.displayName.replaceAll('#', ''), number: buildInfo.number]

            if (result.containsKey(key)) {
                result[key] << value
            } else {
                result[key] = [value]
            }
        }
    }

    echo "Branches found: ${result.keySet()}"

    def buildsForSelectedBranch = result[JOB_BRANCH] ?: []
    echo "Builds for branch ${JOB_BRANCH}: ${buildsForSelectedBranch}"

    def chosenDisplayName = BUILDS.toString()
    def correspondingBuild = buildsForSelectedBranch.find { it.displayName == chosenDisplayName }

    if (correspondingBuild) {
        echo "Found matching build: ${correspondingBuild}"
        return correspondingBuild.number
    } else {
        echo "No matching build found for displayName: ${chosenDisplayName}"
        return null
    }
}

stage('Calculate BUILD_NUM') {
    node('master') {
        script {
            echo "JOB_BRANCH: ${params.JOB_BRANCH}"
            echo "BUILDS: ${params.BUILDS}"
            env.BUILD_NUM = getBuildNumber(params.JOB_BRANCH, params.BUILDS)
            if (env.BUILD_NUM != null) {
                echo "The current BUILD_NUM is: ${env.BUILD_NUM}"
            } else {
                error "Failed to calculate BUILD_NUM. Please check the parameters JOB_BRANCH and BUILDS."
            }
        }
    }
}