/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.data.foundation

import io.reactivecache2.ReactiveCache
import io.victoralbertos.jolyglot.GsonSpeaker
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.util.concurrent.TimeUnit

class WireframeRepositoryTest {
    @get:Rule val testFolder = TemporaryFolder()
    private lateinit var wireframeRepositoryUT : WireframeRepository
    private val KEY = "mockModel"

    @Before fun before() {
        wireframeRepositoryUT = WireframeRepository(ReactiveCache.Builder()
                .using(testFolder.root, GsonSpeaker()))
    }

    @Test fun Verify_Put_And_Get_Success() {
        val observer = wireframeRepositoryUT.put(KEY, MockModel())
                .toSingle { wireframeRepositoryUT.get<MockModel>(KEY) }
                .test()
        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)

        observer.assertNoErrors()
        observer.assertValueCount(1)
        assertNotNull(observer.values()[0])
    }

    @Test fun Verify_Get_Failure() {
        val observer = wireframeRepositoryUT.get<MockModel>(KEY).test()
        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)

        observer.assertNoValues()
        observer.assertErrorMessage("There is not cached data in wireframe repository for key mockModel")
    }
}