/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import CommonJobProperties as commonJobProperties
import CommonTestProperties.Runner
import CommonTestProperties.SDK
import CommonTestProperties.TriggeringContext
import NexmarkBuilder as Nexmark
import NoPhraseTriggeringPostCommitBuilder
import PhraseTriggeringPostCommitBuilder
import InfluxDBCredentialsHelper

import static NexmarkDatabaseProperties.nexmarkBigQueryArgs
import static NexmarkDatabaseProperties.nexmarkInfluxDBArgs

// This job runs the suite of ValidatesRunner tests against the Dataflow runner.
NoPhraseTriggeringPostCommitBuilder.postCommitJob('beam_PostCommit_Java_Nexmark_Dataflow',
    'Dataflow Runner Nexmark Tests', this) {

      description('Runs the Nexmark suite on the Dataflow runner.')

      commonJobProperties.setTopLevelMainJobProperties(delegate, 'master', 240)

      commonJob(delegate, TriggeringContext.POST_COMMIT)
    }

PhraseTriggeringPostCommitBuilder.postCommitJob('beam_PostCommit_Java_Nexmark_Dataflow',
    'Run Dataflow Runner Nexmark Tests', 'Dataflow Runner Nexmark Tests', this) {

      description('Runs the Nexmark suite on the Dataflow runner against a Pull Request, on demand.')

      commonJobProperties.setTopLevelMainJobProperties(delegate, 'master', 240)

      commonJob(delegate, TriggeringContext.PR)
    }

private void commonJob(delegate, TriggeringContext triggeringContext) {
  def final JOB_SPECIFIC_OPTIONS = [
    'region' : 'us-central1',
    'suite' : 'STRESS',
    'numWorkers' : 4,
    'maxNumWorkers' : 4,
    'autoscalingAlgorithm' : 'NONE',
    'nexmarkParallel' : 16,
    'enforceEncodability' : true,
    'enforceImmutability' : true
  ]
  Nexmark.standardJob(delegate, Runner.DATAFLOW, SDK.JAVA, JOB_SPECIFIC_OPTIONS, triggeringContext)
}
