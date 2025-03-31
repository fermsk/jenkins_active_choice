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
                                name: 'JOB_BRANCH',
                                script: [
                                        $class: 'ScriptlerScript',
                                        scriptlerScriptId:'actionChoice.groovy'
                                ]
                        ],
                        [
                                $class: 'CascadeChoiceParameter',
                                choiceType: 'PT_SINGLE_SELECT',
                                name: 'BUILDS',
                                referencedParameters: 'JOB_BRANCH',
                                script: [
                                        $class: 'ScriptlerScript',
                                        scriptlerScriptId:'actionChoiceBuilds.groovy',
                                        parameters: [
                                                [name:'JOB_BRANCH', value: '$JOB_BRANCH']
                                        ]
                                ]
                        ]
                ])
        ]
)