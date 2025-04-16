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
                        [
                                $class: 'CascadeChoiceParameter',
                                choiceType: 'PT_SINGLE_SELECT',
                                description: 'Its Jenkins work build name:',
                                name: 'BUILD_NUM',
                                referencedParameters: 'BUILDS',
                                referencedParameters: 'JOB_BRANCH',
                                script: [
                                        $class: 'ScriptlerScript',
                                        scriptlerScriptId:'actionChoiceBuildsNumber.groovy',
                                        parameters: [
                                                [name:'BUILDS', value: '$BUILDS'],
                                                [name:'JOB_BRANCH', value: '$JOB_BRANCH']
                                        ]
                                ]
                        ]

                ])
        ]
)
