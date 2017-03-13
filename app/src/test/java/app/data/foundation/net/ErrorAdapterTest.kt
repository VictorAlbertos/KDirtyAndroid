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

package app.data.foundation.net

import io.victoralbertos.jolyglot.GsonSpeaker
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ErrorAdapterTest {
    private lateinit var errorAdapterUT : ErrorAdapter

    @Before fun before() {
        errorAdapterUT = ErrorAdapter(GsonSpeaker())
    }

    @Test fun Verify_Adapt() {
        val json = "{\"message\":\"such a nice error\"}"
        val prettified = errorAdapterUT.adapt(json)
        assertEquals(prettified, "such a nice error")
    }
}
